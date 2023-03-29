package sm.sql.parser.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Join implements TableReference {
    private TableReference first;
    private TableReference second;
    private JoinType joinType;
    private Connection connection;

    public enum JoinType {
        INNER_JOIN,
        LEFT_JOIN,
        RIGHT_JOIN,
        FULL_JOIN;
    }
}

