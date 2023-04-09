package net.toadless.radio.web.guild;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.InternalServerErrorResponse;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.ParsingException;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.toadless.radio.Radio;
import net.toadless.radio.modules.WebModule;
import net.toadless.radio.objects.database.GuildConfig;
import net.toadless.radio.util.StringUtils;
import net.toadless.radio.util.WebUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Collectors;

public class GuildDJRoleRoute implements Handler
{
    private final Radio radio;

    public GuildDJRoleRoute(Radio radio)
    {
        this.radio = radio;
    }

    @Override
    public void handle(@NotNull Context ctx)
    {
        @SuppressWarnings("ConstantConditions")
        long guildId = ctx.attribute("guild_id");

        try
        {
            DataObject body = DataObject.fromJson(ctx.body());

            if (!body.hasKey("dj_role"))
            {
                throw new BadRequestResponse("No 'dj_role' provided in request body");
            }

            long djRole = Long.parseLong(body.getString("dj_role"));

            Collection<Role> roles = radio.getShardManager().getGuildById(guildId).getRoles();

            if (roles.stream().filter(role -> role.getIdLong() != djRole).collect(Collectors.toSet()).isEmpty() &&
                    djRole != -1L)
            {
                throw new BadRequestResponse("The provided 'dj_role' does not exist");
            }

            if (!GuildConfig.setDJRole(radio, guildId, djRole))
            {
                throw new InternalServerErrorResponse("Unable to update guild dj_role");
            }

            radio.getModules().get(WebModule.class).ok(ctx, DataObject.empty());
            WebUtils.invalidateGuildCache(radio, guildId);
        } catch (ParsingException e)
        {
            throw new BadRequestResponse("Malformed body");
        } catch (NumberFormatException e)
        {
            throw new BadRequestResponse("The 'dj_role' in the request body was not a numeric value");
        }
    }
}
