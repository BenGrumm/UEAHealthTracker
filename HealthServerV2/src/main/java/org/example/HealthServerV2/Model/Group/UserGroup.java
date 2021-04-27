package org.example.HealthServerV2.Model.Group;

import org.example.HealthServerV2.Model.Goal.Goal;
import org.example.HealthServerV2.Model.User.ServerUser;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.util.Set;

@Entity
@EnableAutoConfiguration
public class UserGroup {

    @Id
    private String name;
    private String description, inviteCode;
    private int size;
    @ManyToMany(mappedBy = "groups")
    private Set<ServerUser> users;
    @OneToMany(mappedBy = "group")
    private Set<Goal> goals;
    public UserGroup(){}

    public UserGroup(int size, String name, String description, String inviteCode, Set<ServerUser> users){
        this.size = size;
        this.name = name;
        this.description = description;
        this.inviteCode = inviteCode;
        this.users = users;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Set<ServerUser> getUsers() {
        return users;
    }

    public void setUsers(Set<ServerUser> users) {
        this.users = users;
    }

}
