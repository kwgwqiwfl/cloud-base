package com.ring.welkin.common.web.method.annotation.servlet;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractAliasNamedValueMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Nullable
	private final ConfigurableBeanFactory configurableBeanFactory;

	@Nullable
	private final BeanExpressionContext expressionContext;

	private final Map<MethodParameter, NamedValueInfo> namedValueInfoCache = new ConcurrentHashMap<>(256);

	/**
	 * Create a new {@link AbstractAliasNamedValueMethodArgumentResolver} instance.
	 *
	 * @param beanFactory a bean factory to use for resolving ${...} placeholder and
	 *                    #{...} SpEL expressions in default values, or {@code null}
	 *                    if default values are not expected to contain expressions
	 */
	public AbstractAliasNamedValueMethodArgumentResolver(@Nullable ConfigurableBeanFactory beanFactory) {
		this.configurableBeanFactory = beanFactory;
		this.expressionContext = (beanFactory != null ? new BeanExpressionContext(beanFactory, new RequestScope())
			: null);
	}

	@Override
	@Nullable
	public final Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
										NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

		NamedValueInfo namedValueInfo = getNamedValueInfo(parameter);
		MethodParameter nestedParameter = parameter.nestedIfOptional();

		Object resolvedName = resolveEmbeddedValuesAndExpressions(namedValueInfo.name);
		if (resolvedName == null) {
			throw new IllegalArgumentException(
				"Specified name must not resolve to null: [" + namedValueInfo.name + "]");
		}

		Object arg = resolveName(resolvedName.toString(), resolveAliasNames(namedValueInfo.alias), nestedParameter,
			webRequest);
		if (arg == null) {
			if (namedValueInfo.defaultValue != null) {
				arg = resolveEmbeddedValuesAndExpressions(namedValueInfo.defaultValue);
			} else if (namedValueInfo.required && !nestedParameter.isOptional()) {
				handleMissingValue(namedValueInfo.name, nestedParameter, webRequest);
			}
			arg = handleNullValue(namedValueInfo.name, arg, nestedParameter.getNestedParameterType());
		} else if ("".equals(arg) && namedValueInfo.defaultValue != null) {
			arg = resolveEmbeddedValuesAndExpressions(namedValueInfo.defaultValue);
		}

		if (binderFactory != null) {
			WebDataBinder binder = binderFactory.createBinder(webRequest, null, namedValueInfo.name);
			try {
				arg = binder.convertIfNecessary(arg, parameter.getParameterType(), parameter);
			} catch (ConversionNotSupportedException ex) {
				throw new MethodArgumentConversionNotSupportedException(arg, ex.getRequiredType(), namedValueInfo.name,
					parameter, ex.getCause());
			} catch (TypeMismatchException ex) {
				throw new MethodArgumentTypeMismatchException(arg, ex.getRequiredType(), namedValueInfo.name, parameter,
					ex.getCause());
			}
			// Check for null value after conversion of incoming argument value
			if (arg == null && namedValueInfo.defaultValue == null && namedValueInfo.required
				&& !nestedParameter.isOptional()) {
				handleMissingValueAfterConversion(namedValueInfo.name, nestedParameter, webRequest);
			}
		}

		handleResolvedValue(arg, namedValueInfo.name, parameter, mavContainer, webRequest);

		return arg;
	}

	/**
	 * Obtain the named value for the given method parameter.
	 */
	private NamedValueInfo getNamedValueInfo(MethodParameter parameter) {
		NamedValueInfo namedValueInfo = this.namedValueInfoCache.get(parameter);
		if (namedValueInfo == null) {
			namedValueInfo = createNamedValueInfo(parameter);
			namedValueInfo = updateNamedValueInfo(parameter, namedValueInfo);
			this.namedValueInfoCache.put(parameter, namedValueInfo);
		}
		return namedValueInfo;
	}

	/**
	 * Create the {@link NamedValueInfo} object for the given method parameter.
	 * Implementations typically retrieve the method annotation by means of
	 * {@link MethodParameter#getParameterAnnotation(Class)}.
	 *
	 * @param parameter the method parameter
	 * @return the named value information
	 */
	protected abstract NamedValueInfo createNamedValueInfo(MethodParameter parameter);

	/**
	 * Create a new NamedValueInfo based on the given NamedValueInfo with sanitized
	 * values.
	 */
	private NamedValueInfo updateNamedValueInfo(MethodParameter parameter, NamedValueInfo info) {
		String name = info.name;
		if (info.name.isEmpty()) {
			name = parameter.getParameterName();
			if (name == null) {
				throw new IllegalArgumentException(
					"Name for argument of type [" + parameter.getNestedParameterType().getName()
						+ "] not specified, and parameter name information not found in class file either.");
			}
		}
		String defaultValue = (ValueConstants.DEFAULT_NONE.equals(info.defaultValue) ? null : info.defaultValue);
		return new NamedValueInfo(name, info.alias, info.required, defaultValue);
	}

	/**
	 * Resolve the given annotation-specified value, potentially containing
	 * placeholders and expressions.
	 */
	@Nullable
	private Object resolveEmbeddedValuesAndExpressions(String value) {
		if (this.configurableBeanFactory == null || this.expressionContext == null) {
			return value;
		}
		String placeholdersResolved = this.configurableBeanFactory.resolveEmbeddedValue(value);
		BeanExpressionResolver exprResolver = this.configurableBeanFactory.getBeanExpressionResolver();
		if (exprResolver == null) {
			return value;
		}
		return exprResolver.evaluate(placeholdersResolved, this.expressionContext);
	}

	/**
	 * Resolve the given parameter type and value name into an argument value.
	 *
	 * @param name      the name of the value being resolved
	 * @param parameter the method parameter to resolve to an argument value
	 *                  (pre-nested in case of a {@link java.util.Optional}
	 *                  declaration)
	 * @param request   the current request
	 * @return the resolved argument (may be {@code null})
	 * @throws Exception in case of errors
	 */
	@Nullable
	protected abstract Object resolveName(String name, String[] alias, MethodParameter parameter,
										  NativeWebRequest request) throws Exception;

	/**
	 * Invoked when a named value is required, but
	 * {@link #resolveName(String, MethodParameter, NativeWebRequest)} returned
	 * {@code null} and there is no default value. Subclasses typically throw an
	 * exception in this case.
	 *
	 * @param name      the name for the value
	 * @param parameter the method parameter
	 * @param request   the current request
	 * @since 4.3
	 */
	protected void handleMissingValue(String name, MethodParameter parameter, NativeWebRequest request)
		throws Exception {

		handleMissingValue(name, parameter);
	}

	/**
	 * Invoked when a named value is required, but
	 * {@link #resolveName(String, MethodParameter, NativeWebRequest)} returned
	 * {@code null} and there is no default value. Subclasses typically throw an
	 * exception in this case.
	 *
	 * @param name      the name for the value
	 * @param parameter the method parameter
	 */
	protected void handleMissingValue(String name, MethodParameter parameter) throws ServletException {
		throw new ServletRequestBindingException("Missing argument '" + name + "' for method parameter of type "
			+ parameter.getNestedParameterType().getSimpleName());
	}

	/**
	 * Invoked when a named value is present but becomes {@code null} after
	 * conversion.
	 *
	 * @param name      the name for the value
	 * @param parameter the method parameter
	 * @param request   the current request
	 * @since 5.3.6
	 */
	protected void handleMissingValueAfterConversion(String name, MethodParameter parameter, NativeWebRequest request)
		throws Exception {

		handleMissingValue(name, parameter, request);
	}

	/**
	 * A {@code null} results in a {@code false} value for {@code boolean}s or an
	 * exception for other primitives.
	 */
	@Nullable
	private Object handleNullValue(String name, @Nullable Object value, Class<?> paramType) {
		if (value == null) {
			if (Boolean.TYPE.equals(paramType)) {
				return Boolean.FALSE;
			} else if (paramType.isPrimitive()) {
				throw new IllegalStateException("Optional " + paramType.getSimpleName() + " parameter '" + name
					+ "' is present but cannot be translated into a null value due to being declared as a "
					+ "primitive type. Consider declaring it as object wrapper for the corresponding primitive type.");
			}
		}
		return value;
	}

	/**
	 * Invoked after a value is resolved.
	 *
	 * @param arg          the resolved argument value
	 * @param name         the argument name
	 * @param parameter    the argument parameter type
	 * @param mavContainer the {@link ModelAndViewContainer} (may be {@code null})
	 * @param webRequest   the current request
	 */
	protected void handleResolvedValue(@Nullable Object arg, String name, MethodParameter parameter,
									   @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest) {
	}

	protected String[] resolveAliasNames(String alias) {
		if (StringUtils.hasText(alias)) {
			Object aliasObject = resolveEmbeddedValuesAndExpressions(alias);
			if (aliasObject != null) {
				return StringUtils
					.trimArrayElements(StringUtils.commaDelimitedListToStringArray(aliasObject.toString()));
			}
		}
		return null;
	}

	/**
	 * Represents the information about a named value, including name, whether it's
	 * required and a default value.
	 */
	protected static class NamedValueInfo {

		private final String name;
		private final String alias;

		private final boolean required;

		@Nullable
		private final String defaultValue;

		public NamedValueInfo(String name, String alias, boolean required, @Nullable String defaultValue) {
			this.name = name;
			this.alias = alias;
			this.required = required;
			this.defaultValue = defaultValue;
		}
	}

}
