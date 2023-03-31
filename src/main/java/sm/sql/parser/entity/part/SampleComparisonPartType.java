package sm.sql.parser.entity.part;

public enum SampleComparisonPartType implements PartType {
    SOME_LEFT("some", Direction.BEFORE),
    SOME_RIGHT("some", Direction.AFTER),
    IN_LEFT("in", Direction.BEFORE),
    IN_RIGHT("in", Direction.AFTER),
    EXISTS_LEFT("exists", Direction.BEFORE),
    EXISTS_RIGHT("exists", Direction.AFTER),
    ANY_LEFT("any", Direction.BEFORE),
    ANY_RIGHT("any", Direction.AFTER),
    ALL_LEFT("all", Direction.BEFORE),
    ALL_RIGHT("all", Direction.AFTER);

    private final String reservedWord;
    private final Direction direction;

    SampleComparisonPartType(String reservedWord, Direction direction) {
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
