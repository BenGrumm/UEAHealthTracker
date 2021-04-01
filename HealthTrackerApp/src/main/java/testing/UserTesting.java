package testing;

import model.User;

public class UserTesting {

    public static void main(String[] args) {

        User user = new User("John","Smith","J.Smith@uea.ac.uk","jsmith1!", 178,"male");

        System.out.println(user.getGender());

        user.setGender(User.Gender.UNDEFINED);

        System.out.println(user.getGender());



    }

}
