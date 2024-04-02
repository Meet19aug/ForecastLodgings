// Importing necessary packages
package org.example;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// Class for implementing a max heap from CSV data
public class MaxHeapFromCSV {

    // Inner class representing an entry in the heap
    static class HeapEntry {
        String word; // Word associated with the entry
        int value; // Value associated with the word

        // Constructor to initialize word and value
        public HeapEntry(String word, int value) {
            this.word = word;
            this.value = value;
        }
    }

    // Inner class representing the max heap
    static class MaxHeap {
        private List<HeapEntry> heap; // List to store heap entries

        // Constructor to initialize the heap
        public MaxHeap() {
            heap = new ArrayList<>(); // Initialize heap as an empty ArrayList
        }

        // Method to insert a new entry into the heap
        public void insert(String word, int value) {
            heap.add(new HeapEntry(word, value)); // Add a new entry to the heap
            heapifyUp(heap.size() - 1); // Heapify up to maintain the max heap property
        }

        // Method to delete the maximum element from the heap
        public HeapEntry deleteMax() {
            if (heap.isEmpty()) { // If heap is empty
                return null; // Return null
            }

            HeapEntry max = heap.get(0); // Get the maximum element
            heap.set(0, heap.get(heap.size() - 1)); // Replace the root with the last element
            heap.remove(heap.size() - 1); // Remove the last element
            heapifyDown(0); // Heapify down to maintain the max heap property

            return max; // Return the maximum element
        }

        // Method to heapify up (maintain the max heap property after insertion)
        private void heapifyUp(int index) {
            while (index > 0) { // While not at the root
                int parentIndex = (index - 1) / 2; // Calculate the parent index
                if (heap.get(parentIndex).value < heap.get(index).value) { // If parent value is less than child value
                    swap(parentIndex, index); // Swap parent and child
                    index = parentIndex; // Update index to parent
                } else {
                    break; // Break the loop if the max heap property is satisfied
                }
            }
        }

        // Method to heapify down (maintain the max heap property after deletion)
        private void heapifyDown(int index) {
            while (true) {
                int leftChild = 2 * index + 1; // Calculate the index of the left child
                int rightChild = 2 * index + 2; // Calculate the index of the right child
                int largest = index; // Initialize the largest element index as the current index

// Find the largest among parent, left child, and right child
                if (leftChild < heap.size() && heap.get(leftChild).value > heap.get(largest).value) {
                    largest = leftChild;
                }
                if (rightChild < heap.size() && heap.get(rightChild).value > heap.get(largest).value) {
                    largest = rightChild;
                }

// If the largest element is not the current index, swap and continue heapifying down
                if (largest != index) {
                    swap(index, largest); // Swap parent and largest child
                    index = largest; // Update index to largest child
                } else {
                    break; // Break the loop if the max heap property is satisfied
                }
            }
        }

        // Method to swap two elements in the heap
        private void swap(int i, int j) {
            HeapEntry temp = heap.get(i); // Store the element at index i in a temporary variable
            heap.set(i, heap.get(j)); // Set the element at index j to index i
            heap.set(j, temp); // Set the element at index j to the temporary variable
        }
    }

    // Main method
    public static void main(String[] args) {
        MaxHeap maxHeap = new MaxHeap(); // Create a new max heap

// Read CSV file
        Path currentPath = Paths.get(System.getProperty("user.dir")); // Get the current directory path
        Path dirpath = Paths.get(currentPath.toString(),"assets"); // Create the path to the assets directory

        String csvFile = dirpath.toString() +"/city_search_frequencies.csv"; // Path to the CSV file
        String line = ""; // Initialize the line variable
        String csvSplitBy = ","; // Define the CSV separator

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) { // Open the CSV file for reading
            while ((line = br.readLine()) != null) { // Read each line of the CSV file
                String[] data = line.split(csvSplitBy); // Split the line by the CSV separator
                String word = data[0].trim(); // Get the word from the data and trim whitespace
                int value = Integer.parseInt(data[1].replace("\"","").trim()); // Get the value from the data and parse it as an integer
                maxHeap.insert(word, value); // Insert the word and value into the max heap
            }
        } catch (IOException e) { // Catch IOException
            e.printStackTrace(); // Print the stack trace
        }

// Delete and print the maximum element
        HeapEntry maxEntry = maxHeap.deleteMax(); // Delete the maximum element from the max heap
        if (maxEntry != null) { // If the maximum element exists
            System.out.println("Max Element: " + maxEntry.word + " - " + maxEntry.value); // Print the maximum element
        } else { // If the heap is empty
            System.out.println("Heap is empty."); // Print a message indicating that the heap is empty
        }

// Delete and print the maximum element
        maxEntry = maxHeap.deleteMax(); // Delete the maximum element from the max heap
        if (maxEntry != null) { // If the maximum element exists
            System.out.println("Max Element: " + maxEntry.word + " - " + maxEntry.value); // Print the maximum element
        } else { // If the heap is empty
            System.out.println("Heap is empty."); // Print a message indicating that the heap is empty
        }
    }
}