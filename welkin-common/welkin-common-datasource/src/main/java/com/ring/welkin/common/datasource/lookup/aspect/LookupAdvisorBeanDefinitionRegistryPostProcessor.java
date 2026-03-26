package com.ring.welkin.common.datasource.lookup.aspect;

import com.ring.welkin.common.datasource.annotation.Lookup;
import com.ring.welkin.common.datasource.config.HikariMasterSlavesDataSourcesProperties;
import com.ring.welkin.common.datasource.lookup.MasterSlavesDataSources;
import com.ring.welkin.common.datasource.lookup.MasterSlavesRoutingDataSource;
import com.ring.welkin.common.datasource.lookup.aspect.LookupAspectConfig.LookupPointCut;
import com.ring.welkin.common.utils.ICollections;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class LookupAdvisorBeanDefinitionRegistryPostProcessor
    implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    private static final String CONFIG_PREFIX = "spring.routing-datasource";

    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.error("register MasterSlavesRoutingDataSource beanNames:"
            + Arrays.toString(beanFactory.getBeanNamesForType(MasterSlavesDataSources.class)));
        log.error("register DefaultPointcutAdvisor beanNames:"
            + Arrays.toString(beanFactory.getBeanNamesForType(PointcutAdvisor.class)));
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        BindResult<HikariMasterSlavesDataSourcesProperties> bindResult = Binder.get(environment).bind(CONFIG_PREFIX,
            HikariMasterSlavesDataSourcesProperties.class);
        if (!bindResult.isBound()) {
            log.warn("No value bound:'" + CONFIG_PREFIX + "',Consider adding it if necessary.");
            return;
        }
        HikariMasterSlavesDataSourcesProperties dbConfig = bindResult.get();
        if (dbConfig != null) {
            // 注册数据源
            // registerRoutingDataSourceBean(registry, dbConfig);

            // 注册数据源
            registerDbConfigBean(registry, dbConfig);

            // 注册切面
            LookupAspectConfig aspectConfig = dbConfig.getAspect();
            if (aspectConfig != null) {
                switch (aspectConfig.getMode()) {
                    case expression:
                        List<LookupPointCut> pointCuts = aspectConfig.getPointcuts();
                        if (ICollections.hasElements(pointCuts)) {
                            for (int i = 0; i < pointCuts.size(); i++) {
                                LookupPointCut pointCut = pointCuts.get(i);
                                String expression = pointCut.getExpression();
                                if (StringUtils.isEmpty(expression)) {
                                    throw new RuntimeException("Empty pointcut expression for item " + i);
                                }
                                registerAspectJExpressionPointcutAdvisorBean(registry, expression, i,
                                    new LookupExpressionMethodAroundAdvice(dbConfig, pointCut));
                            }
                        }
                        break;
                    case annotation:
                        registerDefaultPointcutAdvisorBean(registry, new AnnotationMatchingPointcut(Lookup.class, false), 0,
                            new LookupAnnotationMethodAroundAdvice(dbConfig));
                        break;
                    default:
                        // do notthing,use default routing logic
                        break;
                }
            }
        }
    }

    private void registerDbConfigBean(BeanDefinitionRegistry registry,
                                      HikariMasterSlavesDataSourcesProperties dbConfig) {
        AnnotatedBeanDefinition annotatedBeanDefinition = new AnnotatedGenericBeanDefinition(
            HikariMasterSlavesDataSourcesProperties.class);
        annotatedBeanDefinition.setAttribute("enabled", dbConfig.isEnabled());
        annotatedBeanDefinition.setAttribute("aspect", dbConfig.getAspect());
        annotatedBeanDefinition.setAttribute("master", dbConfig.getMaster());
        annotatedBeanDefinition.setAttribute("slaves", dbConfig.getSlaves());
        annotatedBeanDefinition.setPrimary(true);
        registry.registerBeanDefinition("dbConfig", annotatedBeanDefinition);
    }

    public void registerRoutingDataSourceBean(BeanDefinitionRegistry registry,
                                              MasterSlavesDataSources<? extends DataSource> dbConfig) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
            .genericBeanDefinition(MasterSlavesRoutingDataSource.class);
        builder.addPropertyValue("dbConfig", dbConfig);
        builder.addPropertyValue("defaultTargetDataSource", dbConfig.getMaster());
        builder.addPropertyValue("targetDataSources", dbConfig.getTargetDataSources());
        builder.setPrimary(true);
        registry.registerBeanDefinition("dataSource", builder.getBeanDefinition());
    }

    private void registerAspectJExpressionPointcutAdvisorBean(BeanDefinitionRegistry registry, String expression,
                                                              int order, Advice advice) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
            .genericBeanDefinition(AspectJExpressionPointcutAdvisor.class);
        builder.addPropertyValue("expression", expression);
        builder.addPropertyValue("order", order);
        builder.addPropertyValue("advice", advice);
        registry.registerBeanDefinition("aspectJExpressionPointcutAdvisor" + order, builder.getBeanDefinition());
    }

    private void registerDefaultPointcutAdvisorBean(BeanDefinitionRegistry registry,
                                                    org.springframework.aop.Pointcut pointcut, int order, Advice advice) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DefaultPointcutAdvisor.class);
        builder.addPropertyValue("order", order);
        builder.addPropertyValue("advice", advice);
        registry.registerBeanDefinition("routingDatasourceAdvisor", builder.getBeanDefinition());
    }

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

}
