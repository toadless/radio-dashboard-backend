package net.toadless.radio.modules;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import net.toadless.radio.Radio;
import net.toadless.radio.jooq.tables.records.RefreshTokensRecord;
import net.toadless.radio.objects.auth.UserTokens;
import net.toadless.radio.objects.config.ConfigOption;
import net.toadless.radio.objects.database.RefreshToken;
import net.toadless.radio.objects.module.Module;
import net.toadless.radio.objects.module.Modules;
import net.toadless.radio.util.AuthUtils;
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

    public UserTokens refreshUserTokens(String token)
    {
        try
        {
            Jws<Claims> jwt = parseRefreshToken(token);

            if (!jwt.getBody().getSubject().equals(REFRESH_TOKEN_SUBJECT))
            {
                throw new BadRequestResponse("Invalid subject in 'refresh_token' body");
            }

            String jti = jwt.getBody().get("jti", String.class);

            RefreshTokensRecord refreshToken = RefreshToken.getRefreshToken(radio, UUID.fromString(jti));
            if (!refreshToken.getUserId().equals(Long.parseLong(jwt.getBody().get("id").toString())))
            {
                // Something fishy is going on (should never come up)
                throw new UnauthorizedResponse("This 'refresh_token' is malformed");
            }

            // invalidate refresh token
            RefreshToken.removeRefreshToken(radio, refreshToken.getUserId(), refreshToken.getTokenId());

            return generateUserTokens(refreshToken.getUserId());
        } catch (ExpiredJwtException e)
        {
            throw new UnauthorizedResponse("The provided 'refresh_token' has expired");
        } catch (JwtException e)
        {
            throw new UnauthorizedResponse("The provided 'refresh_token' is invalid");
        }
    }

    public void authenticateUser(Context ctx)
    {
        if (AuthUtils.isOptionsRequest(ctx)) return;

        String token = AuthUtils.pullAccessTokenFromAuthorizationHeader(ctx);

        try
        {
            Jws<Claims> jwt = parseAccessToken(token);

            if (!jwt.getBody().getSubject().equals(ACCESS_TOKEN_SUBJECT))
            {
                throw new BadRequestResponse("Invalid subject in 'access_token' body");
            }

            long userId = Long.parseLong(jwt.getBody().get("id").toString());

            if (radio.getModules().get(OAuth2Module.class).getUser(userId) == null)
            {
                throw new BadRequestResponse("The provided 'access_token' refers to a user that does not exist");
            }

            ctx.attribute("user_id", userId);
        } catch (ExpiredJwtException e)
        {
            throw new UnauthorizedResponse("The provided 'access_token' has expired");
        } catch (JwtException e)
        {
            System.out.println(e);
            throw new UnauthorizedResponse("The provided 'access_token' is invalid");
        }
    }

    private String generateAccessToken(long userId)
    {
        return Jwts.builder()
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .claim("id", String.valueOf(userId))
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
                .claim("id", String.valueOf(userId))
                .claim("jti", jti.toString())
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