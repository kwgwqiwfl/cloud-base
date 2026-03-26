package com.ring.welkin.common.persistence.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *  集合查询的时候忽略该列，加上该注解之后查询的时候会忽略相关字段。
 *  使用场景如：
 *   1）有大字段的对象在批量查询的时候不必要查询大字段列
 *   2）一般字段大部分时间查询无意义可以忽略的列
 * </pre>
 *
 * @author cloud
 * @date 2021年7月9日 下午1:21:03
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreByExampleQuery {
}
