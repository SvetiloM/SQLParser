package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Comparison;
import sm.sql.parser.entity.Condition;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommonComparisonParser implements Parser {

    private final ComparisonParser comparisonParser;
    private final SampleComparisonParser sampleComparisonParser;

    @Override
    public Optional<? extends Condition> parse(String part) {
        Optional<Comparison> parse = comparisonParser.parse(part);
        if(parse.isEmpty()) {
            parse = sampleComparisonParser.parse(part);
        }
        return parse;
    }

}