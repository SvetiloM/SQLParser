package sm.sql.parser.parser.util;

import sm.sql.parser.entity.Column;
import sm.sql.parser.entity.Order;

import java.util.List;
import java.util.function.Function;

public class Comparator {

    public static Column columnGenerator(String name, String alias, String table) {
        Column column = new Column();
        column.setColumnName(name);
        column.setTable(table);
        column.setAlias(alias);
        return column;
    };

    public static Order orderGenerator(Column column, Order.OrderType orderType) {
        Order order = new Order();
        order.setColumn(column);
        order.setType(orderType);
        return order;
    }

    public static boolean compareOrders(Order order, Order expectedOrder) {
        boolean types = order.getType().equals(expectedOrder.getType());
        boolean columns = compareColumns(order.getColumn(), expectedOrder.getColumn());
        return types && columns;
    }

    public static boolean compareColumns(Column o1, Column o2) {
        boolean names = o1.getColumnName().equals(o2.getColumnName());
        boolean aliases = (o1.getAlias() == null && o2.getAlias() == null) || (o1.getAlias().equals(o2.getAlias()));
        boolean tables = (o1.getTable() == null && o2.getTable() == null) || (o1.getTable().equals(o2.getTable()));
        return names && aliases && tables;
    }

    public static boolean compareColumns(List<Column> o1, List<Column> o2) {
        return o1.equals(o2);
    }

//    public static boolean compareColumns(Condition l1, List<Column> l2) {
//        if (l1.size() != l2.size()) return false;
//        for (int i = 0; i < l1.size(); i++) {
//            if (!compareColumns(l1.get(i), l2.get(i))) return false;
//        }
//        return true;
//    }
//
//    public static boolean compareConditions(Condition c1, Condition c2) {
//        boolean lefts = compareColumns(c1.getLeft(), c2.getLeft());
//        boolean rights = compareColumns(c1.getRight(), c2.getRight());
//        boolean types = c1.getType().equals(c2.getType());
//
//        return lefts && rights && types;
//    }
}
