package com.ring.welkin.common.swagger.handle;

import com.ring.welkin.common.core.rest.ApiOperation;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(doNotUseGetters = true)
@NoArgsConstructor
@AllArgsConstructor
public class DefaultApiOperation implements ApiOperation {

	/**
	 * 服务名称
	 */
	private String serviceName;

	/**
	 * 分组名称
	 */
	private String groupName;

	/**
	 * 标签
	 */
	private List<String> tags;

	/**
	 * 方法
	 */
	private String method;

	/**
	 * 根路径
	 */
	private String basePath;

	/**
	 * 相对路径
	 */
	private String relativePath;

	/**
	 * 说明
	 */
	private String summary;

	/**
	 * 描述
	 */
	private String description;

	/**
	 *
	 */
	private String operationId;

	/**
	 * 消费格式
	 */
	private List<String> consumes;

	/**
	 * 生产格式
	 */
	private List<String> produces;

	/**
	 * 是否弃用
	 */
	private Boolean deprecated;

	/**
	 * 是否启用
	 */
	private Boolean enabled;

}
