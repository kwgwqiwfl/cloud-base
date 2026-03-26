package com.ring.welkin.common.web.method.annotation.reactive;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.SyncHandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public abstract class AbstractReactiveAliasNamedValueSyncArgumentResolver
	extends AbstractReactiveAliasNamedValueArgumentResolver implements SyncHandlerMethodArgumentResolver {

	/**
	 * Create a new {@link AbstractReactiveAliasNamedValueSyncArgumentResolver}.
	 *
	 * @param factory  a bean factory to use for resolving {@code ${...}}
	 *                 placeholder and {@code #{...}} SpEL expressions in default
	 *                 values; or {@code null} if default values are not expected to
	 *                 have expressions
	 * @param registry for checking reactive type wrappers
	 */
	protected AbstractReactiveAliasNamedValueSyncArgumentResolver(@Nullable ConfigurableBeanFactory factory,
																  ReactiveAdapterRegistry registry) {
		super(factory, registry);
	}

	@Override
	public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext,
										ServerWebExchange exchange) {

		// Flip the default implementation from SyncHandlerMethodArgumentResolver:
		// instead of delegating to (sync) resolveArgumentValue,
		// call (async) super.resolveArgument shared with non-blocking resolvers;
		// actual resolution below still sync...

		return super.resolveArgument(parameter, bindingContext, exchange);
	}

	@Override
	public Object resolveArgumentValue(MethodParameter parameter, BindingContext context, ServerWebExchange exchange) {

		// This won't block since resolveName below doesn't
		return resolveArgument(parameter, context, exchange).block();
	}

	@Override
	protected final Mono<Object> resolveName(String name, String[] alias, MethodParameter param,
											 ServerWebExchange exchange) {
		return Mono.justOrEmpty(resolveNamedValue(name, alias, param, exchange));
	}

	/**
	 * Actually resolve the value synchronously.
	 */
	@Nullable
	protected abstract Object resolveNamedValue(String name, String[] alias, MethodParameter param,
												ServerWebExchange exchange);

}
