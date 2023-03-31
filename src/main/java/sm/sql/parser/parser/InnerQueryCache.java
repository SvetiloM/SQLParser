package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Select;

import java.util.*;

@Component
@RequiredArgsConstructor
public class InnerQueryCache {

    private final BracketParser bracketParser = new BracketParser();
    private final SelectQueryParser selectQueryParser;
    private final Map<String, Select> selectCache;

    public void fillCache(String s) {
        Optional<List<String>> optional = bracketParser.parse(s);
        if (optional.isPresent()) {
            List<String> parts = optional.get();
            parts.sort(Comparator.comparingInt(String::length));
            for (String part : parts) {
                selectQueryParser.parse(part).ifPresent(select -> selectCache.put(addBracket(part), select));
            }
        }
    }

    private String addBracket(String s) {
        return "(" + s + ")";
    }

}
