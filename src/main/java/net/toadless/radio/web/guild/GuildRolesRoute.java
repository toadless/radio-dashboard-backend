package net.toadless.radio.web.guild;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.toadless.radio.Radio;
import net.toadless.radio.modules.WebModule;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class GuildRolesRoute implements Handler
{
    private final Radio radio;

    public GuildRolesRoute(Radio radio)
    {
        this.radio = radio;
    }

    @Override
    public void handle(@NotNull Context ctx)
    {
        @SuppressWarnings("ConstantConditions")
        long guildId = ctx.attribute("guild_id");

        Collection<Role> roles = radio.getShardManager().getGuildById(guildId).getRoles();

        DataArray result = DataArray.empty();
        for (Role role : roles)
        {
            // dont add @everyone role
            if (role.getIdLong() == guildId) continue;

            result.add(DataObject.empty()
                    .put("id", role.getIdLong())
                    .put("name", role.getName()));
        }

        radio.getModules().get(WebModule.class).ok(ctx, DataObject.empty().put("roles", result));
    }
}