package com.ring.welkin.common.log4j2.nacos;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.alibaba.nacos.api.config.ConfigType;

import lombok.Data;

/**
 * log4j2 nacos 配置项
 */
@Data
@ConfigurationProperties(prefix = Log4j2NacosConfigProperties.PREFIX, ignoreInvalidFields = true, ignoreUnknownFields = true)
public class Log4j2NacosConfigProperties {
	public static final String PREFIX = "log4j2.nacos.config";

	/**
	 * 是否启用nacos上的配置文件，默认值true
	 */
	private boolean enabled = true;

	/**
	 * 配置文件扩展名，默认从dataId名称中解析，支持text,json,xml,yaml,html,properties
	 */
	private ConfigType configType;

	/**
	 * 配置文件名称，默认值“log4j2.xml”
	 */
	private String dataId = "log4j2.xml";

	/**
	 * nacos地址，默认值为null
	 */
	private String serverAddr;

	/**
	 * 命名空间，默认值为null
	 */
	private String namespace;

	/**
	 * 分组名称，默认值为null
	 */
	private String group;

	/**
	 * 用户名，默认值为null
	 */
	private String username;

	/**
	 * 密码，默认值为null
	 */
	private String password;

	public ConfigType getConfigType() {
		if (configType == null) {
			int lastIndexOf = dataId.lastIndexOf(".");
			if (lastIndexOf > 0) {
				String fileExtension = dataId.substring(lastIndexOf + 1, dataId.length());
				if (fileExtension != null) {
					return ConfigType.valueOf(fileExtension.toUpperCase());
				}
			}
		}
		return ConfigType.XML;
	}
}
