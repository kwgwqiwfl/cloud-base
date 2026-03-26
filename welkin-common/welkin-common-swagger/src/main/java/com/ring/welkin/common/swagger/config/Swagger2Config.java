package com.ring.welkin.common.swagger.config;

import com.google.common.collect.Lists;
import com.ring.welkin.common.utils.ICollections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.schema.Example;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnProperty(prefix = Swagger2Properties.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(Swagger2Properties.class)
public class Swagger2Config {
    private final Swagger2Properties properties;

    public Swagger2Config(Swagger2Properties properties) {
        this.properties = properties;
    }

    @Autowired
    @Nullable
    private ReactiveMode reactiveMode;

    @Value("${server.reactive.context-path:${server.servlet.context-path:${spring.webflux.base-path:/}}}")
    private String pathMapping;

    @Bean
    public Docket createRestApi() {
        // set
        List<String> basePackages = properties.getBasePackage();
        Predicate<RequestHandler> predicate = null;
        if (ICollections.hasNoElements(basePackages)) {
            predicate = RequestHandlerSelectors.withClassAnnotation(RestController.class);
        } else {
            predicate = RequestHandlerSelectors.basePackage(basePackages.get(0));
            if (basePackages.size() > 1) {
                for (int i = 1; i < basePackages.size(); i++) {
                    predicate = predicate.or(RequestHandlerSelectors.basePackage(basePackages.get(i)));
                }
            }
        }

        // set path mapping
        if (!(StringUtils.isNotEmpty(pathMapping) && !pathMapping.equals("/") && reactiveMode != null)) {
            pathMapping = "/";
        }

        return new Docket(DocumentationType.SWAGGER_2)//
                .globalRequestParameters(globalRequestParameters())//
                .globalResponses(HttpMethod.GET, responseMessages)//
                .globalResponses(HttpMethod.POST, responseMessages)//
                .enable(properties.isEnabled())//
                .apiInfo(apiInfo())//
                .select()//
                .apis(predicate)//
                .paths(PathSelectors.any())//
                .build()//
                .pathMapping(pathMapping);
    }

    /**
     * 添加接口公共的参数配置项信息，这些参数可以统一设置,如：<br>
     * 1）在客户端请求时是必传参数，如：token信息等 <br>
     * 2）参数在接口定义中没有，但是需要客户端传递，通过过滤器或者其他途径拦截的参数<br>
     * 3）一些我们认为可以统一配置的场景<br>
     *
     * @return 公共参数定义列表
     */
    private List<RequestParameter> globalRequestParameters() {
        return properties.parseGlobalParameters();
    }

    private ApiInfo apiInfo() {
        com.ring.welkin.common.swagger.config.Swagger2Properties.ApiInfo apiInfo = properties.getApiInfo();
        return apiInfo != null ? apiInfo.toApiInfo()
                : new ApiInfoBuilder()//
                .version("1.0")//
                .title("Welkin平台接口文档")//
                .description("Welkin平台接口文档")//
                .termsOfServiceUrl("http://www.xxx.com/")//
                .contact(new Contact("天空云", "http://www.xxx.com/", "xxx"))//
                .build();
    }

    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    // 公共响应信息
    List<Response> responseMessages = Lists.newArrayList(//
            buildFailResponseMessage(HttpStatus.OK), //
            buildFailResponseMessage(HttpStatus.UNAUTHORIZED), //
            buildFailResponseMessage(HttpStatus.FORBIDDEN), //
            buildFailResponseMessage(HttpStatus.NOT_FOUND), //
            // buildFailResponseMessage(HttpStatus.METHOD_NOT_ALLOWED), //
            // buildFailResponseMessage(HttpStatus.NOT_ACCEPTABLE), //
            // buildFailResponseMessage(HttpStatus.PROXY_AUTHENTICATION_REQUIRED), //
            // buildFailResponseMessage(HttpStatus.REQUEST_TIMEOUT), //
            // buildFailResponseMessage(HttpStatus.CONFLICT), //
            // buildFailResponseMessage(HttpStatus.UNSUPPORTED_MEDIA_TYPE), //
            buildFailResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR), //
            // buildFailResponseMessage(HttpStatus.NOT_IMPLEMENTED), //
            buildFailResponseMessage(HttpStatus.BAD_GATEWAY), //
            buildFailResponseMessage(HttpStatus.SERVICE_UNAVAILABLE) //
    );

    // 构建一个响应错误的消息信息
    public Response buildFailResponseMessage(HttpStatus httpStatus) {
        return new ResponseBuilder()//
                .code(httpStatus.value() + "")//
                .description(httpStatus.getReasonPhrase())
                .examples(Arrays.asList(new Example(APPLICATION_JSON_UTF8_VALUE, "{ \"status\": 0, \"message\": \"请求处理成功\"}")))//
                .build();
    }

    @Configuration
    @ConditionalOnExpression(value = "!'${swagger2.resources}'.isEmpty()")
    public class SwaggerResourcesProviderAutoConfiguration {
        @Primary
        @Bean
        public SwaggerResourcesProvider swaggerResourcesProvider() {
            return new SwaggerResourcesProvider() {
                @Override
                public List<SwaggerResource> get() {
                    List<SwaggerResource> resources = properties.getResources();
                    if (resources != null && resources.size() > 0) {
                        return resources;
                    }

                    SwaggerResource resource = new SwaggerResource();
                    resource.setName("default");
                    resource.setUrl("/v2/api-docs");
                    resource.setSwaggerVersion("1.0");
                    return Arrays.asList(resource);
                }
            };
        }
    }

    @Configuration
    @EnableSwagger2
    public class EnableSwagger2AutoConfiguration {
    }

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public class EnableSwagger2WebFluxAutoConfiguration {
        @Bean
        public ReactiveMode reactiveMode() {
            return new ReactiveMode();
        }
    }

    public static final class ReactiveMode {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(WebMvcRequestHandlerProvider.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class FixNpeForSpringfoxHandlerProviderBeanPostProcessorConfiguration {

        @Bean
        public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
            return new BeanPostProcessor() {

                @Override
                public Object postProcessAfterInitialization(@Nonnull Object bean, @Nonnull String beanName) throws BeansException {
                    if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                        customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                    }
                    return bean;
                }

                private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                    List<T> copy = mappings.stream().filter(mapping -> mapping.getPatternParser() == null).collect(Collectors.toList());
                    mappings.clear();
                    mappings.addAll(copy);
                }

                @SuppressWarnings("unchecked")
                private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                    try {
                        Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                        field.setAccessible(true);
                        return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                }
            };
        }
    }
}
