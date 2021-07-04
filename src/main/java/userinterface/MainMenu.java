package userinterface;

import friends.FriendUtility;
import friends.Request;
import messages.MessageUtility;
import org.mindrot.jbcrypt.BCrypt;
import users.UserUtility;
import users.User;
import utility.InputUtility;

import java.util.List;
import java.util.Locale;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

public class MainMenu {

    //Necessary Instances
    MessageUtility messageUtility = new MessageUtility();
    UserUtility userUtility = new UserUtility();
    FriendUtility friendUtility = new FriendUtility();

    private boolean isRunning;
    private long mainUserId;

    public void keepGoing() {
        while (isRunning) {
            openMainMenu();
        }
    }

    public void openMainMenu() {

        keepGoing();
        InputUtility.horLine();
        System.out.println("| Welcome to JPAChat  |");
        InputUtility.horLine();

        System.out.println("| 1) Login            |");
        System.out.println("| 2) Register         |");
        System.out.println("| 3) About this app   |");
        System.out.println("| 4) Quit             |");
        InputUtility.horLine();

        int choiceOne = InputUtility.getInputInt();
        switch (choiceOne) {
            case 1:
                login();
                break;
            case 2:
                registerNewUser();
                break;
            case 3:
                openAbout();
                break;
            case 4:
                quitApp();
                break;
        }
    }

    public void openSubMenu() {
        InputUtility.horLine();
        System.out.println("| 1) Open Friendlist   |");
        System.out.println("| 2) Add New Friend    |");
        System.out.println("| 3) Open General Chat |");
        System.out.println("| 4) Open Settings     |");
        System.out.println("| 5) About this app    |");
        System.out.println("| 6) Quit              |");
        InputUtility.horLine();

        int choiceTwo = InputUtility.getInputInt();
        switch (choiceTwo) {
            case 1:
                pendingRequests();
                openFriendList();
                break;
            case 2:
                searchUsers();
                break;
            case 3:
                openGeneralChat();
                break;
            case 4:
                openSettings();
                break;
            case 5:
                openAbout();
                break;
            case 6:
                quitApp();
                break;
        }
    }


    private void login() {
        User user = userUtility.getUserByNameEmail();
        boolean con = false;
        System.out.println(colorize("Enter your Password", GREEN_TEXT()));
        String password = InputUtility.getInputString();
        outer:
        while (!con) {
            if (BCrypt.checkpw(password, user.getPassword())) {
                con = true;
                System.out.println("Welcome " + user.getUserName());
                mainUserId = user.getUniqueId();
                openSubMenu();
            } else {
                System.out.println(colorize("The entered password is incorrect", RED_TEXT()));
                System.out.println("---------------------------------");
                System.out.println("| 1) Try again                  |");
                System.out.println("| 2) Back to the start-up Menu  |");
                System.out.println("---------------------------------");
                int choiceThree = InputUtility.getInputInt();
                switch (choiceThree) {
                    case 1:
                        System.out.println(colorize("Enter your Password", GREEN_TEXT()));
                        password = InputUtility.getInputString();
                        break;
                    case 2:
                        openMainMenu();
                        break outer;
                }
            }
        }
    }

