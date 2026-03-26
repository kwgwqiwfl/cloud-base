package com.ring.welkin.common.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ring.welkin.common.core.jackson.serializer.LongNumberSafeToStringSerializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(
		JacksonExtSerializationProperties extProperties) {
		Jackson2ObjectMapperBuilderCustomizer cunstomizer = new Jackson2ObjectMapperBuilderCustomizer() {
			@Override
			public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
                jacksonObjectMapperBuilder.modules(new JavaTimeModule());
				if (extProperties != null && extProperties.isWriteLongAsString()) {
					jacksonObjectMapperBuilder.serializerByType(Long.TYPE, LongNumberSafeToStringSerializer.INSTANCE);
					jacksonObjectMapperBuilder.serializerByType(Long.class, LongNumberSafeToStringSerializer.INSTANCE);
				}
			}
		};
		return cunstomizer;
	}

	@Getter
	@Setter
	@Configuration
	@ConfigurationProperties(prefix = JacksonExtSerializationProperties.PREFIX)
	public static class JacksonExtSerializationProperties {
		public static final String PREFIX = "spring.jackson.ext.serialization";

		/**
		 * 是否序列化Long类型数据为String类型
		 */
		private boolean writeLongAsString = false;

		/**
		 * 数组或集合为null时是否序列化为空字符串
		 */
		private boolean writeNullValueAsEmptyString = false;

		/**
		 * 数组或集合为null时是否序列化为空字符串
		 */
		private boolean writeNullArrayAsEmptyString = false;

		/**
		 * 字符串为null时是否序列化为空字符串
		 */
		private boolean writeNullStringAsEmptyString = false;

		/**
		 * Number为null时是否序列化为空字符串
		 */
		private boolean writeNullNumberAsEmptyString = false;

		/**
		 * Boolean为null时是否序列化为空字符串
		 */
		private boolean writeNullBooleanAsEmptyString = false;
	}
}
