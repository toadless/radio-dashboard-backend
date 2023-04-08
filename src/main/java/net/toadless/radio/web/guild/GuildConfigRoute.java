package net.toadless.radio.web.guild;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.toadless.radio.Radio;
import net.toadless.radio.jooq.tables.records.GuildsRecord;
import net.toadless.radio.modules.WebModule;
import net.toadless.radio.objects.database.GuildConfig;
import org.jetbrains.annotations.NotNull;

public class GuildConfigRoute implements Handler
{
    private final Radio radio;

    public GuildConfigRoute(Radio radio)
    {
        this.radio = radio;
    }

    @Override
    public void handle(@NotNull Context ctx)
    {
        @SuppressWarnings("ConstantConditions")
        long guildId = ctx.attribute("guild_id");
        GuildsRecord guild = GuildConfig.getGuildConfig(radio, guildId);
        radio.getModules().get(WebModule.class).ok(ctx, DataObject.empty()
                .put("prefix", guild.getPrefix())
                .put("dj_role", guild.getDjRole()));
    }
}