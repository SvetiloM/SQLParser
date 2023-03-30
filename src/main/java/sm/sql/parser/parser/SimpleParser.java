package sm.sql.parser.parser;

import sm.sql.parser.entity.part.Part;
import sm.sql.parser.entity.part.PartType;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class SimpleParser<E extends PartType, T> implements Parser<T> {

    private final PartParser<E> partParser;
    private final Supplier<T> pojoCreator;
    private final BiConsumer<String, T> simpleValueSetter;
    private final Boolean simpleName;

    public SimpleParser(PartParser<E> partParser, Supplier<T> pojoCreator, BiConsumer<String, T> simpleValueSetter, Boolean simpleName) {
        this.partParser = partParser;
        this.pojoCreator = pojoCreator;
        this.simpleValueSetter = simpleValueSetter;
        this.simpleName = simpleName;
    }

    public SimpleParser(PartParser<E> partParser, Supplier<T> pojoCreator) {
        this(partParser, pojoCreator, (t, d) -> {}, false);
    }

    @Override
    public Optional<T> parse(String part) {
        List<Part<E>> parts = partParser.getParts(part);
        if (parts.size() == 0) {
            if (!simpleName) {
                return Optional.empty();
            }
            T pojo = pojoCreator.get();
            simpleValueSetter.accept(part, pojo);
            return Optional.of(pojo);
        }
        T pojo = pojoCreator.get();
        for (Part<E> connectionPart : parts) {
            parse(connectionPart, pojo);
        }
        return Optional.of(pojo);
    }

    public abstract void parse(Part<E> part, T pojo);
}
