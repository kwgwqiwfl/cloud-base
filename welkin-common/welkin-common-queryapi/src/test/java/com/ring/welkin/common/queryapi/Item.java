package com.ring.welkin.common.queryapi;

import com.ring.welkin.common.queryapi.query.field.SqlEnums.AndOr;
import com.ring.welkin.common.queryapi.query.field.SqlEnums.Operator;
import com.ring.welkin.common.utils.JsonUtils;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Data
@SuperBuilder
public class Item implements Serializable {
    private static final long serialVersionUID = 3444834012059670128L;

    @Default
    private AndOr andOr = AndOr.AND;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @SuperBuilder
    public static class Field extends Item {
        private static final long serialVersionUID = 7346774076177928979L;

        private String name;

        private Operator operator;

        @Singular
        private List<Object> values;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @SuperBuilder
    public static class Group extends Item {
        private static final long serialVersionUID = 7346774076177928979L;

        @Singular
        private List<Item> items;
    }

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        try {
            Group group = Group.builder() //
                    .item(Field.builder().name("name").operator(Operator.LIKE).value("%zhang%").build())//
                    .item(Field.builder().name("age").operator(Operator.GREATER_THAN).value(12).build())//
                    .item(Group.builder().andOr(AndOr.AND) //
                            .item(Field.builder().name("name").operator(Operator.NOT_EQUAL).value("lisi").build())//
                            .item(Field.builder().name("age").operator(Operator.NOT_EQUAL).value(13).build()).build()//
                    ).item(Field.builder().name("name").operator(Operator.NOT_EQUAL).value("wangwu").build())//
                    .item(Field.builder().name("age").operator(Operator.LESS_THAN_OR_EQUAL).value(120).build())//
                    .build();
            System.out.println(JsonUtils.toJson(group));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
