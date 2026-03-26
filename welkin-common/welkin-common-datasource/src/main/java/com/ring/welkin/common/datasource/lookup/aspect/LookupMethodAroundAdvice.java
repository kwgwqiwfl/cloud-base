package com.ring.welkin.common.datasource.lookup.aspect;

import com.ring.welkin.common.datasource.lookup.LookupContext;
import com.ring.welkin.common.datasource.lookup.MasterSlavesDataSources;
import com.ring.welkin.common.datasource.lookup.aspect.LookupAspectConfig.ReadMethods;
import com.ring.welkin.common.utils.ICollections;
import com.ring.welkin.common.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.support.AopUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Set;

@Slf4j
public abstract class LookupMethodAroundAdvice implements MethodBeforeAdvice, AfterReturningAdvice {
	private final MasterSlavesDataSources<? extends DataSource> dbConfig;

	public LookupMethodAroundAdvice(MasterSlavesDataSources<? extends DataSource> dbConfig) {
		this.dbConfig = dbConfig;
	}

	protected void routingLookupKey(String methodName, String lookupKey, boolean readOnly) {
		ReadMethods readMethods = dbConfig.getAspect().getReadMethods();
		if (StringUtils.isNotEmpty(lookupKey)) {
			// 如果标注为readOnly，则该类中只有读取的方法使用指定数据源，否则所有方法都是用指定数据源
			if (readOnly) {
				if (isReadOnlyMethod(methodName, dbConfig.getAspect().getReadMethods())) {
					LookupContext.set(lookupKey, true);
				} else {
					LookupContext.master();
				}
				return;
			} else {
				LookupContext.set(lookupKey, true);
				return;
			}
		} else {
			if (readOnly && isReadOnlyMethod(methodName, readMethods)) {
				LookupContext.slave(dbConfig);
				return;
			}
		}
		LookupContext.master();
	}

	private boolean isReadOnlyMethod(String methodName, ReadMethods readMethods) {
		Set<String> excludes = readMethods.getExcludes();
		if (ICollections.hasElements(excludes) && excludes.contains(methodName)) {
			return false;
		}
		Set<String> includes = readMethods.getIncludes();
		if (ICollections.hasElements(includes) && includes.contains(methodName)) {
			return true;
		}
		String regex = readMethods.getRegex();
		if (StringUtils.isNotEmpty(regex)) {
			return RegexUtils.check(regex, methodName);
		}
		return false;
	}

	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		Class<?> targetClass = AopUtils.getTargetClass(target);
		log.trace("after {} called", targetClass.getSimpleName() + "." + method.getName());
		LookupContext.clear();
	}
}