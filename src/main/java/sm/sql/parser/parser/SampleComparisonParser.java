package sm.sql.parser.parser;

import sm.sql.parser.entity.Comparison;
import sm.sql.parser.entity.Select;
import sm.sql.parser.entity.part.Part;
import sm.sql.parser.entity.part.SampleComparisonPartType;

import java.util.Map;

public class SampleComparisonParser extends SimpleParser<SampleComparisonPartType, Comparison> {

    private final ColumnParser columnParser;
    private final Map<String, Select> innerQueries;

    public SampleComparisonParser(ColumnParser columnParser, Map<String, Select> innerQueries) {
        super(new PartParser<>(SampleComparisonPartType.values()), Comparison::new);
        this.columnParser = columnParser;
        this.innerQueries = innerQueries;
    }

    @Override
    public void parse(Part<SampleComparisonPartType> part, Comparison pojo) {
        switch (part.getType()) {
            case IN_LEFT -> {
                columnParser.parse(part.getPart()).ifPresent(pojo::setLeft);
                pojo.setType(Comparison.ConnectionType.IN);
            }
            case IN_RIGHT -> {
                pojo.setRight(innerQueries.get(part.getPart()));
                pojo.setType(Comparison.ConnectionType.IN);
            }
            case ALL_LEFT -> {
                columnParser.parse(part.getPart()).ifPresent(pojo::setLeft);
                pojo.setType(Comparison.ConnectionType.ALL);
            }
            case ALL_RIGHT -> {
                pojo.setType(Comparison.ConnectionType.ALL);
            }
            case ANY_LEFT -> {
                columnParser.parse(part.getPart()).ifPresent(pojo::setLeft);
                pojo.setType(Comparison.ConnectionType.ANY);
            }
            case ANY_RIGHT -> {
                pojo.setType(Comparison.ConnectionType.ANY);
            }
            case SOME_LEFT -> {
                columnParser.parse(part.getPart()).ifPresent(pojo::setLeft);
                pojo.setType(Comparison.ConnectionType.SOME);
            }
            case SOME_RIGHT -> {
                pojo.setType(Comparison.ConnectionType.SOME);
            }
            case EXISTS_LEFT -> {
                columnParser.parse(part.getPart()).ifPresent(pojo::setLeft);
                pojo.setType(Comparison.ConnectionType.EXISTS);
            }
            case EXISTS_RIGHT -> {
                pojo.setType(Comparison.ConnectionType.EXISTS);
            }
        }
    }
}
