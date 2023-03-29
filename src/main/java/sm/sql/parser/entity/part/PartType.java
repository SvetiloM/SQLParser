package sm.sql.parser.entity.part;

public interface PartType {

    String getValue();

    Direction getDirection();

    enum Direction {
        BEFORE,
        AFTER
    }

}
