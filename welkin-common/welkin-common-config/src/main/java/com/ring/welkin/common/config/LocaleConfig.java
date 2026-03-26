package com.ring.welkin.common.config;

import com.google.common.collect.Lists;
import com.ring.welkin.common.core.i18n.MessageSourceLocator;
import com.ring.welkin.common.utils.ICollections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;

import java.time.Duration;
import java.util.*;

@Configuration
public class LocaleConfig implements ApplicationContextAware {
	private final List<MessageSourceLocator> locators = new ArrayList<MessageSourceLocator>();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		Map<String, MessageSourceLocator> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,
				MessageSourceLocator.class, true, false);
		if (beans != null && !beans.isEmpty()) {
			this.locators.addAll(beans.values());
			AnnotationAwareOrderComparator.sort(this.locators);
		}
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.messages")
	public MessageSourceProperties externalMessageSourceProperties() {
		return new MessageSourceProperties();
	}

	@Primary
	@Bean
	public AcceptHeaderLocaleContextResolver acceptHeaderLocaleContextResolver() {
		AcceptHeaderLocaleContextResolver acceptHeaderLocaleContextResolver = new AcceptHeaderLocaleContextResolver();
		acceptHeaderLocaleContextResolver.setDefaultLocale(Locale.CHINA);
		acceptHeaderLocaleContextResolver.setSupportedLocales(Lists.newArrayList(Locale.CHINA, Locale.US));
		return acceptHeaderLocaleContextResolver;
	}

	@Primary
	@Bean
	public MessageSourceProperties messageSourceProperties(MessageSourceProperties externalProperties) {
		String basename = "";
		if (ICollections.hasElements(locators)) {
			Set<String> basenames = new HashSet<String>();
			for (MessageSourceLocator locator : locators) {
				basenames.addAll(locator.baseNames());
			}
			basename = StringUtils.join(basenames, ",");
		}
		if (externalProperties != null && StringUtils.isNotEmpty(externalProperties.getBasename())) {
			basename = StringUtils.isEmpty(basename) ? externalProperties.getBasename()
					: basename + "," + externalProperties.getBasename();
		}
		MessageSourceProperties messageSourceProperties = new MessageSourceProperties();
		messageSourceProperties.setBasename(basename);
		messageSourceProperties.setAlwaysUseMessageFormat(externalProperties.isAlwaysUseMessageFormat());
		messageSourceProperties.setCacheDuration(externalProperties.getCacheDuration());
		messageSourceProperties.setEncoding(externalProperties.getEncoding());
		messageSourceProperties.setFallbackToSystemLocale(externalProperties.isFallbackToSystemLocale());
		messageSourceProperties.setUseCodeAsDefaultMessage(externalProperties.isUseCodeAsDefaultMessage());
		return messageSourceProperties;
	}

	@Bean
	public MessageSource messageSource(MessageSourceProperties properties) {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		if (StringUtils.isNotEmpty(properties.getBasename())) {
			messageSource.setBasenames(StringUtils.split(StringUtils.trim(properties.getBasename()), ","));
		}
		if (properties.getEncoding() != null) {
			messageSource.setDefaultEncoding(properties.getEncoding().name());
		}
		messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
		Duration cacheDuration = properties.getCacheDuration();
		if (cacheDuration != null) {
			messageSource.setCacheMillis(cacheDuration.toMillis());
		}
		messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
		messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
		return messageSource;
	}

	@Bean
	public MessageSourceAccessor messageSourceAccessor(final MessageSource messageSource) {
		return new MessageSourceAccessor(messageSource, Locale.CHINA);
	}
}
