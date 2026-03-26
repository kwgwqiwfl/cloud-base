package com.ring.welkin.common.core.enums.valuable;

import com.ring.welkin.common.core.enums.labelable.Labelable;

/**
 * 可取常量且可比较大小可取标签常量值枚举接口
 *
 * @param <T> 目标类型
 * @author cloud
 * @date 2019-05-28 14:05
 */
public interface ValuableAndComparableAndLabelable<T extends Comparable<T>>
    extends ValuableAndComparable<T>, Labelable {

}
