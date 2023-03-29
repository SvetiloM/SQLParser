package sm.sql.parser.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Table implements Source {
    public String tableName;
    public String alias;
}
