package model;

import java.util.Base64;

/**
 * This class is used to store information about a user of the health application.
 * This will store basic information about the users and can use these values to calculate additional information
 * about the user such as BMI.
 * @author Harry Burns
 **/

public class User {

    private final int ID;
    private String firstName, surname,email,password,username;
    private double height;
    private int weightStone, weightPounds;
    private Gender gender;

    public enum Gender {MALE, FEMALE}

    public User(int id, String firstName, String surname, String username, String email, String password, double height,
                int weightStone, int weightPounds, String gender) {

        this.ID = id;
        this.firstName = encodeString(firstName);
        this.surname = encodeString(surname);
        this.username = username;
        this.password = encodeString(password);
        this.email = email;
        this.height = height;
        this.weightStone = weightStone;
        this.weightPounds = weightPounds;

        switch(gender.toLowerCase()){
            case "male":
                this.gender = Gender.MALE;
                break;
            case "female":
                this.gender = Gender.FEMALE;
                break;
        }
    }

    /**
     * This method is used to convert plaintext into ciphertext of the string provided then returns the ciphertext.
     * @param plaintext Plaintext given to be encoded to ciphertext.
     * @return ciphertext that is the encrypted version of the input information.
     */
    public String encodeString(String plaintext) {
        return Base64.getEncoder().encodeToString(plaintext.getBytes());
    }

    /**
     * This method is used to decrypt the ciphertext provided and return the original plaintext.
     * @param ciphertext Ciphertext given to be decoded to plaintext.
     * @return plaintext that is the original string.
     */
    public String decodeString(String ciphertext) {
        return new String (Base64.getDecoder().decode(ciphertext.getBytes()));
    }

    public void setFirstName(String firstName) {
        this.firstName = encodeString(firstName);
    }

    public void setSurname(String surname) {
        this.surname = encodeString(surname);
    }


    public void setHeight(double height) {
        this.height = height;
    }

    public String getFirstName() {
        return decodeString(firstName);
    }

    public String getSurname() {
        return decodeString(surname);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return decodeString(password);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public int getWeightStone() {
        return weightStone;
    }

    public int getWeightPounds() {
        return weightPounds;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setWeightStone(int weightStone) {
        this.weightStone = weightStone;
    }

    public void setWeightPounds(int weightPounds) {
        this.weightPounds = weightPounds;
    }

    public void setPassword(String password) {
        this.password = encodeString(password);
    }

    public double getHeight() {
        return height;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
