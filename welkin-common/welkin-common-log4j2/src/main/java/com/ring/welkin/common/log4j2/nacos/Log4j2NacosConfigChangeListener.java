package com.ring.welkin.common.log4j2.nacos;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.builder.api.Component;
import org.apache.logging.log4j.core.config.json.JsonConfiguration;
import org.apache.logging.log4j.core.config.properties.PropertiesConfiguration;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.apache.logging.log4j.core.config.yaml.YamlConfiguration;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.context.ApplicationListener;

import com.alibaba.nacos.api.config.ConfigType;

import lombok.extern.slf4j.Slf4j;

/**
 * log4j2 nacos 配置文件变更事件应用监听器，监听到事件后将新的配置内容更新到log4j2上下文使其生效
 */
@Slf4j
public class Log4j2NacosConfigChangeListener implements ApplicationListener<Log4j2NacosConfigChangeEvent> {

	@Override
	public void onApplicationEvent(Log4j2NacosConfigChangeEvent loggerEvent) {
		try {
			String loggerFactoryClassStr = StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr();
			log.info("get logger factory class >>>> " + loggerFactoryClassStr);
			LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
			InputStream inputStream = loggerEvent.getInputStream();
			ConfigType configType = loggerEvent.getConfigType();
			String dataId = loggerEvent.getDataId();

			log.info("log4j2 reconfigured with nacos changes: dataId = {}, configType = {}", dataId, configType);
			Configuration configuration = null;
			ConfigurationSource configSource = new ConfigurationSource(inputStream);
			switch (configType) {
			case XML:
				configuration = new XmlConfiguration(ctx, configSource);
				break;
			case YAML:
				configuration = new YamlConfiguration(ctx, configSource);
				break;
			case JSON:
				configuration = new JsonConfiguration(ctx, configSource);
				break;
			case PROPERTIES:
				configuration = new PropertiesConfiguration(ctx, configSource, new Component("Properties"));
				break;
			default:
				log.warn("Unsupported config type: {}", configType);
				break;
			}

			if (configuration != null) {
				ctx.reconfigure(configuration);
				log.info("log4j2 re-configuration takes effect.");
			}
		} catch (Exception e) {
			log.info("log4j2 re-configuration failed.", e);
		}
	}
}
