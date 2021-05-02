package testing;

import model.*;

import java.time.LocalDate;

public class FoodDBTesting {

    private static FoodType[] mFoodTypes = new FoodType[5];
    private static FoodTypeDBHelper ftDBH = new FoodTypeDBHelper();
    private static FoodDBHelper fDBH = new FoodDBHelper();

    public static void populateDBWithTestData(){
        User.setLoggedIn(new User(17, "Test", "User", "TestUser16", "test@user.com",
                "testPword", 5, 5, 5, 5, 5, 5,
                "MALE"));

        getFoodTypes();
        
        fDBH.addFoodToDB(new Food(-1, "Incredible Food", 400, "meal?", 850, LocalDate.now().minusDays(0), mFoodTypes[4]));
        fDBH.addFoodToDB(new Food(-1, "Boring Food", 250, "meal?", 300, LocalDate.now().minusDays(1), mFoodTypes[4]));
        fDBH.addFoodToDB(new Food(-1, "Food", 640, "meal?", 1200, LocalDate.now().minusDays(8), mFoodTypes[4]));
        fDBH.addFoodToDB(new Food(-1, "Healthy Food", 15, "meal?", 800, LocalDate.now().minusDays(12), mFoodTypes[4]));
        fDBH.addFoodToDB(new Food(-1, "Shit Food", 500, "meal?", 300, LocalDate.now().minusDays(4), mFoodTypes[4]));
    }

    public static void getFoodTypes(){
        mFoodTypes[0] = ftDBH.getType(1);
        mFoodTypes[1] = ftDBH.getType(8);
        mFoodTypes[2] = ftDBH.getType(12);
        mFoodTypes[3] = ftDBH.getType(5);
        mFoodTypes[4] = ftDBH.getType(19);
    }

}
