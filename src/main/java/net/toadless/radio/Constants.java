package net.toadless.radio;

import net.dv8tion.jda.api.JDAInfo;

public class Constants
{
    public static final String VERSION = "1.0.0";
    public static final String DISCORD_API = String.format("https://discord.com/api/v%s", JDAInfo.DISCORD_REST_VERSION);
    public static final String REDIRECT_URI = "auth/redirect";

    private Constants()
    {
        //Overrides the default, public, constructor
    }
}