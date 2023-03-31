package sm.sql.parser.parser;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import sm.sql.parser.entity.Select;
import sm.sql.parser.entity.part.Part;
import sm.sql.parser.entity.part.SelectQueryPartType;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SelectQueryParser implements Parser<Select> {

    private final PartParser<SelectQueryPartType> partParser = new PartParser<>(SelectQueryPartType.values());
    private final ColumnsCollectionParser columnsCollectionParser;
    private final JoinParser joinParser;
    private final ConditionParser conditionParser;

    private final OrderCollectionParser orderCollectionParser;

    @Override
    public Optional<Select> parse(String s) {
        List<Part<SelectQueryPartType>> parts = partParser.getParts(s);
        val select = new Select();
        for (Part<SelectQueryPartType> part : parts) {
            parse(part, select);
        }

        return Optional.of(select);
    }

    private void parse(Part<SelectQueryPartType> part, Select select) {
        switch (part.getType()) {
            case COLUMNS -> columnsCollectionParser.parse(part.getPart()).ifPresent(select::setColumns);
            case TABLES -> joinParser.parse(part.getPart()).ifPresent(select::setSource);
            case WHERE -> conditionParser.parse(part.getPart()).ifPresent(select::setWhere);
            case GROUP_BY -> columnsCollectionParser.parse(part.getPart()).ifPresent(select::setGroupBy);
            case HAVING -> conditionParser.parse(part.getPart()).ifPresent(select::setHaving);
            case ORDER_BY -> orderCollectionParser.parse(part.getPart()).ifPresent(select::setOrderBy);
            case LIMIT -> select.setLimit(Integer.parseInt(part.getPart()));
            case OFFSET -> select.setOffset(Integer.parseInt(part.getPart()));
        }
    }
}
