package com.ring.welkin.common.datasource.config;

import com.ring.welkin.common.datasource.lookup.MasterSlavesDataSources;
import com.ring.welkin.common.datasource.lookup.MasterSlavesRoutingDataSource;
import com.ring.welkin.common.datasource.lookup.aspect.LookupAdvisorBeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = "spring.routing-datasource", name = {
	"enabled"}, havingValue = "true", matchIfMissing = false)
public class RoutingDataSourceAutoConfiguration {

	@Primary
	@Bean
	@ConditionalOnMissingBean
	public DataSourceTransactionManager transactionManager(DataSource routingDataSource) {
		return new DataSourceTransactionManager(routingDataSource);
	}

	@Primary
	@Bean
	@ConditionalOnMissingBean
	public AbstractRoutingDataSource dataSource(MasterSlavesDataSources<? extends DataSource> dbConfig) {
		return new MasterSlavesRoutingDataSource(dbConfig);
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public BeanDefinitionRegistryPostProcessor lookupAdvisorBeanDefinitionRegistryPostProcessor() {
		return new LookupAdvisorBeanDefinitionRegistryPostProcessor();
	}

}
