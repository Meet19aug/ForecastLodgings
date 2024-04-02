package org.example;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import static org.example.HotelInfoClass.loadKObjectFromCSV;

// Utility class for sending emails using JavaMail API
public class JavaMailUtil {

    // Method to send email with hotel information to the recipient
    public static void sendMail(String recipient, HotelInfoClass[] hotelInfo, int kload) {
        try {
// Configure mail server properties
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", true); // Enable SMTP authentication
            properties.put("mail.smtp.host", "smtp.gmail.com"); // Set SMTP host
            properties.put("mail.smtp.port", "587"); // Set SMTP port
            properties.put("mail.smtp.starttls.enable", true); // Enable STARTTLS encryption

// Sender email and password
            String myAccountEmail = "meetpatelaug"; // Replace with your email
            String password = "heojlgggehmlglsw"; // Replace with your password

// Create a mail session with authentication
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myAccountEmail, password);
                }
            });

// Compose message content
            StringBuilder messageContent = new StringBuilder();
            for (int i = 0; i < kload; i++) {
                messageContent.append(hotelInfo[i].toString()).append("\n");
            }

// Prepare the message
            Message message = prepareMessage(session, myAccountEmail, recipient, messageContent.toString());

// Send the message
            if (message != null) {
                session.setDebug(true); // Enable debugging for troubleshooting
                Transport.send(message); // Send the email
                System.out.println("Message sent successfully to " + recipient);
            } else {
                System.out.println("Failed to prepare the message.");
            }
        } catch (MessagingException ex) {
            Logger.getLogger(JavaMailUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Method to prepare the email message
    private static Message prepareMessage(Session session, String myAccountEmail, String recipient, String messageContent) {
        try {
// Create a MimeMessage object
            Message message = new MimeMessage(session);

// Set sender and recipient addresses, subject, and message content
            message.setFrom(new InternetAddress(myAccountEmail)); // Set sender email address
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); // Set recipient email address
            message.setSubject("Hot deals for you!!!!"); // Email subject
            message.setText(messageContent); // Email body

            return message;
        } catch (MessagingException ex) {
            Logger.getLogger(JavaMailUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; // Return null if there is an error preparing the message
    }

    // Main method to demonstrate email sending functionality
    public static void main(String[] args) {
        try {
            int kload = 5; // Number of hotels to load from CSV
            HotelInfoClass[] hotelInfo = loadKObjectFromCSV("cheapflights.csv", kload); // Load hotel info from CSV
            System.out.println(hotelInfo[0].toString()); // Print the first hotel info for verification
            JavaMailUtil.sendMail("meetpatel494494aug@gmail.com", hotelInfo, kload); // Send email to recipient
        } catch (Exception e) {
            System.out.println("An error occurred while sending the email: " + e.getMessage());
        }
    }
}