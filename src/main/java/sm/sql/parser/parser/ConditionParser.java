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
    private final ColumnParser columnParser;

    @Override
    public Condition parse(Part part) {
        List<Part<ConditionPartType>> parts = partParser.getParts(part.getPart());
        val connection = new Condition();
        for (Part<ConditionPartType> connectionPart : parts) {
            parse(connectionPart, connection);
        }
        return connection;

    }

    private void parse(Part<ConditionPartType> part, Condition Condition) {
        switch (part.getType()) {
            case EQUAL_LEFT -> {
                //todo columns? change
                Condition.setType(ConnectionType.EQUAL);
                Condition.setLeft(columnParser.parse(part));
            }
            case EQUAL_RIGHT -> {
                Condition.setType(ConnectionType.EQUAL);
                Condition.setRight(columnParser.parse(part));
            }
        }
    }
}
