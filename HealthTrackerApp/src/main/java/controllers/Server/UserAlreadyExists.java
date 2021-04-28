package controllers.Server;

public class UserAlreadyExists extends RuntimeException{
    public UserAlreadyExists(String email){
        super("User with email " + email + " already exists on server");
    }
}
