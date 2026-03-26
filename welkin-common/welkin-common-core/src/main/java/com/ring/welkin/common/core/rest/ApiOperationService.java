package com.ring.welkin.common.core.rest;

import java.util.Collection;

public interface ApiOperationService {

	/**
	 * 初始化 ApiOperation
	 *
	 * @param serviceName 服务名称
	 * @param list        API列表
	 */
	void initApiOperations(String serviceName, Collection<? extends ApiOperation> list);

}
