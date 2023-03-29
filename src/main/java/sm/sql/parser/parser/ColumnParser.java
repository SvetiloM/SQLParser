package sm.sql.parser.parser;

import lombok.val;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Column;
import sm.sql.parser.entity.part.ColumnPartType;
import sm.sql.parser.entity.part.Part;

import java.util.ArrayList;
import java.util.List;

@Component
public class ColumnParser implements Parser {

    private final PartParser<ColumnPartType> partParser = new PartParser<>(ColumnPartType.values());

    @Override
    public List<Column> parse(Part part) {
        String s = part.getPart();
        String[] split = s.split(",");

        List<Column> columns = new ArrayList<>();
        int i = 0;
        while (i < split.length) {
            val column = new Column();
            List<Part<ColumnPartType>> parts = partParser.getParts(split[i]);
            if (parts.size() == 0) {
                column.setColumnName(split[i].trim());
            } else {
                for (Part<ColumnPartType> columnPart : parts) {
                    parse(columnPart, column);
                }
            }
            columns.add(column);
            i++;
        }

        return columns;
    }

    private void parse(Part<ColumnPartType> part, Column column) {
        switch (part.getType()) {
            case ALIAS -> column.setAlias(part.getPart());
            case TABLE -> column.setTable(part.getPart());
            case NAME_BEFORE_ALIAS, NAME_AFTER_TABLE -> column.setColumnName(part.getPart());
        }
    }
}
