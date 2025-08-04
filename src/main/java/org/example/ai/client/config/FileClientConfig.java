package org.example.ai.client.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author chenxuanrao06@gmail.com
 */
public class FileClientConfig {

    @Value("${service.file.api-key}")
    private String apiKey;

    @Bean
    public RequestInterceptor headerInterceptor() {
        return template -> template.header("X-API-KEY", apiKey);
    }
}
