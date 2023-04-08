/*
 * This file is generated by jOOQ.
 */
package net.toadless.radio.jooq.tables.records;


import java.time.LocalDateTime;

import net.toadless.radio.jooq.tables.DiscordTokens;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DiscordTokensRecord extends UpdatableRecordImpl<DiscordTokensRecord> implements Record4<Long, String, String, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.discord_tokens.user_id</code>.
     */
    public DiscordTokensRecord setUserId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.discord_tokens.user_id</code>.
     */
    public Long getUserId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.discord_tokens.access_token</code>.
     */
    public DiscordTokensRecord setAccessToken(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.discord_tokens.access_token</code>.
     */
    public String getAccessToken() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.discord_tokens.refresh_token</code>.
     */
    public DiscordTokensRecord setRefreshToken(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.discord_tokens.refresh_token</code>.
     */
    public String getRefreshToken() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.discord_tokens.expiry</code>.
     */
    public DiscordTokensRecord setExpiry(LocalDateTime value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.discord_tokens.expiry</code>.
     */
    public LocalDateTime getExpiry() {
        return (LocalDateTime) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, String, String, LocalDateTime> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Long, String, String, LocalDateTime> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return DiscordTokens.DISCORD_TOKENS.USER_ID;
    }

    @Override
    public Field<String> field2() {
        return DiscordTokens.DISCORD_TOKENS.ACCESS_TOKEN;
    }

    @Override
    public Field<String> field3() {
        return DiscordTokens.DISCORD_TOKENS.REFRESH_TOKEN;
    }

    @Override
    public Field<LocalDateTime> field4() {
        return DiscordTokens.DISCORD_TOKENS.EXPIRY;
    }

    @Override
    public Long component1() {
        return getUserId();
    }

    @Override
    public String component2() {
        return getAccessToken();
    }

    @Override
    public String component3() {
        return getRefreshToken();
    }

    @Override
    public LocalDateTime component4() {
        return getExpiry();
    }

    @Override
    public Long value1() {
        return getUserId();
    }

    @Override
    public String value2() {
        return getAccessToken();
    }

    @Override
    public String value3() {
        return getRefreshToken();
    }

    @Override
    public LocalDateTime value4() {
        return getExpiry();
    }

    @Override
    public DiscordTokensRecord value1(Long value) {
        setUserId(value);
        return this;
    }

    @Override
    public DiscordTokensRecord value2(String value) {
        setAccessToken(value);
        return this;
    }

    @Override
    public DiscordTokensRecord value3(String value) {
        setRefreshToken(value);
        return this;
    }

    @Override
    public DiscordTokensRecord value4(LocalDateTime value) {
        setExpiry(value);
        return this;
    }

    @Override
    public DiscordTokensRecord values(Long value1, String value2, String value3, LocalDateTime value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached DiscordTokensRecord
     */
    public DiscordTokensRecord() {
        super(DiscordTokens.DISCORD_TOKENS);
    }

    /**
     * Create a detached, initialised DiscordTokensRecord
     */
    public DiscordTokensRecord(Long userId, String accessToken, String refreshToken, LocalDateTime expiry) {
        super(DiscordTokens.DISCORD_TOKENS);

        setUserId(userId);
        setAccessToken(accessToken);
        setRefreshToken(refreshToken);
        setExpiry(expiry);
    }

    /**
     * Create a detached, initialised DiscordTokensRecord
     */
    public DiscordTokensRecord(net.toadless.radio.jooq.tables.pojos.DiscordTokens value) {
        super(DiscordTokens.DISCORD_TOKENS);

        if (value != null) {
            setUserId(value.getUserId());
            setAccessToken(value.getAccessToken());
            setRefreshToken(value.getRefreshToken());
            setExpiry(value.getExpiry());
        }
    }
}
