package sm.sql.parser.parser;

import java.util.Optional;

public interface Parser<T> {

    //todo Optional
    Optional<T> parse(String part);

}
