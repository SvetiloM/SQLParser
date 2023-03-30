package sm.sql.parser.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comparison implements Condition {
    private Column left;
    private Column right;
    private ConnectionType type;

    public enum ConnectionType {
        EQUAL,
        MORE,
        LESS,
        MORE_OR_EQUAL,
        LESS_OR_EQUAL,
        NOT_EQUAL,
        NOT_EQUAL_EXCL,
        NOT_LESS,
        NOT_MORE;
    }
}
