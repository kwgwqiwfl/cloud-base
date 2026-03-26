package com.ring.welkin.common.webflux.config;

import com.ring.welkin.common.webflux.saas.DefaultServerSaasContextWebFilter;
import com.ring.welkin.common.webflux.saas.ServerSaasContextWebFilter;
import com.ring.welkin.common.webflux.server.error.DefaultErrorResponseDeterminer;
import com.ring.welkin.common.webflux.server.error.ErrorResponseDeterminer;
import com.ring.welkin.common.webflux.server.error.GlobalErrorAttributes;
import com.ring.welkin.common.webflux.server.result.ServerFilterFieldsHandlerResultHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@Configuration
public class WebFluxServerConfiguration {

	@Bean
	@ConditionalOnMissingBean(ServerSaasContextWebFilter.class)
	public ServerSaasContextWebFilter serverSaasContextWebFilter() {
		return new DefaultServerSaasContextWebFilter();
	}

	@Bean
	public ServerFilterFieldsHandlerResultHandler serverFilterFieldsHandlerResultHandler(
		ServerCodecConfigurer serverCodecConfigurer, RequestedContentTypeResolver webFluxContentTypeResolver,
		ReactiveAdapterRegistry webFluxAdapterRegistry) {
		return new ServerFilterFieldsHandlerResultHandler(serverCodecConfigurer.getWriters(),
			webFluxContentTypeResolver, webFluxAdapterRegistry);
	}

	@Bean
	@ConditionalOnMissingBean(value = ErrorResponseDeterminer.class)
	public ErrorResponseDeterminer errorResponseDeterminer() {
		return new DefaultErrorResponseDeterminer();
	}

	@Bean
	@Primary
	public GlobalErrorAttributes errorAttributes(ServerProperties serverProperties,
			ErrorResponseDeterminer errorResponseDeterminer) {
		return new GlobalErrorAttributes(serverProperties.getError().isIncludeException(), errorResponseDeterminer);
	}

}
