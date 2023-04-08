package net.toadless.radio.util;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Header;
import io.javalin.http.InternalServerErrorResponse;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.toadless.radio.Constants;
import net.toadless.radio.Radio;
import net.toadless.radio.objects.config.ConfigOption;
import net.toadless.radio.objects.oauth2.Guild;
import net.toadless.radio.objects.oauth2.Session;
import net.toadless.radio.objects.oauth2.User;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

public class WebUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WebUtils.class);

    private WebUtils()
    {
        //Overrides the default, public, constructor
    }

    public static Session createSession(Radio radio, String redirectUri, String code)
    {
        Request request = new Request.Builder()
                .url(Constants.DISCORD_API + "/oauth2/token")
                .post(new FormBody.Builder()
                        .add("client_id", radio.getConfiguration().getString(ConfigOption.CLIENT_ID))
                        .add("client_secret", radio.getConfiguration().getString(ConfigOption.CLIENT_SECRET))
                        .add("grant_type", "authorization_code")
                        .add("code", code)
                        .add("redirect_uri", redirectUri)
                        .build())
                .build();

        try (Response response = radio.getOkHttpClient().newCall(request).execute())
        {
            if (response.code() != 200)
            {
                throw new BadRequestResponse("Incorrect 'code' in request url");
            }

            try (ResponseBody body = response.body())
            {
                DataObject json = DataObject.fromJson(body.string());

                String accessToken = json.getString("access_token");
                String refreshToken = json.getString("refresh_token");
                LocalDateTime expiry = LocalDateTime.now().plusSeconds(json.getInt("expires_in"));

                // We dont yet know the user_id
                User user = fetchUserData(radio, new Session(-1,
                        accessToken, refreshToken, expiry));

                // Put user into database
                DatabaseUtils.registerUser(radio, user);

                // These tokens are not yet encrypted
                return new Session(user.getId(), accessToken, refreshToken, expiry);
            }
        } catch (IOException e)
        {
            LOGGER.error("Unable to create user session");
            throw new InternalServerErrorResponse("Something went wrong...");
        }
    }

    public static Session refreshExpiredSession(Radio radio, Session session)
    {
        Request request = new Request.Builder()
                .url(Constants.DISCORD_API + "/oauth2/token")
                .post(new FormBody.Builder()
                        .add("client_id", radio.getConfiguration().getString(ConfigOption.CLIENT_ID))
                        .add("client_secret", radio.getConfiguration().getString(ConfigOption.CLIENT_SECRET))
                        .add("grant_type", "refresh_token")
                        .add("refresh_token", session.getRefreshToken())
                        .build())
                .build();

        try (Response response = radio.getOkHttpClient().newCall(request).execute())
        {
            if (response.code() != 200)
            {
                LOGGER.error("Unable to refresh session");
                throw new InternalServerErrorResponse("Something went wrong...");
            }

            try (ResponseBody body = response.body())
            {
                DataObject json = DataObject.fromJson(body.string());

                String accessToken = json.getString("access_token");
                String refreshToken = json.getString("refresh_token");
                LocalDateTime expiry = LocalDateTime.now().plusSeconds(json.getInt("expires_in"));

                User user = fetchUserData(radio, new Session(-1,
                        accessToken, refreshToken, expiry));

                // Update user in db
                DatabaseUtils.registerUser(radio, user);

                // These tokens are not yet encrypted
                return new Session(user.getId(), accessToken, refreshToken, expiry);
            }
        } catch (IOException e)
        {
            LOGGER.error("Unable to refresh user session");
            throw new InternalServerErrorResponse("Something went wrong...");
        }
    }

    public static User fetchUserData(Radio radio, Session session)
    {
        Request request = new Request.Builder()
                .url(Constants.DISCORD_API + "/users/@me")
                .header(Header.AUTHORIZATION, "Bearer " + session.getAccessToken())
                .get()
                .build();

        try (Response response = radio.getOkHttpClient().newCall(request).execute())
        {
            if (response.code() != 200)
            {
                LOGGER.error("Unable to fetch user data");
                throw new InternalServerErrorResponse("Something went wrong...");
            }

            try (ResponseBody body = response.body())
            {
                DataObject json = DataObject.fromJson(body.string());
                return new User(json.getLong("id"), json.getString("username"),
                        json.getString("discriminator"), json.getString("avatar"));
            }
        } catch (IOException e)
        {
            LOGGER.error("Unable to fetch user data");
            throw new InternalServerErrorResponse("Something went wrong...");
        }
    }

    public static Set<Guild> fetchUserGuilds(Radio radio, Session session)
    {
        return null;
    }
}