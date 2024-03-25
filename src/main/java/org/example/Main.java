package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import static org.example.CrawlerBooking.fetchDataFromBooking;
import static org.example.CrawlerCheapFlight.fetchDataFromCheapFlight;
import static org.example.EditDistance.exe2a;
import static org.example.FrequencyCountOfWord.*;
import static org.example.HotelInfoClass.loadKObjectFromCSV;
import static org.example.CrawlerKayak.fetchDataFromKayak;
import static org.example.PageRanking.mostSuitablePageURL;

public class Main {
    private static void printingQueryValue(String searchTitle, String startDate, String endDate, int numRooms, int numPerson) {
        System.out.println("\nHotel Reservation Details:");
        System.out.println("City: " + searchTitle);
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        System.out.println("Number of Rooms: " + numRooms);
        System.out.println("Number of Person: " + numPerson);
    }

    private static void menu(List<String> filePaths,Path dirpath) {
        int kth=2;
        System.out.println("Welcome to Hotel Reservation System!");

        List<Map<String, String>> products = FetchDataFromExcel.readData(filePaths);
        DSTrie trie = new DSTrie();
        for (Map<String, String> product : products) {
            String title = product.get("City");
            String[] titleWords = title.split("\\s+");
            for (String titleWord : titleWords) {
                // For simplicity, remove non-alphabetic characters
                titleWord = titleWord.replaceAll("[^a-zA-Z]", "").toLowerCase();
                if (!titleWord.isEmpty()) {
                    trie.wordInsert(titleWord);
                }
            }
        }

        Scanner scanner = new Scanner(System.in);

        boolean menu = true;
        String searchTitle = "";
        System.out.println("Most Search Cities are: ");
        printMostSearchCities(dirpath,kth);
        while (menu) {
            System.out.println("Enter \"exit\" to exit.");
            do {
                System.out.print("Enter the city to search for: ");
                searchTitle = scanner.nextLine();
            } while (!searchTitle.matches("[a-z]+"));
            if ("exit".equalsIgnoreCase(searchTitle)) {
                System.out.println("Exiting..");
                menu = false;
                return;
            }
            if (!searchTitle.isEmpty()) {
                int x = trie.suggestWords(searchTitle, searchTitle);
                // returns 1 if no suggesion word found. so check for most 2 similar word
                // return 0 if it found the word, and 3 if found the correct word.
                if (x == 3) {
                    System.out.println("---------------- > May your " + searchTitle + " trip remain memorable.  <----------------");
                    menu = false;
                }
                if (x == 1) {
                    String filepathForCities = dirpath.toString() + "/cityname.txt";
                    int y = exe2a(searchTitle, filepathForCities);
                    if (y != 0) {
                        System.out.println("We suggested most 2 similar words now select one.");

                    }
                }
            }
        }

        // Validating start date input
        String startDate;
        do {
            System.out.print("Enter start date (YYYY-MM-DD): ");
            startDate = scanner.nextLine().trim();
            if (!startDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("Invalid date format! Please use YYYY-MM-DD.");
                continue;
            }
            LocalDate currentDate = LocalDate.now();
            LocalDate inputStartDate = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
            if (inputStartDate.isBefore(currentDate)) {
                System.out.println("Start date cannot be before current date!");
                continue;
            }
            break; // Break added to exit loop when validation passes
        } while (true);

        // Validating end date input
        String endDate;
        do {
            System.out.print("Enter end date (YYYY-MM-DD): ");
            endDate = scanner.nextLine().trim();
            if (!endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("Invalid date format! Please use YYYY-MM-DD.");
                continue;
            }
            LocalDate inputStartDate = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate inputEndDate = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
            if (inputEndDate.isBefore(inputStartDate)) {
                System.out.println("End date cannot be before start date!");
                continue;
            }
            break;
        } while (true);

        // Validating number of rooms input
        int numRooms;
        do {
            System.out.print("Enter number of rooms: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number for rooms.");
                System.out.print("Enter number of rooms: ");
                scanner.next();
            }
            numRooms = scanner.nextInt();
        } while (numRooms <= 0);

        // Validating number of adults input
        int numPerson;
        do {
            System.out.print("Enter number of adults: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number for adults.");
                System.out.print("Enter number of adults: ");
                scanner.next();
            }
            numPerson = scanner.nextInt();
        } while (numPerson <= 0);

        // Printing the entered information
        printingQueryValue(searchTitle,startDate,endDate,numRooms,numPerson);


        System.out.println("Crawling data for you requirements: ");
        fetchDataFromCheapFlight(searchTitle,startDate,endDate,Integer.toString(numPerson),Integer.toString(numRooms));
        fetchDataFromKayak(searchTitle,startDate,endDate,Integer.toString(numPerson),Integer.toString(numRooms));
        fetchDataFromBooking("https://www.booking.com/",searchTitle,startDate,endDate,Integer.toString(numRooms),Integer.toString(numPerson));

        System.out.println("Printing how much much times your search city is appeared in our crawling of data. ");
        Map<String, Integer> wordAppearenceFrequency = frequencyCountFunction(searchTitle);

        // DONE: Use Frequency count to rank the pages.
        // Rank the page on the based of sorting. - The entryList.sort(Map.Entry.comparingByKey()) statement uses a stable sorting algorithm, which typically is a variation of merge sort or a related algorithm.
        // In Java, the Collections.sort() method, which entryList.sort() is effectively calling, uses a modified version of merge sort called TimSort
        String bestUrl = mostSuitablePageURL(wordAppearenceFrequency);


        String emailOfUser ;
        do {
            System.out.print("Enter Your Email so we can send you bestd deals: ");
            emailOfUser = scanner.nextLine();
            if (!(isValidEmail(emailOfUser))) {
                System.out.println("Invalid email.");
            }
            else{
                int kload = 5;
                bestUrl = bestUrl.replace("com", "csv");
                HotelInfoClass[] hi = loadKObjectFromCSV(bestUrl, kload);

                try {
                    //JavaMailUtil.txt.sendMail(emailOfUser,hi,kload);
                } catch (Exception e) {
                    System.out.println("An error occurred while sending the email: " + e.getMessage());
                }finally {
                    break;
                }
            }
        } while (true);

        // TODO: Inverted Indexing Implementation.

        // Closing the scanner
        scanner.close();

    }
    public static boolean isValidEmail(String email) {
        final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        final Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(email).matches();
    }

    private static void printMostSearchCities(Path dirpath,int k) {
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
        for (int i = 0; i < k; i++) {
            MaxHeapFromCSV.HeapEntry maxEntry = maxHeap.deleteMax();
            if (maxEntry != null) {
                System.out.println(maxEntry.word );
            } else {
                System.out.println("Heap is empty.");
            }
        }
    }

    public static void main(String[] args) {


        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path dirpath = Paths.get(currentPath.toString(),"assets");

        String filepathForBook1 = dirpath.toString() +"/Book1.xlsx";
        System.out.println(filepathForBook1);



        List<String> filePaths = new ArrayList<>();
        filePaths.add(filepathForBook1);

        menu(filePaths,dirpath);




    }


}
