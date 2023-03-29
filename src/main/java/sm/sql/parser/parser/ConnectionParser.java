package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Connection;
import sm.sql.parser.entity.part.ConnectionPartType;
import sm.sql.parser.entity.part.Part;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConnectionParser implements Parser {
    private final PartParser<ConnectionPartType> partParser = new PartParser<>(ConnectionPartType.values());
    private final ComparisonParser conditionParser;

    @Override
    public Connection parse(Part part) {
        val where = new Connection();
        List<Part<ConnectionPartType>> parts = partParser.getParts(part.getPart());
        if (parts.size() == 0) return null;
        for (Part<ConnectionPartType> columnPart : parts) {
            parse(columnPart, where);
        }

        return where;
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

    private Object goDeeper(Part<ConnectionPartType> part) {
        Connection innerCondition = parse(part);
        //todo optional
        if (innerCondition == null) {
            return conditionParser.parse(part);
        } else {
            return innerCondition;
        }
    }
}
