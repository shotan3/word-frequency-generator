package org.example.data;

import java.util.Map;
import java.util.Set;

import static org.example.consts.Constants.VALID_WORDS_FILE;
import static org.example.consts.Constants.WORD_STATS_FILE;
import static org.example.file.FileUtils.*;

public final class TextParser {

    public static final Set<String> validWords;

    public static final Map<String, Integer> map;

    static {
        validWords = loadDataIntoSet(VALID_WORDS_FILE);
        map = loadDataIntoMap(WORD_STATS_FILE);
    }

    public static void analyzeText(String targetText) {
        String[] words = targetText.toLowerCase().replaceAll("[^a-zA-Z ]", "").split("\\s+");
        for (String word : words) {
            if (map.containsKey(word)) {
                map.put(word, map.get(word) + 1);
            } else {
                if (validWords.contains(word)) {
                    map.put(word, 1);
                }
            }
        }
        saveMapToFile(WORD_STATS_FILE, map);
    }
}
