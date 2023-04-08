package net.toadless.radio.util;

import io.javalin.http.*;
import net.toadless.radio.Radio;
import net.toadless.radio.modules.OAuth2Module;
import net.toadless.radio.objects.oauth2.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class AuthUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthUtils.class);

    private AuthUtils()
    {
        //Overrides the default, public, constructor
    }

    public static boolean isOptionsRequest(Context ctx)
    {
        if (ctx.method() == HandlerType.OPTIONS)
        {
            return true;
        }

        return false;
    }

    public static String pullAccessTokenFromAuthorizationHeader(Context ctx)
    {
        String header = ctx.header(Header.AUTHORIZATION);

        if (header == null || header.isEmpty())
        {
            throw new BadRequestResponse("No 'Authorization' header provided");
        }

        // Split at space
        String[] contents = header.split("\\s+");

        // index 0 should be token type, eg "Bearer"
        if (!contents[0].equalsIgnoreCase("Bearer"))
        {
            throw new UnauthorizedResponse("Non-Bearer-token provided in 'Authorization' header");
        }

        if (contents[1] == null || contents[1].equals(""))
        {
            throw new UnauthorizedResponse("No token provided in 'Authorization' header");
        }

        return contents[1];
    }

    public static void setGuild(Context ctx, Radio radio)
    {
        if (isOptionsRequest(ctx)) return;

        try
        {
            @SuppressWarnings("ConstantConditions")
            long userId = ctx.attribute("user_id");
            long guildId = Long.parseLong(ctx.pathParam("guild_id"));

            Set<Guild> guilds = radio.getModules().get(OAuth2Module.class).getUserGuilds(userId);
            var guildCache = radio.getShardManager().getGuildCache();

            if (guilds.stream().filter(guild -> guildCache.getElementById(guild.getId()) != null)
                    .filter(guild -> guild.getId() == guildId).collect(Collectors.toSet()).isEmpty())
            {
                throw new ForbiddenResponse("You do not have permission to access and mutate the provided guild's data");
            }

            ctx.attribute("guild_id", guildId);
        } catch (NumberFormatException e)
        {
            throw new BadRequestResponse("'guild_id' in request url was not a numeric value");
        }
    }
}