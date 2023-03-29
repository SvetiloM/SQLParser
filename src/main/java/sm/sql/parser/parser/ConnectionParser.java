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
    private final ColumnParser columnParser;

    @Override
    public Connection parse(Part part) {
        List<Part<ConnectionPartType>> parts = partParser.getParts(part.getPart());
        val connection = new Connection();
        for (Part<ConnectionPartType> connectionPart : parts) {
            parse(connectionPart, connection);
        }
        return connection;

    }

    private void parse(Part<ConnectionPartType> part, Connection connection) {
        switch (part.getType()) {
            case EQUAL_LEFT -> {
                //todo columns? change
                connection.setType(Connection.ConnectionType.EQUAL);
                connection.setLeft(columnParser.parse(part));
            }
            case EQUAL_RIGHT -> {
                connection.setType(Connection.ConnectionType.EQUAL);
                connection.setRight(columnParser.parse(part));
            }
        }
    }
}
