package com.ring.welkin.common.persistence.mybatis.example;

import com.ring.welkin.common.persistence.mybatis.mapper.example.Example;
import com.ring.welkin.common.persistence.mybatis.mybatis.entity.TUser;
import com.ring.welkin.common.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.util.Sqls;

import java.util.Arrays;
import java.util.Date;

public class ExampleTest {

    @Test
    public void test1() {
        Example example = Example.builder(TUser.class)//
            .andWhere(Sqls.custom().andBetween("id", 1, 13).orIn("status", Arrays.asList(1, 2, 3)))//
            .andWhere(Sqls.custom().andEqualTo("username", "lisi").orLessThan("birth", new Date()))//
            .build();
        System.out.println(JsonUtils.toJson(example));
    }
}
