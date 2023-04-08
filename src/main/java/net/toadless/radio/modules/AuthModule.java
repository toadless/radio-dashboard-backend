package net.toadless.radio.modules;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import net.toadless.radio.Radio;
import net.toadless.radio.objects.config.ConfigOption;
import net.toadless.radio.objects.module.Module;
import net.toadless.radio.objects.module.Modules;
import org.jooq.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;

public class AuthModule extends Module
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthModule.class);
    private static final String ACCESS_TOKEN_SUBJECT = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN_SUBJECT = "REFRESH_TOKEN";

    private final SecretKey accessTokenKey;
    private final SecretKey refreshTokenKey;

    private final JwtParser accessTokenParser;
    private final JwtParser refreshTokenParser;

    public AuthModule(Radio radio, Modules modules)
    {
        super(radio, modules);

        this.accessTokenKey = Keys.hmacShaKeyFor(radio.getConfiguration()
                .getString(ConfigOption.JWT_ACCESS_TOKEN_SECRET).getBytes());
        this.refreshTokenKey = Keys.hmacShaKeyFor(radio.getConfiguration()
                .getString(ConfigOption.JWT_REFRESH_TOKEN_SECRET).getBytes());

        this.accessTokenParser = Jwts.parserBuilder().setSigningKey(accessTokenKey).build();
        this.refreshTokenParser = Jwts.parserBuilder().setSigningKey(refreshTokenKey).build();
    }
}