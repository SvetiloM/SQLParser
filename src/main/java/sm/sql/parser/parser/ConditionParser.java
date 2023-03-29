package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Condition;
import sm.sql.parser.entity.part.ConnectionPartType;
import sm.sql.parser.entity.part.Part;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConditionParser implements Parser {

    private final PartParser<ConnectionPartType> connectionPartParser = new PartParser<>(ConnectionPartType.values());

    private final ComparisonParser comparisonParser;
    private final ConnectionParser connectionParser;

    @Override
    public Condition parse(Part part) {
        List<Part<ConnectionPartType>> connections = connectionPartParser.getParts(part.getPart());
        if (connections.size() != 0) {
            return connectionParser.parse(part);
        } else {
            return comparisonParser.parse(part);
        }

    }

}
