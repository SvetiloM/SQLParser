package sm.sql.parser.entity.part;

import java.util.HashMap;
import java.util.Map;

public enum OrderPartType implements PartType {

    ASC(" asc", Direction.BEFORE),

    DESC(" desc", Direction.BEFORE);

    private final String reservedWord;
    private final Direction direction;

    private static final Map<String, PartType> values = new HashMap<>();

    static {
        for (OrderPartType value : OrderPartType.values()) {
            values.put(value.reservedWord, value);
        }
    }

    OrderPartType(String reservedWord, Direction direction) {
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
