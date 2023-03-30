package sm.sql.parser;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Select;
import sm.sql.parser.entity.part.Part;
import sm.sql.parser.entity.part.QueryPartType;
import sm.sql.parser.parser.*;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ParseService {

    private final PartParser<QueryPartType> partParser = new PartParser<>(QueryPartType.values());
    private final ColumnParser columnParser;
    private final JoinParser joinParser;
    private final ConditionParser conditionParser;

    private final OrderParser orderParser;

    public Select parse(String s) {
        List<Part<QueryPartType>> parts = partParser.getParts(s);
        val select = new Select();
        for (Part<QueryPartType> part : parts) {
            parse(part, select);
        }

        return select;
    }

    private void parse(Part<QueryPartType> part, Select select) {
        switch (part.getType()) {
            case COLUMNS -> columnParser.parse(part.getPart()).ifPresent(select::setColumns);
            case TABLES -> joinParser.parse(part.getPart()).ifPresent(select::setSource);
            case WHERE -> conditionParser.parse(part.getPart()).ifPresent(select::setWhere);
            case GROUP_BY -> columnParser.parse(part.getPart()).ifPresent(select::setGroupBy);
            case HAVING -> conditionParser.parse(part.getPart()).ifPresent(select::setHaving);
            case ORDER_BY -> orderParser.parse(part.getPart()).ifPresent(select::setOrderBy);
            case LIMIT -> select.setLimit(Integer.parseInt(part.getPart()));
            case OFFSET -> select.setOffset(Integer.parseInt(part.getPart()));
        }
    }
}
