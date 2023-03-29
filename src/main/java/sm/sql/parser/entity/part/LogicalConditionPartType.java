package sm.sql.parser.entity.part;

public enum LogicalConditionPartType implements PartType {
    SOME("some", Direction.BEFORE),
    OR("or", Direction.AFTER),
    NOT("not", Direction.BEFORE),
    LIKE("like", Direction.AFTER),
    IN("in", Direction.BEFORE),
    EXISTS("exists", Direction.AFTER),
    BETWEEN("between", Direction.BEFORE),
    ANY("any", Direction.AFTER),
    AND("and", Direction.BEFORE),
    ALL("all", Direction.AFTER);

    private final String reservedWord;
    private final Direction direction;

    LogicalConditionPartType(String reservedWord, Direction direction) {
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
