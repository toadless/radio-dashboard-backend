package net.toadless.radio.util;

import io.javalin.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthUtils.class);

    private AuthUtils()
    {
        //Overrides the default, public, constructor
    }

    public static boolean isOptionsRequest(Context ctx)
    {
        if (ctx.method() == HandlerType.OPTIONS)
        {
            return true;
        }

        return false;
    }

    public static String pullAccessTokenFromAuthorizationHeader(Context ctx)
    {
        String header = ctx.header(Header.AUTHORIZATION);

        if (header == null || header.isEmpty())
        {
            throw new BadRequestResponse("No 'Authorization' header provided");
        }

        // Split at space
        String[] contents = header.split("\\s+");

        // index 0 should be token type, eg "Bearer"
        if (!contents[0].equalsIgnoreCase("Bearer"))
        {
            throw new UnauthorizedResponse("Non-Bearer-token provided in 'Authorization' header");
        }

        if (contents[1] == null || contents[1].equals(""))
        {
            throw new UnauthorizedResponse("No token provided in 'Authorization' header");
        }

        return contents[1];
    }
}