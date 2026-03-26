package com.ring.welkin.common.webflux.saas;

import com.ring.welkin.common.core.saas.SaasContext;
import com.ring.welkin.common.core.saas.UserInfo;
import com.ring.welkin.common.utils.JsonUtils;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.function.Function;

/**
 * Allows getting and setting the Spring {@link SecurityContext} into a {@link Context}.
 *
 * @author Rob Winch
 * @since 5.0
 */
public final class ReactiveSaasContextHolder {
	private static final Class<?> SAAS_CONTEXT_KEY = SaasContext.class;

	private ReactiveSaasContextHolder() {
	}

	@SuppressWarnings("deprecation")
	public static Mono<SaasContext> getContext() {
		// @formatter:off
		return Mono.subscriberContext()
			.filter(ReactiveSaasContextHolder::hasSaasContext)
			.flatMap(ReactiveSaasContextHolder::getSaasContext);
		// @formatter:on
	}

	private static boolean hasSaasContext(Context context) {
		return context.hasKey(SAAS_CONTEXT_KEY);
	}

	private static Mono<SaasContext> getSaasContext(Context context) {
		return context.<Mono<SaasContext>>get(SAAS_CONTEXT_KEY);
	}

	public static Function<Context, Context> clearContext() {
		return (context) -> context.delete(SAAS_CONTEXT_KEY);
	}

	public static Context withSaasContext(Mono<? extends SaasContext> saasContext) {
		return Context.of(SAAS_CONTEXT_KEY, saasContext);
	}

	public static Context withUserInfo(UserInfo userInfo) {
		return withSaasContext(Mono.just(JsonUtils.fromObject(userInfo, SaasContext.class)));
	}

}
