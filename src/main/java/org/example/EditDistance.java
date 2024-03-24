package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditDistance {
    public static int editDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();

        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        for (int i = 0; i < len1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = word2.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[len1][len2];
    }
    public static Set<String> readFileAndPutWordsIntoSet(String filePath_mp) {
        Set<String> wordsSet_mp = new HashSet<>();

        try (BufferedReader reader_mp = new BufferedReader(new FileReader(filePath_mp))) {
            String line_mp;
            Pattern pattern_mp = Pattern.compile("[a-zA-Z]+"); // Regex pattern to match lowercase letters only

            while ((line_mp = reader_mp.readLine()) != null) {
                Matcher matcher_mp = pattern_mp.matcher(line_mp.toLowerCase()); // Convert line to lowercase before matching
                while (matcher_mp.find()) {
                    String word_mp = matcher_mp.group();
                    wordsSet_mp.add(word_mp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wordsSet_mp;
    }

    static int exe2a(String targetWord_mp, String fileName_mp) {
        System.out.println("Do you mean one of the following words? ");
        Set<String> wordSet_mp = readFileAndPutWordsIntoSet(fileName_mp);

        Map<String, Integer> wordIntegerMap_mp = new HashMap<>();
        Iterator<String> wordIterator_mp = wordSet_mp.iterator();
        for (int i_mp = 0; i_mp < wordSet_mp.size() ; i_mp++) {
            String word_mp = wordIterator_mp.next();
            int distance_mp = editDistance(word_mp, targetWord_mp);
            wordIntegerMap_mp.put(word_mp, distance_mp);
        }
        Map<String, Integer> sortedWordIntegerMap_mp = sortByValue(wordIntegerMap_mp);
        int i_mp=0;
        for (Map.Entry<String, Integer> entry_mp : sortedWordIntegerMap_mp.entrySet()) {
            i_mp++;
            if(i_mp>2){
                break;
            }
            if(i_mp==0){
                if(entry_mp.getKey()==targetWord_mp){
                    return 0;
                }
            }
            System.out.println(entry_mp.getKey());
        }
        return 1;
    }

    public static Map<String, Integer> sortByValue(Map<String, Integer> unsortedMap_mp) {
        List<Map.Entry<String, Integer>> list_mp = new LinkedList<>(unsortedMap_mp.entrySet());

        Collections.sort(list_mp, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        Map<String, Integer> sortedMap_mp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry_mp : list_mp) {
            sortedMap_mp.put(entry_mp.getKey(), entry_mp.getValue());
        }

        return sortedMap_mp;
    }
}
