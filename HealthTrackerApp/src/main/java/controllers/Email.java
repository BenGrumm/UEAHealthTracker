package controllers;

import model.Group;
import model.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;


public class Email {

    public static void main(String[] args) {
        try {
            sendEmail("Testing", "This is the message body", "youEmail@here.totest");
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }

    /**
     * Email single user about joining group
     * @param user user inviting
     * @param group group being invited to
     * @param email of user being invited
     * @return boolean if email send successful
     */
    public static boolean askUserToJoinGroup(User user, Group group, String email){
        return false;
    }

    /**
     * Email a user group about a members goal being met
     * @param user who met the goal
     * @param group the group object in which the goal was met
     * @param addresses of the users to email
     * @return true if emails were successful false if one or more were not
     */
    public static boolean emailGroupAboutGoalBeingMet(User user, Group group, String[] addresses){
        return false;
    }

    /**
     * Function to send an email with given parameter
     * @param subject of the email
     * @param htmlOrTextBody main body of the email, allows for html styling
     * @param address of the recipient
     * @throws MessagingException when error sending message
     */
    public static void sendEmail(String subject, String htmlOrTextBody, String address) throws MessagingException {
        Message message = new MimeMessage(initSession());

        message.setFrom(new InternetAddress("climatemonitoringpeoplecmp@gmail.com"));

        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
        message.setSubject(subject);

        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setContent(htmlOrTextBody, "text/html");

        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp);

        message.setContent(mp);

        Transport.send(message);
    }

    /**
     * Configure properties for a gmail sender
     * @return configured properties object
     */
    public static Properties configureProperties(){
        Properties prop = System.getProperties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        return prop;
    }

    /**
     * Configure logged in session to gmail
     * @return logged in Session object
     */
    public static Session initSession(){
        Properties prop= configureProperties();
        String username = "";
        String password = "";

        return Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

}
