package sm.sql.parser.parser;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import sm.sql.parser.entity.part.Part;
import sm.sql.parser.entity.part.PartType;

import java.util.ArrayList;
import java.util.Collections;
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
        indexes = mergeConflicts(indexes);

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

    private List<PartIndex> mergeConflicts(List<PartIndex> indexList) {
        if (indexList.size() < 2) {
            return indexList;
        }
        Collections.sort(indexList);
        List<PartIndex> mergedIndexes = new ArrayList<>();
        //add first
        mergedIndexes.add(indexList.get(0));
        int j = 1;
        //compare last from merged with first from indexes
        while (j < indexList.size()) {
            PartIndex previousIndex = mergedIndexes.get(mergedIndexes.size() - 1);
            PartIndex index = indexList.get(j);

            if (previousIndex.type.getValue().contains(index.type.getValue()) &&
                    !previousIndex.type.getValue().equals(index.type.getValue())) {
                int lastIndex = previousIndex.index + previousIndex.type.getValue().length();
                if (index.index < lastIndex) //todo <= ?
                {
                    j++; //delete this index
                }
            } else {
                mergedIndexes.add(index);
                j++;
            }
        }
//        PartIndex index = indexList.get(j);
//        if (index.type == null) {
//            mergedIndexes.add(index);
//        }

        return mergedIndexes;
    }

    private String createPartString(String s, E type, int start, int end) {
        String substring = s.substring(start, end);
        if (substring.contains(type.getValue()))
            substring = substring.replaceFirst(type.getValue(), "");
        substring = substring.trim();
        return substring;
    }

    @AllArgsConstructor
    private class PartIndex implements Comparable<PartIndex> {
        private E type;
        private int index;

        @Override
        public int compareTo(PartIndex o2) {
            if (index > o2.index) return 1;
            if (index < o2.index) return -1;
            return 0;
        }
    }

}
