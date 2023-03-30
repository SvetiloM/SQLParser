package sm.sql.parser.parser;

import sm.sql.parser.entity.part.Part;
import sm.sql.parser.entity.part.PartType;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class SimpleParser<E extends PartType, T> implements Parser<T> {

    private final PartParser<E> partParser;
    private final Supplier<T> supplier;

    protected SimpleParser(PartParser<E> partParser, Supplier<T> supplier) {
        this.partParser = partParser;
        this.supplier = supplier;
    }

    @Override
    public Optional<T> parse(String part) {
        List<Part<E>> parts = partParser.getParts(part);
        if (parts.size() == 0) return Optional.empty();
        T pojo = supplier.get();
        for (Part<E> connectionPart : parts) {
            parse(connectionPart, pojo);
        }
        return Optional.of(pojo);
    }

    public abstract void parse(Part<E> part, T pojo);
}