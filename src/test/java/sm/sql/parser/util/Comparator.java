package sm.sql.parser.util;

import sm.sql.parser.entity.*;

import java.util.List;

public class Comparator {

    public static Column columnGenerator(String name, String alias, String table) {
        Column column = new Column();
        column.setColumnName(name);
        column.setTable(table);
        column.setAlias(alias);
        return column;
    }

    public static Column columnGenerator(String name) {
        Column column = new Column();
        column.setColumnName(name);
        return column;
    }

    public static Order orderGenerator(Column column, Order.OrderType orderType) {
        Order order = new Order();
        order.setColumn(column);
        order.setType(orderType);
        return order;
    }

    public static Comparison comparisonGenerator(Column left, Object right, Comparison.ConnectionType connectionType) {
        Comparison comparison = new Comparison();
        comparison.setLeft(left);
        comparison.setRight(right);
        comparison.setType(connectionType);
        return comparison;
    }

    public static Connection connectionGenerator(Condition left, Condition right, Connection.ConnectionType connectionType) {
        Connection connection = new Connection();
        connection.setLeft(left);
        connection.setRight(right);
        connection.setConnectionType(connectionType);
        return connection;
    }


    public static Table tableGenerator(String name, String alias) {
        Table table = new Table();
        table.setName(name);
        table.setAlias(alias);
        return table;
    }

    public static Table tableGenerator(String name) {
        Table table = new Table();
        table.setName(name);
        return table;
    }

    public static Join joinGenerator(Source first, Source second, Join.JoinType joinType, Comparison comparison) {
        Join join = new Join();
        join.setFirst(first);
        join.setSecond(second);
        join.setJoinType(joinType);
        join.setComparison(comparison);
        return join;
    }

    public static Select selectGenerator(List<Column> columns, Source source, Condition where) {
        Select select = new Select();
        select.setColumns(columns);
        select.setSource(source);
        select.setWhere(where);
        return select;
    }
    public static Select selectGenerator(List<Column> columns, Source source) {
        Select select = new Select();
        select.setColumns(columns);
        select.setSource(source);
        return select;
    }

    private static boolean compareTables(Table table, Table expectedTable) {
        boolean names = table.getName().equals(expectedTable.getName());
        boolean aliases = (table.getAlias() == null && expectedTable.getAlias() == null) ||
                table.getAlias().equals(expectedTable.getAlias());
        return names && aliases;
    }

    public static boolean compareSources(Source source, Source expectedSource) {
        if (source instanceof Table && expectedSource instanceof Table) {
            return compareTables((Table) source, (Table) expectedSource);
        } else if (source instanceof Select && expectedSource instanceof Select) {
            return compareSelects((Select) source, (Select) expectedSource);
        } else if (source instanceof Join && expectedSource instanceof Join) {
            return compareJoints((Join) source, (Join) expectedSource);
        }
        return false;
    }

    public static boolean compareJoints(Join join, Join expectedJoin) {
        boolean firsts = compareSources(join.getFirst(), expectedJoin.getFirst());
        boolean seconds = compareSources(join.getSecond(), expectedJoin.getSecond());
        boolean types = join.getJoinType().equals(expectedJoin.getJoinType());
        boolean comparisons = compareComparisons(join.getComparison(), expectedJoin.getComparison());
        return firsts && seconds && types && comparisons;
    }

    public static boolean compareSelects(Select select, Select expectedSelect) {
        boolean columns = compareColumns(select.getColumns(), expectedSelect.getColumns());
        boolean sources = compareSources(select.getSource(), expectedSelect.getSource());
        boolean wheres = (select.getWhere() == null && expectedSelect.getWhere() == null)
                || compareConditions(select.getWhere(), expectedSelect.getWhere());
        boolean groups = (select.getGroupBy() == null && expectedSelect.getGroupBy() == null)
                || compareColumns(select.getGroupBy(), expectedSelect.getGroupBy());
        boolean havings = (select.getHaving() == null && expectedSelect.getHaving() == null)
                || compareConditions(select.getHaving(), expectedSelect.getHaving());
        boolean orders = (select.getOrderBy() == null && expectedSelect.getOrderBy() == null)
                || compareOrders(select.getOrderBy(), expectedSelect.getOrderBy());
        boolean limits = select.getLimit() == expectedSelect.getLimit();
        boolean offsets = select.getOffset() == expectedSelect.getOffset();

        return columns && sources && wheres && groups && havings && orders && limits && offsets;
    }

    public static boolean compareConditions(Condition condition, Condition expectedCondition) {
        if (condition instanceof Comparison && expectedCondition instanceof Comparison) {
            return compareComparisons((Comparison) condition, (Comparison) expectedCondition);
        } else if (condition instanceof Connection && expectedCondition instanceof Connection) {
            return compareConnections((Connection) condition, (Connection) expectedCondition);
        }
        return false;
    }

    public static boolean compareConnections(Connection connection, Connection expectedConnection) {
        boolean types = connection.getConnectionType().equals(expectedConnection.getConnectionType());
        boolean lefts = compareConditions(connection.getLeft(), expectedConnection.getLeft());
        boolean right = compareConditions(connection.getRight(), expectedConnection.getRight());

        return types && lefts && right;
    }

    public static boolean compareComparisons(Comparison comparison, Comparison expectedComparison) {
        if (comparison == null && expectedComparison == null) return true;
        boolean lefts = compareColumns(comparison.getLeft(), expectedComparison.getLeft());
        boolean rights = false;
        if (comparison.getRight() instanceof Column && expectedComparison.getRight() instanceof Column) {
            rights = compareColumns((Column) comparison.getRight(), (Column) expectedComparison.getRight());
        } else if (comparison.getRight() instanceof Source && expectedComparison.getRight() instanceof Source) {
            rights = compareSources((Source) comparison.getRight(), (Source) expectedComparison.getRight());
        } else if (comparison.getRight() instanceof String && expectedComparison.getRight() instanceof String) {
            rights = comparison.getRight().equals(expectedComparison.getRight());
        }
        boolean types = comparison.getType().equals(expectedComparison.getType());
        return lefts && rights && types;
    }

    public static boolean compareOrders(Order order, Order expectedOrder) {
        boolean types = order.getType().equals(expectedOrder.getType());
        boolean columns = compareColumns(order.getColumn(), expectedOrder.getColumn());
        return types && columns;
    }

    public static boolean compareOrders(List<Order> orders, List<Order> expectedOrders) {
        if (orders.size() == expectedOrders.size()) {
            for (int i = 0; i < orders.size(); i++) {
                if (!compareOrders(orders.get(i), expectedOrders.get(i)))
                    return false;
            }
        } else return false;
        return true;
    }

    public static boolean compareColumns(Column column, Column expectedColumn) {
        boolean names = column.getColumnName().equals(expectedColumn.getColumnName());
        boolean aliases = (column.getAlias() == null && expectedColumn.getAlias() == null) || (column.getAlias().equals(expectedColumn.getAlias()));
        boolean tables = (column.getTable() == null && expectedColumn.getTable() == null) || (column.getTable().equals(expectedColumn.getTable()));
        return names && aliases && tables;
    }

    public static boolean compareColumns(List<Column> columns, List<Column> expectedColumns) {
        if (columns.size() == expectedColumns.size()) {
            for (int i = 0; i < columns.size(); i++) {
                if (!compareColumns(columns.get(i), expectedColumns.get(i)))
                    return false;
            }
        } else return false;
        return true;
    }

}
