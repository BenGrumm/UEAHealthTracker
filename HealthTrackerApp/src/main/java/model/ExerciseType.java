package model;

public class ExerciseType {

    private int dbID;
    private String exerciseDescription;
    private float caloriesBurnedPerKGPerHour;

    public ExerciseType(int id, String description, float calsPerKGPerHour){
        dbID = id;
        exerciseDescription = description;
        caloriesBurnedPerKGPerHour = calsPerKGPerHour;
    }

    public String getExerciseDescription(){
        return exerciseDescription;
    }

    public float getCaloriesBurned(){
        return caloriesBurnedPerKGPerHour;
    }

    public int getDbID() { return dbID; }

    @Override
    public int hashCode() {
        return dbID;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ExerciseType && dbID == ((ExerciseType) obj).dbID;
    }

    @Override
    public String toString() {
        return "ExerciseType{" +
                "dbID=" + dbID +
                ", exerciseDescription='" + exerciseDescription + '\'' +
                ", caloriesBurnedPerKGPerHour=" + caloriesBurnedPerKGPerHour +
                '}';
    }
}
