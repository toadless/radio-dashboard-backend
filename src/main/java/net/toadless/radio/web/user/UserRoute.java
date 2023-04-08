package net.toadless.radio.web.user;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.InternalServerErrorResponse;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.toadless.radio.Radio;
import net.toadless.radio.modules.OAuth2Module;
import net.toadless.radio.modules.WebModule;
import net.toadless.radio.objects.oauth2.User;
import org.jetbrains.annotations.NotNull;

public class UserRoute implements Handler
{
    private final Radio radio;

    public UserRoute(Radio radio)
    {
        this.radio = radio;
    }

    @Override
    public void handle(@NotNull Context ctx)
    {
        @SuppressWarnings("ConstantConditions")
        long userId = ctx.attribute("user_id");

        User user = radio.getModules().get(OAuth2Module.class).getUser(userId);

        if (user == null)
        {
            throw new InternalServerErrorResponse("Unable to find user data");
        }

        radio.getModules().get(WebModule.class).ok(ctx, DataObject.empty()
                .put("id", user.getId())
                .put("name", user.getName())
                .put("discriminator", user.getDiscriminator())
                .put("avatar", user.getAvatar()));
    }
}