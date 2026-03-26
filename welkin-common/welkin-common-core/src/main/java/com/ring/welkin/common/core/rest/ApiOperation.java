package com.ring.welkin.common.core.rest;

import com.ring.welkin.common.utils.ICollections;

import java.util.List;

/**
 * API 接口信息接口
 *
 * @author cloud
 * @date 2021年7月13日 下午7:01:35
 */
public interface ApiOperation {

	/**
	 * 获取所属服务名称
	 *
	 * @return 所属服务名称
	 */
	String getServiceName();

	/**
	 * 获取接口分组名称
	 *
	 * @return 接口分组
	 */
	String getGroupName();

	/**
	 * 获取标签列表 return 标签列表
	 */
	List<String> getTags();

	/**
	 * 获取接口请求方法
	 *
	 * @return 接口请求方法
	 */
	String getMethod();

	/**
	 * 获取请求根路径
	 *
	 * @return 请求根路径
	 */
	String getBasePath();

	/**
	 * 获取相对路径
	 *
	 * @return 相对路径
	 */
	String getRelativePath();

	/**
	 * 获取接口说明
	 *
	 * @return 接口说明
	 */
	String getSummary();

	/**
	 * 获取接口详细描述
	 *
	 * @return 接口详细描述
	 */
	String getDescription();

	/**
	 * 获取操作ID
	 *
	 * @return 操作ID
	 */
	String getOperationId();

	/**
	 * 获取接收格式
	 *
	 * @return 接收格式
	 */
	List<String> getConsumes();

	/**
	 * 获取响应格式
	 *
	 * @return 响应格式
	 */
	List<String> getProduces();

	/**
	 * 获取接口是否弃用
	 *
	 * @return 接口是否弃用
	 */
	Boolean getDeprecated();

	/**
	 * 设置所属服务名称
	 *
	 * @param serviceName 所属服务名称
	 */
	void setServiceName(String serviceName);

	/**
	 * 设置接口分组
	 *
	 * @param groupName 接口分组
	 */
	void setGroupName(String groupName);

	/**
	 * 设置接口标签
	 *
	 * @param tags 接口标签
	 */
	void setTags(List<String> tags);

	/**
	 * 设置接口请求方法
	 *
	 * @param method 接口请求方法
	 */
	void setMethod(String method);

	/**
	 * 设置请求根路径
	 *
	 * @param basePath 请求根路径
	 */
	void setBasePath(String basePath);

	/**
	 * 设置相对路径
	 *
	 * @param relativePath 相对路径
	 */
	void setRelativePath(String relativePath);

	/**
	 * 设置接口说明
	 *
	 * @param summary 接口说明
	 */
	void setSummary(String summary);

	/**
	 * 设置接口详细描述
	 *
	 * @param description 接口详细描述
	 */
	void setDescription(String description);

	/**
	 * 设置操作ID
	 *
	 * @param operationId 操作ID
	 */
	void setOperationId(String operationId);

	/**
	 * 设置接收格式
	 *
	 * @param consumes 接收格式
	 */
	void setConsumes(List<String> consumes);

	/**
	 * 设置响应格式
	 *
	 * @param produces 响应格式
	 */
	void setProduces(List<String> produces);

	/**
	 * 设置接口是否弃用
	 *
	 * @param deprecated 接口是否弃用
	 */
	void setDeprecated(Boolean deprecated);

	/**
	 * 获取是否有效
	 *
	 * @return 是否有效
	 */
	Boolean getEnabled();

	default String getFullPath() {
		String basePath = getBasePath();
		String relativePath = getRelativePath();
		if (basePath != null && relativePath != null) {
			return trimSlash(basePath + relativePath);
		}
		return "";
	}

	default String operationKey() {
		return trimSlash(operationKey(getFullPath(), getMethod()));
	}

	default String trimSlash(String src) {
		return src.replaceAll("//", "/");
	}

	default String tag() {
		List<String> tags = getTags();
		if (ICollections.hasElements(tags)) {
			return tags.get(0);
		}
		return null;
	}

	static String operationKey(String fullPath, String method) {
		return fullPath + "@" + method.toLowerCase();
	}
}
