package sm.sql.parser.parser.util;

import sm.sql.parser.entity.Column;
import java.util.function.Function;

public class Comparator {

    public static Function<String[], Column> columnGenerator = (s) -> {
        Column column = new Column();
        column.setColumnName(s[0]);
        column.setAlias(s[1]);
        column.setTable(s[2]);
        return column;
    };

    public static boolean compareColumns(Column o1, Column o2) {
        boolean names = o1.getColumnName().equals(o2.getColumnName());
        boolean aliases = (o1.getAlias() == null && o2.getAlias() == null) || (o1.getAlias().equals(o2.getAlias()));
        boolean tables = (o1.getTable() == null && o2.getTable() == null) || (o1.getTable().equals(o2.getTable()));
        return names && aliases && tables;
    }
}
