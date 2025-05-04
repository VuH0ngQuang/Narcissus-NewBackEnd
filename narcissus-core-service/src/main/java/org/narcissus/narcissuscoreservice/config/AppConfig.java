package org.narcissus.narcissuscoreservice.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {
    private final Kafka kafka;
    private final Channels channels;

    @Data
    public static class Kafka{
        private String url;
        private int timeout;
        private String clusterId;
    }

    @Data
    public static class Channels {
        private String user;
        private String core;
        private String notification;
    }
}
