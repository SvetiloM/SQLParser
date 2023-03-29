package sm.sql.parser.entity.part;

import java.util.HashMap;
import java.util.Map;

public enum TablePartType implements PartType {

    NAME_BEFORE_ALIAS("as", Direction.BEFORE),
    ALIAS("as", Direction.AFTER);

    private final String reservedWord;
    private final Direction direction;

    private static final Map<String, PartType> values = new HashMap<>();

    static {
        for (TablePartType value : TablePartType.values()) {
            values.put(value.reservedWord, value);
        }
    }

    TablePartType(String reservedWord, Direction direction) {
        this.reservedWord = reservedWord;
        this.direction = direction;
    }

    public static PartType getByReservedWord(String name) {
        return values.get(name);
    }

    public static String[] getReservedWords() {
        return values.keySet().toArray(new String[0]);
    }

    @Override
    public String getValue() {
        return reservedWord;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }
}
