package model;

/**
 * This class is used to store information about a user of the health application.
 * This will store basic information about the users and can use these values to calculate additional information
 * about the user such as BMI.
 * @author Harry Burns
 **/

public class User {

    public static User loggedIn;

    private final int ID;
    private String firstName, surname,username, email, password;
    private int weightStone, weightPounds, idealWeightStone, idealWeightPounds;
    private double height , BMI;
    private Gender gender;

    public enum Gender {MALE, FEMALE}

    public User(int id, String firstName, String surname, String username, String email, String password, double height,
                int weightStone, int weightPounds, int idealWeightStone, int idealWeightPounds,float bmi,String gender) {

        this.ID = id;
        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.height = height;
        this.weightStone = weightStone;
        this.weightPounds = weightPounds;
        this.idealWeightStone = idealWeightStone;
        this.idealWeightPounds = idealWeightPounds;
        this.BMI = bmi;

        switch(gender.toLowerCase()){
            case "male":
                this.gender = Gender.MALE;
                break;
            case "female":
                this.gender = Gender.FEMALE;
                break;
        }
    }

    public static double calculateBMI(double height, int stone, int pounds){
        return ((stone * 6.35029) + (pounds * 0.453592)) /  ((height/100)*(height/100));
    }

    public double getBMI() {
        return BMI;
    }

    public void setBMI(double BMI) {
        this.BMI = BMI;
    }

    public int getIdealWeightStone() {
        return idealWeightStone;
    }

    public void setIdealWeightPounds(int idealWeightPounds) {
        this.idealWeightPounds = idealWeightPounds;
    }

    public void setIdealWeightStone(int idealWeightStone) {
        this.idealWeightStone = idealWeightStone;
    }

    public int getIdealWeightPounds() {
        return idealWeightPounds;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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
        this.password = password;
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

    public static User getLoggedIn(){
        return loggedIn;
    }

    public static void setLoggedIn(User loggedIn) {
        User.loggedIn = loggedIn;
    }

    public double getWeightKG(){
        double stones = this.weightStone + (this.weightPounds / 14);
        double kg = stones * 6.35029318;
        return kg;

    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", weightStone=" + weightStone +
                ", weightPounds=" + weightPounds +
                ", idealWeightStone=" + idealWeightStone +
                ", idealWeightPounds=" + idealWeightPounds +
                ", height=" + height +
                ", BMI=" + BMI +
                ", gender=" + gender +
                '}';
    }
}
