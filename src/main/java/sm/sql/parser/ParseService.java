package sm.sql.parser;

import lombok.RequiredArgsConstructor;
import sm.sql.parser.entity.Select;
import sm.sql.parser.parser.InnerQueryCache;
import sm.sql.parser.parser.SelectQueryParser;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class ParseService {
    private final InnerQueryCache innerQueryParser;
    private final SelectQueryParser selectQueryParser;

    public Select parse(String s) {
        innerQueryParser.fillCache(s);

        return selectQueryParser.parse(s).orElseGet(Select::new);
    }
}
