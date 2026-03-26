package com.ring.welkin.common.datasource.lookup;

import com.ring.welkin.common.core.datasource.Dialect;
import com.ring.welkin.common.core.datasource.DialectContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

@Slf4j
@Setter
@Getter
public class MasterSlavesRoutingDataSource extends AbstractRoutingDataSource {

	private MasterSlavesDataSources<? extends DataSource> dbConfig;

	public MasterSlavesRoutingDataSource() {
		super();
	}

	public MasterSlavesRoutingDataSource(MasterSlavesDataSources<? extends DataSource> dbConfig) {
		this.dbConfig = dbConfig;
		this.setDefaultTargetDataSource(dbConfig.getMaster());
		this.setTargetDataSources(dbConfig.getTargetDataSources());
	}

	@Override
	protected Object determineCurrentLookupKey() {
		String lookupKey = LookupContext.get();
		boolean bound = LookupContext.isBound(lookupKey);
		if (bound) {
			lookupKey = LookupContext.unwrapedLookupKey(lookupKey);
		}

		if (LookupType.master.name().equalsIgnoreCase(lookupKey)) {
			log.debug("determing use master datasource.");
			masterDialect(dbConfig);
			return LookupType.master.name();
		}

		// 如果绑定数据源，则直接返回传入的即可
		if (bound) {
			slaveDialect(dbConfig, lookupKey);
			return lookupKey;
		}

		// 使用随机轮询规则决定使用哪个读库
		LookupContext.slave(dbConfig);
		lookupKey = LookupContext.get();
		slaveDialect(dbConfig, lookupKey);
		log.debug("determing use slave datasource: {}.", lookupKey);
		return lookupKey;
	}

	private void masterDialect(MasterSlavesDataSources<?> dbConfig) {
		DialectContext.set(dbConfig.getMasterDialect());
		log.trace("set datasource dialect:" + dbConfig.getMasterDialect().getValue());
	}

	private void slaveDialect(MasterSlavesDataSources<?> dbConfig, String lookupKey) {
		// 如果从节点数为0，则设置master
		int slavesNum = dbConfig.getSlavesNum();
		if (slavesNum <= 0 || !LookupContext.isSlave(lookupKey)) {
			masterDialect(dbConfig);
			return;
		}

		Dialect[] slavesDialects = dbConfig.getSlavesDialects();
		int slaveIndex = LookupContext.getSlaveIndex(lookupKey);
		if (slaveIndex >= slavesDialects.length) {
			slaveIndex = slavesDialects.length - 1;
		}
		Dialect dialect = slavesDialects[slaveIndex];
		DialectContext.set(dialect);
		log.trace("set datasource dialect:" + dialect.getValue());
	}
}
