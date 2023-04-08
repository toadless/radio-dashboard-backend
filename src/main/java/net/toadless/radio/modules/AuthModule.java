package net.toadless.radio.modules;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import net.toadless.radio.Radio;
import net.toadless.radio.objects.auth.UserTokens;
import net.toadless.radio.objects.config.ConfigOption;
import net.toadless.radio.objects.database.RefreshToken;
import net.toadless.radio.objects.module.Module;
import net.toadless.radio.objects.module.Modules;
import net.toadless.radio.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

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

    public UserTokens generateUserTokens(long userId)
    {
        String accessToken = generateAccessToken(userId);

        UUID jti = RefreshToken.generateRefreshToken(radio, userId);
        String refreshToken = generateRefreshToken(userId, jti);

        LOGGER.debug("Generated tokens for " + userId);
        return new UserTokens(accessToken, refreshToken);
    }

    private String generateAccessToken(long userId)
    {
        return Jwts.builder()
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .claim("id", userId)
                .setIssuer(radio.getConfiguration().getString(ConfigOption.JWT_ISSUER))
                .setAudience(radio.getConfiguration().getString(ConfigOption.JWT_AUDIENCE))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(TimeUtils.dateFromLocalDateTime(TimeUtils.nowPlusDuration(TimeUtils.ACCESS_TOKEN_EXPIRY)))
                .signWith(accessTokenKey)
                .compact();
    }

    private String generateRefreshToken(long userId, UUID jti)
    {
        return Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .claim("id", userId)
                .claim("jti", jti)
                .setIssuer(radio.getConfiguration().getString(ConfigOption.JWT_ISSUER))
                .setAudience(radio.getConfiguration().getString(ConfigOption.JWT_AUDIENCE))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(TimeUtils.dateFromLocalDateTime(TimeUtils.nowPlusDuration(TimeUtils.REFRESH_TOKEN_EXPIRY)))
                .signWith(refreshTokenKey)
                .compact();
    }

    private Jws<Claims> parseAccessToken(String token)
    {
        return accessTokenParser.parseClaimsJws(token);
    }

    private Jws<Claims> parseRefreshToken(String token)
    {
        return refreshTokenParser.parseClaimsJws(token);
    }
}