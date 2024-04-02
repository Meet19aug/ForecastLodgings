package org.example;// Import necessary packages

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Define the class for spell checking using edit distance
public class EditDistanceSpellCheck {
    // Main method to execute spell checking
    public static void main(String[] args) {
// Define the target word for spell checking
        String userInput = "windsedadf";
        System.out.println("User Entered word is : " + userInput);

// Define the file path for the dictionary of words
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path dirpath = Paths.get(currentPath.toString(), "assets");
        String wordFilePath = dirpath.toString() + "/cityname.txt";

// Perform spell checking
        int result = performSpellCheck(userInput, wordFilePath);
        if (result != 0) {
            System.out.println("We suggested most 2 similar words now select one.");
        }
    }

    // Method to calculate theEdit_distances in  2 words
    public static int calculateEditDistance(String word1, String word2) {
// Initialize variables
        int length1 = word1.length();
        int length2 = word2.length();
        int[][] dp = new int[length1 + 1][length2 + 1];

// Populate the dynamic programming array
        for (int I_1 = 0; I_1 <= length1; I_1++) {
            dp[I_1][0] = I_1;
        }
        for (int J_2 = 0; J_2 <= length2; J_2++) {
            dp[0][J_2] = J_2;
        }
        for (int I_1 = 0; I_1 < length1; I_1++) {
            char char1 = word1.charAt(I_1);
            for (int J_2 = 0; J_2 < length2; J_2++) {
                char char2 = word2.charAt(J_2);
// Update the dp value based on the characters
                if (char1 == char2) {
                    dp[I_1 + 1][J_2 + 1] = dp[I_1][J_2];
                } else {
                    int replace = dp[I_1][J_2] + 1;
                    int insert = dp[I_1][J_2 + 1] + 1;
                    int delete = dp[I_1 + 1][J_2] + 1;
                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[I_1 + 1][J_2 + 1] = min;
                }
            }
        }
// Return the final edit distance
        return dp[length1][length2];
    }

    // Method to read words from a file and store them in a set
    public static Set<String> readWordsFromFileAndPutIntoSet(String filePath) {
// Initialize a set to store words
        Set<String> wordSet = new HashSet<>();
        try (BufferedReader Buuff_Reading = new BufferedReader(new FileReader(filePath))) {
            String line;
            Pattern pattern = Pattern.compile("[a-zA-Z]+"); // Regex pattern to match lowercase letters only
// Read each line from the file
            while ((line = Buuff_Reading.readLine()) != null) {
                Matcher Matcher_1 = pattern.matcher(line.toLowerCase()); // Convert line to lowercase before matching
// Find words in the line and add them to the set
                while (Matcher_1.find()) {
                    String word = Matcher_1.group();
                    wordSet.add(word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
// Return the set of words
        return wordSet;
    }

    // Method to perform spell checking and suggest similar words
    static int performSpellCheck(String targetWord, String fileName) {
        System.out.println("*** Do you mean one of the following words? ***");
// Read words from the file and store them in a set
        Set<String> wordSet = readWordsFromFileAndPutIntoSet(fileName);
// Initialize a map to store words and their edit distances from the target word
        Map<String, Integer> wordDistanceMap = new HashMap<>();
        Iterator<String> wordIterator = wordSet.iterator();
// Calculate edit distance for each word and store in the map
        for (int I_1 = 0; I_1 < wordSet.size(); I_1++) {
            String word = wordIterator.next();
            int distance = calculateEditDistance(word, targetWord);
            wordDistanceMap.put(word, distance);
        }
// Sort the map by edit distance
        Map<String, Integer> sortedWordDistanceMap = sortMapByValue(wordDistanceMap);
        int I_1 = 0;
// Iterate over the sorted map and print similar words
        for (Map.Entry<String, Integer> entry : sortedWordDistanceMap.entrySet()) {
            I_1++;
            if (I_1 > 2) {
                break;
            }
            if (I_1 == 0) {
                if (entry.getKey().equals(targetWord)) {
                    return 0;
                }
            }
            System.out.println(entry.getKey());
        }
        return 1;
    }

    // Method to sort a map by its values
    public static Map<String, Integer> sortMapByValue(Map<String, Integer> unsortedMap) {
// Convert the map to a list of entries
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortedMap.entrySet());
// Sort the list based on the values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
// Initialize a new map to store the sorted entries
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
// Iterate over the sorted list and put entries in the new map
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
// Return the sorted map
        return sortedMap;
    }
}