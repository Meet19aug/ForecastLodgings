package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import static org.example.DateRelatedFunctions.getMonthName;
import static org.example.DateRelatedFunctions.getMonthValue;


public class CrawlerBooking {

	public static void fetchDataFromBooking(String urlToCrawl, String destination, String checkInDate, String checkOutDate, String numberOfAdult, String numberOfRooms) {

		System.out.println("Fetching Date for Booking.com.... Wait it takes some time");
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");

		WebDriver driver = null;
		try {
			driver = new ChromeDriver(options);

			Thread.sleep(1000);
			driver.get(urlToCrawl);

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5000));
			WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.id(":re:")));

			searchBox.click();

			Thread.sleep(2000);

			for (int i = 0; i < 10; i++) {
				searchBox.sendKeys(Keys.BACK_SPACE);
			}

			searchBox.sendKeys(destination);
			Thread.sleep(3500);

			searchBox.sendKeys(Keys.ENTER);
			Thread.sleep(3000);

			selectCheckInOutDate(driver, checkInDate, checkOutDate);
			Thread.sleep(2000);

			selectNumberOfAdult(driver, numberOfAdult);
			Thread.sleep(2000);

			selectNumberOfRoomsAndSubmit(driver, numberOfRooms);
			Thread.sleep(2500);

			crawlListingsAndStoreDataForBooking(driver,destination);

			System.out.println("\n**********************************************************************************************\n");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.quit();
		}
	}

	private static void selectCheckInOutDate(WebDriver driver, String checkInDate, String checkOutDate) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (isElementPresent(driver, By.xpath("//button[@aria-label=\"Previous month\"]"))) {
			System.out.println("Element is present.");
		} else {
			System.out.println("Element is not present.");
			driver.findElement(By.xpath("//div[@data-testid=\"searchbox-dates-container\"]")).click();

		}

		String[] inDate = checkInDate.split("-");

		Integer inOnlyDate = Integer.parseInt(inDate[2]);
		Integer inOnlyMonth = Integer.parseInt(inDate[1]);
		Integer inOnlyYear = Integer.parseInt(inDate[0]);
		System.out.println("Requred : " + inOnlyDate + " " + inOnlyMonth  + "  " + inOnlyYear);

		String[] outDate = checkOutDate.split("-");
		Integer outOnlyDate = Integer.parseInt(outDate[2]);
		Integer outOnlyMonth = Integer.parseInt(outDate[1]);
		Integer outOnlyYear = Integer.parseInt(outDate[0]);
		System.out.println("Requred : " + outOnlyDate + " " + outOnlyMonth  + "  " + outOnlyYear);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String monthyear = driver.findElement(By.xpath("//h3[@class=\"e1eebb6a1e ee7ec6b631\"]")).getText();
		System.out.println("Extarcted value is : " +monthyear);
		String extractedData[] = monthyear.split(" ");
		Integer extractedMonth = getMonthValue(extractedData[0]);
		Integer extractedYear = Integer.parseInt(extractedData[1]);

		System.out.println("Extracted: " + extractedMonth.toString() + " " + extractedYear.toString());

		int numberOfTimeToPressRightButton = 0;
		if(!(extractedMonth==inOnlyMonth && extractedYear==inOnlyYear)){
			numberOfTimeToPressRightButton = (inOnlyYear - extractedYear) * 12 + (inOnlyMonth - extractedMonth);
		}
		String rightButtonXPath = "//button[@aria-label=\"Next month\"]";
		while(numberOfTimeToPressRightButton>0){
			driver.findElement(By.xpath(rightButtonXPath)).click();
			numberOfTimeToPressRightButton--;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		String startXPath = inOnlyDate+ " " + capitalize(getMonthName(inOnlyMonth).toLowerCase()) + " " + inOnlyYear;
		String endXPath = outOnlyDate+ " " + capitalize(getMonthName(outOnlyMonth).toLowerCase()) + " " + outOnlyYear;
		System.out.println("StartXPath: " + startXPath + "\nEndXPath = " + endXPath);

		String firstDateXpath = "//span[@aria-label=\""+startXPath+"\"]".strip();
		String lastDateXpath = "//span[@aria-label=\""+endXPath+"\"]".strip();
		System.out.println("Final Path : " + firstDateXpath + lastDateXpath);


		driver.findElement(By.xpath(firstDateXpath)).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.xpath(lastDateXpath)).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	public static boolean isElementPresent(WebDriver driver, By locator) {
		return !driver.findElements(locator).isEmpty();
	}

	public static String capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		} else {
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
	}

	private static void selectNumberOfRoomsAndSubmit(WebDriver driver, String numberOfRooms) {
		Integer numOfRoom = Integer.parseInt(numberOfRooms);
		if(numOfRoom==1){
			return;
		}
		String plusRoomXPath = "/html/body/div[4]/div/div[2]/div/div[1]/div/form/div[1]/div[3]/div/div/div/div/div[3]/div[2]/button[2]";
		String done = "/html/body/div[4]/div/div[2]/div/div[1]/div/form/div[1]/div[3]/div/div/div/button";
		while(numOfRoom>1){
			driver.findElement(By.xpath(plusRoomXPath)).click();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			numOfRoom--;
		}
		driver.findElement(By.xpath(done)).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String submit = "/html/body/div[4]/div/div[2]/div/div[1]/div/form/div[1]/div[4]/button";
		driver.findElement(By.xpath(submit)).click();
	}

	private static void selectNumberOfAdult(WebDriver driver, String numberOfAdult) {
		Integer intAdult = Integer.parseInt(numberOfAdult);
		String occupancyDropDownXPath = "//button[@data-testid=\"occupancy-config\"]";
		String minuAdultXPath = "/html/body/div[4]/div/div[2]/div/div[1]/div/form/div[1]/div[3]/div/div/div/div/div[1]/div[2]/button[1]";
		String plusAdultXpath = "/html/body/div[4]/div/div[2]/div/div[1]/div/form/div[1]/div[3]/div/div/div/div/div[1]/div[2]/button[2]";
		driver.findElement(By.xpath(occupancyDropDownXPath)).click();
		int count=0;
		if(intAdult==2){

		}else if(intAdult<2){
			driver.findElement(By.xpath(minuAdultXPath)).click();
		}else{
			count=(intAdult-2);
			while(count>0){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				driver.findElement(By.xpath(plusAdultXpath)).click();
				count--;
			}
		}

	}

	private static void crawlListingsAndStoreDataForBooking(WebDriver driver,String destination) {
		System.out.println("crawlListingsAndStoreDataForBooking");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String numberOfListingFoundIsXPath = "//h1[@aria-live=\"assertive\"]";
		String listingNumber = driver.findElement(By.xpath(numberOfListingFoundIsXPath)).getText();
		int numberOfList = Integer.parseInt(listingNumber.split(" ")[1]);
		System.out.println("Number of listing is: " + numberOfList);
		String fullContainerXPath ="//div[@data-testid=\"property-card\"]";

		List<WebElement> listings = driver.findElements(By.xpath(fullContainerXPath));

		if (listings.isEmpty()) {
			System.out.println("No listings found on the page.");
			return;
		}

		for (int i = 0; i < listings.size(); i++) {
			WebElement listing = listings.get(i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			try {
				// Extract data from the listing as strings
				String linkToHotel = listing.findElement(By.xpath(".//div[1]/div/div[1]/div/h3/a")).getAttribute("href");
				System.out.println("Link to Rooms: " + linkToHotel);
				linkToHotel = linkToHotel.trim();
				String titleOfHotel = listing.findElement(By.xpath(".//div[1]/div/div[1]/div/h3/a/div[1]")).getText();
				titleOfHotel = titleOfHotel.trim();
				System.out.println("titleOfHotel: " + titleOfHotel);
				String priceOfHotel = listing.findElement(By.xpath(".//div[1]/div[2]/div/div[2]/div[2]/div/div[1]/span")).getText();
				System.out.println("Price Of Hotel Excluding Tax is :" + priceOfHotel);
				priceOfHotel = priceOfHotel.trim();
				String ratingOfHotel = listing.findElement(By.xpath(".//div[1]/div[2]/div/div[1]/div[2]/div/div/a/span/div/div[1]")).getText();
				System.out.println("Rating Of Hotel is : " + ratingOfHotel);
				ratingOfHotel = ratingOfHotel.trim();
				String ratingInWords = listing.findElement(By.xpath(".//div/div/div/a/span/div/div[2]/div[1]")).getText();
				System.out.println("Ratings in words is : " + ratingInWords);
				ratingInWords = ratingInWords.trim();
				String numberOfReview = listing.findElement(By.xpath(".//div/div/div/a/span/div/div[2]/div[2]")).getText();
				System.out.println("Number of Review is : " + numberOfReview);
				numberOfReview = numberOfReview.trim();

				ratingOfHotel = HotelInfoClass.extractScore(ratingOfHotel);

				priceOfHotel = HotelInfoClass.convertToNumeric(priceOfHotel);

				HotelInfoClass obj = new HotelInfoClass(titleOfHotel, linkToHotel, priceOfHotel, ratingOfHotel, ratingInWords);
				obj.saveObjectToCSV("booking.csv");

			} catch (Exception e) {
				System.out.println("Error extracting data for listing " + i + ": " + e.getMessage());
			} finally {
				System.out.println("Competed the crawling for Booking.com");
			}
		}

	}

	public static void main(String[] args) {
		fetchDataFromBooking("https://www.booking.com/","Windsor","2024-05-01", "2024-05-05", "4","2");
	}
}
