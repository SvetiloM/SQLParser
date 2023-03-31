package sm.sql.parser.parser;

import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Column;
import sm.sql.parser.entity.part.CollectionPartType;
import sm.sql.parser.entity.part.ColumnPartType;

@Component
public class ColumnsCollectionParser extends CollectionParser<CollectionPartType, ColumnPartType, Column> {

    public ColumnsCollectionParser() {
        super(new PartParser<>(CollectionPartType.COMMA), new ColumnParser());
    }

}
