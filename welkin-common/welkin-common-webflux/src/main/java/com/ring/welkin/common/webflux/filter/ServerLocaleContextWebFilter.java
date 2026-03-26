package com.ring.welkin.common.webflux.filter;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.google.common.collect.Lists;

import reactor.core.publisher.Mono;

/**
 * LocaleContext 拦截初始化
 *
 * @author cloud
 * @date 2019年12月17日 上午10:37:51
 */
@Component
public class ServerLocaleContextWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		List<Locale> requestLocales = null;
		try {
			requestLocales = exchange.getRequest().getHeaders().getAcceptLanguageAsLocales();
		} catch (IllegalArgumentException ex) {
			// Invalid Accept-Language header: treat as empty for matching purposes
		}
		LocaleContextHolder.setLocaleContext(new SimpleLocaleContext(resolveSupportedLocale(requestLocales)), true);
		return chain.filter(exchange);
	}

	@Nullable
	private Locale resolveSupportedLocale(@Nullable List<Locale> requestLocales) {
		if (CollectionUtils.isEmpty(requestLocales)) {
			return getDefaultLocale(); // may be null
		}
		List<Locale> supportedLocales = getSupportedLocales();
		if (supportedLocales.isEmpty()) {
			return requestLocales.get(0); // never null
		}

		Locale languageMatch = null;
		for (Locale locale : requestLocales) {
			if (supportedLocales.contains(locale)) {
				if (languageMatch == null || languageMatch.getLanguage().equals(locale.getLanguage())) {
					// Full match: language + country, possibly narrowed from earlier language-only match
					return locale;
				}
			} else if (languageMatch == null) {
				// Let's try to find a language-only match as a fallback
				for (Locale candidate : supportedLocales) {
					if (!StringUtils.isNotEmpty(candidate.getCountry())
							&& candidate.getLanguage().equals(locale.getLanguage())) {
						languageMatch = candidate;
						break;
					}
				}
			}
		}
		if (languageMatch != null) {
			return languageMatch;
		}

		Locale defaultLocale = getDefaultLocale();
		return (defaultLocale != null ? defaultLocale : requestLocales.get(0));
	}

	private Locale getDefaultLocale() {
		return LocaleContextHolder.getLocale();
	}

	private List<Locale> getSupportedLocales() {
		return Lists.newArrayList(Locale.CHINA, Locale.US);
	}
}
