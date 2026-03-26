package com.ring.welkin.common.core;

import com.google.common.collect.Lists;
import com.ring.welkin.common.utils.ICollections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 委派模式顶级抽象类
 * 
 * @author cloud
 * @date 2022年11月21日 上午11:38:59
 * @param <D> 委派的处理器类型
 * @param <T> 适配方式
 */
@Slf4j
public abstract class AbstractHandlerDelegate<D extends Supported<T>, T> implements ApplicationContextAware {

    /**
     * 委派处理器列表，从ApplicationContext中根据类型动态获取
     */
    protected final List<D> delegates = Lists.newArrayList();

    /**
     * 处理器类型
     */
    protected final Class<D> handlerClass;

    @SuppressWarnings("unchecked")
    public AbstractHandlerDelegate() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        handlerClass = (Class<D>) params[0];
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        Map<String, D> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, handlerClass, true, false);
        if (beans != null && !beans.isEmpty()) {
            delegates.addAll(beans.values());
            AnnotationAwareOrderComparator.sort(this.delegates);
        }
    }

    protected D findSuitableDelegate(T supportedType) {
        if (supportedType != null && ICollections.hasElements(delegates)) {
            for (D delegate : delegates) {
                boolean supports = delegate.supports(supportedType);
                log.debug("delegate:" + delegate.getClass().getName() + ", supportedType:" + supportedType + ", supports:" + supports);
                if (supports) {
                    log.debug("find suitable delegate '" + delegate.getClass().getSimpleName() + "' for supportedType '" + supportedType + "'.");
                    return delegate;
                }
            }
        }
        throw new RuntimeException("Unsupported type: " + supportedType);
    }
}
