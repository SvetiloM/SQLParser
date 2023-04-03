package sm.sql.parser.entity.part;


public enum CollectionPartType implements PartType {

    COMMA(",", Direction.BEFORE);

    private final String reservedWord;
    private final Direction direction;

    CollectionPartType(String reservedWord, Direction direction) {
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
