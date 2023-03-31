package sm.sql.parser.parser;

import sm.sql.parser.entity.Order;
import sm.sql.parser.entity.part.CollectionPartType;
import sm.sql.parser.entity.part.OrderPartType;

public class OrderCollectionParser extends CollectionParser<CollectionPartType, OrderPartType, Order> {

    public OrderCollectionParser() {
        super(new PartParser<>(CollectionPartType.COMMA), new OrderParser(new ColumnParser()));
    }
}
