package com.ring.welkin.common.persistence.mybatis.mapper.base;

import com.ring.welkin.common.persistence.mybatis.mapper.delete.DeleteByPrimaryKeysMapper;
import com.ring.welkin.common.persistence.mybatis.mapper.select.SelectByPrimaryKeysMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 根据主键集合批量操作接口
 *
 * @author cloud
 * @date 2019-05-28 14:59
 */
@RegisterMapper
public interface BaseByPrimaryKeysMapper<T> extends//
    DeleteByPrimaryKeysMapper<T>, //
    SelectByPrimaryKeysMapper<T> //
{

}
