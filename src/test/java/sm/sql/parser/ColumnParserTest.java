package sm.sql.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sm.sql.parser.entity.Column;
import sm.sql.parser.parser.ColumnParser;

import java.util.Optional;
import java.util.stream.Stream;

import static sm.sql.parser.util.Comparator.columnGenerator;
import static sm.sql.parser.util.Comparator.compareColumns;

public class ColumnParserTest {

    public static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of("3.1 as 2", columnGenerator("1", "2", "3")),
                Arguments.of("3.1", columnGenerator("1", null, "3")),
                Arguments.of("1 as 2", columnGenerator("1", "2", null)),
                Arguments.of("1", columnGenerator("1"))
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    public void test(String s, Column expectedColumn) {
        Optional<Column> optional = new ColumnParser().parse(s);

        Assertions.assertTrue(optional.isPresent());
        Column column = optional.get();
        Assertions.assertTrue(compareColumns(expectedColumn, column));
    }
}
