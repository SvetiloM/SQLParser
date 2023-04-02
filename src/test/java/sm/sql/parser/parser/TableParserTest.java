package sm.sql.parser.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sm.sql.parser.entity.Source;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

import static sm.sql.parser.parser.util.Comparator.*;

public class TableParserTest {

    //todo add source to cache
    public static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of("1 as 2", tableGenerator("1", "2")),
                Arguments.of("1", tableGenerator("1", null))
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    public void test(String s, Source expectedSource) {
        Optional<Source> optional = new TableParser(new HashMap<>()).parse(s);

        Assertions.assertTrue(optional.isPresent());
        Source source = optional.get();
        Assertions.assertTrue(compareSources(source, expectedSource));
    }
}
