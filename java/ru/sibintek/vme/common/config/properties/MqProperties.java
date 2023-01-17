package ru.sibintek.vme.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mq")
public class MqProperties {
    private Queue queue;
    private Exchange exchange;
    private RoutingKey rk;
    private String appUid;
    private int apiVersion;

    @Data
    public static class Queue {
        private String incoming;
        private String outcoming;
    }

    @Data
    public static class Exchange {
        private String incoming;
        private String outcoming;
    }

    @Data
    public static class RoutingKey {
        private String incoming;
        private String outcoming;
    }
}
