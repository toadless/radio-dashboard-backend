package net.toadless.radio.objects.oauth2;

public enum Scope
{
    BOT("bot"),
    CONNECTIONS("connections"),
    EMAIL("email"),
    IDENTIFY("identify"),
    GUILDS("guilds"),
    GUILDS_JOIN("guilds.join"),
    MESSAGES_READ("messages.read"),
    UNKNOWN("");

    private final String identifier;

    Scope(String identifier)
    {
        this.identifier = identifier;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public static String join(Scope... scopes)
    {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < scopes.length; i++)
        {
            result.append(scopes[i].getIdentifier());

            if (i != scopes.length)
            {
                result.append(", ");
            }
        }

        return result.toString();
    }
}