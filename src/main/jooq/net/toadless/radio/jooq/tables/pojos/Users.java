/*
 * This file is generated by jOOQ.
 */
package net.toadless.radio.jooq.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long   userId;
    private final String name;
    private final String discriminator;
    private final String avatar;

    public Users(Users value) {
        this.userId = value.userId;
        this.name = value.name;
        this.discriminator = value.discriminator;
        this.avatar = value.avatar;
    }

    public Users(
        Long   userId,
        String name,
        String discriminator,
        String avatar
    ) {
        this.userId = userId;
        this.name = name;
        this.discriminator = discriminator;
        this.avatar = avatar;
    }

    /**
     * Getter for <code>public.users.user_id</code>.
     */
    public Long getUserId() {
        return this.userId;
    }

    /**
     * Getter for <code>public.users.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for <code>public.users.discriminator</code>.
     */
    public String getDiscriminator() {
        return this.discriminator;
    }

    /**
     * Getter for <code>public.users.avatar</code>.
     */
    public String getAvatar() {
        return this.avatar;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Users (");

        sb.append(userId);
        sb.append(", ").append(name);
        sb.append(", ").append(discriminator);
        sb.append(", ").append(avatar);

        sb.append(")");
        return sb.toString();
    }
}
