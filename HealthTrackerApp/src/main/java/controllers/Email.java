package controllers;

import model.Group;
import model.User;
import org.apache.commons.io.FileUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;


public class Email {

    public static void main(String[] args) {
//        try {
//
//            sendEmail("Testing", "This is the message body", "b.p.grummitt@gmail.com");
//
//        }catch (MessagingException e){
//            e.printStackTrace();
//        }

        askUserToJoinGroup(new User(0, "Ben",
                        "Test", "uname", "test",
                        "test", 0.01, 5, 5,
                        5, 5, 5, "MALE"),
                new Group(0, "test", "test d", 1, "135fr5"), "b.p.grummitt@gmail.com");

        emailGroupAboutGoalBeingMet(new User(0, "Ben",
                        "Test", "uname", "test",
                        "test", 0.01, 5, 5,
                        5, 5, 5, "MALE"),
                new Group(0, "test", "test d", 1, "135fr5"),
                new String[]{"b.p.grummitt@gmail.com", "ben.grummitt3986@gmail.com"});

        emailGroupAboutNewGoal(new Group(15, "Test Name", "This Group Does Stuff", 0, "13RFJ8"),
                new String[]{"b.p.grummitt@gmail.com"});
    }

    /**
     * Email single user about joining group
     * @param user user inviting
     * @param group group being invited to
     * @param email of user being invited
     * @return boolean if email send successful
     */
    public static boolean askUserToJoinGroup(User user, Group group, String email){
        try {
            String emailBody = FileUtils.readFileToString(new File(System.getProperty("user.dir") +
                    "/src/main/resources/UserJoinGroupV2.html"), "UTF-8");

            sendEmail("You've Been Invited To A Group",
                    String.format(emailBody, group.getName(), user.getFirstName(), group.getInvCode(), group.getDescription()),
                    email);
            return true;
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Email a set of addresses of user in a group about a goal being added to the group
     * @param group that the goal was added to
     * @param addresses of user in the group to email
     * @return true if all emails sent successfully else false
     */
    public static boolean emailGroupAboutNewGoal(Group group, String[] addresses){
        int numSent = 0;

        String emailBody;

        try {
            emailBody = FileUtils.readFileToString(new File(System.getProperty("user.dir") +
                    "/src/main/resources/NewGoalAddedToGroup.html"), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            emailBody = "";
        }

        for(int i = 0; i < addresses.length; i++){
            try {
                sendEmail("A New Group Added To " + group.getName(),
                        String.format(emailBody, group.getName(), group.getName()),
                        addresses[i]);
                numSent++;
            }catch (MessagingException e){
                e.printStackTrace();
            }
        }

        return numSent == addresses.length;
    }

    /**
     * Email a user group about a members goal being met
     * @param user who met the goal
     * @param group the group object in which the goal was met
     * @param addresses of the users to email
     * @return true if emails were successful false if one or more were not
     */
    public static boolean emailGroupAboutGoalBeingMet(User user, Group group, String[] addresses){
        int numSent = 0;

        String emailBody;

        try {
            emailBody = FileUtils.readFileToString(new File(System.getProperty("user.dir") +
                    "/src/main/resources/EmailGoalComplete.html"), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            emailBody = "A user in the group %s completed the goal. %s completed one of the goal %s.";
        }

        for(int i = 0; i < addresses.length; i++){
            try {
                sendEmail(String.format("A Member Of %s Completed A Goal", group.getName()),
                        String.format(emailBody, group.getName(), user.getFirstName(), "put goal here?"),
                        addresses[i]);
                numSent++;
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return numSent == addresses.length;
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

        message.setFrom(new InternetAddress("UEAHealthApp@gmail.com"));

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
