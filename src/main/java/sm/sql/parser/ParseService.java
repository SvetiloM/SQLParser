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
    private final WhereParser whereParser;

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
            case WHERE -> select.setWhere(whereParser.parse(part));
        }
    }
}
