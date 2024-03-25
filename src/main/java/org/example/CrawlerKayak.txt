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


public class CrawlerKayak {

    public static void main(String[] args) {
        String checkInDate,checkOutDate,destination,numberOfPerson,numberOfRooms;
        destination="Windsor";
        checkInDate="2024-05-01";
        checkOutDate="2024-05-05";
        numberOfPerson="4";
        numberOfRooms="2";
        CrawlerKayak.fetchDataFromKayak(destination,checkInDate,checkOutDate,numberOfPerson,numberOfRooms);
    }

    public static void fetchDataFromKayak(String destination, String checkInDate, String checkOutDate, String numberOfPerson, String numberOfRooms) {
        System.out.println("Fetching Date for Kayak.com.... Wait it takes some time");
        String url = "https://www.ca.kayak.com/hotels/" + destination + "/" + checkInDate + "/" + checkOutDate + "/" + numberOfPerson + "adults" +"/"+ numberOfRooms+"rooms"+"?sort=rank_a";
        System.out.println(url);

        String fullContainerXPath = "//div[@class=\"yuAt yuAt-pres-rounded\"]";

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);

        driver.get(url);
        Actions action = new Actions(driver);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        action.sendKeys(Keys.PAGE_DOWN).build().perform();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        action.sendKeys(Keys.PAGE_DOWN).build().perform();

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<WebElement> listings = driver.findElements(By.xpath(fullContainerXPath));

        if (listings.isEmpty()) {
            System.out.println("No listings found on the page.");
            return;
        }

        for (int i = 0; i < listings.size(); i++) {
            System.out.println("Inside loop");


            WebElement listing = listings.get(i);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                // Extract data from the listing as strings

                String linkToHotel = listing.findElement(By.xpath(".//div[3]/div[2]/div[1]/div[1]/div[2]/a")).getAttribute("href");
                System.out.println("Link to Rooms: " + linkToHotel);

                String titleOfHotel = listing.findElement(By.xpath(".//div[3]/div[2]/div[1]/div[1]/div[2]/a")).getText();
                System.out.println("titleOfHotel: " + titleOfHotel);

                String priceOfHotel = listing.findElement(By.xpath("//div[@data-target=\"price\"]")).getText();
                System.out.println("Price Of Hotel Excluding Tax is :" + priceOfHotel);
                String ratingOfHotel = listing.findElement(By.xpath(".//div[3]/div[2]/div[1]/div/div[4]/div/div[1]/div[1]")).getText();
                System.out.println("Rating Of Hotel is : " + ratingOfHotel);
                String ratingInWords = listing.findElement(By.xpath(".//div[3]/div[2]/div[1]/div/div[4]/div/div[1]/div[2]")).getText();
                //.//div[3]/div[2]/div[1]/div/div[4]/div/div[1]/div[2]

                System.out.println("Ratings in words is : "+ ratingInWords);

                HotelInfoClass obj = new HotelInfoClass(titleOfHotel,linkToHotel,priceOfHotel,ratingOfHotel,ratingInWords);
                obj.saveObjectToCSV("kayak.csv");

            } catch (Exception e) {
                // Handle any other elements not found exception for this listing
                System.out.println("Error extracting data for listing " + i + ": " + e.getMessage());
            }finally {
                System.out.println("Crawling for one element done...");
            }
        }
        driver.close();

    }
}
