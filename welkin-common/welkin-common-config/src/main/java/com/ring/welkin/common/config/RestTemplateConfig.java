package com.ring.welkin.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @ConditionalOnMissingBean(value = RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

