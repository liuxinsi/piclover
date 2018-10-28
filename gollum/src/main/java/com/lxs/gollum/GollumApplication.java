package com.lxs.gollum;

import com.lxs.sml.filter.LoggingFormat;
import com.lxs.sml.filter.reactive.LoggingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.WebFilter;

/**
 * 入口。
 */
@SpringBootApplication
public class GollumApplication {
    @Bean
    @Order(0)
    public WebFilter logging() {
        LoggingFormat.addIgnoreUrl("/api/precious/v1/getPrecious");
        return new LoggingFilter();
    }

    public static void main(String[] args) {
        SpringApplication.run(GollumApplication.class, args);
    }
}
