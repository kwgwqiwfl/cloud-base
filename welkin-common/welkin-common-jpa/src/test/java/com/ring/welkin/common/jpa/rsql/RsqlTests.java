package com.ring.welkin.common.jpa.rsql;

import com.ring.welkin.common.queryapi.query.field.FieldGroup;
import com.ring.welkin.common.queryapi.query.field.Sort;
import com.ring.welkin.common.queryapi.query.field.SqlEnums.OrderType;
import io.github.perplexhub.rsql.RSQLJPASupport;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class RsqlTests {

	@Test
	public void test() throws Exception {
		RSQLJPASupport.toSpecification("company.name==name")
			.and(RSQLJPASupport.toSort("company.name,asc,ic;user.id,desc"));
	}

	@Test
	public void testRSQLCondition() {
		FieldGroup fieldGroup = FieldGroup.builder().andEqualTo("name", "zhangsan").andLike("name", "li")
			.orGreaterThan("age", 12)
			.group(FieldGroup.builder().andEqualTo("name", "zhangsan").orLike("name", "li"));
		RSQLCondition rsqlCondition = RSQLCondition.build(fieldGroup);
		System.out.println(rsqlCondition.getConditionSql());
	}

	@Test
	public void testRSQLSort() {
		RSQLSort rsqlSort = RSQLSort.build(
			Arrays.asList(Sort.apply("name"), Sort.apply("age", OrderType.DESC), Sort.apply("id", OrderType.DESC)));
		System.out.println(rsqlSort.getSortSql());
		System.out.println(rsqlSort.getSort());
	}
}
