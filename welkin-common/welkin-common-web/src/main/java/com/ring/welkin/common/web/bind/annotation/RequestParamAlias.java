package com.ring.welkin.common.web.bind.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParamAlias {

	/**
	 * 默认取的参数名称
	 */
	@AliasFor("value")
	String name() default "";

	/**
	 * 默认取的参数名称
	 */
	@AliasFor("name")
	String value() default "";

	/**
	 * 别名，多个使用逗号分隔，如果默认参数名没有取到值，则根据别名顺序去取直到取到为止
	 */
	String alias() default "";

	/**
	 * 参数是否必填，如果必填则需要校验默认的参数名和所有的别名带的值
	 */
	boolean required() default true;

	/**
	 * 默认值
	 */
	String defaultValue() default ValueConstants.DEFAULT_NONE;

}
