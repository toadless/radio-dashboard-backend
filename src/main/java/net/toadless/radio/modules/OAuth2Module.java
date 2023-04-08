package net.toadless.radio.modules;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.javalin.http.BadRequestResponse;
import net.dv8tion.jda.api.Permission;
import net.toadless.radio.Constants;
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
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class OAuth2Module extends Module
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2Module.class);
    private static final Scope[] SCOPES = { Scope.IDENTIFY, Scope.GUILDS };

    public static final String ALGORITHM = "AES";

    private final SecretKey secretKey;
    private final LoadingCache<Long, Session> sessions;
    private final LoadingCache<Long, User> users;
    private final LoadingCache<Long, Set<Guild>> userGuilds;

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
    }

    public User getUser(long userId)
    {
        return users.get(userId);
    }

    public Set<Guild> getUserGuilds(long userId)
    {
        return userGuilds.get(userId);
    }

    public long createSession(String code)
    {
        Session session = WebUtils.createSession(radio, generateRedirectURI(), code);
        DatabaseUtils.registerSession(radio, session);

        return session.getId();
    }

    public String generateAuthorizeURL()
    {
        return new StringBuilder()
                .append(Constants.DISCORD_API)
                .append("/oauth2/authorize")
                .append("?client_id=")
                .append(radio.getConfiguration().getString(ConfigOption.CLIENT_ID))
                .append("&redirect_url=")
                .append(URLEncoder.encode(generateRedirectURI(), Charset.defaultCharset()))
                .append("&response_type=code")
                .append("&scope=")
                .append(URLEncoder.encode(Scope.join(SCOPES), Charset.defaultCharset()))
                .toString();
    }

    private String generateRedirectURI()
    {
        return String.format("%s/%s", radio.getConfiguration().getString(ConfigOption.OAUTH2_URL),
                Constants.REDIRECT_URI);
    }

    public void removeUser(long userId)
    {
        DatabaseUtils.removeUser(radio, userId);
        DatabaseUtils.removeSession(radio, userId);
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

        if (session.getExpiry().isBefore(LocalDateTime.now()))
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

        Session session = sessions.get(userId);

        if (session == null)
        {
            return null;
        }

        return WebUtils.fetchUserData(radio, session);
    }

    private Set<Guild> fetchUserGuilds(long userId)
    {
        Session session = sessions.get(userId);

        if (session == null)
        {
            return null;
        }

        return WebUtils.fetchUserGuilds(radio, session).stream().filter(guild ->
                guild.getPermissions().contains(Permission.MANAGE_SERVER) ||
                        guild.getPermissions().contains(Permission.ADMINISTRATOR)).collect(Collectors.toSet());
    }

    public SecretKey getSecretKey()
    {
        return secretKey;
    }
}