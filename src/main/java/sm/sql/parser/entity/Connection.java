package sm.sql.parser.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Connection implements Condition {
    private Condition left;
    private Condition right;
    private ConnectionType connectionType;

    public enum ConnectionType {
        OR,
        AND
    }
}
