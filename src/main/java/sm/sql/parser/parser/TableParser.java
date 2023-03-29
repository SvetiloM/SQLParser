package sm.sql.parser.parser;

import sm.sql.parser.entity.part.Part;

public class TableParser implements Parser {
    @Override
    public String parse(Part part) {
        String s = part.getPart();
        return s.trim();
    }
}
