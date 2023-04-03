package sm.sql.parser.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sm.sql.parser.entity.Column;
import sm.sql.parser.entity.Comparison;
import sm.sql.parser.entity.Condition;
import sm.sql.parser.entity.Connection;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

import static sm.sql.parser.parser.util.Comparator.*;

public class ConnectionParserTest {

    public static Stream<Arguments> source() {
        Column aColumn = columnGenerator("a");
        Column bColumn = columnGenerator("b");
        Column cColumn = columnGenerator("c");
        Column fColumn = columnGenerator("f");
        Condition aMoreB = comparisonGenerator(aColumn, bColumn, Comparison.ConnectionType.MORE);
        Condition bMoreC = comparisonGenerator(bColumn, cColumn, Comparison.ConnectionType.MORE);
        Condition fMoreC = comparisonGenerator(fColumn, cColumn, Comparison.ConnectionType.MORE);

        return Stream.of(
                Arguments.of("a>b and b>c", connectionGenerator(aMoreB, bMoreC, Connection.ConnectionType.AND)),
                Arguments.of("a>b or b>c", connectionGenerator(aMoreB, bMoreC, Connection.ConnectionType.OR)),
                Arguments.of("a>b and b>c and f>c", connectionGenerator(aMoreB, connectionGenerator(bMoreC, fMoreC, Connection.ConnectionType.AND), Connection.ConnectionType.AND)),
                Arguments.of("a>b and b>c or f>c", connectionGenerator(
                        connectionGenerator(aMoreB, bMoreC, Connection.ConnectionType.AND),
                        fMoreC,
                        Connection.ConnectionType.OR))
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    public void test(String s, Connection expectedConnection) {
        Optional<Connection> optional = new ConnectionParser(getCommonComparisonParser()).parse(s);

        Assertions.assertTrue(optional.isPresent());
        Connection connection = optional.get();
        Assertions.assertTrue(compareConnections(connection, expectedConnection));
    }

    private static CommonComparisonParser getCommonComparisonParser() {
        ColumnParser columnParser = new ColumnParser();
        ComparisonParser comparisonParser = new ComparisonParser(columnParser);
        SampleComparisonParser sampleComparisonParser = new SampleComparisonParser(columnParser, new HashMap<>());
        CommonComparisonParser commonComparisonParser = new CommonComparisonParser(comparisonParser, sampleComparisonParser);
        return commonComparisonParser;
    }
}
