package org.example.parser;

import lombok.AllArgsConstructor;
import org.example.entity.Part;
import org.example.entity.PartType;

import java.util.ArrayList;
import java.util.List;

public class PartParser {

    public static final String END_INDEX = "end";

    private String[] reservedWords = new String[]{
            "select",
            "from",
    };

    public List<Part> getParts(String s) {
        PartIndex endIndex = new PartIndex(END_INDEX, s.length());
        List<PartIndex> indexes = new ArrayList<>();
        for (String reservedWord : reservedWords) {
            indexes.add(new PartIndex(reservedWord, s.indexOf(reservedWord)));
        }
        indexes.add(endIndex);
        List<Part> parts = new ArrayList<>();
        int i = 0;
        while (i < indexes.size() - 1) {
            PartIndex index = indexes.get(i);
            i++;
            PartIndex nextIndex = indexes.get(i);
            String part = createPartString(s, index.type, index.index, nextIndex.index);
            parts.add(new Part(PartType.getByReservedWord(index.type), part));
        }
        return parts;
    }

    private String createPartString(String s, String type, int start, int end) {
        String substring = s.substring(start, end);
        substring = substring.replaceFirst(type, "");
        substring = substring.trim();
        return substring;
    }

    @AllArgsConstructor
    private class PartIndex {
        private String type;
        private int index;
    }
}
