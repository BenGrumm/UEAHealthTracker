package testing;

import model.*;

import java.time.LocalDate;

public class ExerciseDBTesting {

    private static final ExerciseTypeDBHelper exTDbh = new ExerciseTypeDBHelper();
    private static final ExerciseDBHelper exDbh = new ExerciseDBHelper();

    private static ExerciseType[] exerciseTypes = new ExerciseType[5];

    public static void main(String[] args) {
        Exercise[] exercisesAdded = populateExercisesWithTestData();
        Exercise[] exercisesFromDB = exDbh.getAllExercises();

        for(int i = 0; i < exercisesFromDB.length; i++){
            System.out.println(exercisesFromDB[i]);
        }
    }

    public static Exercise[] populateExercisesWithTestData(){
        User.setLoggedIn(new User(17, "Test", "User", "TestUser16", "test@user.com",
                "testPword", 5, 5, 5, 5, 5, 5,
                "MALE"));

        exerciseTypes[0] = exTDbh.getType(5);
        exerciseTypes[1] = exTDbh.getType(10);
        exerciseTypes[2] = exTDbh.getType(15);
        exerciseTypes[3] = exTDbh.getType(20);
        exerciseTypes[4] = exTDbh.getType(25);

        return addExercisedToDB();

    }

    public static Exercise[] addExercisedToDB(){
        Exercise[] exercises = new Exercise[5];

        ExerciseDBHelper edbh = new ExerciseDBHelper();

        exercises[0] = new Exercise(-1, 15, 150, exerciseTypes[0], LocalDate.now().minusDays(5));
        exercises[1] = new Exercise(-1, 20, 10, exerciseTypes[2], LocalDate.now().minusDays(3));
        exercises[2] = new Exercise(-1, 54, 350, exerciseTypes[2], LocalDate.now().minusDays(9));
        exercises[3] = new Exercise(-1, 20, 15, exerciseTypes[3], LocalDate.now().minusDays(1));
        exercises[4] = new Exercise(-1, 93, 10, exerciseTypes[4], LocalDate.now().minusDays(12));

        edbh.addExerciseToDB(exercises[0]);
        edbh.addExerciseToDB(exercises[1]);
        edbh.addExerciseToDB(exercises[2]);
        edbh.addExerciseToDB(exercises[3]);
        edbh.addExerciseToDB(exercises[4]);

        return exercises;
    }

}
