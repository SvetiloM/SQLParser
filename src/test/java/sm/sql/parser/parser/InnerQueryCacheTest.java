package sm.sql.parser.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sm.sql.parser.entity.Column;
import sm.sql.parser.entity.Comparison;
import sm.sql.parser.entity.Select;
import sm.sql.parser.entity.Table;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static sm.sql.parser.parser.util.Comparator.*;

public class InnerQueryCacheTest {

    public static Stream<Arguments> source() {

        Select fSelection = selectGenerator(Collections.singletonList(columnGenerator("f")), tableGenerator("ff"));

        Column dColumn = columnGenerator("d");
        Table dTable = tableGenerator("dd");

        Select dSelection = selectGenerator(Collections.singletonList(dColumn), dTable);
        Select innerSelection = selectGenerator(Collections.singletonList(dColumn), dTable, comparisonGenerator(dColumn, fSelection, Comparison.ConnectionType.SOME));

        return Stream.of(
                Arguments.of("select a from b where a in (select d from dd)",
                        Map.of("(select d from dd)", dSelection)),
                Arguments.of("select a from b where a in (select d from dd where d some (select f from ff))",
                        Map.of("(select f from ff)", fSelection,
                                "(select d from dd where d some (select f from ff))", innerSelection)),
                Arguments.of("select a from b where a in (select d from dd) and a any (select f from ff)",
                        Map.of("(select d from dd)", dSelection,
                                "(select f from ff)", fSelection))
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    public void test(String s, Map<String, Select> expectedCache) {
        HashMap<String, Select> cache = new HashMap<>();
        new InnerQueryCache(getSelectQueryParser(cache), cache).fillCache(s);

        Assertions.assertTrue(compareMaps(cache, expectedCache));
    }

    private boolean compareMaps(Map<String, Select> cache, Map<String, Select> expectedCache) {
        if (cache.size() == expectedCache.size()) {
            for (String s : cache.keySet()) {
                if (!compareSelects(cache.get(s), expectedCache.get(s))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private SelectQueryParser getSelectQueryParser(Map<String, Select> cache) {
        ColumnsCollectionParser columnsCollectionParser = new ColumnsCollectionParser();
        ColumnParser columnParser = new ColumnParser();
        ComparisonParser comparisonParser = new ComparisonParser(columnParser);
        TableParser tableParser = new TableParser(cache);
        JoinParser joinParser = new JoinParser(tableParser, comparisonParser);
        SampleComparisonParser sampleComparisonParser = new SampleComparisonParser(columnParser, cache);
        CommonComparisonParser commonComparisonParser = new CommonComparisonParser(comparisonParser, sampleComparisonParser);
        ConnectionParser connectionParser = new ConnectionParser(commonComparisonParser);
        ConditionParser conditionParser = new ConditionParser(commonComparisonParser, connectionParser);
        OrderCollectionParser orderCollectionParser = new OrderCollectionParser();
        return new SelectQueryParser(columnsCollectionParser, joinParser, conditionParser, orderCollectionParser);
    }
}
