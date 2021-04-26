package org.example.HealthServerV2.Model.Group;

import org.example.HealthServerV2.Model.Goal;
import org.example.HealthServerV2.Model.User.ServerUser;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.util.Set;

@Entity
@EnableAutoConfiguration
public class UserGroup {

    private @Id int id;

    private int size;
    private String name, description, inviteCode;
    @ManyToMany(mappedBy = "groups")
    private Set<ServerUser> users;
    @OneToMany
    private Set<Goal> goals;
    public UserGroup(){}

    public UserGroup(int id, int size, String name, String description, String inviteCode, Set<ServerUser> users){
        this.id = id;
        this.size = size;
        this.name = name;
        this.description = description;
        this.inviteCode = inviteCode;
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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