package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Where;
import sm.sql.parser.entity.part.LogicalConnectionPartType;
import sm.sql.parser.entity.part.Part;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WhereParser implements Parser {
    private final PartParser<LogicalConnectionPartType> partParser = new PartParser<>(LogicalConnectionPartType.values());
    private final ConditionParser conditionParser;

    @Override
    public Where parse(Part part) {
        val where = new Where();
        List<Part<LogicalConnectionPartType>> parts = partParser.getParts(part.getPart());
        if (parts.size() == 0) {
            where.setLeft(conditionParser.parse(part));
        } else {
            for (Part<LogicalConnectionPartType> columnPart : parts) {
                parse(columnPart, where);
            }
        }
        return where;
    }

    private void parse(Part<LogicalConnectionPartType> part, Where where) {
        switch (part.getType()) {
            case OR_LEFT -> {
                where.setConnection(Where.Connection.OR);
                where.setLeft(goDeeper(part));
            }
            case OR_RIGHT -> {
                where.setConnection(Where.Connection.OR);
                where.setRight(goDeeper(part));
            }
            case AND_LEFT -> {
                where.setConnection(Where.Connection.AND);
                where.setLeft(goDeeper(part));
            }
            case AND_RIGHT -> {
                where.setConnection(Where.Connection.AND);
                where.setRight(goDeeper(part));
            }
        }
    }

    private Object goDeeper(Part<LogicalConnectionPartType> part) {
        Where innerCondition = parse(part);
        //todo optional
        if (innerCondition == null) {
            return conditionParser.parse(part);
        } else {
            return innerCondition;
        }
    }
}
