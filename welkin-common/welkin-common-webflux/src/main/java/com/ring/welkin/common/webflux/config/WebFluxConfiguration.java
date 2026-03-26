package com.ring.welkin.common.webflux.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ring.welkin.common.config.JacksonConfig.JacksonExtSerializationProperties;
import com.ring.welkin.common.config.serialize.jackson.serializer.CustomizeBeanSerializerModifier;
import com.ring.welkin.common.web.method.annotation.reactive.ReactiveRequestHeaderAliasMethodArgumentResolver;
import com.ring.welkin.common.web.method.annotation.reactive.ReactiveRequestParamAliasMethodArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.codec.CodecProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.ResourceHandlerRegistration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Configuration
public class WebFluxConfiguration implements WebFluxConfigurer, ApplicationContextAware {
	@Autowired
	@Nullable
	private WebProperties webProperties;
	@Autowired
	private ObjectMapper jacksonObjectMapper;
	@Autowired
	private JacksonExtSerializationProperties extProperties;
	@Autowired
	private CodecProperties codecProperties;

	// set to 2M
	private long maxInMemorySize = 2 * 1024 * 1024;

	private ConfigurableApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		if (applicationContext instanceof ConfigurableApplicationContext) {
			this.applicationContext = (ConfigurableApplicationContext) applicationContext;
		}
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
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (webProperties != null) {
			Resources resources = webProperties.getResources();
			String[] staticLocations = resources.getStaticLocations();
			if (resources.isAddMappings() && staticLocations != null && staticLocations.length > 0) {
				ResourceHandlerRegistration resourceHandler = registry.addResourceHandler("/**");
				for (String staticLocation : staticLocations) {
					resourceHandler.addResourceLocations(staticLocation);
				}
			}
		}
	}

	@Override
	public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
		DefaultPartHttpMessageReader partReader = new DefaultPartHttpMessageReader();
		partReader.setBlockingOperationScheduler(Schedulers.immediate());
		configurer.defaultCodecs().multipartReader(new MultipartHttpMessageReader(partReader));

		// SynchronossPartHttpMessageReader syncPartReader = new
		// SynchronossPartHttpMessageReader();
		// partReader.setMaxParts(Integer.parseInt(commonConfig.getMaxparts()));
		// 字节bytes
		// partReader.setMaxDiskUsagePerPart(Integer.parseInt(commonConfig.getMaxFileSize()));
		// partReader.setEnableLoggingRequestDetails(true);

		// 单文件上传大小限制
		// MultipartHttpMessageReader multipartReader = new
		// MultipartHttpMessageReader(partReader);
		// multipartReader.setEnableLoggingRequestDetails(true);
		// configurer.defaultCodecs().multipartReader(multipartReader);

		if (codecProperties != null) {
			DataSize dataSize = codecProperties.getMaxInMemorySize();
			if (dataSize != null) {
				maxInMemorySize = dataSize.toBytes();
			}
		}
		configurer.defaultCodecs().maxInMemorySize((int) maxInMemorySize);

		log.debug("add custom objectMapper and BeanSerializerModifier to Jackson2JsonDecoder and Jackson2JsonEncoder: "
				+ jacksonObjectMapper + ",hashcode:" + jacksonObjectMapper.hashCode());

		/** 为objectMapper注册一个带有SerializerModifier的Factory */
		jacksonObjectMapper.setSerializerFactory(jacksonObjectMapper.getSerializerFactory()
				.withSerializerModifier(new CustomizeBeanSerializerModifier(
						extProperties.isWriteNullValueAsEmptyString(), extProperties.isWriteNullArrayAsEmptyString(),
					extProperties.isWriteNullStringAsEmptyString(), extProperties.isWriteNullNumberAsEmptyString(),
					extProperties.isWriteNullBooleanAsEmptyString())));
		// 配置jackson
		ServerCodecConfigurer.ServerDefaultCodecs defaultCodecs = configurer.defaultCodecs();
		defaultCodecs.enableLoggingRequestDetails(true);
		MimeType[] mimeTypes = new MimeType[]{MediaType.APPLICATION_JSON, MediaType.APPLICATION_NDJSON,
			MediaType.APPLICATION_PROBLEM_JSON, MediaType.valueOf("application/vnd.spring-boot.actuator.v3+json")};
		defaultCodecs.jackson2JsonDecoder(new Jackson2JsonDecoder(jacksonObjectMapper, mimeTypes));
		defaultCodecs.jackson2JsonEncoder(new Jackson2JsonEncoder(jacksonObjectMapper, mimeTypes));
	}

	@Override
	public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
		configurer.addCustomResolver(
			new ReactiveRequestHeaderAliasMethodArgumentResolver(applicationContext.getBeanFactory(),
				ReactiveAdapterRegistry.getSharedInstance()),
			new ReactiveRequestParamAliasMethodArgumentResolver(applicationContext.getBeanFactory(),
				ReactiveAdapterRegistry.getSharedInstance()));
	}

}
