package org.example.UEAHealthServer.Exceptions;

public class GroupNotFoundException extends RuntimeException{
    public GroupNotFoundException(int id){
        super("Could not find group with id " + id);
    }
}
