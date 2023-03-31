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

    private final BracketParser bracketParser = new BracketParser();
    private final SelectQueryParser selectQueryParser;

    @Override
    public Optional<Map<String, Select>> parse(String s) {
        Map<String, Select> selectMap = new HashMap<>();
        Optional<List<String>> optional = bracketParser.parse(s);
        if (optional.isPresent()) {
            List<String> parts = optional.get();
            for (String part : parts) {
                selectQueryParser.parse(part).ifPresent(select -> selectMap.put(addBracket(part), select));
            }
        }

        return Optional.of(selectMap);
    }

    private String addBracket(String s) {
        return "(" + s + ")";
    }

}
