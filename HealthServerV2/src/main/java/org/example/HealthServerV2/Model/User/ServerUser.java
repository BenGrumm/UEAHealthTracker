package org.example.HealthServerV2.Model.User;

import org.example.HealthServerV2.Model.Group.UserGroup;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.util.Set;

/**
 * This class is used to store information about a user of the health application.
 * This will store basic information about the users and can use these values to calculate additional information
 * about the user such as BMI.
 * @author Harry Burns
 **/

@Entity
@EnableAutoConfiguration
public class ServerUser {

    private @Id String email;
    private String firstName, surname, username, password;
    private int weightStone, weightPounds;
    private double height;
    private Gender gender;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_groups",
            joinColumns = @JoinColumn(name = "user_email", referencedColumnName = "email"),
            inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
    private Set<UserGroup> groups;

    public enum Gender {MALE, FEMALE}

    public ServerUser(){}

    public ServerUser(String firstName, String surname, String username, String email, String password, double height,
                      int weightStone, int weightPounds, String gender, Set<UserGroup> groups) {

        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.height = height;
        this.weightStone = weightStone;
        this.weightPounds = weightPounds;
        this.groups = groups;

        switch(gender.toLowerCase()){
            case "male":
                this.gender = Gender.MALE;
                break;
            case "female":
                this.gender = Gender.FEMALE;
                break;
        }
    }

    public String getResourceId(){
        return email;
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

    public Set<UserGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<UserGroup> groups) {
        this.groups = groups;
    }

    @Override
    public int hashCode(){
        return email.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", height=" + height +
                ", weightStone=" + weightStone +
                ", weightPounds=" + weightPounds +
                ", gender=" + gender +
                '}';
    }
}