package com.ring.welkin.common.core.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DriverType {

	MYSQL("Mysql", "com.mysql.jdbc.Driver", "jdbc:mysql://{host}[:{port}]/[{database}]", 3306), //
	MARIADB("Mariadb", "com.mysql.cj.jdbc.Driver", "jdbc:mariadb://{host}[:{port}]/[{database}]", 3306), //
	ORACLE("Oracle Thin", "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@{host}[:{port}]/{database}", 1521), //
	SQLSERVER_MICROSOFT("Microsoft SQL Server (Microsoft Driver)", "com.microsoft.sqlserver.jdbc.SQLServerDriver",
		"jdbc:sqlserver://{host}[:{port}][;DatabaseName={database}]", 1433), //
	SQLSERVER_JTDS("Microsoft SQL Server(JTDS)", "net.sourceforge.jtds.jdbc.Driver",
		"jdbc:jstl:sqlserver://{host}[:{port}][/{database}]", 1433), //
	HIVE("HIVE", "org.apache.hive.jdbc.HiveDriver", "jdbc:hive2://{host}[:{port}][/{database}]", 10000), //
	DBONE("DBONE", "com.intple.dbone.Driver", "", 0), //
	DB2("DB2", "com.ibm.db2.jcc.DB2Driver", "jdbc:db2://{host}[:{port}]/[{database}]", 50000), //
	TERADATA("Teradata", "com.teradata.jdbc.TeraDriver", "jdbc:teradata://{host}/DATABASE={database},DBS_PORT={port}",
		1025), //
	BRIDGE("JDBC-ODBC Bridge", "sun.jdbc.odbc.JdbcOdbcDriver", "jdbc:odbc:{database}", 0), //
	SYBASE("Sybase", "net.sourceforge.jtds.jdbc.Driver", "jdbc:jstl:sybase://{host}[:{port}][/{database}]", 5000), //
	POSTGRESQL("PostgreSQL", "org.postgresql.Driver", "jdbc:postgresql://{host}[:{port}]/[{database}]", 5432), //
	HSQLDB("HSQLDB", "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://{host}[:{port}]/[{database}]", 9001), //
	GREENPLUM("Greenplum", "com.pivotal.jdbc.GreenplumDrive",
		"jdbc:pivotal:greenplum://{host}[:{port}];DatabaseName=[{database}]", 5432), //
	GBASE("GBase", "com.gbase.jdbc.Driver", "", 0), //
	KINGBASE("Kingbase", "com.kingbase.Driver", "jdbc:kingbase://{host}[:{port}]/[{database}]", 54321), //
	KINGBASE8("Kingbase8", "com.kingbase8.Driver", "jdbc:kingbase8://{host}[:{port}]/[{database}]", 54321), //
	DMBASE("DMbase", "dm.jdbc.driver.DmDriver", "jdbc:dm://{host}[:{port}]/[{database}]", 5236), //
	GENERIC_DB("Generic DB", "sybase.jdbc.sqlanywhere.IDriver", "", 0);//

	private final String driverAlias;
	private final String dirverClass;
	private final String urlTemplate;
	private final int defalutPort;

	public static DriverType findJdbcTypeByDriver(String dirver) {
		for (DriverType dialect : DriverType.values()) {
			if (dirver.equals(dialect.getDirverClass())) {
				return dialect;
			}
		}
		return null;
	}

	public static boolean contains(String type) {
		for (DriverType dialect : DriverType.values()) {
			if (dialect.getDriverAlias().equals(type)) {
				return true;
			}
		}
		return false;
	}

	public static DriverType valueOfJdbcType(String jdbcType) {
		for (DriverType typeEnum : DriverType.values()) {
			if (typeEnum.getDriverAlias().equals(jdbcType)) {
				return typeEnum;
			}
		}
		return null;
	}

}
