package net.toadless.radio.objects.database;

import io.javalin.http.InternalServerErrorResponse;
import net.toadless.radio.Radio;
import net.toadless.radio.jooq.Tables;
import net.toadless.radio.jooq.tables.records.GuildsRecord;
import net.toadless.radio.modules.DatabaseModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

import static net.toadless.radio.jooq.tables.Guilds.GUILDS;

public class GuildConfig
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GuildConfig.class);

    private GuildConfig()
    {
        //Overrides the default, public, constructor
    }

    public GuildsRecord getGuildConfig(Radio radio, long guildId)
    {
        try (Connection connection = radio.getModules().get(DatabaseModule.class).getConnection())
        {
            var context = radio.getModules().get(DatabaseModule.class).getContext(connection);
            var existsQuery = context.selectFrom(Tables.GUILDS)
                    .where(GUILDS.GUILD_ID.eq(guildId));

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
            LOGGER.error("An SQL error occurred", exception);
            throw new InternalServerErrorResponse("Something went wrong...");
        }
    }

    public boolean setGuildPrefix(Radio radio, long guildId, String prefix)
    {
        try (Connection connection = radio.getModules().get(DatabaseModule.class).getConnection())
        {
            var context = radio.getModules().get(DatabaseModule.class).getContext(connection)
                    .update(Tables.GUILDS)
                    .set(GUILDS.PREFIX, prefix)
                    .where(GUILDS.GUILD_ID.eq(guildId));

            context.execute();
            return true;
        }
        catch (Exception exception)
        {
            LOGGER.error("An SQL error occurred", exception);
            return false;
        }
    }

    public boolean setDJRole(Radio radio, long guildId, long djRole)
    {
        try (Connection connection = radio.getModules().get(DatabaseModule.class).getConnection())
        {
            var context = radio.getModules().get(DatabaseModule.class).getContext(connection)
                    .update(Tables.GUILDS)
                    .set(GUILDS.DJ_ROLE, djRole)
                    .where(GUILDS.GUILD_ID.eq(guildId));

            context.execute();
            return true;
        }
        catch (Exception exception)
        {
            LOGGER.error("An SQL error occurred", exception);
            return false;
        }
    }
}