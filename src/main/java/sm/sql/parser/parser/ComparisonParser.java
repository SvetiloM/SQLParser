package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Comparison;
import sm.sql.parser.entity.Comparison.ConnectionType;
import sm.sql.parser.entity.part.ComparisonPartType;
import sm.sql.parser.entity.part.Part;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ComparisonParser implements Parser {

    private final PartParser<ComparisonPartType> partParser = new PartParser<>(ComparisonPartType.values());
    private final PartParser<ComparisonPartType> compositePartParser = new PartParser<>(ComparisonPartType.getCompositeValues());
    private final ColumnParser columnParser;

    @Override
    public Optional<Comparison> parse(String part) {
        List<Part<ComparisonPartType>> parts = compositePartParser.getParts(part);
        if (parts.size() == 0) {
            parts = partParser.getParts(part);
        }
        if (parts.size() == 0) return null;
        val connection = new Comparison();
        for (Part<ComparisonPartType> connectionPart : parts) {
            parse(connectionPart, connection);
        }
        return Optional.of(connection);

    }

    private void parse(Part<ComparisonPartType> part, Comparison comparison) {
        switch (part.getType()) {
            case EQUAL_LEFT -> {
                comparison.setType(ConnectionType.EQUAL);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setLeft(c.get(0)));
            }
            case EQUAL_RIGHT -> {
                comparison.setType(ConnectionType.EQUAL);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setRight(c.get(0)));
            }
            case MORE_LEFT -> {
                comparison.setType(ConnectionType.MORE);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setLeft(c.get(0)));
            }
            case MORE_RIGHT -> {
                comparison.setType(ConnectionType.MORE);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setRight(c.get(0)));
            }
            case LESS_LEFT -> {
                comparison.setType(ConnectionType.LESS);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setLeft(c.get(0)));
            }
            case LESS_RIGHT -> {
                comparison.setType(ConnectionType.LESS);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setRight(c.get(0)));
            }
            case MORE_OR_EQUAL_LEFT -> {
                comparison.setType(ConnectionType.MORE_OR_EQUAL);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setLeft(c.get(0)));
            }
            case MORE_OR_EQUAL_RIGHT -> {
                comparison.setType(ConnectionType.MORE_OR_EQUAL);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setRight(c.get(0)));
            }
            case LESS_OR_EQUAL_LEFT -> {
                comparison.setType(ConnectionType.LESS_OR_EQUAL);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setLeft(c.get(0)));
            }
            case LESS_OR_EQUAL_RIGHT -> {
                comparison.setType(ConnectionType.LESS_OR_EQUAL);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setRight(c.get(0)));
            }
            case NOT_EQUAL_LEFT -> {
                comparison.setType(ConnectionType.NOT_EQUAL);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setLeft(c.get(0)));
            }
            case NOT_EQUAL_RIGHT -> {
                comparison.setType(ConnectionType.NOT_EQUAL);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setRight(c.get(0)));
            }
            case NOT_EQUAL_EXCL_LEFT -> {
                comparison.setType(ConnectionType.NOT_EQUAL_EXCL);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setLeft(c.get(0)));
            }
            case NOT_EQUAL_EXCL_RIGHT -> {
                comparison.setType(ConnectionType.NOT_EQUAL_EXCL);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setRight(c.get(0)));
            }
            case NOT_LESS_LEFT -> {
                comparison.setType(ConnectionType.NOT_LESS);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setLeft(c.get(0)));
            }
            case NOT_LESS_RIGHT -> {
                comparison.setType(ConnectionType.NOT_LESS);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setRight(c.get(0)));
            }
            case NOT_MORE_LEFT -> {
                comparison.setType(ConnectionType.NOT_MORE);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setLeft(c.get(0)));
            }
            case NOT_MORE_RIGHT -> {
                comparison.setType(ConnectionType.NOT_MORE);
                columnParser.parse(part.getPart()).ifPresent(c -> comparison.setRight(c.get(0)));
            }
        }
    }

}
