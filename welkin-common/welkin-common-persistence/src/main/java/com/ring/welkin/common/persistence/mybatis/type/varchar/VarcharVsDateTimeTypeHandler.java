package com.ring.welkin.common.persistence.mybatis.type.varchar;

import com.xkzhangsan.time.formatter.DateTimeFormatterUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@Getter
@Setter
public class VarcharVsDateTimeTypeHandler extends AbstractVarcharTypeHandler<Date> {

	private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
	private String dataFormat;

	public VarcharVsDateTimeTypeHandler() {
		this(PATTERN);
	}

	protected VarcharVsDateTimeTypeHandler(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	@Override
	public String translate2Str(Date t) {
		if (t != null) {
			return DateTimeFormatterUtil.format(t, PATTERN);
		}
		return null;
	}

	@Override
	public Date translate2Bean(String result) {
		if (result != null && !result.isEmpty()) {
			try {
				return DateTimeFormatterUtil.smartParseToDate(result);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
		}
		return null;
	}
}
