package net.toadless.radio.util;

import net.dv8tion.jda.api.utils.MarkdownSanitizer;

public class StringUtils
{
    private StringUtils()
    {
        //Overrides the default, public, constructor
    }

    public static String markdownSanitize(String text)
    {
        return MarkdownSanitizer.sanitize(text, MarkdownSanitizer.SanitizationStrategy.REMOVE);
    }
}