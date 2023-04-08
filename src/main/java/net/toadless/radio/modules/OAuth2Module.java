package net.toadless.radio.modules;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import net.toadless.radio.Radio;
import net.toadless.radio.objects.config.ConfigOption;
import net.toadless.radio.objects.module.Module;
import net.toadless.radio.objects.module.Modules;
import net.toadless.radio.objects.oauth2.Guild;
import net.toadless.radio.objects.oauth2.Scope;
import net.toadless.radio.objects.oauth2.Session;
import net.toadless.radio.objects.oauth2.User;
import net.toadless.radio.util.DatabaseUtils;
import net.toadless.radio.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class OAuth2Module extends Module
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2Module.class);
    private static final Scope[] SCOPES = { Scope.IDENTIFY, Scope.GUILDS };

    public static final String ALGORITHM = "AES";

    private final SecretKey secretKey;
    private final LoadingCache<Long, Session> sessions;
    private final LoadingCache<Long, User> users;
    private final LoadingCache<Long, Set<Long>> userGuilds;
    private final LoadingCache<Long, Guild> guilds;

    public OAuth2Module(Radio radio, Modules modules)
    {
        super(radio, modules);
        this.secretKey = new SecretKeySpec(radio.getConfiguration().getString(ConfigOption.OAUTH2_KEY).getBytes(), ALGORITHM);
        this.sessions = Caffeine.newBuilder()
                .expireAfterAccess(2, TimeUnit.HOURS)
                .recordStats()
                .build(this::fetchSession);
        this.users = Caffeine.newBuilder()
                .expireAfterAccess(2, TimeUnit.HOURS)
                .recordStats()
                .build(this::fetchUser);
        this.userGuilds = Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .refreshAfterWrite(2, TimeUnit.MINUTES)
                .recordStats()
                .build(this::fetchUserGuilds);
        this.guilds = Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .recordStats()
                .build(this::fetchGuild);
    }

    private Session refreshUserSession(Session expiredSession)
    {
        Session session = WebUtils.refreshExpiredSession(radio, expiredSession);
        DatabaseUtils.registerSession(radio, session);
        return session;
    }

    private Session fetchSession(long userId)
    {
        Session session = DatabaseUtils.fetchSession(radio, userId);

        if (session == null)
        {
            return null;
        }

        if (LocalDateTime.now().isBefore(session.getExpiry()))
        {
            return refreshUserSession(session);
        }

        return session;
    }

    private User fetchUser(long userId)
    {
        User user = DatabaseUtils.fetchUser(radio, userId);

        if (user != null)
        {
            return user;
        }

        return WebUtils.fetchUserData(radio, sessions.get(userId));
    }

    private Set<Long> fetchUserGuilds(long userId)
    {
        return null;
    }

    private Guild fetchGuild(long guildId)
    {
        return null;
    }

    public SecretKey getSecretKey()
    {
        return secretKey;
    }
}
