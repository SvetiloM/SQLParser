package org.example.parser;

import org.example.entity.Part;

public class TableParser implements Parser {
    @Override
    public String parse(Part part) {
        String s = part.getPart();
        return s.trim();
    }
}
