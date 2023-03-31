package sm.sql.parser;

import lombok.RequiredArgsConstructor;
import sm.sql.parser.entity.Select;
import sm.sql.parser.parser.InnerQueryParser;
import sm.sql.parser.parser.SelectQueryParser;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class ParseService {

    private final Map<String, Select> selectCashe;
    private final InnerQueryParser innerQueryParser;
    private final SelectQueryParser selectQueryParser;

    public Select parse(String s) {
        Optional<Map<String, Select>> parse = innerQueryParser.parse(s);
        parse.ifPresent(selectCashe::putAll);

        return selectQueryParser.parse(s).orElseGet(Select::new);
    }
}
