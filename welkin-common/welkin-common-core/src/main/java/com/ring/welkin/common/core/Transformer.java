package com.ring.welkin.common.core;

import java.util.function.Function;

/**
 * 类型转化器：S（源类型）-> D（目标类型）
 *
 * @param <S> 源类型
 * @param <D> 目标类型
 * @author cloud
 * @date 2020年8月17日 下午3:22:29
 */
public interface Transformer<S, D> extends Function<S, D> {
    /**
     * S（源类型）-> D（目标类型）
     *
     * @param s 源对象
     * @return 目标对象
     */
    D transform(S s);

    @Override
    default D apply(S s) {
        return transform(s);
    }
}
