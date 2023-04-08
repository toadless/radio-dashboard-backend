package net.toadless.radio.web.auth;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.toadless.radio.Radio;
import net.toadless.radio.modules.AuthModule;
import net.toadless.radio.modules.WebModule;
import net.toadless.radio.objects.auth.UserTokens;
import org.jetbrains.annotations.NotNull;

public class TokenRoute implements Handler
{
    private final Radio radio;

    public TokenRoute(Radio radio)
    {
        this.radio = radio;
    }

    @Override
    public void handle(@NotNull Context ctx)
    {
        try
        {
            DataObject body = DataObject.fromJson(ctx.body());

            if (!body.hasKey("grant_type"))
            {
                throw new BadRequestResponse("No 'grant_type' provided in request body");
            }

            switch (body.getString("grant_type").toLowerCase())
            {
                case "refresh_token" -> refresh(ctx, body);
                default -> throw new BadRequestResponse("Invalid 'grant_type'");
            }
        } catch (Exception e)
        {
            throw new BadRequestResponse("Malformed body");
        }
    }

    public void refresh(Context ctx, DataObject body)
    {
        if (!body.hasKey("refresh_token"))
        {
            throw new BadRequestResponse("No 'refresh_token' in request body");
        }

        UserTokens tokens = radio.getModules().get(AuthModule.class)
                .refreshUserTokens(body.getString("refresh_token"));

        radio.getModules().get(WebModule.class).ok(ctx, DataObject.empty()
                .put("access_token", tokens.getAccessToken())
                .put("refresh_token", tokens.getRefreshToken()));
    }
}