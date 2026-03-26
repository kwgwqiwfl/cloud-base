package com.ring.welkin.common.persistence.mybatis.mapper.base;

import com.ring.welkin.common.persistence.mybatis.mapper.delete.DeleteByPrimaryKeysMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 批量删除接口
 *
 * @param <T>
 * @author cloud
 * @date 2019-05-28 15:00
 */
@RegisterMapper
public interface BaseDeleteListMapper<T> extends DeleteByPrimaryKeysMapper<T> {
}