    private void registerNewUser() {
        boolean nameIsUnique = false;
        boolean emailIsUnique = false;
        String name = null;
        String email = null;
        System.out.println(colorize("Please enter a Name", GREEN_TEXT()));
        while (!nameIsUnique) {
            name = InputUtility.getInputString();
            int a = userUtility.checkNameAvailability(name);
            if (a == 0) {
                System.out.println(colorize("Name already in use, please enter a different one", RED_TEXT()));
            } else {
                System.out.println("You have chosen the name: " + name);   //TODO change mind option
                nameIsUnique = true;
            }
        }

        System.out.println(colorize("Please enter your Email", GREEN_TEXT()));
        while (!emailIsUnique) {
            email = InputUtility.getInputString();
            int b = userUtility.checkEmailAvailability(email);
            outer:
            if (b == 0) {
                System.out.println(colorize("\n The " + email + " is already in use", RED_TEXT()));
                System.out.println("--------------------------------------------");
                System.out.println("| 1) Find name associated with this email  |");
                System.out.println("| 2) Enter a different email               |");
                System.out.println("| 3) Back to the start-up Menu             |");
                System.out.println("--------------------------------------------");
                int choiceEmail = InputUtility.getInputInt();
                switch (choiceEmail) {
                    case 1:
                        User user = userUtility.findUserAssociatedWithEmail(email);
                        System.out.println("The account associated with the " + email + " is: " + user.getUserName());
                        System.out.println("You will be directed back to the Start-up Menu");
                        openMainMenu();
                        break;
                    case 2:
                        System.out.println(colorize("Please enter your Email", GREEN_TEXT()));
                        break outer;
                    case 3:
                        openMainMenu();
                        break outer;
                }
            } else {
                System.out.println("Your email is: " + email);   //TODO change mind option
                emailIsUnique = true;
            }
        }

        boolean passwordCorrect = false;
        System.out.println(colorize("Please enter your password", GREEN_TEXT()));
        String password = InputUtility.getInputString();

        System.out.println(colorize("Please confirm your password", GREEN_TEXT()));
        String passwordConfirm = InputUtility.getInputString();

        while (!passwordCorrect) {
            if (password.equals(passwordConfirm)) {
                System.out.println("Your account has been created, welcome! You will be redirected to the Main Menu.");
                passwordCorrect = true;
            } else {
                System.out.println(colorize("The passwords you entered did not match, please try again.", RED_TEXT()));
                System.out.println(colorize("Please enter your password", GREEN_TEXT()));
                password = InputUtility.getInputString();

                System.out.println(colorize("Please confirm your password", GREEN_TEXT()));
                passwordConfirm = InputUtility.getInputString();
            }
        }
        String hashedPass = hashPassword(password);

        userUtility.addUser(name, hashedPass, email);
        openSubMenu();
    }

    private void searchUsers() {
        int c = 1;
        System.out.println(colorize("Please enter the name of the user you're searching", GREEN_TEXT()));
        String userSearch = InputUtility.getInputString();
        List<User> userList = userUtility.findUsers(userSearch);
        System.out.println("Application users with a similar name: ");
        for (User user : userList) {
            System.out.println(c++ + ") " + user.getUserName());
        }
        System.out.println(colorize("Please select the user you want to add to your friendlist", GREEN_TEXT()));
        int choice = InputUtility.getInputInt();
        if (choice < 1 || choice > userList.size()) {
            System.out.println(colorize("Please enter a valid number", RED_TEXT()));
            choice = InputUtility.getInputInt();
        } else {
            User user = userList.get(choice - 1);
            System.out.println("You have send a friend request to: " + user.getUserName());
            friendUtility.makeFriendRequest(mainUserId, user.getUniqueId(), true);   //TODO review
            openSubMenu();
        }
    }

    public void pendingRequests() {
        List<Request> friendRequests = friendUtility.checkFriendRequests(mainUserId);
        if (friendRequests.size() == 0) {
            System.out.println("You dont have any new friend requests");
        } else {
            int requestCount = friendRequests.size();
            System.out.println("You have " + requestCount + " friend requests pending");
            for (Request el : friendRequests) {
                long idSender = el.getIdSender();
                User userSender = userUtility.getUserById(idSender);
                System.out.println("Friend request from " + userSender.getUserName());
                System.out.println("------------------------------");
                System.out.println("| 1) Accept Friend Request   |");
                System.out.println("| 2) Refuse Friend Request   |");
                System.out.println("| 3) Decide Later            |");
                System.out.println("| 4) Block User              |");
                System.out.println("------------------------------");
                int newChoice = InputUtility.getInputInt();
                switch (newChoice) {
                    case 1:
                        acceptFriend(userSender.getUniqueId(), el);
                        break;    //TODO does this work?
                    case 2:
                        //refuseFriend(); //TODO !!
                        break;
                    case 3:
                        break;              //TODO !!
                    case 4:                 //TODO !!
                        break;
                }
            }
        }
    }



