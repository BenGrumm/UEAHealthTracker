package org.example.HealthServerV2.Model.Goal;

import org.example.HealthServerV2.Model.Group.UserGroup;

import javax.persistence.*;

@Entity
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @ManyToOne
    @JoinColumn(name="group_id")
    UserGroup group;
    String inviteCode;

    public Goal(){
    }

    public Goal(int id, UserGroup group, String inviteCode) {
        this.id = id;
        this.group = group;
        this.inviteCode = inviteCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
