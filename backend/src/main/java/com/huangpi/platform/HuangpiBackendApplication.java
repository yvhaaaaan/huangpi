package com.huangpi.platform;

import com.huangpi.platform.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableConfigurationProperties(AppProperties.class)
public class HuangpiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuangpiBackendApplication.class, args);
    }
}
