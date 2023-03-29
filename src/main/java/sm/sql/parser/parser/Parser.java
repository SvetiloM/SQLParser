package sm.sql.parser.parser;

import sm.sql.parser.entity.part.Part;

public interface Parser {

    //todo Optional
    Object parse(Part part);

}
