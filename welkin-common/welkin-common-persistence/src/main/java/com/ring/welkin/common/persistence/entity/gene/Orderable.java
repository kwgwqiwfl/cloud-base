package com.ring.welkin.common.persistence.entity.gene;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 可排序的对象接口
 *
 * @author cloud
 * @date 2021年11月9日 上午10:30:28
 */
public interface Orderable<T extends Orderable<T>> extends Comparable<T> {

    /**
     * 序号
     *
     * @return 序号
     */
    Integer getOrder();

    /**
     * 设置序号
     *
     * @param order 新序号
     */
    void setOrder(Integer order);

    @Override
    default int compareTo(T o) {
        Integer thisOrder = getOrder();
        Integer otherOrder = o.getOrder();
        if ((thisOrder == null && otherOrder == null) || thisOrder == otherOrder) {
            return 0;
        }
        return thisOrder - otherOrder;
    }

    @ApiModel
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderableEntity implements Serializable {
        private static final long serialVersionUID = 4091735979557208896L;

        @ApiModelProperty("序号")
		@NotNull
		private Integer order;

	}

	@ApiModel
	@Setter
	@Getter
	@NoArgsConstructor
	public static class OrderableIdEntity<ID extends Serializable> extends OrderableEntity {
		private static final long serialVersionUID = 4349953062366330346L;

		@ApiModelProperty("ID属性")
		@NotNull
		private ID id;

		public OrderableIdEntity(ID id, Integer order) {
			super(order);
			this.id = id;
		}
	}
}
