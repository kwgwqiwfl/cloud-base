package com.ring.welkin.common.persistence.mybatis.mapper;

import com.ring.welkin.common.persistence.mybatis.mapper.delete.DeleteByPrimaryKeysMapper;
import com.ring.welkin.common.persistence.mybatis.mapper.insert.InsertListWithPrimaryKeyMapper;
import com.ring.welkin.common.persistence.mybatis.mapper.select.SelectByPrimaryKeysMapper;
import com.ring.welkin.common.persistence.mybatis.mapper.update.UpdateListByPrimaryKeyMapper;
import com.ring.welkin.common.persistence.mybatis.mapper.update.UpdateListByPrimaryKeySelectiveMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.base.delete.DeleteByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.select.ExistsWithPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.select.SelectByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeySelectiveMapper;

/**
 * 存在主键的实体根据主键参数进行增删改查操作接口.
 *
 * @param <T>
 * @author cloud
 * @date 2019-05-28 15:07
 */
@RegisterMapper
public interface MyIdableMapper<T>
    extends //
    InsertListWithPrimaryKeyMapper<T>, //
    UpdateByPrimaryKeyMapper<T>, //
    UpdateByPrimaryKeySelectiveMapper<T>, //
    UpdateListByPrimaryKeyMapper<T>, //
    UpdateListByPrimaryKeySelectiveMapper<T>, //
    DeleteByPrimaryKeyMapper<T>, //
    DeleteByPrimaryKeysMapper<T>, //
    SelectByPrimaryKeyMapper<T>, //
    SelectByPrimaryKeysMapper<T>, //
    ExistsWithPrimaryKeyMapper<T>, //
    MyBaseMapper<T> //
{
}
