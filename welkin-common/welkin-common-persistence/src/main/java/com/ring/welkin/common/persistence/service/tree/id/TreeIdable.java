package com.ring.welkin.common.persistence.service.tree.id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.persistence.service.tree.code.TreeCodeable;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 根据编码(ID)维护的树结构数据类型接口
 *
 * @param <ID> 主键类型，即关系维护的字段类型
 * @param <T>  实体类型
 * @author cloud
 * @date 2019年9月24日 下午5:20:24
 */
public interface TreeIdable<ID extends Serializable, T extends TreeIdable<ID, T>>
	extends Idable<ID>, TreeCodeable<ID, T> {

	/**
	 * 获取父节点ID
	 *
	 * @return 父节点ID
     */
    ID getParentId();

    /**
     * 设置父节点ID
     *
     * @param parentId 父节点ID
     */
    void setParentId(ID parentId);

    @Transient
    @JsonIgnore
    @Override
    default ID getSelfCode() {
        return getId();
    }

    @Override
    default void setSelfCode(ID code) {
        setId(code);
    }

    @Transient
    @JsonIgnore
    @Override
    default ID getParentCode() {
        return getParentId();
    }

    @Override
    default void setParentCode(ID parentCode) {
        setParentId(parentCode);
    }
}
