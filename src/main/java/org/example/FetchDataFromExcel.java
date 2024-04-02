package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchDataFromExcel {

    // Method to read data from Excel files
    public static List<Map<String, String>> readData(List<String> filePaths) {
        List<Map<String, String>> cityList = new ArrayList<>();

        // Loop through each file path
        for (String filePath : filePaths) {
            try (FileInputStream fileInputStream = new FileInputStream(filePath);
                 Workbook workbook = new XSSFWorkbook(fileInputStream)) {

                // Get the sheet named "Sheet2"
                Sheet sheet = workbook.getSheet("Sheet2");

                // Skip the first row (header row)
                boolean firstRowSkipped = false;

                // Iterate through each row of the sheet
                for (Row row : sheet) {
                    if (!firstRowSkipped) {
                        firstRowSkipped = true;
                        continue;
                    }

                    // Get the cell containing city name (assuming it's in the first column) and city ID (in the second column)
                    Cell cell = row.getCell(0);
                    if (cell != null && cell.getCellType() == CellType.STRING) {

                        // Extract city name and ID
                        String cityName = row.getCell(0).getStringCellValue();
                        Double cityId = row.getCell(1).getNumericCellValue();

                        // Create a map to store city details and add it to the list
                        Map<String, String> cityDetails = new HashMap<>();
                        cityDetails.put("City", cityName);
                        cityDetails.put("City_id", String.valueOf(cityId));
                        cityList.add(cityDetails);
                    }
                }
            } catch (IOException e) {
                // Handle IOException
                System.out.println("IOException Occurred: " + e.getMessage());
            }
        }
        return cityList;
    }

    public static void main(String[] args) {
        // List of file paths
        List<String> filePaths = new ArrayList<>();

        // Get current working directory
        Path currentPath = Paths.get(System.getProperty("user.dir"));

        // Construct path to the assets directory
        Path dirpath = Paths.get(currentPath.toString(), "assets");

        // Construct path to the Excel file
        String filename = dirpath.toString() + "/Book1.xlsx";

        // Add file path to the list
        filePaths.add(filename);

        // Read data from Excel files
        List<Map<String, String>> cityList = readData(filePaths);

        // Print city details
        for (Map<String, String> city : cityList) {
            System.out.println("City Name: " + city.get("City"));
            System.out.println("City Id: " + city.get("City_id"));
            System.out.println();
        }
    }
}
