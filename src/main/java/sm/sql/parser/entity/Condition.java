package sm.sql.parser.entity;

import lombok.Getter;
import lombok.Setter;
import sm.sql.parser.entity.part.PartType;

@Getter
@Setter
public class Condition {
    private Object left;
    private Object right;
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
