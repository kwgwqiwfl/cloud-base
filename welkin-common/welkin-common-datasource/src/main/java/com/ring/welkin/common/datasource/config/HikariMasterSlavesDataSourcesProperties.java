package com.ring.welkin.common.datasource.config;

import com.ring.welkin.common.core.datasource.Dialect;
import com.ring.welkin.common.datasource.lookup.MasterSlavesDataSources;
import com.ring.welkin.common.datasource.lookup.aspect.LookupAspectConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;

@Getter
@Setter
@ConfigurationProperties(prefix = HikariMasterSlavesDataSourcesProperties.PREFIX)
public class HikariMasterSlavesDataSourcesProperties implements MasterSlavesDataSources<HikariDataSource> {
    public static final String PREFIX = "spring.routing-datasource";

    /**
     * 是否启用routing-datasource，默认不启用
     */
    private boolean enabled = false;

    /**
     * 切入点配置
     */
    private LookupAspectConfig aspect;

    /**
     * 主数据源
     */
    private HikariDataSource master;

    /**
     * 从数据源列表
     */
    private HikariDataSource[] slaves;

    public HikariDataSource getMaster() {
        return master;
    }

    @Override
    public Dialect parseDialect(DataSource dataSource) {
        HikariDataSource ds = (HikariDataSource) dataSource;
        return Dialect.fromJdbcUrl(ds.getJdbcUrl());
    }

}
