/*
 * Copyright (c) Worldline 2018.
 */

package io.fonimus.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class Conf {

    private Properties properties;

    public Conf(Properties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        LOGGER.info(">>> {}", properties);
    }
}
