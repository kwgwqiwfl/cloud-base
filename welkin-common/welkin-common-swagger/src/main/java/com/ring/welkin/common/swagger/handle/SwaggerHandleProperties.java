package com.ring.welkin.common.swagger.handle;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = SwaggerHandleProperties.PREFIX)
public class SwaggerHandleProperties {
	public static final String PREFIX = "swagger2.handler";

	/**
	 * 是否自动初始化API Operation
	 */
	private boolean enabled = true;

}
