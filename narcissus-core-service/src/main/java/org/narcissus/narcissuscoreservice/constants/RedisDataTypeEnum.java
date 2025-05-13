package org.narcissus.narcissuscoreservice.constants;

public enum RedisDataTypeEnum {
    UNDEFINED("u"), NULL("n"), BOOLEAN("b"), STRING("s"), NUMBER("n"), DATE("d"), OBJECT("o");

    private final String type;

    RedisDataTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
