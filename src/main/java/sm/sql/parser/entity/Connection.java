package sm.sql.parser.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Connection {
    private Object left;
    private Object right;
    private ConnectionType type;

    public enum ConnectionType {
        EQUAL,
    }
}
