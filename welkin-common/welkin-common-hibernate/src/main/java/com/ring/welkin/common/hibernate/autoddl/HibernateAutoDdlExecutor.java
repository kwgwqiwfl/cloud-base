package com.ring.welkin.common.hibernate.autoddl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "hibernate.hbm2ddl", name = "auto", havingValue = "update", matchIfMissing = false)
public class HibernateAutoDdlExecutor implements ApplicationContextAware {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private HibernateProperties hibernateProperties;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(EntityScan.class);
        final List<String> entityScanBasePackages = new ArrayList<>();
        if (!beans.isEmpty()) {
            beans.forEach((k, v) -> {
                EntityScan ann = AnnotationUtils.findAnnotation(v.getClass(), EntityScan.class);
                String[] basePackages = ann.basePackages();
                if (basePackages != null && basePackages.length > 0) {
                    for (String p : basePackages) {
                        entityScanBasePackages.add(p);
                    }
                }
            });
        }
        log.debug("get entityScanBasePackages => {}", entityScanBasePackages);
        final Properties properties = hibernateProperties.renderProperties();
        log.debug("get properties => {}", properties);
        if (!entityScanBasePackages.isEmpty()) {
            LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource);

            // #物理命名策略的完全限定名
            // #hibernate.physical_naming_strategy=org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
            builder.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
            SessionFactory sessionFactory = builder.scanPackages(entityScanBasePackages.stream().toArray(String[]::new))
                    .addProperties(properties).buildSessionFactory();
            log.debug("get sessionFactory => {}", sessionFactory);
        }
    }
}
