package com.ring.welkin.common.webflux.saas;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ring.welkin.common.core.saas.SaasContext;
import com.ring.welkin.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * SaasContext 拦截初始化
 *
 * @author cloud
 * @date 2019年12月17日 上午10:37:51
 */
@Slf4j
public class DefaultServerSaasContextWebFilter implements ServerSaasContextWebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		try {
			ServerHttpRequest request = exchange.getRequest();
			HttpHeaders headers = request.getHeaders();
			String saasContextHeader = headers.getFirst(SaasContext.SAAS_CONTEXT_KEY);
			String userId = headers.getFirst("userId");
			if (StringUtils.isNotEmpty(saasContextHeader)) {
				String decode = null;
				try {
					decode = URLDecoder.decode(saasContextHeader, "UTF-8");
					SaasContext saasContext = JsonUtils.fromJson(decode, new TypeReference<SaasContext>() {
					});
					if (saasContext != null) {
						SaasContext.setCurrentSaasContext(saasContext);
					}
					log.trace("fix current SaasContext :{}", decode);
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				}
			} else if (StringUtils.isNotEmpty(userId)) {
				String admin = headers.getFirst("admin");
				SaasContext.initSaasContext(//
						headers.getFirst("tenantId"), //
						headers.getFirst("tenantName"), //
						userId, //
						headers.getFirst("username"), //
						StringUtils.isNotEmpty(admin) && "true".equalsIgnoreCase(admin), //
						headers.getFirst("userType"), //
						headers.getFirst("organizationId"),
						headers.getFirst("safetyLevelId") //
				);
			}
			log.debug(
				"SaasContext Initialized:CurrentPath => {}, CurrentTenantId => {}, CurrentTenantName => {}, CurrentUserId => {}, CurrentUsername => {}",
				SaasContext.getCurrentTenantId(), SaasContext.getCurrentTenantName(),
				SaasContext.getCurrentUserId(), SaasContext.getCurrentUsername(), request.getPath().value());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return chain.filter(exchange).doFinally(t -> SaasContext.clear());
	}
}
