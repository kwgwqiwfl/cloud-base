package com.ring.welkin.common.persistence.entity.gene;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 使用标签的数据抽象接口
 *
 * @author cloud
 * @date 2021年7月7日 上午11:43:51
 */
public interface Tagsable {
	public static final String TAGS = "tags";

	/**
	 * 获取标签
	 *
	 * @return 获取标签
	 */
	Set<String> getTags();

	/**
	 * 设置标签
	 *
	 * @param tags 标签列表
	 */
	void setTags(Set<String> tags);

	/**
	 * 获取标签描述
	 * @return set
	 */
	default List<Object> getTagObjs(){
		return null;
	}

	/**
	 * 设置标签描述
	 *
	 * @param tags 标签列表
	 */
	default void setTagObjs(List<Object> tags){

	}

	@ApiModel
	@Setter
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TagsEntity implements Tagsable, Serializable {
		private static final long serialVersionUID = -2532955011278451152L;

		@ApiModelProperty("标签列表")
		private Set<String> tags;

	}

	@ApiModel
	@Setter
	@Getter
	@NoArgsConstructor
	public static class TagsIdEntity<ID extends Serializable> extends TagsEntity {
		private static final long serialVersionUID = 5786930809032748439L;

		@ApiModelProperty("ID属性")
		private ID id;

		public TagsIdEntity(ID id, Set<String> tags) {
			super(tags);
			this.id = id;
		}
	}

	@ApiModel
	@Setter
	@Getter
	@NoArgsConstructor
	public static class TagsIdsEntity<ID extends Serializable> extends TagsEntity {
		private static final long serialVersionUID = 7945391776845330527L;

		@ApiModelProperty("ID属性")
		private List<ID> ids;

		public TagsIdsEntity(List<ID> ids, Set<String> tags) {
			super(tags);
			this.ids = ids;
		}
	}

}
