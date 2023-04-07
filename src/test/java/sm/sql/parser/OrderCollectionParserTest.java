package sm.sql.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sm.sql.parser.entity.Order;
import sm.sql.parser.parser.OrderCollectionParser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static sm.sql.parser.util.Comparator.*;

public class OrderCollectionParserTest {

    public static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of("name1, name2, name3",
                        List.of(orderGenerator(columnGenerator("name1"), Order.OrderType.ASC),
                                orderGenerator(columnGenerator("name2"),  Order.OrderType.ASC),
                                orderGenerator(columnGenerator("name3"),  Order.OrderType.ASC))),
                Arguments.of("tab1.name1 as alias1 asc, tab2.name2 as alias2 desc, tab3.name3 as alias3",
                        List.of(orderGenerator(columnGenerator("name1", "alias1", "tab1"),  Order.OrderType.ASC),
                                orderGenerator(columnGenerator("name2", "alias2", "tab2"),  Order.OrderType.DESC),
                                orderGenerator(columnGenerator("name3", "alias3", "tab3"),  Order.OrderType.ASC))),
                Arguments.of("tab1.name1 desc, name2 desc, name3 as alias3 desc",
                        List.of(orderGenerator(columnGenerator("name1", null, "tab1"),  Order.OrderType.DESC),
                                orderGenerator(columnGenerator("name2"),  Order.OrderType.DESC),
                                orderGenerator(columnGenerator("name3", "alias3", null),  Order.OrderType.DESC)))
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    public void test(String s, List<Order> expectedOrders) {
        Optional<List<Order>> optional = new OrderCollectionParser().parse(s);

        Assertions.assertTrue(optional.isPresent());
        List<Order> orders = optional.get();
        Assertions.assertTrue(compareOrders(orders, expectedOrders));
    }
}
