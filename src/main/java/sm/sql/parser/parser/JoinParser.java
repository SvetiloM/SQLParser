package sm.sql.parser.parser;

import lombok.val;
import sm.sql.parser.entity.Join;
import sm.sql.parser.entity.TableReference;
import sm.sql.parser.entity.part.JoinPartType;
import sm.sql.parser.entity.part.Part;

import java.util.List;

public class JoinParser implements Parser {

    PartParser<JoinPartType> partParser = new PartParser<>(JoinPartType.values());
    TableParser tableParser = new TableParser();
    ConnectionParser connectionParser = new ConnectionParser();

    @Override
    public TableReference parse(Part part) {
        List<Part<JoinPartType>> parts = partParser.getParts(part.getPart());
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
            case INNER_JOIN_FIRST -> {
                join.setFirst(tableParser.parse(part));
                join.setJoinType(Join.JoinType.INNER_JOIN);
            }
            case INNER_JOIN_SECOND -> {
                join.setSecond(tableParser.parse(part));
                join.setJoinType(Join.JoinType.INNER_JOIN);
            }
            case LEFT_JOIN_FIRST -> {
                join.setFirst(tableParser.parse(part));
                join.setJoinType(Join.JoinType.LEFT_JOIN);
            }
            case LEFT_JOIN_SECOND -> {
                join.setSecond(tableParser.parse(part));
                join.setJoinType(Join.JoinType.LEFT_JOIN);
            }
            case RIGHT_JOIN_FIRST -> {
                join.setFirst(tableParser.parse(part));
                join.setJoinType(Join.JoinType.RIGHT_JOIN);
            }
            case RIGHT_JOIN_SECOND -> {
                join.setSecond(tableParser.parse(part));
                join.setJoinType(Join.JoinType.RIGHT_JOIN);
            }
            case FULL_JOIN_FIRST -> {
                join.setFirst(tableParser.parse(part));
                join.setJoinType(Join.JoinType.FULL_JOIN);
            }
            case FULL_JOIN_SECOND -> {
                join.setSecond(tableParser.parse(part));
                join.setJoinType(Join.JoinType.FULL_JOIN);
            }
            case ON -> join.setConnection(connectionParser.parse(part));
        }
    }
}
