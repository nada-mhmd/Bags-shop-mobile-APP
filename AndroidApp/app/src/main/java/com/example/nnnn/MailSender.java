package com.example.nnnn;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
    private final String emailHost = "smtp.gmail.com"; // مزود البريد الإلكتروني (Gmail في هذا المثال)
    private final String emailSender = "your-email@gmail.com"; // بريدك الإلكتروني
    private final String emailSenderPassword = "your-email-password"; // كلمة مرور بريدك

    public void sendEmail(String recipientEmail, String subject, String body) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", emailHost);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSender, emailSenderPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailSender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
