package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Condition;
import sm.sql.parser.entity.Condition.ConnectionType;
import sm.sql.parser.entity.part.ConditionPartType;
import sm.sql.parser.entity.part.Part;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConditionParser implements Parser {

    private final PartParser<ConditionPartType> partParser = new PartParser<>(ConditionPartType.values());
    private final PartParser<ConditionPartType> compositePartParser = new PartParser<>(ConditionPartType.getCompositeValues());
    private final ColumnParser columnParser;

    @Override
    public Condition parse(Part part) {
        List<Part<ConditionPartType>> parts = compositePartParser.getParts(part.getPart());
        if (parts.size() == 0) {
            parts = partParser.getParts(part.getPart());
        }
        if (parts.size() == 0) return null;
        val connection = new Condition();
        for (Part<ConditionPartType> connectionPart : parts) {
            parse(connectionPart, connection);
        }
        return connection;

    }

    private void parse(Part<ConditionPartType> part, Condition condition) {
        switch (part.getType()) {
            case EQUAL_LEFT -> {
                condition.setType(ConnectionType.EQUAL);
                condition.setLeft(goDeeper(part));
            }
            case EQUAL_RIGHT -> {
                condition.setType(ConnectionType.EQUAL);
                condition.setRight(goDeeper(part));
            }
            case MORE_LEFT -> {
                condition.setType(ConnectionType.MORE);
                condition.setLeft(goDeeper(part));
            }
            case MORE_RIGHT -> {
                condition.setType(ConnectionType.MORE);
                condition.setRight(goDeeper(part));
            }
            case LESS_LEFT -> {
                condition.setType(ConnectionType.LESS);
                condition.setLeft(goDeeper(part));
            }
            case LESS_RIGHT -> {
                condition.setType(ConnectionType.LESS);
                condition.setRight(goDeeper(part));
            }
            case MORE_OR_EQUAL_LEFT -> {
                condition.setType(ConnectionType.MORE_OR_EQUAL);
                condition.setLeft(goDeeper(part));
            }
            case MORE_OR_EQUAL_RIGHT -> {
                condition.setType(ConnectionType.MORE_OR_EQUAL);
                condition.setRight(goDeeper(part));
            }
            case LESS_OR_EQUAL_LEFT -> {
                condition.setType(ConnectionType.LESS_OR_EQUAL);
                condition.setLeft(goDeeper(part));
            }
            case LESS_OR_EQUAL_RIGHT -> {
                condition.setType(ConnectionType.LESS_OR_EQUAL);
                condition.setRight(goDeeper(part));
            }
            case NOT_EQUAL_LEFT -> {
                condition.setType(ConnectionType.NOT_EQUAL);
                condition.setLeft(goDeeper(part));
            }
            case NOT_EQUAL_RIGHT -> {
                condition.setType(ConnectionType.NOT_EQUAL);
                condition.setRight(goDeeper(part));
            }
            case NOT_EQUAL_EXCL_LEFT -> {
                condition.setType(ConnectionType.NOT_EQUAL_EXCL);
                condition.setLeft(goDeeper(part));
            }
            case NOT_EQUAL_EXCL_RIGHT -> {
                condition.setType(ConnectionType.NOT_EQUAL_EXCL);
                condition.setRight(goDeeper(part));
            }
            case NOT_LESS_LEFT -> {
                condition.setType(ConnectionType.NOT_LESS);
                condition.setLeft(goDeeper(part));
            }
            case NOT_LESS_RIGHT -> {
                condition.setType(ConnectionType.NOT_LESS);
                condition.setRight(goDeeper(part));
            }
            case NOT_MORE_LEFT -> {
                condition.setType(ConnectionType.NOT_MORE);
                condition.setLeft(goDeeper(part));
            }
            case NOT_MORE_RIGHT -> {
                condition.setType(ConnectionType.NOT_MORE);
                condition.setRight(goDeeper(part));
            }
        }
    }

    private Object goDeeper(Part<ConditionPartType> part) {
        Condition innerCondition = parse(part);
        //todo optional
        if (innerCondition == null) {
            return columnParser.parse(part);
        } else {
            return innerCondition;
        }
    }
}
