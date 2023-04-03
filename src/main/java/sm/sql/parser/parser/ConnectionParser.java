package sm.sql.parser.parser;

import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Connection;
import sm.sql.parser.entity.part.ConnectionPartType;
import sm.sql.parser.entity.part.Part;

import java.util.List;
import java.util.Optional;

@Component
public class ConnectionParser implements Parser<Connection> {
    private final CommonComparisonParser commonComparisonParser;
    private final PartParser<ConnectionPartType> partParser = new PartParser<>(ConnectionPartType.values());


    public ConnectionParser(CommonComparisonParser commonComparisonParser) {
        this.commonComparisonParser = commonComparisonParser;
    }

    @Override
    public Optional<Connection> parse(String part) {
        List<Part<ConnectionPartType>> parts = partParser.getParts(part);
        if (parts.size() == 0) {
            return Optional.empty();
        }
        Connection innerConnection = new Connection();
        Connection rootConnection = innerConnection;
        for (Part<ConnectionPartType> connectionPart : parts) {
            innerConnection = parse(connectionPart, innerConnection);
        }
        return Optional.of(rootConnection);
    }

    public Connection parse(Part<ConnectionPartType> part, Connection connection) {
        switch (part.getType()) {
            case OR_LEFT -> {
                if (connection.getLeft() == null) {
                    connection.setLeft(commonComparisonParser.parse(part.getPart()).orElse(null));
                    connection.setConnectionType(Connection.ConnectionType.OR);
                }
                return connection;
            }
            case OR_RIGHT -> {
                return fillOr(part, connection);
            }
            case AND_LEFT -> {
                if (connection.getLeft() == null) {
                    connection.setLeft(commonComparisonParser.parse(part.getPart()).orElse(null));
                    connection.setConnectionType(Connection.ConnectionType.AND);
                }
                return connection;
            }
            case AND_RIGHT -> {
                return fillAnd(part, connection);
            }
        }
        return connection;
    }

    private Connection fillOr(Part<ConnectionPartType> part, Connection connection) {
        if (connection.getRight() != null) {
            Connection inner = new Connection();
            inner.setLeft(connection.getLeft());
            inner.setRight(connection.getRight());
            inner.setConnectionType(connection.getConnectionType());

            connection.setLeft(inner);
            commonComparisonParser.parse(part.getPart()).ifPresent(connection::setRight);
            connection.setConnectionType(Connection.ConnectionType.OR);

            return connection;
        } else {
            commonComparisonParser.parse(part.getPart()).ifPresent(connection::setLeft);
        }
        return connection;
    }

    private Connection fillAnd(Part<ConnectionPartType> part, Connection connection) {
        if (connection.getRight() != null) {
            Connection inner = new Connection();
            inner.setLeft(connection.getRight());
            commonComparisonParser.parse(part.getPart()).ifPresent(inner::setRight);
            inner.setConnectionType(Connection.ConnectionType.AND);
            connection.setRight(inner);

            return connection;
        } else {
            commonComparisonParser.parse(part.getPart()).ifPresent(connection::setRight);
        }
        return connection;
    }

}
