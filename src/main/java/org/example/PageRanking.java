package org.example;

// Import necessary libraries
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Class for calculating page ranking based on word frequency map
public class PageRanking {
    // Display the content of the most suitable page URL
    public static String mostSuitablePageURL(Map<String, Integer> wordFrequencyMap) {
// Convert the word frequency map to a list of entries
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordFrequencyMap.entrySet());

// Sort the list based on values using custom quicksort
        customQuickSort(entryList, 0, entryList.size() - 1);

// Print the sorted entries
        /*for (Map.Entry<String, Integer> entry : entryList) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }*/

// Find the maximum key after sorting
        String maxKey = entryList.isEmpty() ? "" : entryList.get(entryList.size() - 1).getKey();
        System.out.println("*** Highest Rank page is ***");
        System.out.println(maxKey);
        return maxKey;
    }

    // Custom quicksort implementation
    public static void customQuickSort(List<Map.Entry<String, Integer>> list, int low, int high) {
// Check if there are elements to sort
        if (low < high) {
// Partition the list
            int pivot = partition(list, low, high);
// Recursively sort the partitions
            customQuickSort(list, low, pivot - 1);
            customQuickSort(list, pivot + 1, high);
        }
    }

    // Partition method for quicksort
    public static int partition(List<Map.Entry<String, Integer>> list, int low, int high) {
// Set pivot value
        int pivotValue = list.get(high).getValue();
        int i = low - 1;
// Iterate through the list
        for (int j = low; j < high; j++) {
// Check if current value is less than pivot
            if (list.get(j).getValue() < pivotValue) {
// Swap elements if condition is met
                i++;
                swap(list, i, j);
            }
        }
// Swap pivot with element at i + 1
        swap(list, i + 1, high);
        return i + 1;
    }

    // Method to swap elements in a list
    public static void swap(List<Map.Entry<String, Integer>> list, int i, int j) {
// Temporary variable to hold element during swap
        Map.Entry<String, Integer> temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    // Main method to test the functions
    public static void main(String[] args) {
        System.out.println("Page Ranking...");
// Sample word frequency map
        Map<String, Integer> freqOfWord = new HashMap<String, Integer>() {{
            put("booking.com", 27);
            put("kayak.com", 24);
            put("cheapflight.com", 36);
        }};
// Calculate and print the most suitable page URL
        mostSuitablePageURL(freqOfWord);
    }
}