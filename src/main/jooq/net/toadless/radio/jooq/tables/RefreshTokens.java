/*
 * This file is generated by jOOQ.
 */
package net.toadless.radio.jooq.tables;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.toadless.radio.jooq.Keys;
import net.toadless.radio.jooq.Public;
import net.toadless.radio.jooq.tables.records.RefreshTokensRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RefreshTokens extends TableImpl<RefreshTokensRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.refresh_tokens</code>
     */
    public static final RefreshTokens REFRESH_TOKENS = new RefreshTokens();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RefreshTokensRecord> getRecordType() {
        return RefreshTokensRecord.class;
    }

    /**
     * The column <code>public.refresh_tokens.token_id</code>.
     */
    public final TableField<RefreshTokensRecord, UUID> TOKEN_ID = createField(DSL.name("token_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.refresh_tokens.user_id</code>.
     */
    public final TableField<RefreshTokensRecord, Long> USER_ID = createField(DSL.name("user_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.refresh_tokens.expiry</code>.
     */
    public final TableField<RefreshTokensRecord, LocalDateTime> EXPIRY = createField(DSL.name("expiry"), SQLDataType.LOCALDATETIME(6).nullable(false), this, "");

    private RefreshTokens(Name alias, Table<RefreshTokensRecord> aliased) {
        this(alias, aliased, null);
    }

    private RefreshTokens(Name alias, Table<RefreshTokensRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.refresh_tokens</code> table reference
     */
    public RefreshTokens(String alias) {
        this(DSL.name(alias), REFRESH_TOKENS);
    }

    /**
     * Create an aliased <code>public.refresh_tokens</code> table reference
     */
    public RefreshTokens(Name alias) {
        this(alias, REFRESH_TOKENS);
    }

    /**
     * Create a <code>public.refresh_tokens</code> table reference
     */
    public RefreshTokens() {
        this(DSL.name("refresh_tokens"), null);
    }

    public <O extends Record> RefreshTokens(Table<O> child, ForeignKey<O, RefreshTokensRecord> key) {
        super(child, key, REFRESH_TOKENS);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<RefreshTokensRecord> getPrimaryKey() {
        return Keys.REFRESH_TOKENS_PKEY;
    }

    @Override
    public List<ForeignKey<RefreshTokensRecord, ?>> getReferences() {
        return Arrays.asList(Keys.REFRESH_TOKENS__REFRESH_TOKENS_USER_ID_FKEY);
    }

    private transient Users _users;

    /**
     * Get the implicit join path to the <code>public.users</code> table.
     */
    public Users users() {
        if (_users == null)
            _users = new Users(this, Keys.REFRESH_TOKENS__REFRESH_TOKENS_USER_ID_FKEY);

        return _users;
    }

    @Override
    public RefreshTokens as(String alias) {
        return new RefreshTokens(DSL.name(alias), this);
    }

    @Override
    public RefreshTokens as(Name alias) {
        return new RefreshTokens(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public RefreshTokens rename(String name) {
        return new RefreshTokens(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public RefreshTokens rename(Name name) {
        return new RefreshTokens(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<UUID, Long, LocalDateTime> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}