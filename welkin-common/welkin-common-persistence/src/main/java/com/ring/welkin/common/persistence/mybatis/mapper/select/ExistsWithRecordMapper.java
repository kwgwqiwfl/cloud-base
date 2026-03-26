package com.ring.welkin.common.persistence.mybatis.mapper.select;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

@RegisterMapper
public interface ExistsWithRecordMapper<T> {


	/**
	 * 根据条件进行查询是否存在
	 *
	 * @param record 条件
	 * @return 存在记录
	 */
	@SelectProvider(type = ExistsWithRecordProvider.class, method = "dynamicSQL")
	List<Integer> existsWithRecord(T record);
}
