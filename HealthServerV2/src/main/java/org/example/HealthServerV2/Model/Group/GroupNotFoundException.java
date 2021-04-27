package org.example.HealthServerV2.Model.Group;

public class GroupNotFoundException extends RuntimeException{
    public GroupNotFoundException(String name){
        super("Could Not Find Group With Name " + name);
    }
}
