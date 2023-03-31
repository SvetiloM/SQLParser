package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Join;
import sm.sql.parser.entity.Source;
import sm.sql.parser.entity.part.JoinPartType;
import sm.sql.parser.entity.part.Part;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JoinParser implements Parser {

    private final PartParser<JoinPartType> partParser = new PartParser<>(JoinPartType.values());
    private final TableParser tableParser;
    private final ComparisonParser conditionParser;

    @Override
    public Optional<? extends Source> parse(String part) {
        List<Part<JoinPartType>> parts = partParser.getParts(part);
        if (parts.size() == 0) {
            return tableParser.parse(part);
        } else {
            Join innerJoin = new Join();
            Join rootJoin = innerJoin;
            rootJoin.setSecond(innerJoin);
            for (Part<JoinPartType> joinPart : parts) {
                innerJoin = parse(joinPart, innerJoin);
            }
            return Optional.of(rootJoin);
        }
    }

    private Join parse(Part<JoinPartType> part, Join join) {
        switch (part.getType()) {
            case OLD_INNER_JOIN_LEFT, INNER_JOIN_FIRST -> {
                tableParser.parse(part.getPart()).ifPresent(join::setFirst);
                join.setJoinType(Join.JoinType.INNER_JOIN);
            }
            case OLD_INNER_JOIN_RIGHT, INNER_JOIN_SECOND -> {
                join.setJoinType(Join.JoinType.INNER_JOIN);
                return fillJoin(part, join);
            }
            case LEFT_JOIN_FIRST -> {
                tableParser.parse(part.getPart()).ifPresent(join::setFirst);
                join.setJoinType(Join.JoinType.LEFT_JOIN);
            }
            case LEFT_JOIN_SECOND -> {
                join.setJoinType(Join.JoinType.LEFT_JOIN);
                return fillJoin(part, join);
            }
            case RIGHT_JOIN_FIRST -> {
                tableParser.parse(part.getPart()).ifPresent(join::setFirst);
                join.setJoinType(Join.JoinType.RIGHT_JOIN);
            }
            case RIGHT_JOIN_SECOND -> {
                join.setJoinType(Join.JoinType.RIGHT_JOIN);
                return fillJoin(part, join);
            }
            case FULL_JOIN_FIRST -> {
                tableParser.parse(part.getPart()).ifPresent(join::setFirst);
                join.setJoinType(Join.JoinType.FULL_JOIN);
            }
            case FULL_JOIN_SECOND -> {
                join.setJoinType(Join.JoinType.FULL_JOIN);
                return fillJoin(part, join);
            }
            case ON -> conditionParser.parse(part.getPart()).ifPresent(join::setComparison);
        }
        return join;
    }

    private Join fillJoin(Part part, Join join) {
        if (join.getSecond() != null) {
            Join inner = new Join();
            tableParser.parse(part.getPart()).ifPresent(inner::setFirst);
            join.setSecond(inner);
            return inner;
        } else {
            tableParser.parse(part.getPart()).ifPresent(join::setSecond);
            return join;
        }
    }
}
