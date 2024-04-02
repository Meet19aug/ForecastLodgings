package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Class for counting the frequency of words in CSV files
public class FrequencyCountOfWord {

    // Method to count the frequency of a word in different CSV files
    public static Map<String,Integer> frequencyCountFunction(String searchTitle) {
// Read word frequencies from CSV files of different websites
        Map<String, Integer> wordFrequencyMapBooking = readDataFromCSV("booking.csv");
        Map<String, Integer> wordFrequencyMapKayak = readDataFromCSV("kayak.csv");
        Map<String, Integer> wordFrequencyMapCheapFlight = readDataFromCSV("cheapflights.csv");

// Get frequency of the search title in each website
        int frequencyInBooking = getFrequencyOfWord(wordFrequencyMapBooking, searchTitle);
        int frequencyInKayak = getFrequencyOfWord(wordFrequencyMapKayak, searchTitle);
        int frequencyInCheapFlight = getFrequencyOfWord(wordFrequencyMapCheapFlight, searchTitle);

// Display the frequency of the search title in each website
        System.out.println("Frequency of `" + searchTitle + "` in booking.com is " + frequencyInBooking + " .");
        System.out.println("Frequency of `" + searchTitle + "` in kayak.com is " + frequencyInKayak + " .");
        System.out.println("Frequency of `" + searchTitle + "` in cheapflight.com is " + frequencyInCheapFlight + " .");

// Create a map to store the frequencies for each website
        Map<String,Integer> results = new HashMap<String, Integer>() {{
            put("booking.com", frequencyInBooking);
            put("kayak.com", frequencyInKayak);
            put("cheapflights.com", frequencyInCheapFlight);
        }};
        return results; // Return the map containing frequencies for each website
    }

    // Method to read data from a CSV file and count word frequencies
    public static Map<String, Integer> readDataFromCSV(String filename) {
// Initialize a map to store word frequencies
        Map<String, Integer> wordFrequencyMap = new HashMap<>();

// Define a regex pattern to match words
        Pattern pattern = Pattern.compile("\\b[a-zA-Z]+\\b");

// Get the current directory path
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path dirpath = Paths.get(currentPath.toString(),"assets");

// Construct the file path
        filename = dirpath.toString() + "/" + filename;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
// Read each line from the file
            while ((line = reader.readLine()) != null) {
// Match words using regex pattern
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String word = matcher.group().toLowerCase(); // Convert word to lowercase
                    wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1); // Update word frequency
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle IOException
        }
        return wordFrequencyMap; // Return the map containing word frequencies
    }

    // Method to get the frequency of a word from the word frequency map
    public static int getFrequencyOfWord(Map<String, Integer> wordFrequencyMap, String word) {
        return wordFrequencyMap.getOrDefault(word.toLowerCase(), 0); // Return the frequency of the word
    }

    // Main method to demonstrate frequency count functionality
    public static void main(String[] args) {
// Call the frequencyCountFunction with a search title and get the frequency for cheapflights.com
        frequencyCountFunction("windsor").get("cheapflights.com");
    }
}