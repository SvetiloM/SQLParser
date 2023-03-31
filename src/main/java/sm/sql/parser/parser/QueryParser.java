package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Select;
import sm.sql.parser.entity.part.Part;
import sm.sql.parser.entity.part.QueryPartType;
import sm.sql.parser.entity.part.SelectQueryPartType;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class QueryParser implements Parser<Select> {

    private final PartParser<QueryPartType> partParser = new PartParser<>(QueryPartType.values());
    private final SelectQueryParser selectQueryParser;

    @Override
    public Optional<Select> parse(String s) {
        List<Part<QueryPartType>> parts = partParser.getParts(s);
        Select select = new Select();
        for (Part<QueryPartType> part : parts) {
            parse(part);
        }

        return Optional.of(select);
    }

    private Optional<Select> parse(Part<QueryPartType> part) {
        switch (part.getType()) {
            case SELECT -> {
                return selectQueryParser.parse(part.getPart());
            }
        }
        return Optional.empty();
    }
}
