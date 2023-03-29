package sm.sql.parser.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    private Column column;
    private OrderType type = OrderType.ASC;

    public enum OrderType {
        ASC,
        DESC
    }
}
