package sm.sql.parser.entity.part;

import java.util.HashMap;
import java.util.Map;

public enum SelectQueryPartType implements PartType {

    COLUMNS("select", Direction.AFTER),
    TABLES("from", Direction.AFTER),
    WHERE("where", Direction.AFTER),
    GROUP_BY("group by", Direction.AFTER),
    HAVING("having", Direction.AFTER),
    ORDER_BY("order by", Direction.AFTER),
    LIMIT("limit", Direction.AFTER),
    OFFSET("offset", Direction.AFTER),;

    private final String reservedWord;
    private final Direction direction;

    private static final Map<String, SelectQueryPartType> values = new HashMap<>();

    static {
        for (SelectQueryPartType value : SelectQueryPartType.values()) {
            values.put(value.reservedWord, value);
        }
    }

    SelectQueryPartType(String reservedWord, Direction direction) {
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
