package com.ring.welkin.common.persistence.mybatis.type.routing;

import com.ring.welkin.common.core.datasource.Dialect;
import com.ring.welkin.common.core.datasource.DialectContext;
import com.ring.welkin.common.persistence.mybatis.type.varchar.VarcharVsDateTimeTypeHandler;
import org.apache.ibatis.type.DateTypeHandler;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DateTypeRoutingHandler extends DateTypeHandler {

	private final VarcharVsDateTimeTypeHandler varcharTypeHandler;

	public DateTypeRoutingHandler() {
		this.varcharTypeHandler = new VarcharVsDateTimeTypeHandler();
	}

	@Override
	public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Dialect dialect = DialectContext.get();
		switch (dialect) {
			case elasticsearch:
				return varcharTypeHandler.getNullableResult(rs, columnName);
			default:
				return super.getNullableResult(rs, columnName);
		}
	}

	@Override
	public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		Dialect dialect = DialectContext.get();
		switch (dialect) {
			case elasticsearch:
				return varcharTypeHandler.getNullableResult(rs, columnIndex);
			default:
				return super.getNullableResult(rs, columnIndex);
		}
	}

	@Override
	public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		Dialect dialect = DialectContext.get();
		switch (dialect) {
			case elasticsearch:
				return varcharTypeHandler.getNullableResult(cs, columnIndex);
			default:
				return super.getNullableResult(cs, columnIndex);
		}
	}
}
