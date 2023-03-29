package sm.sql.parser.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Join implements Source {
    private Source first;
    private Source second;
    private JoinType joinType;
    private Comparison comparison;

    public enum JoinType {
        INNER_JOIN,
        LEFT_JOIN,
        RIGHT_JOIN,
        FULL_JOIN;
    }
}

