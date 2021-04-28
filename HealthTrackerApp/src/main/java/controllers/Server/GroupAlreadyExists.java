package controllers.Server;

public class GroupAlreadyExists extends RuntimeException{
    public GroupAlreadyExists(int id){
        super("Group with id " + id + " already exists");
    }
}
