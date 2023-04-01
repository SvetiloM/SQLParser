package sm.sql.parser.parser;

import org.springframework.stereotype.Component;
import sm.sql.parser.entity.part.Part;
import sm.sql.parser.entity.part.PartType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public abstract class CollectionParser<E extends PartType, I extends PartType, T> implements Parser<List<T>> {

    private final PartParser<E> partParser;
    private final SimpleParser<I, T> simpleParser;

    protected CollectionParser(PartParser<E> partParser, SimpleParser<I, T> simpleParser) {
        this.partParser = partParser;
        this.simpleParser = simpleParser;
    }

    @Override
    public Optional<List<T>> parse(String part) {
        List<Part<E>> parts = partParser.getParts(part);
        if (parts.size() == 0) {
            Optional<T> parse = simpleParser.parse(part);
            if (parse.isPresent()) {
                return Optional.of(List.of(parse.get()));
            }
        }
        List<T> pojos = new ArrayList<>();
        for (Part<E> ePart : parts) {
            Optional<T> parse = simpleParser.parse(ePart.getPart());
            parse.ifPresent(pojos::add);
        }
        return Optional.of(pojos);
    }
}
