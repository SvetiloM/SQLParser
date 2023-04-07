package sm.sql.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sm.sql.parser.parser.BracketParser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class BracketParserTest {


    public static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of("(1)", List.of("1")),
                Arguments.of("(1(2))", List.of("1(2)", "2")),
                Arguments.of("((2))", List.of("(2)", "2")),
                Arguments.of("(1(2)3(4)5)", List.of("1(2)3(4)5", "2", "4")),
                Arguments.of("(1(2)3(4(5))6)", List.of("1(2)3(4(5))6", "5", "2", "4(5)"))
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    public void test(String s, List<String> strings) {
        Optional<List<String>> optional = new BracketParser().parse(s);

        Assertions.assertTrue(optional.isPresent());
        List<String> resultStrings = optional.get();
        Assertions.assertEquals(resultStrings.size(), strings.size());
        Assertions.assertTrue(resultStrings.containsAll(strings));
    }
}
