package org.example.entity;

import java.util.HashMap;
import java.util.Map;

public enum QueryPartType implements PartType {

    COLUMNS("select", Direction.AFTER),
    TABLES("from", Direction.AFTER);

    private final String reservedWord;
    private final Direction direction;

    private static final Map<String, QueryPartType> values = new HashMap<>();

    static {
        for (QueryPartType value : QueryPartType.values()) {
            values.put(value.reservedWord, value);
        }
    }

    QueryPartType(String reservedWord, Direction direction) {
        this.reservedWord = reservedWord;
        this.direction = direction;
    }

    public static PartType getByReservedWord(String name) {
        return values.get(name);
    }

    public static String[] getReservedWords() {
        return values.keySet().toArray(new String[0]);
    }

    public String getValue() {
        return reservedWord;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

}