    private void acceptFriend(long userTwoId, Request request) {
        friendUtility.acceptRequest(mainUserId, userTwoId, request);
        System.out.println("Friend has been added!");
    }

    private void openAbout() {
        System.out.println("About this app .... etc etc");
        //TODO

    }

    private void openSettings() {
        System.out.println("------------------------");
        System.out.println("| 1) History Messages  |");
        System.out.println("| 2) Back to Menu      |");
        System.out.println("------------------------");
        int choiceThree = InputUtility.getInputInt();
        switch (choiceThree) {
            case 1:
                changeAmountMessages();
                break;
            case 2:
                openSubMenu();
                break;
        }

    }

    private void changeAmountMessages() {
        System.out.println("Set the amount of previous messages visible in a conversation");
        System.out.println("-------------------");
        System.out.println("| 1) 20 messages  |");
        System.out.println("| 2) 30 messages  |");
        System.out.println("| 3) 40 messages  |");
        System.out.println("-------------------");
        int choiceThree = InputUtility.getInputInt();
        switch (choiceThree) {
            case 1:
                messageUtility.setAmountOfMessages(mainUserId, 20);
                break;
            case 2:
                messageUtility.setAmountOfMessages(mainUserId, 30);
                break;
            case 3:
                messageUtility.setAmountOfMessages(mainUserId, 40);
                break;
        }

        openSubMenu();

    }

    private void openGeneralChat() {
        System.out.println("Coming soon");
        //TODO

    }

    private void openFriendList() {
        int count = 1;
        List<User> userFriendsList = friendUtility.getFriendsList(mainUserId);
        if (userFriendsList.size()==0){
            System.out.println("You dont have any friends yet, you can search\nfor people and add them as Friends from the Main Menu.");
            openSubMenu();
        } else {
            for (User el : userFriendsList) {
                System.out.println(count++ + ") " + el.getUserName());
            }
            System.out.println(colorize("Select a friend", GREEN_TEXT()));
            int choiceUser = InputUtility.getInputInt();
            if (choiceUser < 1 || choiceUser > userFriendsList.size()) {
                System.out.println(colorize("Please enter a valid number", RED_TEXT()));
                choiceUser = InputUtility.getInputInt();
            } else {
                System.out.println("-------------------------------");
                System.out.println("| 1) Start Chatting           |");
                System.out.println("| 2) Get Fun Statistics       |");
                System.out.println("| 3) Remove from Friendslist  |");
                System.out.println("| 4) Back to Main Menu        |");
                System.out.println("-------------------------------");
                int friendChoice = InputUtility.getInputInt();
                switch (friendChoice) {
                    case 1:
                        startChat(mainUserId, userFriendsList.get(choiceUser - 1).getUniqueId());
                        break;
                    case 2:
                        getStats();
                        break;
                    case 3:
                        removeFriend(mainUserId, userFriendsList.get(choiceUser - 1).getUniqueId());
                        break;
                    case 4:
                        openSubMenu();
                        break;
                }
            }
        }
    }


    private void removeFriend(long mainUserId, long idFriend) {
        friendUtility.removeFriend(mainUserId, idFriend);
    }

    private void getStats() {
        System.out.println("TODO");
    }

    private void startChat(long userOneId, long userTwoId) { //TODO Add TimerTask so that incoming chat changes every like two seconds
        boolean keepChatting = true;
        long conversationId = messageUtility.doesConversationExist(userOneId, userTwoId);
        while (keepChatting) {
            System.out.println(colorize("Start Messaging \n(type QuitChat to return to Main Menu) ", BLUE_TEXT()));        //TODO add /commands
            String textMessage = "";
            textMessage += InputUtility.getInputText();
            if (textMessage.toLowerCase(Locale.ROOT).equals("quitchat")) {
                keepChatting = false;
                openSubMenu();
            } else {
                messageUtility.writeMessage(conversationId, textMessage, mainUserId);
            }
        }
    }

    private void quitApp() {
        isRunning = true;

    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


}
