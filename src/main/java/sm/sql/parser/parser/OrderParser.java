package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Column;
import sm.sql.parser.entity.Order;
import sm.sql.parser.entity.part.OrderPartType;
import sm.sql.parser.entity.part.Part;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderParser implements Parser {

    private final PartParser<OrderPartType> partParser = new PartParser<>(OrderPartType.values());

    private final ColumnParser columnParser;

    @Override
    public Optional<List<Order>> parse(String part) {
        //todo send to enum
        String[] split = part.split(",");

        List<Order> orders = new ArrayList<>();
        int i = 0;
        while (i < split.length) {
            val order = new Order();
            List<Part<OrderPartType>> parts = partParser.getParts(split[i]);
            if (parts.size() == 0) {
                columnParser.parse(split[i]).ifPresent(columns -> order.setColumn(columns.get(0)));
            } else {
                for (Part<OrderPartType> columnPart : parts) {
                    parse(columnPart, order);
                }
            }
            orders.add(order);
            i++;
        }

        return Optional.of(orders);
    }

    private void parse(Part<OrderPartType> part, Order order) {
        switch (part.getType()) {
            case ASC -> {
                columnParser.parse(part.getPart()).ifPresent(columns -> order.setColumn(columns.get(0)));
                order.setType(Order.OrderType.ASC);
            }
            case DESC -> {
                columnParser.parse(part.getPart()).ifPresent(columns -> order.setColumn(columns.get(0)));
                order.setType(Order.OrderType.DESC);
            }
        }
    }
}
