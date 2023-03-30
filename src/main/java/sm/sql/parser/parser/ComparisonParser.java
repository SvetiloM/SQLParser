package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Comparison;
import sm.sql.parser.entity.Comparison.ConnectionType;
import sm.sql.parser.entity.part.ComparisonPartType;
import sm.sql.parser.entity.part.Part;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ComparisonParser implements Parser {

    private final PartParser<ComparisonPartType> partParser = new PartParser<>(ComparisonPartType.values());
    private final PartParser<ComparisonPartType> compositePartParser = new PartParser<>(ComparisonPartType.getCompositeValues());
    private final ColumnParser columnParser;

    @Override
    public Comparison parse(String part) {
        List<Part<ComparisonPartType>> parts = compositePartParser.getParts(part);
        if (parts.size() == 0) {
            parts = partParser.getParts(part);
        }
        if (parts.size() == 0) return null;
        val connection = new Comparison();
        for (Part<ComparisonPartType> connectionPart : parts) {
            parse(connectionPart, connection);
        }
        return connection;

    }

    private void parse(Part<ComparisonPartType> part, Comparison comparison) {
        switch (part.getType()) {
            case EQUAL_LEFT -> {
                comparison.setType(ConnectionType.EQUAL);
                comparison.setLeft(columnParser.parse(part.getPart()).get(0));
            }
            case EQUAL_RIGHT -> {
                comparison.setType(ConnectionType.EQUAL);
                comparison.setRight(columnParser.parse(part.getPart()).get(0));
            }
            case MORE_LEFT -> {
                comparison.setType(ConnectionType.MORE);
                comparison.setLeft(columnParser.parse(part.getPart()).get(0));
            }
            case MORE_RIGHT -> {
                comparison.setType(ConnectionType.MORE);
                comparison.setRight(columnParser.parse(part.getPart()).get(0));
            }
            case LESS_LEFT -> {
                comparison.setType(ConnectionType.LESS);
                comparison.setLeft(columnParser.parse(part.getPart()).get(0));
            }
            case LESS_RIGHT -> {
                comparison.setType(ConnectionType.LESS);
                comparison.setRight(columnParser.parse(part.getPart()).get(0));
            }
            case MORE_OR_EQUAL_LEFT -> {
                comparison.setType(ConnectionType.MORE_OR_EQUAL);
                comparison.setLeft(columnParser.parse(part.getPart()).get(0));
            }
            case MORE_OR_EQUAL_RIGHT -> {
                comparison.setType(ConnectionType.MORE_OR_EQUAL);
                comparison.setRight(columnParser.parse(part.getPart()).get(0));
            }
            case LESS_OR_EQUAL_LEFT -> {
                comparison.setType(ConnectionType.LESS_OR_EQUAL);
                comparison.setLeft(columnParser.parse(part.getPart()).get(0));
            }
            case LESS_OR_EQUAL_RIGHT -> {
                comparison.setType(ConnectionType.LESS_OR_EQUAL);
                comparison.setRight(columnParser.parse(part.getPart()).get(0));
            }
            case NOT_EQUAL_LEFT -> {
                comparison.setType(ConnectionType.NOT_EQUAL);
                comparison.setLeft(columnParser.parse(part.getPart()).get(0));
            }
            case NOT_EQUAL_RIGHT -> {
                comparison.setType(ConnectionType.NOT_EQUAL);
                comparison.setRight(columnParser.parse(part.getPart()).get(0));
            }
            case NOT_EQUAL_EXCL_LEFT -> {
                comparison.setType(ConnectionType.NOT_EQUAL_EXCL);
                comparison.setLeft(columnParser.parse(part.getPart()).get(0));
            }
            case NOT_EQUAL_EXCL_RIGHT -> {
                comparison.setType(ConnectionType.NOT_EQUAL_EXCL);
                comparison.setRight(columnParser.parse(part.getPart()).get(0));
            }
            case NOT_LESS_LEFT -> {
                comparison.setType(ConnectionType.NOT_LESS);
                comparison.setLeft(columnParser.parse(part.getPart()).get(0));
            }
            case NOT_LESS_RIGHT -> {
                comparison.setType(ConnectionType.NOT_LESS);
                comparison.setRight(columnParser.parse(part.getPart()).get(0));
            }
            case NOT_MORE_LEFT -> {
                comparison.setType(ConnectionType.NOT_MORE);
                comparison.setLeft(columnParser.parse(part.getPart()).get(0));
            }
            case NOT_MORE_RIGHT -> {
                comparison.setType(ConnectionType.NOT_MORE);
                comparison.setRight(columnParser.parse(part.getPart()).get(0));
            }
        }
    }

}
