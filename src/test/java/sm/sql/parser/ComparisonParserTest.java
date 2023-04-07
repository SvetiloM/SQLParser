package sm.sql.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sm.sql.parser.entity.Comparison;
import sm.sql.parser.parser.ColumnParser;
import sm.sql.parser.parser.ComparisonParser;

import java.util.Optional;
import java.util.stream.Stream;

import static sm.sql.parser.util.Comparator.*;

public class ComparisonParserTest {

    public static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of("1.2 > 2.3", comparisonGenerator(
                        columnGenerator("2", null, "1"),
                        columnGenerator("3", null, "2"),
                        Comparison.ConnectionType.MORE)),
                Arguments.of("1 > 2", comparisonGenerator(
                        columnGenerator("1"),
                        columnGenerator("2"),
                        Comparison.ConnectionType.MORE)),
                Arguments.of("1.2 > 2", comparisonGenerator(
                        columnGenerator("2", null, "1"),
                        columnGenerator("2"),
                        Comparison.ConnectionType.MORE)),
                Arguments.of("1 > 1.2", comparisonGenerator(
                        columnGenerator("1"),
                        columnGenerator("2", null, "1"),
                        Comparison.ConnectionType.MORE)),
                Arguments.of("1 < 2", comparisonGenerator(
                        columnGenerator("1"),
                        columnGenerator("2"),
                        Comparison.ConnectionType.LESS)),
                Arguments.of("1 >= 2", comparisonGenerator(
                        columnGenerator("1"),
                        columnGenerator("2"),
                        Comparison.ConnectionType.MORE_OR_EQUAL)),
                Arguments.of("1 <= 2", comparisonGenerator(
                        columnGenerator("1"),
                        columnGenerator("2"),
                        Comparison.ConnectionType.LESS_OR_EQUAL)),
                Arguments.of("1 <> 2", comparisonGenerator(
                        columnGenerator("1"),
                        columnGenerator("2"),
                        Comparison.ConnectionType.NOT_EQUAL)),
                Arguments.of("1 != 2", comparisonGenerator(
                        columnGenerator("1"),
                        columnGenerator("2"),
                        Comparison.ConnectionType.NOT_EQUAL_EXCL)),
                Arguments.of("1 !< 2", comparisonGenerator(
                        columnGenerator("1"),
                        columnGenerator("2"),
                        Comparison.ConnectionType.NOT_LESS)),
                Arguments.of("1 !> 2", comparisonGenerator(
                        columnGenerator("1"),
                        columnGenerator("2"),
                        Comparison.ConnectionType.NOT_MORE)),
                Arguments.of("1 like \'jjjj\'", comparisonGenerator(
                        columnGenerator("1"),
                        "\'jjjj\'",
                        Comparison.ConnectionType.LIKE))
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    public void test(String s, Comparison expectedComparison) {
        Optional<Comparison> optional = new ComparisonParser(new ColumnParser()).parse(s);

        Assertions.assertTrue(optional.isPresent());
        Comparison comparison = optional.get();
        Assertions.assertTrue(compareComparisons(comparison, expectedComparison));
    }
}
