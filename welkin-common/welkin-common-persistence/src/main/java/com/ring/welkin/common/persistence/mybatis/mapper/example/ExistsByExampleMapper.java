package com.ring.welkin.common.persistence.mybatis.mapper.example;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

@RegisterMapper
public interface ExistsByExampleMapper<T> {


	/**
	 * 根据Example条件进行查询是否存在
	 *
	 * @param example 条件
	 * @return
	 */
	@SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
	List<Integer> existsWithExample(Example example);
}
