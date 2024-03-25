package org.example;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MaxHeapFromCSV {
    static class HeapEntry {
        String word;
        int value;

        public HeapEntry(String word, int value) {
            this.word = word;
            this.value = value;
        }
    }

    static class MaxHeap {
        private List<HeapEntry> heap;

        public MaxHeap() {
            heap = new ArrayList<>();
        }

        public void insert(String word, int value) {
            heap.add(new HeapEntry(word, value));
            heapifyUp(heap.size() - 1);
        }

        public HeapEntry deleteMax() {
            if (heap.isEmpty()) {
                return null;
            }

            HeapEntry max = heap.get(0);
            heap.set(0, heap.get(heap.size() - 1));
            heap.remove(heap.size() - 1);
            heapifyDown(0);

            return max;
        }

        private void heapifyUp(int index) {
            while (index > 0) {
                int parentIndex = (index - 1) / 2;
                if (heap.get(parentIndex).value < heap.get(index).value) {
                    swap(parentIndex, index);
                    index = parentIndex;
                } else {
                    break;
                }
            }
        }

        private void heapifyDown(int index) {
            while (true) {
                int leftChild = 2 * index + 1;
                int rightChild = 2 * index + 2;
                int largest = index;

                if (leftChild < heap.size() && heap.get(leftChild).value > heap.get(largest).value) {
                    largest = leftChild;
                }
                if (rightChild < heap.size() && heap.get(rightChild).value > heap.get(largest).value) {
                    largest = rightChild;
                }

                if (largest != index) {
                    swap(index, largest);
                    index = largest;
                } else {
                    break;
                }
            }
        }

        private void swap(int i, int j) {
            HeapEntry temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
    }

    public static void main(String[] args) {
        MaxHeap maxHeap = new MaxHeap();

        // Read CSV file
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path dirpath = Paths.get(currentPath.toString(),"assets");

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
        HeapEntry maxEntry = maxHeap.deleteMax();
        if (maxEntry != null) {
            System.out.println("Max Element: " + maxEntry.word + " - " + maxEntry.value);
        } else {
            System.out.println("Heap is empty.");
        }
        // Delete and print max element
        maxEntry = maxHeap.deleteMax();
        if (maxEntry != null) {
            System.out.println("Max Element: " + maxEntry.word + " - " + maxEntry.value);
        } else {
            System.out.println("Heap is empty.");
        }
    }
}
