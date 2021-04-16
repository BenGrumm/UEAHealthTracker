package org.example.UEAHealthServer.Exceptions;

public class UserNotFoundException extends  RuntimeException{
    public UserNotFoundException(String email){
        super("Could Not Find User With Email " + email);
    }
}
