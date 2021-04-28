package org.example.HealthServerV2.Model.User;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String email){
        super("Could not find user with email " + email);
    }

}
