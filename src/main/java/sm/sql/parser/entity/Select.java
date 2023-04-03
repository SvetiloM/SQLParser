package sm.sql.parser.entity;


import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Select implements Source, Serializable {
    private List<Column> columns;
    private Source source;
    private Condition where;
    private List<Column> groupBy;
    private Condition having;
    private List<Order> orderBy;
    private Integer limit;
    private Integer offset;

    @Override
    @Transient
    public String getName() {
        return "";
    }
}


//[ WITH { [ XMLNAMESPACES ,] [ <common_table_expression> ] } ]
//        SELECT select_list [ INTO new_table ]
//        [ FROM table_source ] [ WHERE search_condition ]
//        [ GROUP BY group_by_expression ]
//        [ HAVING search_condition ]
//        [ WINDOW window expression]
//        [ ORDER BY order_expression [ ASC | DESC ] ]
