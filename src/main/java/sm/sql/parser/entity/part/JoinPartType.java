package sm.sql.parser.entity.part;

import java.util.HashMap;
import java.util.Map;

public enum JoinPartType implements PartType {
    INNER_JOIN_FIRST("join", Direction.BEFORE),
    INNER_JOIN_SECOND("join", Direction.AFTER),

    LEFT_JOIN_FIRST("left join", Direction.BEFORE),
    LEFT_JOIN_SECOND("left join", Direction.AFTER),

    RIGHT_JOIN_FIRST("right join", Direction.BEFORE),
    RIGHT_JOIN_SECOND("right join", Direction.AFTER),

    FULL_JOIN_FIRST("full join", Direction.BEFORE),
    FULL_JOIN_SECOND("full join", Direction.AFTER),

    ON("on", Direction.AFTER);

    private final String reservedWord;
    private final Direction direction;

    private static final Map<String, PartType> values = new HashMap<>();

    static {
        for (JoinPartType value : JoinPartType.values()) {
            values.put(value.reservedWord, value);
        }
    }

    JoinPartType(String reservedWord, Direction direction) {
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
