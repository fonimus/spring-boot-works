package io.fonimus;

import io.fonimus.configs.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(Properties.class)
public class ConfigsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigsApplication.class, args);
    }
}
