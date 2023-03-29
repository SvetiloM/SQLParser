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
            case COLUMNS -> select.setColumns(columnParser.parse(part));
            case TABLES -> select.setTable(joinParser.parse(part));
            case WHERE -> select.setWhere(conditionParser.parse(part));
            case GROUP_BY -> select.setGroupBy(columnParser.parse(part));
            case HAVING -> select.setHaving(conditionParser.parse(part));
            case ORDER_BY -> select.setOrderBy(orderParser.parse(part));
        }
    }
}
