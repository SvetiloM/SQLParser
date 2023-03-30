package sm.sql.parser.parser;

import lombok.val;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Table;
import sm.sql.parser.entity.part.Part;
import sm.sql.parser.entity.part.TablePartType;

import java.util.List;
import java.util.Optional;

@Component
public class TableParser implements Parser {

    private final PartParser<TablePartType> partParser = new PartParser<>(TablePartType.values());

    @Override
    public Optional<Table> parse(String part) {
        val table = new Table();
        List<Part<TablePartType>> parts = partParser.getParts(part);
        if (parts.size() == 0) {
            table.setTableName(part.trim());
        } else {
            for (Part<TablePartType> columnPart : parts) {
                parse(columnPart, table);
            }
        }
        return Optional.of(table);
    }

    private void parse(Part<TablePartType> part, Table t) {
        switch (part.getType()) {
            case ALIAS -> t.setAlias(part.getPart());
            case NAME_BEFORE_ALIAS -> t.setTableName(part.getPart());
        }
    }
}
