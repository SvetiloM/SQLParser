package sm.sql.parser.parser;

import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Column;
import sm.sql.parser.entity.part.ColumnPartType;
import sm.sql.parser.entity.part.Part;

@Component
public class ColumnParser extends SimpleParser<ColumnPartType, Column> {

    public ColumnParser() {
        super(new PartParser<>(ColumnPartType.values()), Column::new, (name, column) -> column.setColumnName(name), true);
    }

    @Override
    public void parse(Part<ColumnPartType> part, Column column) {
        switch (part.getType()) {
            case ALIAS -> column.setAlias(part.getPart());
            case TABLE -> column.setTable(part.getPart());
            case NAME_BEFORE_ALIAS, NAME_AFTER_TABLE -> column.setColumnName(part.getPart());
        }
    }
}
