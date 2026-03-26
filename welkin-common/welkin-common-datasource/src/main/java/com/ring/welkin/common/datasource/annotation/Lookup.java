package com.ring.welkin.common.datasource.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 注解指定类或方法优先使用的数据源 LookupKey，如果没有找到指定的名称则使用默认的
 *
 * @author cloud
 * @date 2021年7月2日 上午12:27:02
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Lookup {

	/**
	 * 指定lookupKey：如 master,slave0,slave1等
	 *
	 * @return 指定的数据源
	 */
	@AliasFor(value = "lookupKey")
	String value() default "";

	/**
	 * 指定lookupKey：如 master,slave0,slave1等
	 *
	 * @return 指定的数据源
	 */
	@AliasFor(value = "value")
	String lookupKey() default "";

	/**
	 * 是否只读,如果只读并且value不为空则使用该数据源只读，写入操作使用主数据源进行操作，否则读写都是用该数据源
	 *
	 * @return 是否只读
	 */
	boolean readOnly() default false;

}
