package sm.sql.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sm.sql.parser.entity.Comparison;
import sm.sql.parser.entity.Join;
import sm.sql.parser.entity.Source;
import sm.sql.parser.entity.Table;
import sm.sql.parser.parser.ColumnParser;
import sm.sql.parser.parser.ComparisonParser;
import sm.sql.parser.parser.JoinParser;
import sm.sql.parser.parser.TableParser;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

import static sm.sql.parser.util.Comparator.*;

public class JoinParserTest {

    public static Stream<Arguments> source() {
        Table table1 = tableGenerator("table1", "alias1");
        Table table2 = tableGenerator("table2", "alias2");
        Comparison comparison = comparisonGenerator(columnGenerator("a", null, "table1"),
                columnGenerator("b", null, "table2"),
                Comparison.ConnectionType.EQUAL);
        return Stream.of(
                Arguments.of("table1 as alias1 join table2 as alias2 on table1.a = table2.b",
                        joinGenerator(table1,
                                table2,
                                Join.JoinType.INNER_JOIN,
                                comparison)),
                Arguments.of("table1 as alias1 left join table2 as alias2 on table1.a = table2.b",
                        joinGenerator(table1,
                                table2,
                                Join.JoinType.LEFT_JOIN,
                                comparison)),
                Arguments.of("table1 as alias1 right join table2 as alias2 on table1.a = table2.b",
                        joinGenerator(table1,
                                table2,
                                Join.JoinType.RIGHT_JOIN,
                                comparison)),
                Arguments.of("table1 as alias1 full join table2 as alias2 on table1.a = table2.b",
                        joinGenerator(table1,
                                table2,
                                Join.JoinType.FULL_JOIN,
                                comparison)),
                Arguments.of("table1, table2, table3 ",
                        joinGenerator(joinGenerator(tableGenerator("table1"),
                                        tableGenerator("table2"),
                                        Join.JoinType.INNER_JOIN,
                                        null),
                                tableGenerator("table3"),
                                Join.JoinType.INNER_JOIN,
                                null)),
                Arguments.of("table1 left join table2 on table1.a = table2.b " +
                                "join table3 on table1.a = table2.b " +
                                "right join table4 on table1.a = table2.b " +
                                "full join table5 on table1.a = table2.b",
                        joinGenerator(joinGenerator(joinGenerator(joinGenerator(tableGenerator("table1", null),
                                                        tableGenerator("table2", null),
                                                        Join.JoinType.LEFT_JOIN,
                                                        comparison),
                                                tableGenerator("table3", null),
                                                Join.JoinType.INNER_JOIN,
                                                comparison),
                                        tableGenerator("table4", null),
                                        Join.JoinType.RIGHT_JOIN,
                                        comparison),
                                tableGenerator("table5", null),
                                Join.JoinType.FULL_JOIN,
                                comparison))
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    public void test(String s, Source expectedSource) {
        TableParser tableParser = new TableParser(new HashMap<>());
        Optional<? extends Source> optional = new JoinParser(tableParser, new ComparisonParser(new ColumnParser())).parse(s);

        Assertions.assertTrue(optional.isPresent());
        Source source = optional.get();
        Assertions.assertTrue(compareSources(source, expectedSource));
    }

}
