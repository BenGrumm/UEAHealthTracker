package org.example.UEAHealthServer.Model.Group;

import org.example.UEAHealthServer.Model.User.ServerUser;
import org.example.UEAHealthServer.Model.UserInGroup;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@EnableAutoConfiguration
public class UserGroup {

    private @Id int id;

    private int size;
    private String name, description, role;
    @OneToMany
    @JoinColumn(name="user")
    private Set<UserInGroup> users;
    public UserGroup(){}

    public UserGroup(int id, int size, String name, String description, String role, Set<UserInGroup> users){
        this.id = id;
        this.size = size;
        this.name = name;
        this.description = description;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<UserInGroup> getUsers() {
        return users;
    }

    public void setUsers(Set<UserInGroup> users) {
        this.users = users;
    }

}
