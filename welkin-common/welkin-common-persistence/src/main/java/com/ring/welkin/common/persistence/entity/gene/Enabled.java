package com.ring.welkin.common.persistence.entity.gene;

import com.ring.welkin.common.core.enums.types.YesNoType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 记录有效状态
 *
 * @param <E> 启用状态类型
 * @author cloud
 * @date 2020年11月11日 下午8:15:49
 */
public interface Enabled<E extends Serializable> {
	public static final String ENABLED = "enabled";

	public static final Integer ENABLE = YesNoType.YES.getValue();
	public static final Integer DISABLE = YesNoType.NO.getValue();

	/**
	 * 获取启用状态
	 *
	 * @return 启用状态
	 */
	E getEnabled();

	/**
	 * 设置启用状态
	 *
	 * @param enabled 启用状态
	 */
	void setEnabled(E enabled);

	@ApiModel
	@Setter
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EnabledEntity<E extends Serializable> implements Enabled<E>, Serializable {
		private static final long serialVersionUID = -2532955011278451152L;

		@ApiModelProperty("启用状态")
		@NotNull
		private E enabled;

	}

	@ApiModel
	@Setter
	@Getter
	@NoArgsConstructor
	public static class EnabledIdEntity<ID extends Serializable, E extends Serializable> extends EnabledEntity<E> {
		private static final long serialVersionUID = 5786930809032748439L;

		@ApiModelProperty("ID属性")
		@NotNull
		private ID id;

		public EnabledIdEntity(ID id, E enabled) {
			super(enabled);
			this.id = id;
		}
	}

	@ApiModel
	@Setter
	@Getter
	@NoArgsConstructor
	public static class EnabledIdsEntity<ID extends Serializable, E extends Serializable> extends EnabledEntity<E> {
		private static final long serialVersionUID = 7945391776845330527L;

		@ApiModelProperty("ID属性")
		@NotEmpty
		private List<ID> ids;

		public EnabledIdsEntity(List<ID> ids, E enabled) {
			super(enabled);
			this.ids = ids;
		}
	}

}
