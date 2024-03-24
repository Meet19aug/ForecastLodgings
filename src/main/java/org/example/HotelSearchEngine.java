package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

// Define HotelDataEntry class to represent hotel data
class HotelDataEntry {
    String hotelName;
    String city;
    double price;
    boolean available;

    public HotelDataEntry(String hotelName, String city, double price, boolean available) {
        this.hotelName = hotelName;
        this.city = city;
        this.price = price;
        this.available = available;
    }
}

// Define InvertedIndex class to build and manage the inverted index
class InvertedIndex {
    private Map<String, Set<Integer>> index;
    private Map<Integer, HotelDataEntry> hotelMap;

    public InvertedIndex() {
        this.hotelMap = new HashMap<>();
        this.index = new HashMap<>();
    }

    private String[] extractTerms(HotelDataEntry document) {
        // Extract hotelName, city, and price from the document
        String hotelName = document.hotelName.toLowerCase();
        String city = document.city.toLowerCase();
        String price = String.valueOf(document.price); // Convert price to string
        String available = String.valueOf(document.available); // Convert availability to string
        return new String[] { hotelName, city, price, available };
    }

    // Method to add a hotel to the index
    public void addDocument(int documentId, HotelDataEntry hotel) {
        String[] terms = extractTerms(hotel);

        for (var term : terms) {
            index.computeIfAbsent(term, k -> new HashSet<>()).add(documentId);
        }
        hotelMap.put(documentId, hotel);

    }

    // Method to delete a hotel from the index
    public void deleteDocument(int documentId) {
        if (!hotelMap.containsKey(documentId)) {
            System.out.println("Hotel with ID " + documentId + " not found.");
            return;
        }
        hotelMap.remove(documentId);
        for (Set<Integer> documentSet : index.values()) {
            documentSet.remove(documentId);
        }
        System.out.println("Hotel with ID " + documentId + " deleted successfully.");
    }
    // Method to update an existing hotel from the index

    public void updateDocument(int documentId, HotelDataEntry hotel) {
        if (!hotelMap.containsKey(documentId)) {
            System.out.println("Hotel with ID " + documentId + " not found.");
            return;
        }
        deleteDocument(documentId);
        addDocument(documentId, hotel);
        System.out.println("Hotel with ID " + documentId + " updated successfully.");
    }

    // Method to retrieve hotels containing a specific term
    public Set<Integer> getDocuments(String term) {
        return index.getOrDefault(term, Collections.emptySet());
    }

    public HotelDataEntry getHotelById(int hotelId) {
        return hotelMap.get(hotelId);
    }

    // Method to parse the search query and extract query terms

    public String[] parseQuery(String query) {
        // For simplicity, split the query string by spaces and return the terms
        return query.split("\\s+");
    }

    // Method to process queries and retrieve matching hotels
    public Set<Integer> search(String[] queryTerms) {
        Set<Integer> result = new HashSet<>();
        for (String term : queryTerms) {
            Set<Integer> termDocuments = getDocuments(term);
            if (termDocuments != null) {
                if (result.isEmpty()) {
                    result.addAll(termDocuments);
                } else {
                    result.retainAll(termDocuments);
                }
            }
        }
        return result;
    }

    public void showAllHotels() {
        System.out.println("All Hotels:");
        for (Map.Entry<Integer, HotelDataEntry> entry : hotelMap.entrySet()) {
            HotelDataEntry hotel = entry.getValue();
            System.out.println("ID: " + entry.getKey() + ", Name: " + hotel.hotelName + ", City: " + hotel.city
                    + ", Price: " + hotel.price + ", Availability: " + hotel.available);
        }
    }

}

// Main class for user interaction and hotel data analysis
public class HotelSearchEngine {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        InvertedIndex index = new InvertedIndex();

        // Populate the inverted index with sample hotel data

        // Menu-driven user interaction loop
        while (true) {
            // Display menu options
            System.out.println("\nSelect an option:");
            System.out.println("1. Add a hotel");
            System.out.println("2. Update a hotel");
            System.out.println("3. Delete a hotel");
            System.out.println("4. Search for hotels");
            System.out.println("5. List hotels");
            System.out.println("6. Exit");

            // Get user choice
            int choice = getUserChoice();

            // Perform action based on user choice
            switch (choice) {
                case 1:
                    addHotel(index);
                    break;
                case 2:
                    updateHotel(index);
                    break;
                case 3:
                    deleteHotel(index);
                    break;
                case 4:
                    searchHotels(index);
                    break;
                case 5:
                    listHotels(index);
                    return;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void addHotel(InvertedIndex index) {
        // Expected input: hotelId, hotelName, city, price
        System.out.println("Enter hotel details:");
        System.out.print("Hotel ID: ");
        int hotelId = scanner.nextInt();
        System.out.print("Hotel name: ");
        String hotelName = scanner.next();
        System.out.print("City: ");
        String city = scanner.next();
        System.out.print("Price: ");
        double price = scanner.nextDouble();
        System.out.print("Availablity: ");
        boolean available = scanner.nextBoolean();
        // Add the hotel to the index
        index.addDocument(hotelId, new HotelDataEntry(hotelName, city, price, available));
    }

    private static void updateHotel(InvertedIndex index) {
        // Expected input: hotelId, newHotelName, newCity, newPrice
        System.out.println("Enter updated hotel details:");
        System.out.print("Hotel ID: ");
        int hotelId = scanner.nextInt();
        System.out.print("New hotel name: ");
        String newHotelName = scanner.next();
        System.out.print("New city: ");
        String newCity = scanner.next();
        System.out.print("New price: ");
        double newPrice = scanner.nextDouble();
        System.out.print("New availablity: ");
        boolean newAvailable = scanner.nextBoolean();

        // Update the hotel in the index
        index.updateDocument(hotelId, new HotelDataEntry(newHotelName, newCity, newPrice, newAvailable));
    }

    private static void deleteHotel(InvertedIndex index) {
        // Expected input: hotelId
        System.out.print("Enter hotel ID to delete: ");
        int hotelId = scanner.nextInt();

        // Delete the hotel from the index
        index.deleteDocument(hotelId);
    }

    private static void listHotels(InvertedIndex index) {
        index.showAllHotels();
    }

    private static void searchHotels(InvertedIndex index) {
        // Example query: Find hotels in New York City with price range $100-$200
        System.out.println("Enter search query (e.g., 'New York City price:[100 TO 200]'): ");
        scanner.nextLine(); // Consume the newline character left by previous nextInt/nextDouble/nextBoolean
        String query = scanner.nextLine();
        // Process the query and retrieve matching document IDs
        String[] queryTerms = index.parseQuery(query);
        Set<Integer> documentIds = index.search(queryTerms);
        if (documentIds.isEmpty()) {
            System.out.println("No results found.");
            return;
        }
        List<HotelDataEntry> searchResults = new ArrayList<>();
        for (int documentId : documentIds) {
            // Retrieve hotel data entry for each document ID and add to searchResults

            HotelDataEntry hotel = index.getHotelById(documentId); // Implement this method
            if (hotel != null) {
                searchResults.add(hotel);
            }
        }
        // Display the search results
        System.out.println("Search Results:");
        for (HotelDataEntry hotel : searchResults) {
            System.out.println(hotel.hotelName + " - " + hotel.city + " - $" + hotel.price + " " + hotel.available);
        }
    }

}
