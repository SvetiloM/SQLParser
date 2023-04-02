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
    private final ComparisonParser comparisonParser;

    @Override
    public Optional<? extends Source> parse(String part) {
        List<Part<JoinPartType>> parts = partParser.getParts(part);
        if (parts.size() == 0) {
            return tableParser.parse(part);
        } else {
            Join innerJoin = new Join();
            Join rootJoin = innerJoin;
            for (Part<JoinPartType> joinPart : parts) {
                innerJoin = parse(joinPart, innerJoin);
            }
            return Optional.of(rootJoin);
        }
    }

    private Join parse(Part<JoinPartType> part, Join join) {
        switch (part.getType()) {
            case OLD_INNER_JOIN_LEFT, INNER_JOIN_FIRST -> {
                Join newJoin = fillFirst(part, join);
                newJoin.setJoinType(Join.JoinType.INNER_JOIN);
                return newJoin;
            }
            case LEFT_JOIN_FIRST -> {
                Join newJoin = fillFirst(part, join);
                newJoin.setJoinType(Join.JoinType.LEFT_JOIN);
                return newJoin;
            }
            case RIGHT_JOIN_FIRST -> {
                Join newJoin = fillFirst(part, join);
                newJoin.setJoinType(Join.JoinType.RIGHT_JOIN);
                return newJoin;
            }
            case FULL_JOIN_FIRST -> {
                Join newJoin = fillFirst(part, join);
                newJoin.setJoinType(Join.JoinType.FULL_JOIN);
                return newJoin;
            }
            case OLD_INNER_JOIN_RIGHT, INNER_JOIN_SECOND, FULL_JOIN_SECOND, RIGHT_JOIN_SECOND, LEFT_JOIN_SECOND -> {
                tableParser.parse(part.getPart()).ifPresent(join::setSecond);
            }
            case ON -> comparisonParser.parse(part.getPart()).ifPresent(join::setComparison);
        }
        return join;
    }

    private Join fillFirst(Part part, Join join) {
        if (join.getFirst() != null) {
            if (comparisonParser.parse(part.getPart()).isPresent()) {
                Join inner = new Join();
                //todo copy
                inner.setFirst(join.getFirst());
                inner.setSecond(join.getSecond());
                inner.setJoinType(join.getJoinType());
                inner.setComparison(join.getComparison());
                join.setFirst(inner);
                join.setSecond(null);
                join.setComparison(null);
                join.setJoinType(null);

                return join;
            }
        } else {
            tableParser.parse(part.getPart()).ifPresent(join::setFirst);
        }
        return join;
    }
}
