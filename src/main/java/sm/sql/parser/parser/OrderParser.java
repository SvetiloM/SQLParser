package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Order;
import sm.sql.parser.entity.part.OrderPartType;
import sm.sql.parser.entity.part.Part;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderParser implements Parser {

    private final PartParser<OrderPartType> partParser = new PartParser<>(OrderPartType.values());

    private final ColumnParser columnParser;

    @Override
    public List<Order> parse(String part) {
        //todo send to enum
        String[] split = part.split(",");

        List<Order> orders = new ArrayList<>();
        int i = 0;
        while (i < split.length) {
            val order = new Order();
            List<Part<OrderPartType>> parts = partParser.getParts(split[i]);
            if (parts.size() == 0) {
                order.setColumn(columnParser.parse(split[i]).get(0));
            } else {
                for (Part<OrderPartType> columnPart : parts) {
                    parse(columnPart, order);
                }
            }
            orders.add(order);
            i++;
        }

        return orders;
    }

    private void parse(Part<OrderPartType> part, Order order) {
        switch (part.getType()) {
            case ASC -> {
                order.setColumn(columnParser.parse(part.getPart()).get(0));
                order.setType(Order.OrderType.ASC);
            }
            case DESC -> {
                order.setColumn(columnParser.parse(part.getPart()).get(0));
                order.setType(Order.OrderType.DESC);
            }
        }
    }
}
