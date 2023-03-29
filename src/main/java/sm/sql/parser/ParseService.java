package sm.sql.parser;

import lombok.val;
import sm.sql.parser.entity.part.Part;
import sm.sql.parser.entity.part.QueryPartType;
import sm.sql.parser.entity.Select;
import sm.sql.parser.parser.ColumnParser;
import sm.sql.parser.parser.JoinParser;
import sm.sql.parser.parser.PartParser;
import sm.sql.parser.parser.TableParser;

import java.util.List;

public class ParseService {

    private PartParser<QueryPartType> partParser = new PartParser<>(QueryPartType.values());
    private ColumnParser columnParser = new ColumnParser();
    private JoinParser joinParser = new JoinParser();

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
            case TABLES -> {
                select.setTable(joinParser.parse(part));
            }
        }
    }
}
