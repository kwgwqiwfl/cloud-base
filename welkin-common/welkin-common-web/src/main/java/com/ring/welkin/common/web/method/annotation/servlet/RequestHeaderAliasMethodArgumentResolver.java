package com.ring.welkin.common.web.method.annotation.servlet;

import com.ring.welkin.common.web.bind.annotation.RequestHeaderAlias;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Map;

public class RequestHeaderAliasMethodArgumentResolver extends AbstractAliasNamedValueMethodArgumentResolver {

	public RequestHeaderAliasMethodArgumentResolver(ConfigurableBeanFactory beanFactory) {
		super(beanFactory);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return (parameter.hasParameterAnnotation(RequestHeaderAlias.class)
			&& !Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType()));
	}

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		RequestHeaderAlias annotation = parameter.getParameterAnnotation(RequestHeaderAlias.class);
		return new RequestHeaderAliasNamedValueInfo(annotation);
	}

	@Override
	protected Object resolveName(String name, String[] alias, MethodParameter parameter, NativeWebRequest request)
		throws Exception {
		Object result = resolveName(name, parameter, request);
		if (result == null && ArrayUtils.isNotEmpty(alias)) {
			for (String a : alias) {
				result = resolveName(a, parameter, request);
				if (result != null) {
					break;
				}
			}
		}
		return result;
	}

	private Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		String[] headerValues = request.getHeaderValues(name);
		if (headerValues != null) {
			return (headerValues.length == 1 ? headerValues[0] : headerValues);
		} else {
			return null;
		}
	}

	@Override
	protected void handleMissingValue(String name, MethodParameter parameter) throws ServletRequestBindingException {
		throw new ServletRequestBindingException("Missing request header '" + name + "' for method parameter of type "
			+ parameter.getNestedParameterType().getSimpleName());
	}

	private static class RequestHeaderAliasNamedValueInfo extends NamedValueInfo {
		private RequestHeaderAliasNamedValueInfo(RequestHeaderAlias annotation) {
			super(annotation.name(), annotation.alias(), annotation.required(), annotation.defaultValue());
		}
	}
}
