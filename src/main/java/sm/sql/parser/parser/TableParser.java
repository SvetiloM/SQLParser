package sm.sql.parser.parser;

import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Table;
import sm.sql.parser.entity.part.Part;
import sm.sql.parser.entity.part.TablePartType;

@Component
public class TableParser extends SimpleParser<TablePartType, Table> {

    private final PartParser<TablePartType> partParser = new PartParser<>(TablePartType.values());

    public TableParser() {
        super(new PartParser<>(TablePartType.values()), Table::new, (s, t) -> t.setName(s), true);
    }

    @Override
    public void parse(Part<TablePartType> part, Table t) {
        switch (part.getType()) {
            case ALIAS -> t.setAlias(part.getPart());
            case NAME_BEFORE_ALIAS -> t.setName(part.getPart());
        }
    }
}
