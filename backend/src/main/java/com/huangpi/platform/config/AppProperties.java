package com.huangpi.platform.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(Jwt jwt, Storage storage, Wechat wechat) {

    public record Jwt(String secret, Duration expiration) {
    }

    public record Storage(String directory, String publicBaseUrl) {
    }

    public record Wechat(boolean mockEnabled, String appId, String appSecret) {
    }
}
