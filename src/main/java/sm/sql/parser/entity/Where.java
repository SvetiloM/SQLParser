package sm.sql.parser.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Where {
    //todo condition
    private Object left;
    private Object right;
    private Connection connection;

    public enum Connection {
        OR,
        AND
    }
}