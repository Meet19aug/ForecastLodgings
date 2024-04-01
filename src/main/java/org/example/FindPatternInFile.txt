package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindPatternInFile {
    // Function to read file line by line
    public static void readFile(String filename,String regex) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path dirpath = Paths.get(currentPath.toString(),"assets");

        filename = dirpath.toString() +"/"+filename;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String result = findPattern(line,regex);
                if (result != null) {
                    System.out.println(result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to find pattern in a line using regex
    public static String findPattern(String line,String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return line;
        }
        return null;
    }

    // Main method to test the functions
    public static void main(String[] args) {

        String fileName = "cheapflights.csv"; // Specify the path to your input file
        String regex = "poor";
        readFile(fileName,regex);
    }
}
