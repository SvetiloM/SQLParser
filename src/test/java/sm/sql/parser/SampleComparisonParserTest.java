package sm.sql.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sm.sql.parser.entity.Column;
import sm.sql.parser.entity.Comparison;
import sm.sql.parser.entity.Select;
import sm.sql.parser.entity.Table;
import sm.sql.parser.parser.ColumnParser;
import sm.sql.parser.parser.SampleComparisonParser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static sm.sql.parser.util.Comparator.*;

public class SampleComparisonParserTest {

    public static Stream<Arguments> source() {
        Column c = columnGenerator("c");

        Select select = new Select();
        select.setColumns(Collections.singletonList(c));
        Table table = new Table();
        table.setName("b");
        select.setSource(table);

        String key = "(select a from b)";
        HashMap<String, Select> cache = new HashMap<>();
        cache.put(key, select);
        return Stream.of(
                Arguments.of("c some (select a from b)", comparisonGenerator(c, select, Comparison.ConnectionType.SOME), cache),
                Arguments.of("c in (select a from b)", comparisonGenerator(c, select, Comparison.ConnectionType.IN), cache),
                Arguments.of("c exists (select a from b)", comparisonGenerator(c, select, Comparison.ConnectionType.EXISTS), cache),
                Arguments.of("c any (select a from b)", comparisonGenerator(c, select, Comparison.ConnectionType.ANY), cache),
                Arguments.of("c all (select a from b)", comparisonGenerator(c, select, Comparison.ConnectionType.ALL), cache)
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    public void test(String s, Comparison expectedComparison, Map<String, Select> cache) {
        Optional<Comparison> optional = new SampleComparisonParser(new ColumnParser(), cache).parse(s);

        Assertions.assertTrue(optional.isPresent());
        Comparison comparison = optional.get();
        Assertions.assertTrue(compareComparisons(comparison, expectedComparison));
    }
}
