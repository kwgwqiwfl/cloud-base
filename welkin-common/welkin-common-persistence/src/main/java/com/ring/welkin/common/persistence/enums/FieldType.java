package com.ring.welkin.common.persistence.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FieldType {
    BYTE("byte"), //
    SHORT("short"), //
    INT("int"), //
    BIGINT("bigint"), //
    FLOAT("float"), //
    DOUBLE("double"), //
    BOOLEAN("boolean"), //
    DATE("date"), //
    TIMESTAMP("timestamp"), //
    STRING("string"), //
    BINARY("binary"), //
    DECIMAL("decimal")//
    ;

    public final String value;

    public static FieldType of(String value) {
        for (FieldType type : values()) {
            if (type.getValue()
                    .equals(value)) {
                return type;
            }
        }
        return STRING;
    }
}
