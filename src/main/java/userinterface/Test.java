package userinterface;

import friends.FriendUtility;
import users.UserUtility;

public class Test {

    public static void main(String[] args) {
        //TODO For testing can be deleted later

        UserUtility userUtility = new UserUtility();
        FriendUtility friendUtility = new FriendUtility();


//        userUtility.addUser("John", "password", "John@gmail.com");
//        userUtility.addUser("Marvin", "password", "marvin@gmail.com");
//        userUtility.addUser("fred", "password", "fred@gmail.com");
//        userUtility.addUser("jacky525", "password", "jacky525@gmail.com");
//        userUtility.addUser("jack", "password", "jack@gmail.com");
//        userUtility.addUser("janice", "password", "janice@gmail.com");
//        userUtility.addUser("jeff_bezos", "password", "jeff_bezos@gmail.com");
//        userUtility.addUser("xXx_jack_xXx", "password", "jackchallenged@gmail.com");
//        userUtility.addUser("I_mBritneyBitch_Smoosh", "password", "britneybitch@gmail.com");
//        userUtility.addUser("alfred", "password", "alfred@gmail.com");
//        userUtility.addUser("batman_imean_jack_the_second", "password", "notbatman@gmail.com");
//
//        userUtility.addUser("testchat", "feest", "testchat@test.eu");

//        long idSender = 0;
//        List<Request> list = friendUtility.checkFriendRequests(4);
//        for (Request el: list) {
//            idSender = el.getIdSender();
//        }
//
//        User user = userUtility.getUserById(idSender);
//        System.out.println("You have a friend request from: " + user.getUserName());

              userUtility.removeUser(13);

       //userUtility.removeRequest(14);




    }
}
