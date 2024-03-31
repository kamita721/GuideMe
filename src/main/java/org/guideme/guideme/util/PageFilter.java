package org.guideme.guideme.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.generated.model.Page;
import org.guideme.guideme.model.Guide;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class PageFilter {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Random random;
    private final Pattern rangePattern = Pattern.compile("\\((\\d+)\\.\\.(\\d+)\\)");

    public PageFilter() {
        random = new Random();
    }

    public List<String> getAllMatchingPages(String target, Guide guide, String chapter) {
        boolean usingAbsoluteMatch = false;
        Stream<Map.Entry<String, Page>> pageStream = guide.getChapters().get(chapter).getPages().entrySet().stream();
        LOGGER.debug("filtering {}", target);

        if (target.startsWith("regex:")) { // Handle a regex target
            LOGGER.debug("using regex filters");
            pageStream = filterRegex(target.substring(6), pageStream);
        } else if (rangePattern.asPredicate().test(target)) { // Filter ranges
            LOGGER.debug("using range filters");
            pageStream = filterRanges(target, pageStream);
        } else if (target.contains("*")) { // Filter wildcards
            LOGGER.debug("using wildcard filters");
            pageStream = filterRegex(convertWildcardsToRegex(target), pageStream);
        } else { // Absolute match
            LOGGER.debug("using absolute match");
            usingAbsoluteMatch = true;
            pageStream = pageStream.filter(m -> m.getKey().equals(target));
        }

        // If we aren't asking for an absolute patch, filter on canShow
        if (!usingAbsoluteMatch) {
            pageStream = pageStream.filter(m -> m.getValue().canShow(guide.getFlags()));
        }

        // Return list of page IDs
        List<String> results = pageStream.map(Map.Entry::getKey).toList();
        LOGGER.debug("filtered to {} possibilities", results.size());
        return results;
    }

    public String getSingleMatchingPage(String target, Guide guide, String chapter) {
        List<String> possibilities = getAllMatchingPages(target, guide, chapter);

        if (possibilities.isEmpty()) {
            LOGGER.warn("empty collection, returning null");
            return null;
        }

        if (possibilities.size() == 1) {
            String chosenPage = possibilities.get(0);
            LOGGER.debug("returning only item in page list: {}", chosenPage);
            return chosenPage;
        }

        String chosenPage = possibilities.get(random.nextInt(possibilities.size()));
        LOGGER.debug("choosing {} from {} possibilities", chosenPage, possibilities.size());

        return chosenPage;
    }

    private Stream<Map.Entry<String, Page>> filterRegex(String regex, Stream<Map.Entry<String, Page>> pageStream) {
        Pattern pattern;
        try {
            pattern = Pattern.compile(regex);
            return pageStream.filter(m -> pattern.matcher(m.getKey()).matches());
        } catch (Exception e) {
            LOGGER.error("failed to compile regex for pattern {}", regex);
            return pageStream.filter(m -> false); // Apply a filter that will fail everything
        }
    }

    private Stream<Map.Entry<String, Page>> filterRanges(String target, Stream<Map.Entry<String, Page>> pageStream) {
        LOGGER.debug("processing ranges on {}", target);

        HashSet<String> matchList = convertRangeToList(target,new HashSet<>());
        LOGGER.debug("ranges expanded to {} possibilities", matchList.size());

        return pageStream.filter(m -> matchList.contains(m.getKey()));
    }

    private HashSet<String> convertRangeToList(String target, HashSet<String> matchList) {
        Matcher matcher = rangePattern.matcher(target);

        if (matcher.find()) {
            String rangeString = matcher.group(0);
            LOGGER.debug("iterating range {} on {}", rangeString, target);

            int start = Integer.parseInt(matcher.group(1));
            int end = Integer.parseInt(matcher.group(2));

            String targetIterator;
            for (int i = start; i <= end; i++) {
                targetIterator = target.replaceFirst(Pattern.quote(rangeString), String.valueOf(i));
                matchList.addAll(convertRangeToList(targetIterator, matchList));
            }
        } else {
            matchList.add(target);
        }

        return matchList;
    }

    private String convertWildcardsToRegex(String target) {
        // First, regex quote the entire string
        target = Pattern.quote(target);

        // Next, replace * with the regex .*, and end/restart quoting
        target = target.replace("*", "\\E.+\\Q");

        // Finally remove any empty quote contexts
        target = target.replace("\\Q\\E", "");

        return target;
    }
}
