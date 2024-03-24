package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageRanking {
    // Display the content of Most suitable Page
    public static String mostSuitablePageURL(Map<String,Integer> wordFrequencyMap){
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordFrequencyMap.entrySet());

        // Sort the list based on values using custom quicksort
        customQuickSort(entryList, 0, entryList.size() - 1);

        // Print the sorted entries
        for (Map.Entry<String, Integer> entry : entryList) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Find the maximum key after sorting
        String maxKey = entryList.isEmpty() ? "" : entryList.get(entryList.size() - 1).getKey();
        System.out.println("Key of maximum value after sorting: " + maxKey);
        return maxKey;
    }

    // Custom quicksort implementation
    public static void customQuickSort(List<Map.Entry<String, Integer>> list, int low, int high) {
        if (low < high) {
            int pivot = partition(list, low, high);
            customQuickSort(list, low, pivot - 1);
            customQuickSort(list, pivot + 1, high);
        }
    }

    public static int partition(List<Map.Entry<String, Integer>> list, int low, int high) {
        int pivotValue = list.get(high).getValue();
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j).getValue() < pivotValue) {
                i++;
                swap(list, i, j);
            }
        }
        swap(list, i + 1, high);
        return i + 1;
    }

    public static void swap(List<Map.Entry<String, Integer>> list, int i, int j) {
        Map.Entry<String, Integer> temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    public static void main(String[] args) {
        System.out.println("Page Ranking...");
        Map<String,Integer> freqOfWord = new HashMap<String, Integer>() {{
            put("booking.com", 27);
            put("kayak.com", 24);
            put("cheapflight.com", 36);
        }};
        mostSuitablePageURL(freqOfWord);
    }
}
