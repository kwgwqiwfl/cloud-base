package com.ring.welkin.common.core.datasource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DialectContext {
	private static final ThreadLocal<Dialect> CONTEXTHOLDER = new ThreadLocal<>();

	public static void set(Dialect dialect) {
		CONTEXTHOLDER.set(dialect);
	}

	private static void toDefault() {
		CONTEXTHOLDER.set(Dialect.mysql);
	}

	public static Dialect get() {
		Dialect dialect = CONTEXTHOLDER.get();
		if (dialect == null) {
			toDefault();
		}
		return CONTEXTHOLDER.get();
	}

	public static void clear() {
		toDefault();
		log.debug("clear datasource dialect contextHolder");
	}
}
