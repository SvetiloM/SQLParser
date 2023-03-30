package sm.sql.parser.entity.part;

public enum JoinPartType implements PartType {

    OLD_INNER_JOIN_LEFT(",", Direction.BEFORE),
    OLD_INNER_JOIN_RIGHT(",", Direction.AFTER),
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

    JoinPartType(String reservedWord, Direction direction) {
        this.reservedWord = reservedWord;
        this.direction = direction;
    }

    public String getValue() {
        return reservedWord;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }
}
