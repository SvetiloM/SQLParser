package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Select;
import sm.sql.parser.entity.Source;
import sm.sql.parser.entity.Table;
import sm.sql.parser.entity.part.Part;
import sm.sql.parser.entity.part.TablePartType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TableParser implements Parser<Source>{

    private final PartParser<TablePartType> partParser = new PartParser<>(TablePartType.values());

    private final Map<String, Select> cache;

    @Override
    public Optional<Source> parse(String part) {
        List<Part<TablePartType>> parts = partParser.getParts(part);
        if (parts.size() == 0) {
            if (cache.containsKey(part)) {
                return Optional.of(cache.get(part));
            }
            Table pojo = new Table();
            pojo.setName(part);
            return Optional.of(pojo);
        }
        Table pojo = new Table();
        for (Part<TablePartType> connectionPart : parts) {
            parse(connectionPart, pojo);
        }
        return Optional.of(pojo);
    }

    private void parse(Part<TablePartType> part, Table t) {
        switch (part.getType()) {
            case ALIAS -> t.setAlias(part.getPart());
            case NAME_BEFORE_ALIAS -> t.setName(part.getPart());
        }
    }
}
