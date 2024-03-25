package org.example;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class DateFunctions {
    private static Map<String, Integer> monthToInt = new HashMap<>();
    public static String getMonthName(int monthNumber) {
        return Month.of(monthNumber).name();
    }

    static {

        monthToInt.put("January", 1);
        monthToInt.put("February", 2);
        monthToInt.put("March", 3);
        monthToInt.put("April", 4);
        monthToInt.put("May", 5);
        monthToInt.put("June", 6);
        monthToInt.put("July", 7);
        monthToInt.put("August", 8);
        monthToInt.put("September", 9);
        monthToInt.put("October", 10);
        monthToInt.put("November", 11);
        monthToInt.put("December", 12);
    }

    public static int getMonthValue(String monthName) {
        if (monthToInt.containsKey(monthName)) {
            return monthToInt.get(monthName);
        } else {
            throw new IllegalArgumentException("Invalid month name: " + monthName);
        }
    }

    public static void main(String[] args) {

        // Test the mapping
        String monthName = "January";
        int monthValue = monthToInt.get(monthName);
        System.out.println(monthName + " is mapped to: " + monthValue);

        monthName = "December";
        monthValue = monthToInt.get(monthName);
        System.out.println(monthName + " is mapped to: " + monthValue);

        // Test the method
        String monthName1 = "January";
        int monthValue1 = getMonthValue(monthName1);
        System.out.println(monthName1 + " is mapped to: " + monthValue1);

        String monthName2 = "December";
        int monthValue2 = getMonthValue(monthName2);
        System.out.println(monthName2 + " is mapped to: " + monthValue2);
    }

}
