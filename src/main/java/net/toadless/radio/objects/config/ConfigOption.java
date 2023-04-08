package net.toadless.radio.objects.config;

public enum ConfigOption
{
    PORT("port"),

    TOKEN("token"),
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),

    // OAuth2
    OAUTH2_KEY("oauth2.key"),
    OAUTH2_URL("oauth2.url"),

    // JWT
    JWT_ACCESS_TOKEN_SECRET("jwt.access_token_secret"),
    JWT_REFRESH_TOKEN_SECRET("jwt.refresh_token_secret"),

    JWT_ISSUER("jwt.issuer"),
    JWT_AUDIENCE("jwt.audience"),

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