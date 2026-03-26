package com.ring.welkin.common.config.utils;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;

@Component
public class ApplicationContextUtils extends BeanFactoryUtils implements ApplicationContextAware {

	@Getter
	private static ApplicationContext ctx;

	public void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationContextUtils.ctx = applicationContext;
	}

	public static <T> T processInjection(T bean) {
		AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
		bpp.setBeanFactory(ctx.getAutowireCapableBeanFactory());
		bpp.processInjection(bean);
		ctx.getAutowireCapableBeanFactory().initializeBean(bean, bean.getClass().getName());
		return bean;
	}

	@SuppressWarnings("unchecked")
	public static <T> T registerSingleton(String beanName, T singletonBean) {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
		beanFactory.registerSingleton(beanName, singletonBean);
		return (T) ctx.getBean(beanName);
	}

	public static boolean register(String beanName, Class<?> beanClass, Object... constructValues) {
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

	public static void unregister(String beanName) {
		BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory) ((AbstractRefreshableApplicationContext) ctx)
			.getBeanFactory();
		beanDefReg.removeBeanDefinition(beanName);
	}

	public static Object getBean(String name) throws BeansException {
		return ctx.getBean(name);
	}

	public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		return ctx.getBean(name, requiredType);
	}

	public static Object getBean(String name, Object... args) throws BeansException {
		return ctx.getBean(name, args);
	}

	public static <T> T getBean(Class<T> requiredType) throws BeansException {
		return ctx.getBean(requiredType);
	}

	public static <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
		return ctx.getBean(requiredType, args);
	}

	public static <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
		return ctx.getBeanProvider(requiredType);
	}

	public static <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType) {
		return ctx.getBeanProvider(requiredType);
	}

	public static boolean containsBean(String name) {
		return ctx.containsBean(name);
	}

	public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		return ctx.isSingleton(name);
	}

	public static boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
		return ctx.isPrototype(name);
	}

	public static boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
		return ctx.isTypeMatch(name, typeToMatch);
	}

	public static boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
		return ctx.isTypeMatch(name, typeToMatch);
	}

	public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		return ctx.getType(name);
	}

	public static Class<?> getType(String name, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException {
		return ctx.getType(name, allowFactoryBeanInit);
	}

	public static String[] getAliases(String name) {
		return ctx.getAliases(name);
	}

	public static boolean containsBeanDefinition(String beanName) {
		return ctx.containsBeanDefinition(beanName);
	}

	public static int getBeanDefinitionCount() {
		return ctx.getBeanDefinitionCount();
	}

	public static String[] getBeanDefinitionNames() {
		return ctx.getBeanDefinitionNames();
	}

	public static <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType, boolean allowEagerInit) {
		return ctx.getBeanProvider(requiredType, allowEagerInit);
	}

	public static <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType, boolean allowEagerInit) {
		return ctx.getBeanProvider(requiredType, allowEagerInit);
	}

	public static String[] getBeanNamesForType(ResolvableType type) {
		return ctx.getBeanNamesForType(type);
	}

	public static String[] getBeanNamesForType(ResolvableType type, boolean includeNonSingletons,
											   boolean allowEagerInit) {
		return ctx.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
	}

	public static String[] getBeanNamesForType(Class<?> type) {
		return ctx.getBeanNamesForType(type);
	}

	public static String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
		return ctx.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
	}

	public static <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
		return ctx.getBeansOfType(type);
	}

	public static <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
		throws BeansException {
		return ctx.getBeansOfType(type, includeNonSingletons, allowEagerInit);
	}

	public static String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
		return ctx.getBeanNamesForAnnotation(annotationType);
	}

	public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType)
		throws BeansException {
		return ctx.getBeansWithAnnotation(annotationType);
	}

	public static <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
		throws NoSuchBeanDefinitionException {
		return ctx.findAnnotationOnBean(beanName, annotationType);
	}

	public static void publishEvent(Object event) {
		ctx.publishEvent(event);
	}

	public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		return ctx.getMessage(code, args, defaultMessage, locale);
	}

	public static String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
		return ctx.getMessage(code, args, locale);
	}

	public static String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		return ctx.getMessage(resolvable, locale);
	}

	public static Resource[] getResources(String locationPattern) throws IOException {
		return ctx.getResources(locationPattern);
	}

	public static Resource getResource(String location) {
		return ctx.getResource(location);
	}

	public static ClassLoader getClassLoader() {
		return ctx.getClassLoader();
	}

	public static BeanFactory getParentBeanFactory() {
		return ctx.getParentBeanFactory();
	}

	public static boolean containsLocalBean(String name) {
		return ctx.containsLocalBean(name);
	}

	public static Environment getEnvironment() {
		return ctx.getEnvironment();
	}

	public static String getId() {
		return ctx.getId();
	}

	public static String getApplicationName() {
		return ctx.getApplicationName();
	}

	public static String getDisplayName() {
		return ctx.getDisplayName();
	}

	public static long getStartupDate() {
		return ctx.getStartupDate();
	}

	public static ApplicationContext getParent() {
		return ctx.getParent();
	}

	public static AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
		return ctx.getAutowireCapableBeanFactory();
	}
}
