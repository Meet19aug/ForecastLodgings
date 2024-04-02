// Import necessary packages
package org.example;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
// Define the class for crawling data from Kayak website

// Define the class for crawling data from Kayak website
public class CrawlerKayak {
    // Main method to initiate data fetching

    // Main method to initiate data fetching
    public static void main(String[] args) {

        // Define parameters for hotel search
        String checkInDate,checkOutDate,destination,numberOfPerson,numberOfRooms;
        destination="Windsor";
        checkInDate="2024-05-01";
        checkOutDate="2024-05-05";
        numberOfPerson="4";
        numberOfRooms="2";
        // Fetch data from Kayak website
        CrawlerKayak.fetchDataFromKayak(destination,checkInDate,checkOutDate,numberOfPerson,numberOfRooms);
    }



    // Method to fetch hotel data from Kayak website
    public static void fetchDataFromKayak(String destination, String checkInDate, String checkOutDate, String numberOfPerson, String numberOfRooms) {
// Display message indicating data fetching process initiation
        System.out.println("Fetching Data for Kayak.com.... Please wait, it may take some time.");
// Construct the URL for hotel search on Kayak
        String url = "https://www.ca.kayak.com/hotels/" + destination + "/" + checkInDate + "/" + checkOutDate + "/" + numberOfPerson + "adults" + "/" + numberOfRooms + "rooms" + "?sort=rank_a";
// Print the constructed URL for reference
        System.out.println(url);

// Define XPath for full container element
        String fullContainerXPath = "//div[@class=\"yuAt yuAt-pres-rounded\"]";

// Setup WebDriver using WebDriverManager for Chrome
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);

// Navigate to the specified URL
        driver.get(url);
        Actions action = new Actions(driver);

        try {
// Wait for page to load
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

// Scroll down the page using Page Down key
        action.sendKeys(Keys.PAGE_DOWN).build().perform();

        try {
// Wait for page to load
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

// Scroll down the page again
        action.sendKeys(Keys.PAGE_DOWN).build().perform();

        try {
// Wait for page to load
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

// Find all hotel listings on the page
        List<WebElement> listings = driver.findElements(By.xpath(fullContainerXPath));

// Check if listings are found
        if (listings.isEmpty()) {
            System.out.println("No listings found on the page.");
            return;
        }

// Iterate through each listing to extract data
        for (int i = 0; i < listings.size(); i++) {
            //System.out.println("Inside loop");


// Get the current listing
            WebElement listing = listings.get(i);

            try {
// Pause for a moment to ensure elements are loaded

                Thread.sleep(100);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
// Extract data from the listing elements
                String linkToHotel = listing.findElement(By.xpath(".//div[3]/div[2]/div[1]/div[1]/div[2]/a")).getAttribute("href");
                //System.out.println("Link to Rooms: " + linkToHotel);
                linkToHotel = linkToHotel.trim();
                String titleOfHotel = listing.findElement(By.xpath(".//div[3]/div[2]/div[1]/div[1]/div[2]/a")).getText();

                //System.out.println("Title of Hotel: " + titleOfHotel);
                titleOfHotel = titleOfHotel.trim();
                String priceOfHotel = listing.findElement(By.xpath(".//div[3]/div[3]/div/div/div[2]/div[1]")).getText();
                //System.out.println("Price Of Hotel Excluding Tax is :" + priceOfHotel);

                if (priceOfHotel.equals("Before signing in")) {
                    priceOfHotel = listing.findElement(By.xpath(".//div[3]/div[3]/div/div/div[2]/div[2]")).getText();
                    //System.out.println("Price Of Hotel Excluding Tax is :" + priceOfHotel);
                }
                priceOfHotel = priceOfHotel.trim();
                String ratingOfHotel = listing.findElement(By.xpath(".//div[3]/div[2]/div[1]/div[2]/div[1]/div/div[1]")).getText();
                //System.out.println("Rating Of Hotel is : " + ratingOfHotel);
                ratingOfHotel = ratingOfHotel.trim();
                String ratingInWords = listing.findElement(By.xpath(".//div[3]/div[2]/div[1]/div[2]/div[1]/div/div[2]")).getText();
                ratingInWords = ratingInWords.replaceAll("\n", " ").trim();
                priceOfHotel = HotelInfoClass.convertToNumeric(priceOfHotel);

                //System.out.println("New Price is : " + priceOfHotel);
                //System.out.println("Ratings in words is : " + ratingInWords);


// Create HotelInfoClass object and save data to CSV
                HotelInfoClass obj = new HotelInfoClass(titleOfHotel, linkToHotel, priceOfHotel, ratingOfHotel, ratingInWords);
                obj.saveObjectToCSV("kayak.csv");

            } catch (Exception e) {
// Handle any other elements not found exception for this listing

                //System.out.println("Error extracting data for listing " + i + ": " + e.getMessage());
            } finally {
// Print message indicating completion of crawling for one element
                //System.out.println("Crawling for one element done...");
            }
        }
// Quit the WebDriver instance
        System.out.println("Crawling completed for Cheapflights.com..");

        driver.quit();

    }
}