package com.ring.welkin.common.jpa.rsql;

import com.google.common.collect.Lists;
import com.ring.welkin.common.queryapi.query.field.Sort;
import com.ring.welkin.common.queryapi.query.field.SqlEnums.OrderType;
import com.ring.welkin.common.utils.ICollections;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort.Order;

import java.util.Collection;
import java.util.List;

public class RSQLSort {
	@Getter
	private String sortSql;
	@Getter
	private org.springframework.data.domain.Sort sort;

	private RSQLSort(Collection<Sort> sorts) {
		orderBy(sorts);
	}

	public static RSQLSort build(Collection<Sort> sorts) {
		return new RSQLSort(sorts);
	}

	private void orderBy(Collection<Sort> sorts) {
		StringBuffer buf = new StringBuffer();
		if (ICollections.hasElements(sorts)) {
			List<Order> orders = Lists.newArrayList();
			for (Sort sort : sorts) {
				buf.append(sort.getName()).append(",").append(sort.getOrder().name().toLowerCase()).append(";");

				OrderType orderType = sort.getOrder();
				String property = sort.getName();
				orders.add((orderType == null || orderType.equals(OrderType.ASC)) ? Order.asc(property)
					: Order.desc(property));
			}
			this.sortSql = StringUtils.removeEnd(buf.toString().trim(), ";");
			this.sort = org.springframework.data.domain.Sort.by(orders);
		} else {
            this.sortSql = null;
            this.sort = org.springframework.data.domain.Sort.unsorted();
        }
	}
}
