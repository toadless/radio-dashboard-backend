package net.toadless.radio.objects.config;

public enum ConfigOption
{
    PORT("port"),

    TOKEN("token"),
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),

    // OAuth2
    OAUTH2_KEY("oauth2.key"),

    DBUSERNAME("database.username"),
    DBPASSWORD("database.password"),
    DBURL("database.url"),
    DBDRIVER("database.driver"); // jdbc:type://host:port/database

    private final String key;

    ConfigOption(String key)
    {
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }
}