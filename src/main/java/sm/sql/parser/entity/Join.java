package sm.sql.parser.entity;

import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;

@Getter
@Setter
public class Join implements Source {
    private Source first;
    private Source second;
    private JoinType joinType;
    private Comparison comparison;

    @Override
    @Transient
    public String getName() {
        return first.getName();
    }

    public enum JoinType {
        INNER_JOIN,
        LEFT_JOIN,
        RIGHT_JOIN,
        FULL_JOIN;
    }
}

