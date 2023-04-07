/*
 * This file is generated by jOOQ.
 */
package net.toadless.radio.jooq.tables.records;


import net.toadless.radio.jooq.tables.Users;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UsersRecord extends UpdatableRecordImpl<UsersRecord> implements Record4<Long, String, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.users.user_id</code>.
     */
    public UsersRecord setUserId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.users.user_id</code>.
     */
    public Long getUserId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.users.name</code>.
     */
    public UsersRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.users.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.users.discriminator</code>.
     */
    public UsersRecord setDiscriminator(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.users.discriminator</code>.
     */
    public String getDiscriminator() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.users.avatar</code>.
     */
    public UsersRecord setAvatar(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.users.avatar</code>.
     */
    public String getAvatar() {
        return (String) get(3);
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
    public Row4<Long, String, String, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Long, String, String, String> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Users.USERS.USER_ID;
    }

    @Override
    public Field<String> field2() {
        return Users.USERS.NAME;
    }

    @Override
    public Field<String> field3() {
        return Users.USERS.DISCRIMINATOR;
    }

    @Override
    public Field<String> field4() {
        return Users.USERS.AVATAR;
    }

    @Override
    public Long component1() {
        return getUserId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public String component3() {
        return getDiscriminator();
    }

    @Override
    public String component4() {
        return getAvatar();
    }

    @Override
    public Long value1() {
        return getUserId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public String value3() {
        return getDiscriminator();
    }

    @Override
    public String value4() {
        return getAvatar();
    }

    @Override
    public UsersRecord value1(Long value) {
        setUserId(value);
        return this;
    }

    @Override
    public UsersRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public UsersRecord value3(String value) {
        setDiscriminator(value);
        return this;
    }

    @Override
    public UsersRecord value4(String value) {
        setAvatar(value);
        return this;
    }

    @Override
    public UsersRecord values(Long value1, String value2, String value3, String value4) {
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
     * Create a detached UsersRecord
     */
    public UsersRecord() {
        super(Users.USERS);
    }

    /**
     * Create a detached, initialised UsersRecord
     */
    public UsersRecord(Long userId, String name, String discriminator, String avatar) {
        super(Users.USERS);

        setUserId(userId);
        setName(name);
        setDiscriminator(discriminator);
        setAvatar(avatar);
    }

    /**
     * Create a detached, initialised UsersRecord
     */
    public UsersRecord(net.toadless.radio.jooq.tables.pojos.Users value) {
        super(Users.USERS);

        if (value != null) {
            setUserId(value.getUserId());
            setName(value.getName());
            setDiscriminator(value.getDiscriminator());
            setAvatar(value.getAvatar());
        }
    }
}
