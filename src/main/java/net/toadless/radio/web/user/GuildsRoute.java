package net.toadless.radio.web.user;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.toadless.radio.Radio;
import net.toadless.radio.modules.OAuth2Module;
import net.toadless.radio.modules.WebModule;
import net.toadless.radio.objects.oauth2.Guild;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class GuildsRoute implements Handler
{
    private final Radio radio;

    public GuildsRoute(Radio radio)
    {
        this.radio = radio;
    }

    @Override
    public void handle(@NotNull Context ctx)
    {
        @SuppressWarnings("ConstantConditions")
        long userId = ctx.attribute("user_id");

        Set<Guild> userGuilds = radio.getModules().get(OAuth2Module.class).getUserGuilds(userId);
        var guildCache = radio.getShardManager().getGuildCache();

        Set<Guild> mutualGuilds = userGuilds.stream().filter(guild -> guildCache.getElementById(guild.getId()) != null)
                .collect(Collectors.toSet());
        Set<Guild> individualGuilds = userGuilds.stream().filter(guild -> guildCache.getElementById(guild.getId()) == null)
                .collect(Collectors.toSet());

        radio.getModules().get(WebModule.class).ok(ctx, DataObject.empty()
                .put("mutual_guilds", DataArray.fromCollection(mutualGuilds))
                .put("individual_guilds", DataArray.fromCollection(individualGuilds)));
    }
}