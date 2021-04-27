package org.example.HealthServerV2.Model.Goal;

import org.example.HealthServerV2.Model.Group.UserGroup;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Goal {

    @Id
    int id;

    @ManyToOne
    @JoinColumn(name="group_name")
    UserGroup group;

    public Goal(int id, UserGroup group) {
        this.id = id;
        this.group = group;
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
}
