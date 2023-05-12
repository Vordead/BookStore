package org.example.helper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Helpers {
    public static int countCommonWords(String str1, String str2) {
        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();

        String[] words1 = str1.split("\\s+");
        String[] words2 = str2.split("\\s+");

        Set<String> uniqueWords = new HashSet<>(Arrays.asList(words1));

        int commonWordCount = 0;
        for (String word : words2) {
            if (uniqueWords.contains(word)) {
                commonWordCount++;
            }
        }
        return commonWordCount;
    }
}