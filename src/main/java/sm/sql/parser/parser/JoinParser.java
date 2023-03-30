package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Join;
import sm.sql.parser.entity.Source;
import sm.sql.parser.entity.part.JoinPartType;
import sm.sql.parser.entity.part.Part;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JoinParser implements Parser {

    private final PartParser<JoinPartType> partParser = new PartParser<>(JoinPartType.values());
    private final TableParser tableParser;
    private final ComparisonParser conditionParser;

    @Override
    public Source parse(String part) {
        List<Part<JoinPartType>> parts = partParser.getParts(part);
        if (parts.size() == 0) {
            return tableParser.parse(part);
        } else {
            val join = new Join();
            for (Part<JoinPartType> joinPart : parts) {
                parse(joinPart, join);
            }
            return join;
        }
    }

    private void parse(Part<JoinPartType> part, Join join) {
        switch (part.getType()) {
            case OLD_INNER_JOIN_LEFT, INNER_JOIN_FIRST -> {
                join.setFirst(goDeeper(part.getPart()));
                join.setJoinType(Join.JoinType.INNER_JOIN);
            }
            case OLD_INNER_JOIN_RIGHT, INNER_JOIN_SECOND -> {
                join.setSecond(goDeeper(part.getPart()));
                join.setJoinType(Join.JoinType.INNER_JOIN);
            }
            case LEFT_JOIN_FIRST -> {
                join.setFirst(goDeeper(part.getPart()));
                join.setJoinType(Join.JoinType.LEFT_JOIN);
            }
            case LEFT_JOIN_SECOND -> {
                join.setSecond(goDeeper(part.getPart()));
                join.setJoinType(Join.JoinType.LEFT_JOIN);
            }
            case RIGHT_JOIN_FIRST -> {
                join.setFirst(goDeeper(part.getPart()));
                join.setJoinType(Join.JoinType.RIGHT_JOIN);
            }
            case RIGHT_JOIN_SECOND -> {
                join.setSecond(goDeeper(part.getPart()));
                join.setJoinType(Join.JoinType.RIGHT_JOIN);
            }
            case FULL_JOIN_FIRST -> {
                join.setFirst(goDeeper(part.getPart()));
                join.setJoinType(Join.JoinType.FULL_JOIN);
            }
            case FULL_JOIN_SECOND -> {
                join.setSecond(goDeeper(part.getPart()));
                join.setJoinType(Join.JoinType.FULL_JOIN);
            }
            case ON -> join.setComparison(conditionParser.parse(part.getPart()));
        }
    }

    private Source goDeeper(String part) {
        Source innerSource = parse(part);
        //todo optional
        if (innerSource == null) {
            return tableParser.parse(part);
        } else {
            return innerSource;
        }
    }
}
