package sm.sql.parser.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Column {
    private String columnName;
    private String alias;
    private String table;
}
