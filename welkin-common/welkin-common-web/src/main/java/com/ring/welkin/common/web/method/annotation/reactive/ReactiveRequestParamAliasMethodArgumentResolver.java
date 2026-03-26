package com.ring.welkin.common.web.method.annotation.reactive;

import com.ring.welkin.common.web.bind.annotation.RequestHeaderAlias;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;
import java.util.Map;

public class ReactiveRequestParamAliasMethodArgumentResolver
	extends AbstractReactiveAliasNamedValueSyncArgumentResolver {

	public ReactiveRequestParamAliasMethodArgumentResolver(@Nullable ConfigurableBeanFactory factory,
														   ReactiveAdapterRegistry registry) {
		super(factory, registry);
	}

	@Override
	public boolean supportsParameter(MethodParameter param) {
		return checkAnnotatedParamNoReactiveWrapper(param, RequestHeaderAlias.class, this::singleParam);
	}

	private boolean singleParam(RequestHeaderAlias annotation, Class<?> type) {
		return !Map.class.isAssignableFrom(type);
	}

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		RequestHeaderAlias ann = parameter.getParameterAnnotation(RequestHeaderAlias.class);
		Assert.state(ann != null, "No RequestHeader annotation");
		return new RequestHeaderNamedValueInfo(ann);
	}

	@Override
	protected Object resolveNamedValue(String name, String[] alias, MethodParameter parameter,
									   ServerWebExchange exchange) {
		Object result = resolveNamedValue(name, parameter, exchange);
		if (result == null & ArrayUtils.isNotEmpty(alias)) {
			for (String a : alias) {
				result = resolveNamedValue(a, parameter, exchange);
				if (result != null) {
					break;
				}
			}
		}
		return result;
	}

	protected Object resolveNamedValue(String name, MethodParameter parameter, ServerWebExchange exchange) {
		List<String> headerValues = exchange.getRequest().getHeaders().get(name);
		Object result = null;
		if (headerValues != null) {
			result = (headerValues.size() == 1 ? headerValues.get(0) : headerValues);
		}
		return result;
	}

	@Override
	protected void handleMissingValue(String name, MethodParameter parameter) {
		String type = parameter.getNestedParameterType().getSimpleName();
		throw new ServerWebInputException(
			"Missing request header '" + name + "' " + "for method parameter of type " + type, parameter);
	}

	private static final class RequestHeaderNamedValueInfo extends NamedValueInfo {
		private RequestHeaderNamedValueInfo(RequestHeaderAlias annotation) {
			super(annotation.name(), annotation.alias(), annotation.required(), annotation.defaultValue());
		}
	}

}
