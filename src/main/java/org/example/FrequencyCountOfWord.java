package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Use Map
public class FrequencyCountOfWord {
    public static Map<String,Integer> frequencyCountFunction(String searchTitle) {
        Map<String, Integer> wordFrequencyMapBooking = readDataFromCSV("booking.csv");
        Map<String, Integer> wordFrequencyMapKayak = readDataFromCSV("kayak.csv");
        Map<String, Integer> wordFrequencyMapChaepFlight = readDataFromCSV("cheapflight.csv");

        int frequencyInBooking = getFrequencyOfWord(wordFrequencyMapBooking, searchTitle);
        int frequencyInKayak = getFrequencyOfWord(wordFrequencyMapKayak, searchTitle);
        int frequencyInCheapFlight = getFrequencyOfWord(wordFrequencyMapChaepFlight, searchTitle);

        System.out.println("Frequency of '" + searchTitle + " in booking.com is " + frequencyInBooking + " .");
        System.out.println("Frequency of '" + searchTitle + " in kayak.com is " + frequencyInKayak + " .");
        System.out.println("Frequency of '" + searchTitle + " in cheapflight.com is " + frequencyInCheapFlight + " .");
        Map<String,Integer> results = new HashMap<String, Integer>() {{
            put("booking.com", frequencyInBooking);
            put("kayak.com", frequencyInKayak);
            put("cheapflight.com", frequencyInCheapFlight);
        }};
        System.out.println(results);
        return results;
    }
    public static Map<String, Integer> readDataFromCSV(String filename) {
        Map<String, Integer> wordFrequencyMap = new HashMap<>();
        Pattern pattern = Pattern.compile("\\b[a-zA-Z]+\\b"); // Regex pattern to match words
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path dirpath = Paths.get(currentPath.toString(),"assets");

        filename = dirpath.toString() +"/"+filename;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String word = matcher.group().toLowerCase(); // Convert word to lowercase for case-insensitive comparison
                    wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordFrequencyMap;
    }
    public static int getFrequencyOfWord(Map<String, Integer> wordFrequencyMap, String word) {
        return wordFrequencyMap.getOrDefault(word.toLowerCase(), 0);
    }

    public static void main(String[] args) {
        System.out.println(frequencyCountFunction("windsor").get("cheapflight.com"));
    }
}
