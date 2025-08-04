package org.example.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@SpringBootApplication
@MapperScan("org.example.ai.mapper")
@EnableFeignClients(basePackages = "org.example.ai.client", defaultConfiguration = org.example.ai.config.DefaultFeignConfig.class)
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class SpringAiDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiDemoApplication.class, args);
    }

}
