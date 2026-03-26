package com.ring.welkin.common.webmvc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ring.welkin.common.config.JacksonConfig.JacksonExtSerializationProperties;
import com.ring.welkin.common.config.serialize.jackson.serializer.CustomizeBeanSerializerModifier;
import com.ring.welkin.common.web.method.annotation.servlet.RequestHeaderAliasMethodArgumentResolver;
import com.ring.welkin.common.web.method.annotation.servlet.RequestParamAliasMethodArgumentResolver;
import com.ring.welkin.common.webmvc.servlet.result.PathTweakingRequestMappingHandlerMapping;
import com.ring.welkin.common.webmvc.servlet.result.ServletFilterFieldsHandlerResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@Configuration
public class WebMvcExtConfig implements WebMvcConfigurer, ApplicationContextAware {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private JacksonExtSerializationProperties properties;
	@Autowired
	private ServletFilterFieldsHandlerResultHandler servletFilterFieldsHandlerResultHandler;

	private ConfigurableApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		if (applicationContext instanceof ConfigurableApplicationContext) {
			this.applicationContext = (ConfigurableApplicationContext) applicationContext;
		}
	}

	@Bean
	public ServletListenerRegistrationBean<RequestContextListener> servletListenerRegistrationBean() {
		return new ServletListenerRegistrationBean<RequestContextListener>(new RequestContextListener());
	}

	private ObjectMapper enhanceObjectMapper(ObjectMapper objectMapper, JacksonExtSerializationProperties properties) {
		objectMapper.setSerializerFactory(objectMapper.getSerializerFactory()
			.withSerializerModifier(new CustomizeBeanSerializerModifier(properties.isWriteNullValueAsEmptyString(),
				properties.isWriteNullArrayAsEmptyString(), properties.isWriteNullStringAsEmptyString(),
				properties.isWriteNullNumberAsEmptyString(), properties.isWriteNullBooleanAsEmptyString())));
		return objectMapper;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
		converters.add(0, new MappingJackson2HttpMessageConverter(enhanceObjectMapper(objectMapper, properties)));
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
		handlers.removeIf(handler -> handler instanceof ServletFilterFieldsHandlerResultHandler);
		handlers.add(0, servletFilterFieldsHandlerResultHandler);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 设置允许跨域的路由
		registry.addMapping("/**")
			// 是否允许证书（cookies）
			.allowCredentials(true)
			// 设置允许跨域请求的域名
			.allowedOriginPatterns("*")
			// 设置允许的方法
			.allowedMethods("*")
			// 设置header
			.allowedHeaders("*")
			// 跨域允许时间
			.maxAge(3600);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(0, new RequestParamAliasMethodArgumentResolver(applicationContext.getBeanFactory(), true));
		resolvers.add(0, new RequestHeaderAliasMethodArgumentResolver(applicationContext.getBeanFactory()));
	}

	@Configuration
	class WebMvcRegistrationsConfig implements WebMvcRegistrations {
		@Override
		public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
			return new PathTweakingRequestMappingHandlerMapping();
		}
	}
}
