package com.ring.welkin.common.persistence.entity.base;

/**
 * 继承{@link com.ring.welkin.common.persistence.entity.base.Maintable}的实体类公共查询接口定义
 *
 * @param <T> 继承Maintable的类型
 * @author cloud
 * @date 2019年10月9日 上午11:06:23
 */
public interface BaseMaintableService<T extends Maintable> extends BaseCommonEntityService<String, T> {
}
