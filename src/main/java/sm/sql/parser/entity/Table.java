package sm.sql.parser.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Table implements Source {
    private String name;
    private String alias;
}
