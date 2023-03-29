package sm.sql.parser.entity.part;

public enum ComparisonPartType implements PartType {
    EQUAL_LEFT("=", Direction.BEFORE),
    EQUAL_RIGHT("=", Direction.AFTER),
    MORE_LEFT(">", Direction.BEFORE),
    MORE_RIGHT(">", Direction.AFTER),
    LESS_LEFT("<", Direction.BEFORE),
    LESS_RIGHT("<", Direction.AFTER),
    MORE_OR_EQUAL_LEFT(">=", Direction.BEFORE),
    MORE_OR_EQUAL_RIGHT(">=", Direction.AFTER),
    LESS_OR_EQUAL_LEFT("<=", Direction.BEFORE),
    LESS_OR_EQUAL_RIGHT("<=", Direction.AFTER),
    NOT_EQUAL_LEFT("<>", Direction.BEFORE),
    NOT_EQUAL_RIGHT("<>", Direction.AFTER),
    NOT_EQUAL_EXCL_LEFT("!=", Direction.BEFORE),
    NOT_EQUAL_EXCL_RIGHT("!=", Direction.AFTER),
    NOT_LESS_LEFT("!<", Direction.BEFORE),
    NOT_LESS_RIGHT("!<", Direction.AFTER),
    NOT_MORE_LEFT("!>", Direction.BEFORE),
    NOT_MORE_RIGHT("!>", Direction.AFTER);

    private final String reservedWord;
    private final Direction direction;

    private static final ComparisonPartType[] compositeValues = new ComparisonPartType[]{
            MORE_OR_EQUAL_LEFT,
            MORE_OR_EQUAL_RIGHT,
            LESS_OR_EQUAL_LEFT,
            LESS_OR_EQUAL_RIGHT,
            NOT_EQUAL_LEFT,
            NOT_EQUAL_RIGHT,
            NOT_EQUAL_EXCL_LEFT,
            NOT_EQUAL_EXCL_RIGHT,
            NOT_LESS_LEFT,
            NOT_LESS_RIGHT,
            NOT_MORE_LEFT,
            NOT_MORE_RIGHT
    };


    ComparisonPartType(String reservedWord, Direction direction) {
        this.reservedWord = reservedWord;
        this.direction = direction;
    }

    public static ComparisonPartType[] getCompositeValues() {
        return compositeValues;
    }

    public String getValue() {
        return reservedWord;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }
}
