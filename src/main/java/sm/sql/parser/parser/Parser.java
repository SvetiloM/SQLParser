package sm.sql.parser.parser;

import java.util.Optional;

public interface Parser<T> {

    Optional<T> parse(String part);

}
