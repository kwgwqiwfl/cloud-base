package com.ring.welkin.common.swagger.handle;

import com.google.common.collect.Lists;
import com.ring.welkin.common.core.rest.ApiOperationService;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;
import java.util.Map;

/**
 * 自定义Swagger文档处理器
 *
 * @author cloud
 * @date 2020年4月24日 上午9:52:34
 */
@Slf4j
@Component
public class ApiOperationSwggerHanadler implements SwaggerHandler {

	@Autowired
	@Nullable
	private ApiOperationService apiOperationService;

	@Value("${spring.application.name}")
	private String serviceName = "default";

	@Override
	public void handle(Swagger swagger) {
		if (swagger == null) {
			log.warn("swagger instance does not exist, swagger instance handle shiped!");
		}
		if (apiOperationService != null) {
			apiOperationService.initApiOperations(serviceName, parse(serviceName, swagger));
		}
	}

	private List<DefaultApiOperation> parse(String serviceName, Swagger swagger) {
		String basePath = swagger.getBasePath();
		Map<String, Path> paths = swagger.getPaths();
		final List<DefaultApiOperation> list = Lists.newArrayList();
		if (!paths.isEmpty()) {
			paths.forEach((p, path) -> {
				Map<HttpMethod, Operation> operationMap = path.getOperationMap();
				if (!operationMap.isEmpty()) {
					operationMap.forEach((m, o) -> {
						list.add(new DefaultApiOperation(serviceName, Docket.DEFAULT_GROUP_NAME, o.getTags(), m.name(),
							basePath, p, o.getSummary(), o.getDescription(), o.getOperationId(), o.getConsumes(),
							o.getProduces(), o.isDeprecated(), false));
					});
				}
			});
		}
		return list;
	}
}
