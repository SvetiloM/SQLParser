package org.example.parser;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.entity.Part;
import org.example.entity.PartType;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PartParser<E extends PartType> {

    private final E[] reservedWords;

    public List<Part<E>> getParts(String s) {
        List<PartIndex> indexes = new ArrayList<>();
        for (E reservedWord : reservedWords) {
            int i = s.indexOf(reservedWord.getValue());
            if (i > -1) {
                indexes.add(new PartIndex(reservedWord, i));
            }
        }
        PartIndex endIndex = new PartIndex(null, s.length());
        indexes.add(endIndex);

        List<Part<E>> parts = new ArrayList<>();
        int i = 0;
        while (i < indexes.size() - 1) {
            PartIndex index = indexes.get(i);
            if (index.type.getDirection().equals(PartType.Direction.BEFORE)) {
                int start = 0;
                if (i > 1) {
                    start = indexes.get(i - 1).index + 1;
                }
                String part = createPartString(s, index.type, start, index.index);
                parts.add(new Part<>(index.type, part));
            } else {
                int end = s.length();
                if (i < s.length() - 1) {
                    end = indexes.get(i + 1).index;
                }
                String part = createPartString(s, index.type, index.index, end);
                parts.add(new Part<>(index.type, part));
            }
            i++;
        }
        return parts;
    }

    private String createPartString(String s, E type, int start, int end) {
        String substring = s.substring(start, end);
        if (substring.contains(type.getValue()))
            substring = substring.replaceFirst(type.getValue(), "");
        substring = substring.trim();
        return substring;
    }

    @AllArgsConstructor
    private class PartIndex {
        private E type;
        private int index;
    }

}
