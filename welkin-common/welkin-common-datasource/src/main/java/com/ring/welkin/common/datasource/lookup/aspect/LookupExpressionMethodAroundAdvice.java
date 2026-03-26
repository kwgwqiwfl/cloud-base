package com.ring.welkin.common.datasource.lookup.aspect;

import com.ring.welkin.common.datasource.lookup.MasterSlavesDataSources;
import com.ring.welkin.common.datasource.lookup.aspect.LookupAspectConfig.LookupPointCut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;

@Slf4j
public class LookupExpressionMethodAroundAdvice extends LookupMethodAroundAdvice {
	private final LookupPointCut pointCut;

	public LookupExpressionMethodAroundAdvice(MasterSlavesDataSources<? extends DataSource> dbConfig,
											  LookupPointCut pointCut) {
		super(dbConfig);
		this.pointCut = pointCut;
	}

	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		String methodName = method.getName();
		Class<?> targetClass = AopUtils.getTargetClass(target);
		log.trace("before {} called", targetClass.getSimpleName() + "." + methodName);
		routingLookupKey(methodName, pointCut.getLookupKey(), pointCut.isReadOnly());
	}

}
