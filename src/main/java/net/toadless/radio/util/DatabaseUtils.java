package net.toadless.radio.util;

import io.javalin.http.InternalServerErrorResponse;
import net.toadless.radio.Radio;
import net.toadless.radio.jooq.Tables;
import net.toadless.radio.jooq.tables.records.DiscordTokensRecord;
import net.toadless.radio.jooq.tables.records.UsersRecord;
import net.toadless.radio.modules.DatabaseModule;
import net.toadless.radio.modules.OAuth2Module;
import net.toadless.radio.objects.oauth2.Session;
import net.toadless.radio.objects.oauth2.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class DatabaseUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtils.class);

    private DatabaseUtils()
    {
        //Overrides the default, public, constructor
    }

    public static void registerSession(Radio radio, Session session)
    {
        LOGGER.debug("Registered session " + session.getId());
        try (Connection connection = radio.getModules().get(DatabaseModule.class).getConnection())
        {
            DiscordTokensRecord record = new DiscordTokensRecord(session.getId(), EncryptUtils.encrypt(
                    session.getAccessToken(), radio.getModules().get(OAuth2Module.class).getSecretKey(), OAuth2Module.ALGORITHM),
                    EncryptUtils.encrypt(session.getRefreshToken(), radio.getModules().get(OAuth2Module.class).getSecretKey(),
                            OAuth2Module.ALGORITHM), session.getExpiry());

            var context = radio.getModules().get(DatabaseModule.class).getContext(connection)
                    .insertInto(Tables.DISCORD_TOKENS)
                    .set(record)
                    .onDuplicateKeyUpdate()
                    .set(record);
            context.execute();
        }
        catch (Exception exception)
        {
            radio.getLogger().error("An SQL error occurred", exception);
        }
    }

    public static Session fetchSession(Radio radio, long userId)
    {
        try (Connection connection = radio.getModules().get(DatabaseModule.class).getConnection())
        {
            var context = radio.getModules().get(DatabaseModule.class).getContext(connection);
            var existsQuery = context.selectFrom(Tables.DISCORD_TOKENS)
                    .where(Tables.DISCORD_TOKENS.USERS_ID.eq(userId));

            if (existsQuery.fetch().isEmpty())
            {
                return null;
            }

            var result = existsQuery.fetch().get(0);
            existsQuery.close();

            return new Session(result.getUsersId(), EncryptUtils.decrypt(result.getAccessToken(),
                    radio.getModules().get(OAuth2Module.class).getSecretKey(), OAuth2Module.ALGORITHM),
                    EncryptUtils.decrypt(result.getRefreshToken(), radio.getModules().get(OAuth2Module.class)
                            .getSecretKey(), OAuth2Module.ALGORITHM), result.getExpiry());
        }
        catch (Exception exception)
        {
            radio.getLogger().error("An SQL error occurred", exception);
            throw new InternalServerErrorResponse("Something went wrong...");
        }
    }

    public static void registerUser(Radio radio, User user)
    {
        LOGGER.debug("Registered user " + user.getId());
        try (Connection connection = radio.getModules().get(DatabaseModule.class).getConnection())
        {
            UsersRecord record = new UsersRecord(user.getId(), user.getName(),
                    user.getDiscriminator(), user.getAvatar());

            var context = radio.getModules().get(DatabaseModule.class).getContext(connection)
                    .insertInto(Tables.USERS)
                    .set(record)
                    .onDuplicateKeyUpdate()
                    .set(record);
            context.execute();
        }
        catch (Exception exception)
        {
            radio.getLogger().error("An SQL error occurred", exception);
        }
    }

    public static User fetchUser(Radio radio, long userId)
    {
        try (Connection connection = radio.getModules().get(DatabaseModule.class).getConnection())
        {
            var context = radio.getModules().get(DatabaseModule.class).getContext(connection);
            var existsQuery = context.selectFrom(Tables.USERS)
                    .where(Tables.USERS.USER_ID.eq(userId));

            if (existsQuery.fetch().isEmpty())
            {
                return null;
            }

            var result = existsQuery.fetch().get(0);
            existsQuery.close();

            return new User(result.getUserId(), result.getName(), result.getDiscriminator(), result.getAvatar());
        }
        catch (Exception exception)
        {
            radio.getLogger().error("An SQL error occurred", exception);
            throw new InternalServerErrorResponse("Something went wrong...");
        }
    }
}