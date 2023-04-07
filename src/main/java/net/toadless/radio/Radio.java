package net.toadless.radio;

import net.toadless.radio.objects.config.Configuration;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class Radio
{
    private final Logger logger;
    private final LocalDateTime startTimestamp;
    private final Configuration configuration;
    private final OkHttpClient okHttpClient;

    public Radio()
    {
        this.logger = LoggerFactory.getLogger(Radio.class);
        this.configuration = new Configuration(this);
        this.okHttpClient = new OkHttpClient();
        this.startTimestamp = LocalDateTime.now();
    }

    public OkHttpClient getOkHttpClient()
    {
        return okHttpClient;
    }

    public Configuration getConfiguration()
    {
        return this.configuration;
    }

    public Logger getLogger()
    {
        return this.logger;
    }
}