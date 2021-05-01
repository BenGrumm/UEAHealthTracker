package model;

import java.time.LocalDate;

public class GroupGoal extends Goal{
    public GroupGoal(){
        super("",goal.WEIGHT, LocalDate.now(), 0, 0);
    }

}
