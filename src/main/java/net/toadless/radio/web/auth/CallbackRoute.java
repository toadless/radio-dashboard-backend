package net.toadless.radio.web.auth;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.toadless.radio.Radio;
import net.toadless.radio.modules.OAuth2Module;
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

        radio.getModules().get(OAuth2Module.class).createSession(code);

        // TODO: generate jwts
    }
}