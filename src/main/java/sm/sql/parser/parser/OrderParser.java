package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Order;
import sm.sql.parser.entity.part.OrderPartType;
import sm.sql.parser.entity.part.Part;

@Component
public class OrderParser extends SimpleParser<OrderPartType, Order> {

    private final ColumnParser columnParser;

    public OrderParser(ColumnParser columnParser) {
        super(new PartParser<>(OrderPartType.values()),
                Order::new,
                (column, order) -> columnParser.parse(column).ifPresent(order::setColumn),
                true);
        this.columnParser = columnParser;
    }

    @Override
    public void parse(Part<OrderPartType> part, Order order) {
        switch (part.getType()) {
            case ASC -> {
                columnParser.parse(part.getPart()).ifPresent(order::setColumn);
                order.setType(Order.OrderType.ASC);
            }
            case DESC -> {
                columnParser.parse(part.getPart()).ifPresent(order::setColumn);
                order.setType(Order.OrderType.DESC);
            }
        }
    }
}
