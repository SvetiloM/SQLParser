package org.example.entity;

import java.util.HashMap;
import java.util.Map;

public enum PartType {
    COLUMNS("select"),
    TABLES("from");

    private final String reservedWord;

    private static final Map<String, PartType> values = new HashMap<>();

    static {
        for (PartType value : PartType.values()) {
            values.put(value.reservedWord, value);
        }
    }

    PartType(String reservedWord) {
        this.reservedWord = reservedWord;
    }

    public static PartType getByReservedWord(String name) {
        return values.get(name);
    }
}
