package org.example.HealthServerV2.Model.Group;

public class GroupNotFoundException extends RuntimeException{
    public GroupNotFoundException(int id){
        super("Could Not Find Group With id " + id);
    }
}
