package net.toadless.radio.objects.oauth2;

import java.time.LocalDateTime;

public class Session
{
    private final long id;

    private final String accessToken;
    private final String refreshToken;
    private final LocalDateTime expiry;

    public Session(long id,
                   String accessToken,
                   String refreshToken,
                   LocalDateTime expiry)
    {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiry = expiry;
    }

    public long getId()
    {
        return id;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public LocalDateTime getExpiry()
    {
        return expiry;
    }
}
