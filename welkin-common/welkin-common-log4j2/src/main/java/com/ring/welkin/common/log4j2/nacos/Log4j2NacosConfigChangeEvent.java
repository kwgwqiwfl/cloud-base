package com.ring.welkin.common.log4j2.nacos;

import java.io.InputStream;

import org.springframework.context.ApplicationEvent;

import com.alibaba.nacos.api.config.ConfigType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * log4j2 nacos配置文件变更事件
 */
@Setter
@Getter
@ToString
public class Log4j2NacosConfigChangeEvent extends ApplicationEvent {
	private static final long serialVersionUID = -450008801528002742L;

	/**
	 * 配置文件扩展名,支持text,json,xml,yaml,html,properties
	 */
	private final ConfigType configType;

	/**
	 * 配置文件名称
	 */
	private final String dataId;

	/**
	 * logger配置文件转化的输入流
	 */
	private final InputStream inputStream;

	/**
	 * Create a new {@code ApplicationEvent}.
	 *
	 * @param source the object on which the event initially occurred or with which
	 *               the event is associated (never {@code null})
	 */
	public Log4j2NacosConfigChangeEvent(Object source, String dataId, ConfigType configType, InputStream inputStream) {
		super(source);
		this.dataId = dataId;
		this.configType = configType;
		this.inputStream = inputStream;
	}
}
