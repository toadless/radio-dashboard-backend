package net.toadless.radio.web.guild;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.toadless.radio.Radio;
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

        
    }
}