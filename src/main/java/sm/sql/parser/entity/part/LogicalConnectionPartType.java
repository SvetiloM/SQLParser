package sm.sql.parser.entity.part;

public enum LogicalConnectionPartType implements PartType {
    OR_LEFT("or", Direction.BEFORE),
    OR_RIGHT("or", Direction.AFTER),
    AND_LEFT("and", Direction.BEFORE),
    AND_RIGHT("and", Direction.AFTER);

    private final String reservedWord;
    private final Direction direction;

    LogicalConnectionPartType(String reservedWord, Direction direction) {
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
