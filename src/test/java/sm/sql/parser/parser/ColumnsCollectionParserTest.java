package sm.sql.parser.parser;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sm.sql.parser.entity.Column;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static sm.sql.parser.parser.util.Comparator.columnGenerator;
import static sm.sql.parser.parser.util.Comparator.compareColumns;

public class ColumnsCollectionParserTest {

    public static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of("name1, name2, name3",
                        List.of(columnGenerator("name1"),
                                columnGenerator("name2"),
                                columnGenerator("name3"))),
                Arguments.of("tab1.name1 as alias1, tab2.name2 as alias2, tab3.name3 as alias3",
                        List.of(columnGenerator("name1", "alias1", "tab1"),
                                columnGenerator("name2", "alias2", "tab2"),
                                columnGenerator("name3", "alias3", "tab3"))),
                Arguments.of("tab1.name1, name2, name3 as alias3",
                        List.of(columnGenerator("name1", null, "tab1"),
                        columnGenerator("name2"),
                columnGenerator("name3", "alias3", null)))
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    public void test(String s, List<Column> expectedColumn) {
        Optional<List<Column>> optional = new ColumnsCollectionParser().parse(s);

        Assertions.assertTrue(optional.isPresent());
        List<Column> columns = optional.get();
        Assertions.assertTrue(compareColumns(columns, expectedColumn));
    }
}
