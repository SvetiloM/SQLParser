package org.example;

import lombok.val;
import org.example.entity.Part;
import org.example.entity.QueryPartType;
import org.example.entity.Select;
import org.example.parser.ColumnParser;
import org.example.parser.PartParser;
import org.example.parser.TableParser;

import java.util.List;

public class ParseService {

    private PartParser partParser = new PartParser(QueryPartType.values());
    private ColumnParser columnParser = new ColumnParser();
    private TableParser tableParser = new TableParser();

    public Select parse(String s) {
        List<Part> parts = partParser.getParts(s);
        val select = new Select();
        for (Part part : parts) {
            parse(part, select);
        }

        return select;
    }

    private void parse(Part<QueryPartType> part, Select select) {
        switch (part.getType()) {
            case COLUMNS -> select.setColumns(columnParser.parse(part));
            case TABLES -> {
                select.setTable(tableParser.parse(part));
            }
        }
    }
}
