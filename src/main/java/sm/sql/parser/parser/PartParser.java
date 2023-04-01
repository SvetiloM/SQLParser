package sm.sql.parser.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sm.sql.parser.entity.part.Part;
import sm.sql.parser.entity.part.PartType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PartParser<E extends PartType> {

    private final E[] reservedWords;

    private final List<Interval> ignoringIntervals = new ArrayList<>();

    @SafeVarargs
    public PartParser(E... reservedWords) {
        this.reservedWords = reservedWords;
    }

    public List<Part<E>> getParts(String s) {
        fillIgnoringIntervals(s);
        List<PartIndex> indexes = getReservedWordsIndexes(s);
        if (indexes.size() == 0) return Collections.emptyList();

        List<Part<E>> parts = new ArrayList<>();
        int i = 0;
        while (i < indexes.size()) {
            PartIndex index = indexes.get(i);
            if (index.type.getDirection().equals(PartType.Direction.BEFORE)) {
                int start = 0;
                if (i > 0) {
                    PartIndex prevIndex = indexes.get(i - 1);
                    start = prevIndex.index + prevIndex.type.getValue().length();
                }
                String part = createPartString(s, index.type, start, index.index);
                parts.add(new Part<>(index.type, part));
            } else {
                int end = s.length();
                if (i < indexes.size() - 1) {
                    end = indexes.get(i + 1).index;
                }
                String part = createPartString(s, index.type, index.index, end);
                parts.add(new Part<>(index.type, part));
            }
            i++;
        }
        getLastElem(indexes.get(indexes.size() - 1), s).ifPresent(parts::add);
        return parts;
    }

    private void fillIgnoringIntervals(String s) {
        char openBracket = '(';
        char closeBracket = ')';
        char quotes = '"';
        char singleQuotes = '\'';

        List<Interval> intervals = new ArrayList<>();

        intervals.addAll(findIntervals(s, openBracket, closeBracket));
        intervals.addAll(findIntervals(s, quotes, quotes));
        intervals.addAll(findIntervals(s, singleQuotes, singleQuotes));

        ignoringIntervals.addAll(mergeIntervals(intervals));
    }

    private List<Interval> findIntervals(String s, char open, char close) {
        List<Interval> intervals = new ArrayList<>();
        int i = 0;
        while (i != -1) {
            i = s.indexOf(open, i + 1);
            if (i != -1) {
                intervals.add(new Interval(i, s.indexOf(close, i)));
            }
        }
        return intervals;
    }

    private List<Interval> mergeIntervals(List<Interval> intervals) {
        if (intervals.size() < 2) return intervals;
        Collections.sort(intervals);

        List<Interval> merged = new ArrayList<>();

        merged.add(intervals.get(0));
        for (Interval interval : intervals) {
            Interval lastMerged = merged.get(merged.size() - 1);
            if (!(interval.x > lastMerged.x
                    && interval.y < lastMerged.y)) {
                merged.add(interval);
            }
        }
        return merged;
    }

    private List<PartIndex> getReservedWordsIndexes(String s) {
        List<PartIndex> indexes = new ArrayList<>();
        for (E reservedWord : reservedWords) {
            indexes.addAll(findAllIndexes(s, reservedWord));
        }

        Collections.sort(indexes);

        return mergeConflicts(indexes);
    }

    private List<PartIndex> findAllIndexes(String s, E reservedWord) {
        List<PartIndex> indexes = new ArrayList<>();
        int i = -1;
        do {
            i = s.indexOf(reservedWord.getValue(), i + 1);
            if (i > -1) {
                boolean ignore = false;
                for (Interval ignoringInterval : ignoringIntervals) {
                    if (i >= ignoringInterval.x && i <= ignoringInterval.y) {
                        ignore = true;
                        break;
                    }
                }
                if (!ignore) {
                    indexes.add(new PartIndex(reservedWord, i));
                }
            }
        } while (i > -1);
        return indexes;
    }

    private List<PartIndex> mergeConflicts(List<PartIndex> indexList) {
        if (indexList.size() < 2) {
            return indexList;
        }
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
                if (index.index < lastIndex) {
                    j++; //delete this index
                }
            } else {
                mergedIndexes.add(index);
                j++;
            }
        }

        return mergedIndexes;
    }

    private Optional<Part<E>> getLastElem(PartIndex prevIndex, String s) {
        if (prevIndex.type.getDirection().equals(PartType.Direction.AFTER)) return Optional.empty();
        int end = s.length();
        String part = createPartString(s, prevIndex.type, prevIndex.index, end);
        if (!part.isEmpty())
            return Optional.of(new Part<>(prevIndex.type, part));
        else return Optional.empty();
    }

    private String createPartString(String s, E type, int start, int end) {
        String substring = s.substring(start, end);
        substring = remove(substring, type.getValue(), type.getDirection());
        substring = substring.trim();
        return substring;
    }

    private String remove(String from, String what, PartType.Direction direction) {
        if (from.contains(what)) {
            int i = from.indexOf(what);
            if (direction.equals(PartType.Direction.BEFORE)) {
                return from.substring(0, i);
            }
            return from.substring(i + what.length());
        }
        return from;
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

    @Getter
    @RequiredArgsConstructor
    public static class Interval implements Comparable<Interval> {
        private final int x;
        private final int y;

        @Override
        public int compareTo(Interval o2) {
            return Integer.compare(x, o2.getX());
        }
    }

}
