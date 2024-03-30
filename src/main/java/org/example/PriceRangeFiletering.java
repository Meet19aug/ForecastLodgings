package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// Define HotelDataEntry class to represent hotel data
class HotelDataEntry {
    String hotelName;
    String city;
    double price;
    double rating;
    String review;
    String url;

    public HotelDataEntry(String hotelName, String city, double price, double rating, String review, String url) {
        this.hotelName = hotelName;
        this.city = city;
        this.price = price;
        this.rating = rating;
        this.review = review;
        this.url = url;
    }
}

// Define InvertedIndex class to build and manage the inverted index
class InvertedIndex {
    private final Map<String, Set<Integer>> index;
    private final Map<Integer, HotelDataEntry> hotelMap;

    public InvertedIndex() {
        this.hotelMap = new HashMap<>();
        this.index = new HashMap<>();
    }

    private String[] extractTerms(HotelDataEntry document) {
        // Extract hotelName, city, price, rating, review, and url from the document
        String hotelName = document.hotelName.toLowerCase();
        String city = document.city.toLowerCase();
        String price = String.valueOf(document.price); // Convert price to string
        String rating = String.valueOf(document.rating); // Convert rating to string
        String review = document.review.toLowerCase(); // Convert review to lowercase string
        return new String[]{hotelName, city, price, rating, review};
    }

    // Method to add a hotel to the index
    public void addDocument(int documentId, HotelDataEntry hotel) {
        String[] terms = extractTerms(hotel);

        for (var term : terms) {
            index.computeIfAbsent(term, k -> new HashSet<>()).add(documentId);
        }
        hotelMap.put(documentId, hotel);
    }

    // Method to filter hotels based on price range
    // Method to filter hotels based on price range
    // Method to filter hotels based on price range
    public Set<Integer> filterByPriceRange(double minPrice, double maxPrice) {
        Set<Integer> result = new HashSet<>();
        for (Map.Entry<Integer, HotelDataEntry> entry : hotelMap.entrySet()) {
            HotelDataEntry hotel = entry.getValue();
            if (hotel.price >= minPrice && hotel.price <= maxPrice) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public HotelDataEntry getHotelById(int hotelId) {
        return hotelMap.get(hotelId);
    }
}

// Main class for user interaction and hotel data analysis
public class PriceRangeFiletering {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        InvertedIndex index = new InvertedIndex();

        // Populate the inverted index with hotel data from CSV file
        loadKObjectFromCSV(index, "kayak.csv");

        // Menu-driven user interaction loop
        while (true) {
            // Display menu options
            System.out.println("\nSelect an option:");
            System.out.println("1. Filter hotels by price range");
            System.out.println("2. Exit");

            // Get user choice
            int choice = getUserChoice();

            // Perform action based on user choice
            switch (choice) {
                case 1:
                    // Perform price range filtering
                    filterHotelsByPriceRange(index);
                    break;
                case 2:
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

    // Method to load hotel data from CSV file and populate the inverted index
    private static void loadKObjectFromCSV(InvertedIndex index, String csvFileName) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path dirpath = Paths.get(currentPath.toString(),"assets");

        csvFileName = dirpath.toString() +"/"+csvFileName;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFileName))) {
            String line;
            int id = 1; // Starting ID for hotels
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) { // Ensure data has at least six parts
                    String hotelName = data[0].replace("\"", "").trim();
                    String url = data[1].replace("\"", "").trim();
                    String city = extractCity(data[2]); // Extract city from the provided format
                    double rating = parsePrice(data[3]);
                    double price = parseRating(data[4].trim());
                    System.out.println(price+"--\t\t--"+data[5]);
                    String review = parseReview(data[5]);
                    if (price != -1) { // Check if price is valid
                        index.addDocument(id++, new HotelDataEntry(hotelName, city, price, rating, review, url));
                    } else {
                        System.out.println("Invalid price format: " + data[3]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Method to extract the city name from the provided format
    private static String extractCity(String data) {
        // Extract city name from the provided format
        int index = data.indexOf('-');
        if (index != -1) {
            return data.substring(0, index).trim();
        }
        return data.trim(); // Return the original string if "-" is not found
    }

    private static double parsePrice(String priceStr) {
        // Remove non-numeric characters except for decimal point
        String cleanPriceStr = priceStr.replaceAll("[^\\d.]", "");
        // Parse the cleaned string into a double
        try {
            return Double.parseDouble(cleanPriceStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format: " + priceStr);
            return -1; // Return -1 to indicate invalid price
        }
    }

    private static double parseRating(String ratingStr) {
        // Remove leading and trailing double quotes if present
        ratingStr = ratingStr.replaceAll("^\"|\"$", "");
        return Double.parseDouble(ratingStr);
    }

    private static String parseReview(String reviewStr) {
        return reviewStr.replaceAll("[()]", "").trim();
    }

    private static void filterHotelsByPriceRange(InvertedIndex index) {
        System.out.print("Enter minimum price: ");
        double minPrice = scanner.nextDouble();
        System.out.print("Enter maximum price: ");
        double maxPrice = scanner.nextDouble();

        // Filter hotels by price range
        Set<Integer> filteredHotels = index.filterByPriceRange(minPrice, maxPrice);

        if (filteredHotels.isEmpty()) {
            System.out.println("No hotels found within the specified price range.");
            return;
        }

        // Display the search results
        System.out.println("Filtered Hotels:");
        for (int hotelId : filteredHotels) {
            HotelDataEntry hotel = index.getHotelById(hotelId);
            if (hotel != null) {
                System.out.println("Name: " + hotel.hotelName + ", \nCity: " + hotel.city
                        + ", \nPrice: " + hotel.price + ", \nRating: " + hotel.rating + ", \nReview: " + hotel.review + ", \nURL: " + hotel.url);
                System.out.println("********************************************");
            }
        }
    }
}

