package com.ring.welkin.common.persistence.mybatis.mybatis.service.impl;

import com.ring.welkin.common.persistence.mybatis.mybatis.entity.TUser;
import com.ring.welkin.common.persistence.service.BaseIdableService;
import com.ring.welkin.common.persistence.service.criteria.example.ExampleQueryService;

public interface TUserServiceImpl extends BaseIdableService<Long, TUser>, ExampleQueryService<TUser> {
}
