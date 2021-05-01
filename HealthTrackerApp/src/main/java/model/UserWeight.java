package model;


import java.time.LocalDate;

public class UserWeight {
    private int stones;
    private int pounds;
    private LocalDate dateRecorded;


    public UserWeight(int stones, int pounds) {
        this.stones = stones;
        this.pounds = pounds;
        this.dateRecorded = dateRecorded.now();
    }

    public UserWeight(int stones, int pounds, LocalDate dateRecorded) {
        this.stones = stones;
        this.pounds = pounds;
        this.dateRecorded = dateRecorded;
    }

    public int getStones() {
        return stones;
    }

    public int getPounds() {
        return pounds;
    }

    public LocalDate getDateRecorded() {
        return dateRecorded;
    }
}
