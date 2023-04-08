package net.toadless.radio.modules;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import net.toadless.radio.Radio;
import net.toadless.radio.objects.module.Module;
import net.toadless.radio.objects.module.Modules;
import net.toadless.radio.objects.oauth2.Guild;
import net.toadless.radio.objects.oauth2.Scope;
import net.toadless.radio.objects.oauth2.Session;
import net.toadless.radio.objects.oauth2.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class OAuth2Module extends Module
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2Module.class);
    private static final Scope[] SCOPES = { Scope.IDENTIFY, Scope.GUILDS };

    private final SecretKey secretKey;
    private final LoadingCache<Long, Session> sessions;
    private final LoadingCache<Long, User> users;
    private final LoadingCache<Long, Set<Long>> userGuilds;
    private final LoadingCache<Long, Guild> guilds;

    public OAuth2Module(Radio radio, Modules modules)
    {
        super(radio, modules);
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

    private Session fetchSession(long userId)
    {
        return null;
    }

    private User fetchUser(long userId)
    {
        return null;
    }

    private Set<Long> fetchUserGuilds(long userId)
    {
        return null;
    }

    private Guild fetchGuild(long guildId)
    {
        return null;
    }
}
