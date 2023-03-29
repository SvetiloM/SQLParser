package sm.sql.parser.entity.part;

import java.util.HashMap;
import java.util.Map;

public enum ConnectionPartType implements PartType {
    EQUAL_LEFT("=", Direction.BEFORE),
    EQUAL_RIGHT("=", Direction.AFTER);

    private final String reservedWord;
    private final Direction direction;

    private static final Map<String, PartType> values = new HashMap<>();

    static {
        for (ConnectionPartType value : ConnectionPartType.values()) {
            values.put(value.reservedWord, value);
        }
    }

    ConnectionPartType(String reservedWord, Direction direction) {
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
