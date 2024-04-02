package org.example;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrendingSearchFunctionality {
    public static void printMostSearchCities(Path dirpath, int k) {
        MaxHeapFromCSV.MaxHeap maxHeap = new MaxHeapFromCSV.MaxHeap();
        String csvFile = dirpath.toString() +"/city_search_frequencies.csv";
        String line = "";
        String csvSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                String word = data[0].trim();
                int value = Integer.parseInt(data[1].replace("\"","").trim());
                maxHeap.insert(word, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Delete and print max element
        System.out.println("********* Most Searched Destinations *********");
        for (int i = 0; i < k; i++) {
            MaxHeapFromCSV.HeapEntry maxEntry = maxHeap.deleteMax();
            if (maxEntry != null) {
                System.out.println(maxEntry.word+ " - ("+ maxEntry.value + ")");
            } else {
                System.out.println("Heap is empty.");
            }
        }
    }
    public static void updateFrequency(String csvFile, String word) {
        boolean updated = false;
        List<String[]> rows = new ArrayList<>();
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path dirpath = Paths.get(currentPath.toString(),"assets");
        csvFile = dirpath+"/"+csvFile;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String city = parts[0].trim().replaceAll("\"", ""); // remove quotes and trim
                    String frequency = parts[1].trim().replaceAll("\"", ""); // remove quotes and trim

                    if (city.equalsIgnoreCase(word)) {
                        int newFrequency = Integer.parseInt(frequency) + 1;
                        rows.add(new String[]{city, String.valueOf(newFrequency)});
                        updated = true;
                    } else {
                        rows.add(new String[]{city, frequency});
                    }
                }
            }

            if (!updated) {
                rows.add(new String[]{word, "1"});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            for (String[] row : rows) {
                bw.write(String.join(",", Arrays.asList(row)));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path dirpath = Paths.get(currentPath.toString(),"assets");
        printMostSearchCities(dirpath,3);

    }
}
