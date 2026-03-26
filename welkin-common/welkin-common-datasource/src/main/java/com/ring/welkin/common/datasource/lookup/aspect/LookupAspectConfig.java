package com.ring.welkin.common.datasource.lookup.aspect;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class LookupAspectConfig {

	/**
	 * 切面切入模式：注解模式和表达式模式，默认是注解模式，表达式模式更通用
	 */
	private LookupMode mode = LookupMode.none;

	/**
	 * 只读方法的配置
	 */
	private ReadMethods readMethods = new ReadMethods();

	/**
	 * 定义多种切面的切入点实例
	 */
	private List<LookupPointCut> pointcuts;

	public static enum LookupMode {
		none, annotation, expression;
	}

	@Getter
	@Setter
	public static class LookupPointCut {

		/**
		 * 切入点的表达式
		 */
		private String expression;

		/**
		 * 指定数据源
		 */
		private String lookupKey;

		/**
		 * 是否只读，还是可读可写
		 */
		private boolean readOnly;
	}

	/**
	 * 只读方法配置，配置优先级: excludes > includes > regex
	 *
	 * @author cloud
	 * @date 2021年7月16日 下午2:50:14
	 */
	@Setter
	@Getter
	public static class ReadMethods {

		/**
		 * 正则表达式
		 */
		private String regex = "^((select)|(find)|(count)|(exist)|(query)|(load)|(fetch))[a-zA-Z\\d_]*$";

		/**
		 * 从正则表达式排除只读的方法
		 */
		private Set<String> excludes = Sets.newHashSet();

		/**
		 * 从正则表达式排除只读的方法
		 */
		private Set<String> includes = Sets.newHashSet();

	}

}
