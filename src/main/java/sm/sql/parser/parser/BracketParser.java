package sm.sql.parser.parser;

import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class BracketParser implements Parser<List<String>> {

    @Override
    public Optional<List<String>> parse(String part) {
        //find intervals
        List<PartParser.Interval> intervals = getIntervals(part);
        //cut strings
        return Optional.of(cutWithIntervals(part, intervals));
    }

    private List<PartParser.Interval> getIntervals(String s) {
        char openBracket = '('; //todo to enum?
        char closeBracket = ')';

        List<PartParser.Interval> intervals = new ArrayList<>();
        intervals.addAll(findIntervals(s, openBracket, closeBracket));

        return intervals;
    }

    private List<PartParser.Interval> findIntervals(String s, char open, char close) {
        List<PartParser.Interval> intervals = new ArrayList<>();
        LinkedList<Integer> openedBracket = new LinkedList<>();
        int i = s.indexOf(open);
        while (i != -1) {
            openedBracket.add(i);
            i = s.indexOf(open, i + 1);
        }
        List<Integer> closedBracket = new ArrayList<>();
        i = s.indexOf(close);
        while (i != -1) {
            closedBracket.add(i);
            i = s.indexOf(close, i + 1);
        }

        for (int c = 0; c < closedBracket.size(); c++) {
            boolean find = false;
            int closed = closedBracket.get(c);
            ListIterator<Integer> iterator = openedBracket.listIterator();
            Integer opened = iterator.next();
            while (iterator.hasNext()) {
                Integer previous = opened;
                opened = iterator.next();
                if (opened > closed) {
                    intervals.add(new PartParser.Interval(previous, closed));
                    find = true;
                    openedBracket.remove(previous);
                    break;
                }
            }
            if (!find) {
                intervals.add(new PartParser.Interval(openedBracket.getLast(), closed));
                openedBracket.removeLast();
            }
        }

        return intervals;
    }

    private List<String> cutWithIntervals(String s, List<PartParser.Interval> intervals) {
        List<String> parts = new ArrayList<>();
        for (PartParser.Interval interval : intervals) {
            parts.add(s.substring(interval.getX() + 1, interval.getY()));
        }
        return parts;
    }

}
