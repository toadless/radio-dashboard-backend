package net.toadless.radio.objects.database;

import io.javalin.http.InternalServerErrorResponse;
import net.toadless.radio.Radio;
import net.toadless.radio.jooq.Tables;
import net.toadless.radio.jooq.tables.records.RefreshTokensRecord;
import net.toadless.radio.modules.DatabaseModule;
import net.toadless.radio.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.toadless.radio.jooq.tables.RefreshTokens.REFRESH_TOKENS;

public class RefreshToken
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshToken.class);

    private RefreshToken()
    {
        //Overrides the default, public, constructor
    }

    public static RefreshTokensRecord getRefreshToken(Radio radio, UUID jti)
    {
        try (Connection connection = radio.getModules().get(DatabaseModule.class).getConnection())
        {
            var context = radio.getModules().get(DatabaseModule.class).getContext(connection);
            var existsQuery = context.selectFrom(Tables.REFRESH_TOKENS)
                    .where(REFRESH_TOKENS.TOKEN_ID.eq(jti));

            if (existsQuery.fetch().isEmpty())
            {
                return null;
            }

            var result = existsQuery.fetch().get(0);
            existsQuery.close();

            return result;
        }
        catch (Exception exception)
        {
            radio.getLogger().error("An SQL error occurred", exception);
            throw new InternalServerErrorResponse("Something went wrong...");
        }
    }

    public static boolean removeRefreshToken(Radio radio, long userId, UUID jti)
    {
        try (Connection connection = radio.getModules().get(DatabaseModule.class).getConnection())
        {
            var context = radio.getModules().get(DatabaseModule.class).getContext(connection)
                    .deleteFrom(Tables.REFRESH_TOKENS).where(REFRESH_TOKENS.TOKEN_ID.eq(jti)
                            .and(REFRESH_TOKENS.USER_ID.eq(userId)));
            context.execute();

            LOGGER.debug("Removed refresh_token " + jti);
            return true;
        }
        catch (Exception exception)
        {
            radio.getLogger().error("An SQL error occurred", exception);
            return false;
        }
    }

    private static boolean insertRefreshToken(Radio radio, long userId, UUID jti, LocalDateTime expiry)
    {
        try (Connection connection = radio.getModules().get(DatabaseModule.class).getConnection())
        {
            var context = radio.getModules().get(DatabaseModule.class).getContext(connection)
                    .insertInto(Tables.REFRESH_TOKENS)
                    .columns(REFRESH_TOKENS.TOKEN_ID, REFRESH_TOKENS.USER_ID, REFRESH_TOKENS.EXPIRY)
                    .values(jti, userId, expiry)
                    .onDuplicateKeyIgnore();
            context.execute();

            LOGGER.debug("Created refresh_token " + jti);
            return true;
        }
        catch (Exception exception)
        {
            radio.getLogger().error("An SQL error occurred", exception);
            return false;
        }
    }

    public static UUID generateRefreshToken(Radio radio, long userId)
    {
        UUID jti = UUID.randomUUID();

        if (getRefreshToken(radio, jti) != null)
        {
            // The jti that was generated
            // was already in use
            return generateRefreshToken(radio, userId);
        }

        if (!insertRefreshToken(radio, userId, jti,
                TimeUtils.nowPlusDuration(TimeUtils.REFRESH_TOKEN_EXPIRY)))
        {
            LOGGER.error("Unable to insert refresh_token JTI");
            throw new InternalServerErrorResponse("Something went wrong...");
        }

        return jti;
    }

    public static void removeExpiredRefreshTokens(Radio radio)
    {
        try (Connection connection = radio.getModules().get(DatabaseModule.class).getConnection())
        {
            var context = radio.getModules().get(DatabaseModule.class).getContext(connection)
                    .selectFrom(Tables.REFRESH_TOKENS);

            for (var token : context.fetch())
            {
                if (token.getExpiry().isBefore(LocalDateTime.now()))
                {
                    // Remove Token
                    radio.getModules().get(DatabaseModule.class).getContext(connection)
                            .deleteFrom(Tables.REFRESH_TOKENS).where(REFRESH_TOKENS.TOKEN_ID.eq(token.getTokenId())
                            .and(REFRESH_TOKENS.USER_ID.eq(token.getUserId()))).execute();

                    LOGGER.debug("Removed expired refresh_token " + token.getTokenId());
                }
            }
        }
        catch (Exception exception)
        {
            radio.getLogger().error("An SQL error occurred", exception);
        }
    }
}