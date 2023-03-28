package org.example.parser;

import org.example.entity.Part;

import java.util.ArrayList;
import java.util.List;

public class ColumnParser implements Parser {

    @Override
    public List<String> parse(Part part) {
        String s = part.getPart();
        String[] split = s.split(",");

        List<String> columns = new ArrayList<>();
        int i = 0;
        while (i < split.length) {
            columns.add(split[i]);
            i++;
        }

        return columns;
    }
}
