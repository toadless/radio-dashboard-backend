package net.toadless.radio.web.auth;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.toadless.radio.Radio;
import net.toadless.radio.modules.AuthModule;
import net.toadless.radio.modules.OAuth2Module;
import net.toadless.radio.objects.auth.UserTokens;
import net.toadless.radio.objects.config.ConfigOption;
import org.jetbrains.annotations.NotNull;

public class CallbackRoute implements Handler
{
    private final Radio radio;

    public CallbackRoute(Radio radio)
    {
        this.radio = radio;
    }

    @Override
    public void handle(@NotNull Context ctx)
    {
        String code = ctx.queryParam("code");

        if (code == null || code.equals(""))
        {
            throw new BadRequestResponse("No 'code' found in request url");
        }

        long userId = radio.getModules().get(OAuth2Module.class).createSession(code);
        UserTokens userTokens = radio.getModules().get(AuthModule.class).generateUserTokens(userId);

        ctx.redirect(new StringBuilder()
                .append(radio.getConfiguration().getString(ConfigOption.REDIRECT_URL))
                .append("?access_token=")
                .append(userTokens.getAccessToken())
                .append("&refresh_token=")
                .append(userTokens.getRefreshToken())
                .toString());
    }
}