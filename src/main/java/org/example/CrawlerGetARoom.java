package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerGetARoom {
    public static void main(String[] args) {
        String checkInDate,checkOutDate,destination,numberOfPerson,numberOfRooms;
        destination="Windsor";
        checkInDate="2024-05-01";
        checkOutDate="2024-05-05";
        numberOfPerson="4";
        numberOfRooms="2";
        CrawlerGetARoom.fetchDataFromGetARoom(destination,checkInDate,checkOutDate,numberOfPerson,numberOfRooms);
    }

    private static void fetchDataFromGetARoom(String destination, String checkInDate, String checkOutDate, String numberOfPerson, String numberOfRooms) {
        String []checkInPart = checkInDate.split("-");
        String []checkOutPart = checkOutDate.split("-");
        String checkInD=checkInPart[2];
        String checkInMonth=checkInPart[1];
        String checkInYear=checkInPart[0];
        String checkOutD=checkOutPart[2];
        String checkOutMonth=checkOutPart[1];
        String checkOutYear=checkOutPart[0];
        String url = "https://www.getaroom.com/searches/show?amenities%5B%5D=&check_in="+checkInMonth+"%2F"+checkInD+"%2F"+checkInYear+"&check_out="+checkOutMonth+"%2F"+checkOutD+"%2F"+checkOutYear+"&destination="+destination+"&lucky=true&page=1&property_name=&rinfo=%5B%5B18%2C18%5D%5D&sort_order=position";
        String outputFile = "getARoom.html"; // Name of the output HTML file


        System.out.println("Fetching Date for Booking.com.... Wait it takes some time");
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver driver = null;

        try {
            driver = new ChromeDriver(options);

            Thread.sleep(1000);
            driver.get(url);
            // Get the entire page source
            String pageSource = driver.getPageSource();

            // Extract the HTML content between <html> and </html> tags
            String htmlContent = extractHTMLContent(pageSource);
            // Save the page source to an HTML file
            savePageSource(htmlContent, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
        System.out.println("HTML content downloaded and saved successfully.");
    }
    private static String extractHTMLContent(String pageSource) {
        String patternString = "<html.*?>([\\s\\S]*?)</html>";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(pageSource);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    private static void savePageSource(String pageSource, String fileName) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path dirpath = Paths.get(currentPath.toString(),"assets");
        fileName = dirpath.toString() +"/"+fileName;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(pageSource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
