package com.ring.welkin.common.log4j2.nacos;

import java.io.InputStream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;

import lombok.RequiredArgsConstructor;

/**
 * log4j2 使用nacos配置文件配置类
 */
@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(prefix = Log4j2NacosConfigProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(value = { Log4j2NacosConfigProperties.class })
public class Log4j2NacosConfigChangeConfiguration {

	private final ApplicationContext applicationContext;
	private final Log4j2NacosConfigProperties properties;

	// @formatter:off
    @NacosConfigListener(
            groupId = "${log4j2.nacos.config.group:${spring.cloud.nacos.config.group}}",
            dataId = "${log4j2.nacos.config.data-id:log4j2.xml}",
            properties = @NacosProperties(
                    serverAddr = "${log4j2.nacos.config.server-addr:${spring.cloud.nacos.config.server-addr}}",
                    namespace = "${log4j2.nacos.config.namespace:${spring.cloud.nacos.config.namespace}}",
                    username = "${log4j2.nacos.config.username:${spring.cloud.nacos.config.username}}",
                    password = "${log4j2.nacos.config.password:${spring.cloud.nacos.config.password}}"
            ),
            timeout = 3000,
            converter = Log4j2NacosConfigConverter.class)
    public void onChange(InputStream inputStream) {
        applicationContext.publishEvent(
                new Log4j2NacosConfigChangeEvent(Log4j2NacosConfigChangeConfiguration.class,properties.getDataId(), properties.getConfigType(), inputStream));
    }
    // @formatter:on

	@Bean
	Log4j2NacosConfigChangeListener log4j2NacosConfigChangeListener() {
		return new Log4j2NacosConfigChangeListener();
	}
}
