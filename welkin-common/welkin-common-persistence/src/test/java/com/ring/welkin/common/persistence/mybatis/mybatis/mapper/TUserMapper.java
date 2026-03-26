package com.ring.welkin.common.persistence.mybatis.mybatis.mapper;

import com.ring.welkin.common.persistence.mybatis.mapper.MyIdableMapper;
import com.ring.welkin.common.persistence.mybatis.mybatis.entity.TUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TUserMapper extends MyIdableMapper<TUser> {
}