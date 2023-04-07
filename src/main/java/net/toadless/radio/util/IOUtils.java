package net.toadless.radio.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.toadless.radio.modules.DatabaseModule;

public class IOUtils
{
    private IOUtils()
    {
        //Overrides the default, public, constructor
    }

    public static InputStream getResourceFile(String fileName)
    {
        InputStream file;
        try
        {
            file = DatabaseModule.class.getClassLoader().getResourceAsStream(fileName);
        }
        catch (Exception exception)
        {
            return null;
        }
        return file;
    }


    public static String convertToString(InputStream inputStream)
    {
        InputStreamReader isReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(isReader);

        StringBuilder stringBuilder = new StringBuilder();
        String str;
        try
        {
            while ((str = reader.readLine()) != null)
            {
                stringBuilder.append(str);
            }
        }
        catch (Exception exception)
        {
            return "";
        }

        return stringBuilder.toString();
    }
}