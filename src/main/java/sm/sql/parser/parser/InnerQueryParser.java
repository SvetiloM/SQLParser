package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Select;
import sm.sql.parser.entity.part.CollectionPartType;
import sm.sql.parser.entity.part.Part;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InnerQueryParser implements Parser<Map<String, Select>> {

    private final PartParser<CollectionPartType> partParser = new PartParser<>(false, CollectionPartType.BRACKET_OPEN, CollectionPartType.BRACKET_CLOSE);
    private final SelectQueryParser selectQueryParser;

    @Override
    public Optional<Map<String, Select>> parse(String s) {
        List<Part<CollectionPartType>> parts = partParser.getParts(s);
        Map<String, Select> selectMap = new HashMap<>();
        for (Part<CollectionPartType> part : parts) {
            parse(part).ifPresent(select -> selectMap.put(addBracket(part.getPart()), select));
        }

        return Optional.of(selectMap);
    }

    private String addBracket(String s) {
        return "(" + s + ")";
    }

    private Optional<Select> parse(Part<CollectionPartType> part) {
        switch (part.getType()) {
            case BRACKET_OPEN -> {
                return selectQueryParser.parse(part.getPart());
            }
        }
        return Optional.empty();
    }
}
