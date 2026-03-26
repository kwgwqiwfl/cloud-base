package com.ring.welkin.common.datasource.lookup.aspect;

import com.ring.welkin.common.datasource.annotation.Lookup;
import com.ring.welkin.common.datasource.lookup.LookupContext;
import com.ring.welkin.common.datasource.lookup.MasterSlavesDataSources;
import com.ring.welkin.common.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.lang.reflect.Method;

//@Aspect
// @Component
// @Order(0)
@Slf4j
@Deprecated
public class LookupAnnotationAspect {

    @Autowired
    private MasterSlavesDataSources<? extends DataSource> dbConfig;

    @Pointcut("@within(com.ring.welkin.common.datasource.annotation.Lookup)||@annotation(com.ring.welkin.common.datasource.annotation.Lookup)}")
    public void pointCut() {
    }

    // @Around("pointCut() && @annotation(lookup)")
    @Around("pointCut()")
    public Object lookup(ProceedingJoinPoint pjd/* , Lookup lookup */) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjd.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = pjd.getArgs();
        String methodName = method.getName();

        Object target = pjd.getTarget();
        Class<?> clazz = AopUtils.getTargetClass(target);
        log.trace("before {} called", clazz.getSimpleName() + "." + methodName);

        try {
            // 方法上的注解优先级高于类上的，故先获取方法注解，如果没有在获取类上的
            Lookup lookup = method.getAnnotation(Lookup.class);
            if (lookup == null) {
                lookup = clazz.getAnnotation(Lookup.class);
            }

            if (lookup == null) {
                LookupContext.master();
                return pjd.proceed(args);
            }

            String lookupKey = StringUtils.defaultString(lookup.lookupKey(), lookup.value());
            boolean readOnly = lookup.readOnly();
            if (StringUtils.isNotEmpty(lookupKey)) {
                // 如果标注为readOnly，则该类中只有读取的方法使用指定数据源，否则所有方法都是用指定数据源
                if (readOnly) {
                    if (isReadOnlyMethod(methodName)) {
                        LookupContext.set(lookupKey);
                    } else {
                        LookupContext.master();
                    }
                    return pjd.proceed(args);
                } else {
                    LookupContext.set(lookupKey);
                    return pjd.proceed(args);
                }
            }
            // 如果指定只读，并且没有指定具体的lookupKey则自动路由
            else if (readOnly && isReadOnlyMethod(methodName)) {
                LookupContext.slave(dbConfig);
                return pjd.proceed(args);
            }
            LookupContext.master();
            return pjd.proceed(args);
        } catch (Exception e) {
            throw e;
        } finally {
            LookupContext.clear();
            log.trace("after {} called", clazz.getSimpleName() + "." + methodName);
        }
    }

    private boolean isReadOnlyMethod(String methodName) {
        String regex = "^((select)|(find)|(count)|(exist)|(query)|(load)|(fetch))[a-zA-Z\\d_]*$";
        return RegexUtils.check(regex, methodName);
    }
}