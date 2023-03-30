package sm.sql.parser.parser;

import sm.sql.parser.entity.part.Part;
import sm.sql.parser.entity.part.PartType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class CollectionParser<E extends PartType, T> implements Parser {

    private final PartParser<E> partParser;
    private final Supplier<T> supplier;
    private final SimpleParser<E, T> simpleParser;

    protected CollectionParser(PartParser<E> partParser, Supplier<T> supplier, SimpleParser<E, T> simpleParser) {
        this.partParser = partParser;
        this.supplier = supplier;
        this.simpleParser = simpleParser;
    }

    @Override
    public Optional<List<T>> parse(String part) {
        List<Part<E>> parts = partParser.getParts(part);
        if (parts.size() == 0) return Optional.empty();
        List<T> pojos = new ArrayList<>();
        for (Part<E> ePart : parts) {
            Optional<T> parse = simpleParser.parse(ePart.getPart());
            parse.ifPresent(pojos::add);
        }
        return Optional.of(pojos);
    }
}
