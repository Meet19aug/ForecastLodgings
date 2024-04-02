package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Class to find a specific pattern in a file
public class FindPatternInFile {
    // Function to read file line by line
    public static void readFile(String filename, String regex) {

        System.out.println("RegEx " + regex +" found on following information or Data : ");

// Get the current directory path
        Path currentPath = Paths.get(System.getProperty("user.dir"));
// Specify the directory path for the assets folder
        Path dirpath = Paths.get(currentPath.toString(), "assets");

// Concatenate the directory path and filename to get the complete file path
        filename = dirpath.toString() + "/" + filename;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
// Read each line from the file
            while ((line = br.readLine()) != null) {
// Find the pattern in the line and print if found
                String result = findPattern(line, regex);
                if (result != null) {
                    System.out.println(result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to find pattern in a line using regex
    public static String findPattern(String line, String regex) {
// Compile the regex pattern with case insensitivity
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
// Create a matcher to find the pattern in the line
        Matcher matcher = pattern.matcher(line);
// If the pattern is found in the line, return the line
        if (matcher.find()) {
            return line;
        }
        return null; // Return null if pattern is not found in the line
    }

    // Main method to test the functions
    public static void main(String[] args) {
// Specify the input file name
        String fileName = "cheapflights.csv";
// Specify the regex pattern to search for
        String regex = "https://[^\\s]*jBCEG-Ty9A&pm[^\\s]*";
// Print the description of the search
        System.out.println("Word `" + regex + "` appearance found using regex in crawled data of " + fileName.replace(".csv", ""));
// Call the function to read the file and find the pattern
        readFile(fileName, regex);
    }
}