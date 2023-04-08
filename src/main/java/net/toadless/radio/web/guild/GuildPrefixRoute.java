package net.toadless.radio.web.guild;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.InternalServerErrorResponse;
import net.dv8tion.jda.api.exceptions.ParsingException;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.toadless.radio.Radio;
import net.toadless.radio.modules.WebModule;
import net.toadless.radio.objects.database.GuildConfig;
import net.toadless.radio.util.StringUtils;
import org.jetbrains.annotations.NotNull;

public class GuildPrefixRoute implements Handler
{
    private final Radio radio;

    public GuildPrefixRoute(Radio radio)
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

            if (!body.hasKey("prefix"))
            {
                throw new BadRequestResponse("No 'prefix' provided in request body");
            }

            String prefix = StringUtils.markdownSanitize(body.getString("prefix"));

            if (prefix.length() < 1 || prefix.isBlank() || prefix.length() > 5)
            {
                throw new BadRequestResponse("Prefix was too long, contained markdown or contained spaces");
            }

            if (!GuildConfig.setGuildPrefix(radio, guildId, prefix))
            {
                throw new InternalServerErrorResponse("Unable to update guild prefix");
            }

            radio.getModules().get(WebModule.class).ok(ctx, DataObject.empty());
        } catch (ParsingException e)
        {
            throw new BadRequestResponse("Malformed body");
        }
    }
}