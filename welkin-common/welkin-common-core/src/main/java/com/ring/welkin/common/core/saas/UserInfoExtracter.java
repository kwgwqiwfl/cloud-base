package com.ring.welkin.common.core.saas;

/**
 * 从对象中提取UserInfo信息
 *
 * @param <T> 来源对象类型
 * @author cloud
 * @date 2021年8月5日 下午2:59:19
 */
public interface UserInfoExtracter<T> {

	/**
	 * 提取信息
	 *
	 * @param t 来源对象
	 * @return UserInfo信息
	 */
	UserInfo extract(T t);
}
