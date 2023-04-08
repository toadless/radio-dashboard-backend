/*
 * This file is generated by jOOQ.
 */
package net.toadless.radio.jooq;


import net.toadless.radio.jooq.tables.DiscordTokens;
import net.toadless.radio.jooq.tables.Guilds;
import net.toadless.radio.jooq.tables.RefreshTokens;
import net.toadless.radio.jooq.tables.Users;
import net.toadless.radio.jooq.tables.records.DiscordTokensRecord;
import net.toadless.radio.jooq.tables.records.GuildsRecord;
import net.toadless.radio.jooq.tables.records.RefreshTokensRecord;
import net.toadless.radio.jooq.tables.records.UsersRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<DiscordTokensRecord> DISCORD_TOKENS_PKEY = Internal.createUniqueKey(DiscordTokens.DISCORD_TOKENS, DSL.name("discord_tokens_pkey"), new TableField[] { DiscordTokens.DISCORD_TOKENS.USER_ID }, true);
    public static final UniqueKey<GuildsRecord> GUILDS_PKEY = Internal.createUniqueKey(Guilds.GUILDS, DSL.name("guilds_pkey"), new TableField[] { Guilds.GUILDS.GUILD_ID }, true);
    public static final UniqueKey<RefreshTokensRecord> REFRESH_TOKENS_PKEY = Internal.createUniqueKey(RefreshTokens.REFRESH_TOKENS, DSL.name("refresh_tokens_pkey"), new TableField[] { RefreshTokens.REFRESH_TOKENS.TOKEN_ID }, true);
    public static final UniqueKey<UsersRecord> USERS_PKEY = Internal.createUniqueKey(Users.USERS, DSL.name("users_pkey"), new TableField[] { Users.USERS.USER_ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<DiscordTokensRecord, UsersRecord> DISCORD_TOKENS__DISCORD_TOKENS_USER_ID_FKEY = Internal.createForeignKey(DiscordTokens.DISCORD_TOKENS, DSL.name("discord_tokens_user_id_fkey"), new TableField[] { DiscordTokens.DISCORD_TOKENS.USER_ID }, Keys.USERS_PKEY, new TableField[] { Users.USERS.USER_ID }, true);
    public static final ForeignKey<RefreshTokensRecord, UsersRecord> REFRESH_TOKENS__REFRESH_TOKENS_USER_ID_FKEY = Internal.createForeignKey(RefreshTokens.REFRESH_TOKENS, DSL.name("refresh_tokens_user_id_fkey"), new TableField[] { RefreshTokens.REFRESH_TOKENS.USER_ID }, Keys.USERS_PKEY, new TableField[] { Users.USERS.USER_ID }, true);
}
