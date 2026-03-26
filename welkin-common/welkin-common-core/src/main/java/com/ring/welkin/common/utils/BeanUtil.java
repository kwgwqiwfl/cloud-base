package com.ring.welkin.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 反射工具类
 *
 * @author cloud
 * @date 2021年10月13日 上午10:07:54
 */
@Slf4j
public class BeanUtil extends BeanUtils {

	/**
	 * 递归获取所有的非静态字段，包含所有父类字段
	 *
	 * @param entityClass 实体类型
	 * @param fieldList   字段列表
	 * @param level       层级
	 * @return 所有的字段列表
	 */
	public static List<Field> getFields(Class<?> entityClass, List<Field> fieldList, Integer level) {
		if (fieldList == null) {
			throw new NullPointerException("fieldList参数不能为空!");
		}
		if (level == null) {
			level = 0;
		}
		if (entityClass == Object.class) {
			return null;
		}
		Field[] fields = entityClass.getDeclaredFields();
		int index = 0;
		for (Field field : fields) {
			// 忽略static和transient字段#106
			if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
				if (level.intValue() != 0) {
					// 将父类的字段放在前面
					fieldList.add(index, field);
					index++;
				} else {
					fieldList.add(field);
				}
			}
		}
		// 获取父类和泛型信息
		Class<?> superClass = entityClass.getSuperclass();
		// 判断superClass
		if (superClass != null && !superClass.equals(Object.class)
			&& ((!Map.class.isAssignableFrom(superClass) && !Collection.class.isAssignableFrom(superClass)))) {
			level++;
			return getFields(superClass, fieldList, level);
		}
		return fieldList;
	}

	/**
	 * 将父类全部的属性COPY到子类中
	 *
	 * @param parent 父类实例
	 * @param child  子类实例
	 */
	public static <P, T extends P> T copyParentProperties(T child, P parent) {
		try {
			copyProperties(parent, child);
			return child;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将父类全部的属性COPY到子类中
	 *
	 * @param orig 来源实例
	 * @param desc 目标实例
	 */
	public static <T> T copyOtherProperties(T desc, Object orig) {
		try {
			copyProperties(orig, desc);
			return desc;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 功能 : 只复制source对象的非空属性到target对象上
	 */
	public static <T> T copyNotNullProperties(Object source, T target) throws BeansException {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						// 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
						if (value != null) {
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
					} catch (Throwable e) {
						log.error(e.getMessage(), e);
						throw new FatalBeanException("Could not copy properties from source to target", e);
					}
				}
			}
		}
		return target;
	}
}
