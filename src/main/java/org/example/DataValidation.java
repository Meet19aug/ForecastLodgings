package org.example;
import java.util.regex.Pattern;
import java.time.LocalDate;

// Class for data validation methods
public class DataValidation {

    // Method to validate a date string
    public static boolean isValidDate(String startDate) {
// Check the date format using regex
        if (!startDate.matches("\\d{4}-(0[1-9]|1[0-2])-\\d{2}")) {
            return false; // Return false if the format is incorrect
        }

// Check if the day value is valid for the month
        try {
// Parse the date string into a LocalDate object for further validation
            LocalDate date = LocalDate.parse(startDate);
            int day = date.getDayOfMonth();
            int month = date.getMonthValue();
            int year = date.getYear();

// Check February for leap years
            if (month == 2) {
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    return (day <= 29); // Leap year, February has 29 days
                } else {
                    return day <= 28; // Non-leap year, February has 28 days
                }
            }

// Check months with 30 days
            if (month == 4 || month == 6 || month == 9 || month == 11) {
                return day <= 30;
            }

// All other months have 31 days
            return day <= 31;
        } catch (Exception e) {
            return false; // Return false if there is any exception during date parsing or validation
        }
    }

    // Method to validate an email address
    public static boolean isValidEmail(String email) {
// Regular expression pattern for email validation
        final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        final Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(email).matches(); // Return true if the email matches the pattern
    }

    // Main method to test the data validation methods
    public static void main(String[] args) {
// Test date validation
        String datecheck = "2004-02-29";
        System.out.println("Date given for checking is: " + datecheck);
        System.out.println("Result is : " + isValidDate(datecheck));

// Test invalid date formats
        datecheck = "2004-ew-10";
        System.out.println("Date given for checking is: " + datecheck);
        System.out.println("Result is : " + isValidDate(datecheck));

// Test invalid day values
        datecheck = "2004-02-30";
        System.out.println("Date given for checking is: " + datecheck);
        System.out.println("Result is : " + isValidDate(datecheck));

// Test invalid month values
        datecheck = "2004-13-01";
        System.out.println("Date given for checking is: " + datecheck);
        System.out.println("Result is : " + isValidDate(datecheck));

// Test invalid day values for January
        datecheck = "2004-01-32";
        System.out.println("Date given for checking is: " + datecheck);
        System.out.println("Result is : " + isValidDate(datecheck));

// Test email validation
        String emailTest = "abc.com";
        System.out.println("Email given for checking is: " + emailTest);
        System.out.println("Result is : " + isValidEmail(emailTest));

// Test email validation with missing '@' symbol
        emailTest = "abc@com";
        System.out.println("Email given for checking is: " + emailTest);
        System.out.println("Result is : " + isValidEmail(emailTest));

// Test email validation with invalid domain name
        emailTest = "abc@c123om";
        System.out.println("Email given for checking is: " + emailTest);
        System.out.println("Result is : " + isValidEmail(emailTest));

// Test valid email
        emailTest = "abc@abc.com";
        System.out.println("Email given for checking is: " + emailTest);
        System.out.println("Result is : " + isValidEmail(emailTest));
    }
}