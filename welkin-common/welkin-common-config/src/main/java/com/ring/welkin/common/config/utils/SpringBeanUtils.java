package com.ring.welkin.common.config.utils;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SpringBeanUtils {

	public static Object registerSingletonBean(ApplicationContext ctx, String beanName, Object singletonObject) {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
		beanFactory.registerSingleton(beanName, singletonObject);
		return ctx.getBean(beanName);
	}

	public static boolean addBean(ApplicationContext ctx, String beanName, Class<?> beanClass,
								  Object... constructValues) {
		BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory) ((AbstractRefreshableApplicationContext) ctx)
			.getBeanFactory();
		if (beanDefReg.containsBeanDefinition(beanName)) {
			return false;
		}
		BeanDefinitionBuilder beanDefBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
		for (Object constructValue : constructValues) {
			beanDefBuilder.addConstructorArgValue(constructValue);
		}
		BeanDefinition beanDefinition = beanDefBuilder.getBeanDefinition();
		beanDefReg.registerBeanDefinition(beanName, beanDefinition);
		return true;
	}

	public static void removeBean(ApplicationContext ctx, String beanName) {
		BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory) ((AbstractRefreshableApplicationContext) ctx)
			.getBeanFactory();
		beanDefReg.removeBeanDefinition(beanName);
	}

	private static final String RESOURCE_PATTERN = "/**/*.class";

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static Map<String, Class<?>> findAllClass(String basePackage, Class cls) {
		Map<String, Class<?>> handlerMap = new HashMap<>();

		// spring工具类，可以获取指定路径下的全部类
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		try {
			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
				+ ClassUtils.convertClassNameToResourcePath(basePackage) + RESOURCE_PATTERN;
			Resource[] resources = resourcePatternResolver.getResources(pattern);
			// MetadataReader 的工厂类
			MetadataReaderFactory readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
			for (Resource resource : resources) {
				// 用于读取类信息
				MetadataReader reader = readerfactory.getMetadataReader(resource);
				// 扫描到的class
				String classname = reader.getClassMetadata().getClassName();
				Class<?> clazz = Class.forName(classname);
				if (cls.isAnnotation()) {
					Object anno = clazz.getAnnotation(cls);
					if (anno != null) {
						handlerMap.put(clazz.getSimpleName(), clazz);
					}
				} else if (cls.isAssignableFrom(clazz)) {
					handlerMap.put(clazz.getSimpleName(), clazz);
				}
			}
		} catch (IOException | ClassNotFoundException e) {
		}
		return handlerMap;
	}
}
