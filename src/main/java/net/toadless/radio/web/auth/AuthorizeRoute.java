package net.toadless.radio.web.auth;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.toadless.radio.Radio;
import net.toadless.radio.modules.OAuth2Module;
import org.jetbrains.annotations.NotNull;

public class AuthorizeRoute implements Handler
{
    private final Radio radio;

    public AuthorizeRoute(Radio radio)
    {
        this.radio = radio;
    }

    @Override
    public void handle(@NotNull Context ctx)
    {
        ctx.redirect(radio.getModules().get(OAuth2Module.class).generateAuthorizeURL());
    }
}