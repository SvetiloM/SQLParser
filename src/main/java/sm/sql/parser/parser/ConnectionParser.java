package sm.sql.parser.parser;

import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Condition;
import sm.sql.parser.entity.Connection;
import sm.sql.parser.entity.part.ConnectionPartType;
import sm.sql.parser.entity.part.Part;

import java.util.Optional;

@Component
public class ConnectionParser extends SimpleParser<ConnectionPartType, Connection> {
    private final CommonComparisonParser conditionParser;

    public ConnectionParser(CommonComparisonParser conditionParser) {
        super(new PartParser<>(ConnectionPartType.values()), Connection::new);
        this.conditionParser = conditionParser;
    }

    @Override
    public void parse(Part<ConnectionPartType> part, Connection connection) {
        switch (part.getType()) {
            case OR_LEFT -> {
                connection.setConnectionType(Connection.ConnectionType.OR);
                connection.setLeft(goDeeper(part));
            }
            case OR_RIGHT -> {
                connection.setConnectionType(Connection.ConnectionType.OR);
                connection.setRight(goDeeper(part));
            }
            case AND_LEFT -> {
                connection.setConnectionType(Connection.ConnectionType.AND);
                connection.setLeft(goDeeper(part));
            }
            case AND_RIGHT -> {
                connection.setConnectionType(Connection.ConnectionType.AND);
                connection.setRight(goDeeper(part));
            }
        }
    }

    private Condition goDeeper(Part<ConnectionPartType> part) {
        Optional<Connection> innerCondition = parse(part.getPart());
        //todo optional
        if (innerCondition.isEmpty()) {
            return conditionParser.parse(part.getPart()).orElse(null);
        } else {
            return innerCondition.get();
        }
    }
}
