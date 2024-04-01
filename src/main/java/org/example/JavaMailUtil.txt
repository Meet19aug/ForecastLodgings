package org.example;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import static org.example.HotelInfoClass.loadKObjectFromCSV;


public class JavaMailUtil {

    public static void sendMail(String recipient, HotelInfoClass[] hi, int kload) {
        try {
            Properties properties = new Properties();

            properties.put("mail.smtp.auth", true);
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.starttls.enable", true);

            String myAccountEmail = "meetpatelaug";
            String password = "heojlgggehmlglsw";

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myAccountEmail, password);
                }
            });
            String messToSend = "";
            for (int i = 0; i < kload; i++) {
                messToSend+=hi[i].toString();
                messToSend=messToSend+"\n";
            }
            Message message = prepareMessage(session, myAccountEmail, recipient,messToSend);
            if (message != null) {
                session.setDebug(true);
                Transport.send(message);
                System.out.println("Message sent successfully to " + recipient);
            } else {
                System.out.println("Failed to prepare the message.");
            }
        } catch (MessagingException ex) {
            Logger.getLogger(JavaMailUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recipient,String messToSend) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Hot deals for you!!!!");
            message.setText(messToSend);
            return message;
        } catch (MessagingException ex) {
            Logger.getLogger(JavaMailUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            int kload=5;
            HotelInfoClass[] hi = loadKObjectFromCSV("cheapflights.csv", kload);
            System.out.println(hi[0].toString());
            JavaMailUtil.sendMail("meetpatel19aug@gmail.com",hi,kload);
        } catch (Exception e) {
            System.out.println("An error occurred while sending the email: " + e.getMessage());
        }
    }
}
