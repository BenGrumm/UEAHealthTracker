package org.example.UEAHealthServer.Model.UserGroupManyToMany;

import org.example.UEAHealthServer.Model.Group.UserGroup;
import org.example.UEAHealthServer.Model.User.ServerUser;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@EnableAutoConfiguration
public class UserInGroup {

    @Id
    int id;

    @ManyToOne
    ServerUser user;

    @ManyToOne
    UserGroup group;

    public UserInGroup(){}

    public UserInGroup(int id, ServerUser user, UserGroup group) {
        this.id = id;
        this.user = user;
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ServerUser getUser() {
        return user;
    }

    public void setUser(ServerUser user) {
        this.user = user;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup userGroup) {
        this.group = userGroup;
    }
}
