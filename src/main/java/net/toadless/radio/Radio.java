package net.toadless.radio;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.toadless.radio.objects.config.ConfigOption;
import net.toadless.radio.objects.config.Configuration;
import net.toadless.radio.objects.info.BotInfo;
import net.toadless.radio.objects.module.Modules;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.Objects;

public class Radio extends ListenerAdapter
{
    private final Logger logger;
    private final LocalDateTime startTimestamp;
    private final Configuration configuration;
    private final OkHttpClient okHttpClient;
    private final Modules modules;
    private ShardManager shardManager;

    public Radio()
    {
        this.logger = LoggerFactory.getLogger(Radio.class);
        this.configuration = new Configuration(this);
        this.okHttpClient = new OkHttpClient();
        this.startTimestamp = LocalDateTime.now();
        this.modules = new Modules(this);
    }

    public OkHttpClient getOkHttpClient()
    {
        return okHttpClient;
    }

    public Modules getModules()
    {
        return modules;
    }

    public void build() throws LoginException
    {

        this.shardManager = DefaultShardManagerBuilder
                .create(getConfiguration().getString(ConfigOption.TOKEN),
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_PRESENCES,

                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.DIRECT_MESSAGE_REACTIONS,

                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS,

                        GatewayIntent.MESSAGE_CONTENT)

                .disableCache(
                        CacheFlag.ACTIVITY,
                        CacheFlag.CLIENT_STATUS,
                        CacheFlag.ROLE_TAGS,
                        CacheFlag.ONLINE_STATUS,
                        CacheFlag.MEMBER_OVERRIDES,
                        CacheFlag.STICKER,
                        CacheFlag.SCHEDULED_EVENTS)

                .setHttpClient(okHttpClient)

                .setMemberCachePolicy(MemberCachePolicy.NONE)
                .setShardsTotal(-1)

                .addEventListeners(
                        this,
                        modules.getModules()
                )

                .setActivity(Activity.playing("loading..."))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();
    }

    @Override
    public void onReady(ReadyEvent event)
    {
        getLogger().info("Account:         " + event.getJDA().getSelfUser().getAsTag() + " / " + event.getJDA().getSelfUser().getId());
        getLogger().info("Total Shards:    " + BotInfo.getTotalShards(event.getJDA().getShardManager()));
        getLogger().info("Total Guilds:    " + BotInfo.getGuildCount(event.getJDA().getShardManager()));
        getLogger().info("JDA Version:     " + JDAInfo.VERSION);
        getLogger().info("Radio Version:   " + Constants.VERSION);
        getLogger().info("JVM Version:     " + BotInfo.getJavaVersion());
    }

    public SelfUser getSelfUser()
    {
        if (getJDA() == null)
        {
            throw new UnsupportedOperationException("No JDA present.");
        }
        return getJDA().getSelfUser();
    }

    public JDA getJDA()
    {
        return shardManager.getShardCache().stream().filter(Objects::nonNull).findFirst().orElse(null);
    }

    public LocalDateTime getStartTimestamp()
    {
        return this.startTimestamp;
    }

    public ShardManager getShardManager()
    {
        if (this.shardManager == null)
        {
            throw new UnsupportedOperationException("Shardmanager is not built.");
        }
        return this.shardManager;
    }

    public Configuration getConfiguration()
    {
        return this.configuration;
    }

    public Logger getLogger()
    {
        return this.logger;
    }
}