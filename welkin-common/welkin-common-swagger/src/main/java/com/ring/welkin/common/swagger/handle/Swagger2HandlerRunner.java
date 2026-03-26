package com.ring.welkin.common.swagger.handle;

import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.SpringIntegrationPluginNotPresentInClassPathCondition;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 当DocumentationPluginsBootstrapper类完成swagger相关的扫描之后开始初始化Swagger对象
 *
 * @author cloud
 * @date 2020年4月23日 下午7:39:34
 */
@Slf4j
@Component
@Conditional(SpringIntegrationPluginNotPresentInClassPathCondition.class)
@ConditionalOnProperty(prefix = SwaggerHandleProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(SwaggerHandleProperties.class)
public class Swagger2HandlerRunner implements CommandLineRunner, ApplicationContextAware {

	private final DocumentationCache documentationCache;
	private final ServiceModelToSwagger2Mapper mapper;
	private final List<SwaggerHandler> handlers = new ArrayList<SwaggerHandler>();

	public Swagger2HandlerRunner(DocumentationCache documentationCache, ServiceModelToSwagger2Mapper mapper) {
		this.documentationCache = documentationCache;
		this.mapper = mapper;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) {
		Map<String, SwaggerHandler> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context,
			SwaggerHandler.class, true, false);
		this.handlers.addAll(beans.values());
		AnnotationAwareOrderComparator.sort(this.handlers);
	}

	@Override
	public void run(String... args) throws Exception {
		Swagger swagger = swagger();
		if (swagger == null) {
			log.debug("no swagger to be handle.");
			return;
		}

		if (handlers == null || handlers.isEmpty()) {
			log.debug("no SwaggerHandler to handle swagger.");
			return;
		}

		for (SwaggerHandler handler : handlers) {
			log.debug("SwaggerHandler {} handle swagger.", handler.getClass().getName());
			handler.handle(swagger);
		}
	}

	public Swagger swagger() {
		String groupName = Docket.DEFAULT_GROUP_NAME;
		Documentation documentation = documentationCache.documentationByGroup(groupName);
		if (documentation == null) {
			log.warn("Unable to find specification for group {}", groupName);
			throw new RuntimeException(String.format("Unable to find specification for group %s", groupName));
		}
		Swagger swagger = mapper.mapDocumentation(documentation);
		swagger.basePath("/");
		return swagger;
	}
}
