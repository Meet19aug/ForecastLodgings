package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;


public class CrawlerCheapFlight {

    public static void main(String[] args) {
        String checkInDate,checkOutDate,destination,numberOfPerson,numberOfRooms;
        destination="Windsor";
        checkInDate="2024-05-01";
        checkOutDate="2024-05-05";
        numberOfPerson="4";
        numberOfRooms="2";
        fetchDataFromCheapFlight(destination,checkInDate,checkOutDate,numberOfPerson,numberOfRooms);
    }

    static void fetchDataFromCheapFlight(String destination, String checkInDate, String checkOutDate, String numberOfPerson, String numberOfRooms) {

        System.out.println("Fetching Date for ChaepFlight.com.... Wait it takes some time");

        String url = "https://www.cheapflights.ca/hotels/" + destination + "/" + checkInDate + "/" + checkOutDate + "/" + numberOfPerson + "adults" +"/"+ numberOfRooms+"rooms"+"?sort=rank_a";

        String fullContainerXPath = "//div[@class=\"yuAt yuAt-pres-rounded\"]";

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);

        driver.get(url);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> listings = driver.findElements(By.xpath(fullContainerXPath));
        for(WebElement listing : listings){
            System.out.println(listing.getText());
        }

        if (listings.isEmpty()) {
            System.out.println("No listings found on the page.");
            return;
        }

        for (int i = 0; i < listings.size(); i++) {
            System.out.println("Inside loop");
            if(i>10){
                break;
            }

            WebElement listing = listings.get(i);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {

                String linkToHotel = listing.findElement(By.xpath(".//div[3]/div[2]/div[1]/div[1]/div[2]/a")).getAttribute("href");
                System.out.println("Link to Rooms: " + linkToHotel);
                linkToHotel = linkToHotel.trim();
                String titleOfHotel = listing.findElement(By.xpath(".//div[3]/div[2]/div[1]/div[1]/div[2]/a")).getText();
                System.out.println("titleOfHotel: " + titleOfHotel);
                titleOfHotel = titleOfHotel.trim();
                String priceOfHotel = listing.findElement(By.xpath(".//div[3]/div[3]/div/div/div[2]/div[1]")).getText();
                System.out.println("Price Of Hotel Excluding Tax is :" + priceOfHotel);
                if(priceOfHotel.equals("Before signing in")){
                    priceOfHotel = listing.findElement(By.xpath(".//div[3]/div[3]/div/div/div[2]/div[2]")).getText();
                    System.out.println("Price Of Hotel Excluding Tax is :" + priceOfHotel);
                }
                priceOfHotel = priceOfHotel.trim();
                String ratingOfHotel = listing.findElement(By.xpath(".//div[3]/div[2]/div[1]/div[2]/div[1]/div/div[1]")).getText();
                System.out.println("Rating Of Hotel is : " + ratingOfHotel);
                ratingOfHotel = ratingOfHotel.trim();
                String ratingInWords = listing.findElement(By.xpath(".//div[3]/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[1]")).getText();
                System.out.println("Ratings in words is : "+ ratingInWords);
                ratingInWords = ratingInWords.replaceAll("\n"," ").trim();
                priceOfHotel= HotelInfoClass.convertToNumeric(priceOfHotel);
                HotelInfoClass obj = new HotelInfoClass(titleOfHotel,linkToHotel,priceOfHotel,ratingOfHotel,ratingInWords);
                obj.saveObjectToCSV("cheapflights.csv");
            } catch (Exception e) {
                System.out.println("Error extracting data for listing " + i + ": " + e.getMessage());

            }
            finally {
                System.out.println("Crawling for one element done...");
            }
        }
        driver.quit();

    }

}
