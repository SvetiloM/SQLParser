package sm.sql.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sm.sql.parser.entity.Order;
import sm.sql.parser.parser.ColumnParser;
import sm.sql.parser.parser.OrderParser;

import java.util.Optional;
import java.util.stream.Stream;

import static sm.sql.parser.util.Comparator.*;

public class OrderParserTest {

    public static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of("1.2", orderGenerator(columnGenerator("2", null, "1"), Order.OrderType.ASC)),
                Arguments.of("1.2 asc", orderGenerator(columnGenerator("2", null, "1"), Order.OrderType.ASC)),
                Arguments.of("1.2 desc", orderGenerator(columnGenerator("2", null, "1"), Order.OrderType.DESC))
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    public void test(String s, Order expectedOrder) {
        Optional<Order> optional = new OrderParser(new ColumnParser()).parse(s);

        Assertions.assertTrue(optional.isPresent());
        Order order = optional.get();
        Assertions.assertTrue(compareOrders(order, expectedOrder));
    }
}
