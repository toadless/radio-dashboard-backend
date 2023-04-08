package net.toadless.radio.objects.oauth2;

public class User
{
    private final long id;

    private final String name;
    private final String discriminator;
    private final String avatar;

    public User(long id,
                String name,
                String discriminator,
                String avatar)
    {
        this.id = id;
        this.name = name;
        this.discriminator = discriminator;
        this.avatar = avatar;
    }

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDiscriminator()
    {
        return discriminator;
    }

    public String getAvatar()
    {
        return avatar;
    }
}