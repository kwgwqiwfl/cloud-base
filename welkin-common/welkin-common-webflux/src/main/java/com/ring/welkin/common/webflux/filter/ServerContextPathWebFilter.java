package com.ring.welkin.common.webflux.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 所有/contextPath前缀的请求都会自动去除该前缀
 */
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ServerContextPathWebFilter implements WebFilter {

    @Value("${server.reactive.context-path:${server.servlet.context-path:${spring.webflux.base-path:/}}}")
    private String contextPath;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (StringUtils.isNotBlank(contextPath)) {
            ServerHttpRequest request = exchange.getRequest();
            if (request.getURI().getPath().startsWith(contextPath)) {
                return chain
                        .filter(exchange.mutate().request(request.mutate().contextPath(contextPath).build()).build());
            }
        }
        return chain.filter(exchange);
    }
}
