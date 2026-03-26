package com.ring.welkin.common.datasource.lookup.aspect;

import com.ring.welkin.common.datasource.annotation.Lookup;
import com.ring.welkin.common.datasource.lookup.LookupContext;
import com.ring.welkin.common.datasource.lookup.MasterSlavesDataSources;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;

@Slf4j
public class LookupAnnotationMethodAroundAdvice extends LookupMethodAroundAdvice {

    public LookupAnnotationMethodAroundAdvice(MasterSlavesDataSources<? extends DataSource> dbConfig) {
        super(dbConfig);
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        String methodName = method.getName();
        Class<?> targetClass = AopUtils.getTargetClass(target);
        log.trace("before {} called", targetClass.getSimpleName() + "." + methodName);

        // 方法上的注解优先级高于类上的，故先获取方法注解，如果没有在获取类上的
        Lookup lookup = method.getAnnotation(Lookup.class);
        if (lookup == null) {
            lookup = targetClass.getAnnotation(Lookup.class);
        }

        if (lookup == null) {
            LookupContext.master();
            return;
        }
        routingLookupKey(methodName, StringUtils.defaultString(lookup.lookupKey(), lookup.value()), lookup.readOnly());
    }

}