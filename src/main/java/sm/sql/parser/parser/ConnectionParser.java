package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Comparison;
import sm.sql.parser.entity.Condition;
import sm.sql.parser.entity.Connection;
import sm.sql.parser.entity.part.ConnectionPartType;
import sm.sql.parser.entity.part.Part;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConnectionParser implements Parser {
    private final PartParser<ConnectionPartType> partParser = new PartParser<>(ConnectionPartType.values());
    private final ComparisonParser conditionParser;

    @Override
    public Optional<Connection> parse(String part) {
        val where = new Connection();
        List<Part<ConnectionPartType>> parts = partParser.getParts(part);
        if (parts.size() == 0) return null;
        for (Part<ConnectionPartType> columnPart : parts) {
            parse(columnPart, where);
        }

        return Optional.of(where);
    }

    private void parse(Part<ConnectionPartType> part, Connection connection) {
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
            return  conditionParser.parse(part.getPart()).orElse(null);
        } else {
            return innerCondition.get();
        }
    }
}
