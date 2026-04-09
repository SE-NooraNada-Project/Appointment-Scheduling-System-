package com.appointment.service.notification;

import com.appointment.domain.User;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Real Email Notification Service
 * Sends actual emails using SMTP.
 */
public class EmailNotificationService implements Observer {

    private final String fromEmail = "nadajallad6@gmail.com";
    private final String password = "axxt iibc cbry hplk";

    @Override
    public void notify(User user, String message) {

        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            System.out.println("Failed to send email: user email is missing.");
            return;
        }

        String toEmail = user.getEmail();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        javax.mail.Session session = javax.mail.Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );

            msg.setSubject("Appointment Reminder");
            msg.setText(message);

            Transport.send(msg);

            System.out.println("Email sent to: " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}