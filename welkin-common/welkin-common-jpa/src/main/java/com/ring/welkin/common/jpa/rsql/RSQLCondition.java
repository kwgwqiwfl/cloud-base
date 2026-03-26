package com.ring.welkin.common.jpa.rsql;

import com.ring.welkin.common.queryapi.query.field.Field;
import com.ring.welkin.common.queryapi.query.field.FieldGroup;
import com.ring.welkin.common.queryapi.query.field.FieldItem;
import com.ring.welkin.common.queryapi.query.field.SqlEnums.AndOr;
import com.ring.welkin.common.queryapi.query.field.SqlEnums.Operator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class RSQLCondition {
	@Getter
	private final String conditionSql;

	private RSQLCondition(FieldGroup fieldGroup) {
		this.conditionSql = where(fieldGroup);
	}

	public static RSQLCondition build(FieldGroup fieldGroup) {
		return new RSQLCondition(fieldGroup);
	}

	private String where(FieldGroup fieldGroup) {
		StringBuffer buf = new StringBuffer("");
		if (fieldGroup != null) {
			List<FieldItem> ordItems = fieldGroup.reIndex().ordItems();
			if (ordItems != null && !ordItems.isEmpty()) {
				for (FieldItem item : ordItems) {
					if (item instanceof FieldGroup) {
						FieldGroup group = (FieldGroup) item;
						buf.append(group.getAndOr().equals(AndOr.AND) ? ";" : ",").append("(")
							.append(trimAndOr(where(group))).append(")");
					} else {
						Field field = (Field) item;
						buf.append(field(field));
					}
				}
			}
		}
		String whereSql = trimAndOr(buf.toString());
		log.debug("conditionSql ==> {}", whereSql);
		return whereSql;
	}

	// 去除sql开头的“;”和“,”
	private String trimAndOr(String sql) {
		return StringUtils.removeStartIgnoreCase(StringUtils.removeStartIgnoreCase(StringUtils.trim(sql), ";"), ",");
	}

	private String field(Field field) {
		AndOr andOr = field.getAndOr();
		String property = field.getName();
		Operator oper = field.getOper();
		Object[] values = field.getValue();
		if (property == null || property.length() == 0) {
			return "";
		}
		if (oper == null) {
			return "";
		}
		switch (andOr) {
			case OR:
				switch (oper) {
					case EQUAL:
						return orEqualTo(property, values[0]);
					case NOT_EQUAL:
						return orNotEqualTo(property, values[0]);
					case LIKE:
						return orLike(property, values[0]);
					case NOT_LIKE:
						return orNotLike(property, values[0]);
					case BETWEEN:
						return orBetween(property, values[0], values[1]);
					case NOT_BETWEEN:
						return orNotBetween(property, values[0], values[1]);
					case GREATER_THAN:
						return orGreaterThan(property, values[0]);
					case GREATER_THAN_OR_EQUAL:
						return orGreaterThanOrEqualTo(property, values[0]);
					case LESS_THAN:
						return orLessThan(property, values[0]);
					case LESS_THAN_OR_EQUAL:
						return orLessThanOrEqualTo(property, values[0]);
					case IS_NULL:
						return orIsNull(property);
					case NOT_NULL:
						return orIsNotNull(property);
					case IN:
						return orIn(property, Arrays.asList(values));
					case NOT_IN:
						return orNotIn(property, Arrays.asList(values));
					default:
						return orEqualTo(property, values[0]);
				}
			default:
				switch (oper) {
					case EQUAL:
						return andEqualTo(property, values[0]);
					case NOT_EQUAL:
						return andNotEqualTo(property, values[0]);
					case LIKE:
						return andLike(property, values[0]);
					case NOT_LIKE:
						return andNotLike(property, values[0]);
					case BETWEEN:
						return andBetween(property, values[0], values[1]);
					case NOT_BETWEEN:
						return andNotBetween(property, values[0], values[1]);
					case GREATER_THAN:
						return andGreaterThan(property, values[0]);
					case GREATER_THAN_OR_EQUAL:
						return andGreaterThanOrEqualTo(property, values[0]);
					case LESS_THAN:
						return andLessThan(property, values[0]);
					case LESS_THAN_OR_EQUAL:
						return andLessThanOrEqualTo(property, values[0]);
					case IS_NULL:
						return andIsNull(property);
					case NOT_NULL:
						return andIsNotNull(property);
					case IN:
						return andIn(property, Arrays.asList(values));
					case NOT_IN:
						return andNotIn(property, Arrays.asList(values));
					default:
						return andEqualTo(property, values[0]);
				}
		}
	}

	private String condition(String andOr, String property, String operator, Object value) {
		return new StringBuffer().append(andOr).append(property).append(operator).append(value).toString();
	}

	private String andCondition(String property, String operator, Object value) {
		return condition(";", property, operator, value);
	}

	private String orCondition(String property, String operator, Object value) {
		return condition(",", property, operator, value);
	}

	private String andIsNull(String property) {
		return andCondition(property, "=na=", "''");
	}

	private String andIsNotNull(String property) {
		return andCondition(property, "=nn=", "''");
	}

	private String andEqualTo(String property, Object value) {
		return andCondition(property, "==", value);
	}

	private String andNotEqualTo(String property, Object value) {
		return andCondition(property, "!=", value);
	}

	private String andGreaterThan(String property, Object value) {
		return andCondition(property, "=gt=", value);
	}

	private String andGreaterThanOrEqualTo(String property, Object value) {
		return andCondition(property, "=ge=", value);
	}

	private String andLessThan(String property, Object value) {
		return andCondition(property, "=lt=", value);
	}

	private String andLessThanOrEqualTo(String property, Object value) {
		return andCondition(property, "=le=", value);
	}

	private String andIn(String property, Iterable<?> values) {
		return andCondition(property, "=in=", StringUtils.join("(", StringUtils.join(values, ","), ")"));
	}

	private String andNotIn(String property, Iterable<?> values) {
		return andCondition(property, "=out=", StringUtils.join("(", StringUtils.join(values, ","), ")"));
	}

	private String andBetween(String property, Object value1, Object value2) {
		return andCondition(property, "=bt=", StringUtils.join("(", value1, ",", value2, ")"));
	}

	private String andNotBetween(String property, Object value1, Object value2) {
		return andCondition(property, "=nb=", StringUtils.join("(", value1, ",", value2, ")"));
	}

	private String andLike(String property, Object value) {
		return andCondition(property, "=like=", convertLikeValue(value));
	}

	private String andNotLike(String property, Object value) {
		return andCondition(property, "=notlike=", convertLikeValue(value));
	}

	private String orIsNull(String property) {
		return orCondition(property, "=na=", "''");
	}

	private String orIsNotNull(String property) {
		return orCondition(property, "=nn=", "''");
	}

	private String orEqualTo(String property, Object value) {
		return orCondition(property, "==", value);
	}

	private String orNotEqualTo(String property, Object value) {
		return orCondition(property, "!=", value);
	}

	private String orGreaterThan(String property, Object value) {
		return orCondition(property, "=gt=", value);
	}

	private String orGreaterThanOrEqualTo(String property, Object value) {
		return orCondition(property, "=ge=", value);
	}

	private String orLessThan(String property, Object value) {
		return orCondition(property, "=lt=", value);
	}

	private String orLessThanOrEqualTo(String property, Object value) {
		return orCondition(property, "=le=", value);
	}

	private String orIn(String property, Iterable<?> values) {
		return orCondition(property, "=in=", StringUtils.join(values, ","));
	}

	private String orNotIn(String property, Iterable<?> values) {
		return orCondition(property, "=out=", StringUtils.join(values, ","));
	}

	private String orBetween(String property, Object value1, Object value2) {
		return orCondition(property, "=bt=", StringUtils.join("(", value1, ",", value2, ")"));
	}

	private String orNotBetween(String property, Object value1, Object value2) {
		return orCondition(property, "=nb=", StringUtils.join("(", value1, ",", value2, ")"));
	}

	private String orLike(String property, Object value) {
		return orCondition(property, "=like=", convertLikeValue(value));
	}

	private String orNotLike(String property, Object value) {
		return orCondition(property, "=notlike=", convertLikeValue(value));
	}

	private String convertLikeValue(Object value) {
		String likeValue = value.toString();
		if (likeValue.startsWith("%")) {
			likeValue = StringUtils.join("*", likeValue.substring(1));
		}
		if (likeValue.endsWith("%")) {
			likeValue = StringUtils.join(likeValue.substring(0, likeValue.length() - 1), "*");
		}
		return likeValue;
	}
}
