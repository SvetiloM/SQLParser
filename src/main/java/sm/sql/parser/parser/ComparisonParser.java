package sm.sql.parser.parser;

import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Comparison;
import sm.sql.parser.entity.Comparison.ConnectionType;
import sm.sql.parser.entity.part.ComparisonPartType;
import sm.sql.parser.entity.part.Part;

@Component
public class ComparisonParser extends SimpleParser<ComparisonPartType, Comparison> {
    private final ColumnParser columnParser;

    public ComparisonParser(ColumnParser columnParser) {
        super(new PartParser<>(ComparisonPartType.values()), Comparison::new);
        this.columnParser = columnParser;
    }

    @Override
    public void parse(Part<ComparisonPartType> part, Comparison comparison) {
        switch (part.getType()) {
            case EQUAL_LEFT -> {
                comparison.setType(ConnectionType.EQUAL);
                columnParser.parse(part.getPart()).ifPresent(comparison::setLeft);
            }
            case EQUAL_RIGHT -> {
                comparison.setType(ConnectionType.EQUAL);
                columnParser.parse(part.getPart()).ifPresent(comparison::setRight);
            }
            case MORE_LEFT -> {
                comparison.setType(ConnectionType.MORE);
                columnParser.parse(part.getPart()).ifPresent(comparison::setLeft);
            }
            case MORE_RIGHT -> {
                comparison.setType(ConnectionType.MORE);
                columnParser.parse(part.getPart()).ifPresent(comparison::setRight);
            }
            case LESS_LEFT -> {
                comparison.setType(ConnectionType.LESS);
                columnParser.parse(part.getPart()).ifPresent(comparison::setLeft);
            }
            case LESS_RIGHT -> {
                comparison.setType(ConnectionType.LESS);
                columnParser.parse(part.getPart()).ifPresent(comparison::setRight);
            }
            case MORE_OR_EQUAL_LEFT -> {
                comparison.setType(ConnectionType.MORE_OR_EQUAL);
                columnParser.parse(part.getPart()).ifPresent(comparison::setLeft);
            }
            case MORE_OR_EQUAL_RIGHT -> {
                comparison.setType(ConnectionType.MORE_OR_EQUAL);
                columnParser.parse(part.getPart()).ifPresent(comparison::setRight);
            }
            case LESS_OR_EQUAL_LEFT -> {
                comparison.setType(ConnectionType.LESS_OR_EQUAL);
                columnParser.parse(part.getPart()).ifPresent(comparison::setLeft);
            }
            case LESS_OR_EQUAL_RIGHT -> {
                comparison.setType(ConnectionType.LESS_OR_EQUAL);
                columnParser.parse(part.getPart()).ifPresent(comparison::setRight);
            }
            case NOT_EQUAL_LEFT -> {
                comparison.setType(ConnectionType.NOT_EQUAL);
                columnParser.parse(part.getPart()).ifPresent(comparison::setLeft);
            }
            case NOT_EQUAL_RIGHT -> {
                comparison.setType(ConnectionType.NOT_EQUAL);
                columnParser.parse(part.getPart()).ifPresent(comparison::setRight);
            }
            case NOT_EQUAL_EXCL_LEFT -> {
                comparison.setType(ConnectionType.NOT_EQUAL_EXCL);
                columnParser.parse(part.getPart()).ifPresent(comparison::setLeft);
            }
            case NOT_EQUAL_EXCL_RIGHT -> {
                comparison.setType(ConnectionType.NOT_EQUAL_EXCL);
                columnParser.parse(part.getPart()).ifPresent(comparison::setRight);
            }
            case NOT_LESS_LEFT -> {
                comparison.setType(ConnectionType.NOT_LESS);
                columnParser.parse(part.getPart()).ifPresent(comparison::setLeft);
            }
            case NOT_LESS_RIGHT -> {
                comparison.setType(ConnectionType.NOT_LESS);
                columnParser.parse(part.getPart()).ifPresent(comparison::setRight);
            }
            case NOT_MORE_LEFT -> {
                comparison.setType(ConnectionType.NOT_MORE);
                columnParser.parse(part.getPart()).ifPresent(comparison::setLeft);
            }
            case NOT_MORE_RIGHT -> {
                comparison.setType(ConnectionType.NOT_MORE);
                columnParser.parse(part.getPart()).ifPresent(comparison::setRight);
            }
        }
    }

}
