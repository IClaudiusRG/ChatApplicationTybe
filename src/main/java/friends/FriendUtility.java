package friends;

import messages.Conversation;
import users.User;
import utility.JpaHelper;
import users.UserUtility;
import java.util.ArrayList;
import java.util.List;

public class FriendUtility {

    JpaHelper jpaHelper = new JpaHelper();
    UserUtility userUtility = new UserUtility();

    public void makeFriendRequest(long idSender, long idReceiver, boolean pending) {
        jpaHelper.execute(eM -> {
            Request request = new Request(idSender, idReceiver, pending);
            eM.persist(request);
        });
    }

    public List<Request> checkFriendRequests(long idMainUser) {
        String fullQuery = "select r from Request r where r.idReceiver = " + idMainUser;
        return userUtility.executeTypedQuery(fullQuery, Request.class);
    }

    public void acceptRequest(long idUserOne, long idUserTwo, Request request) {
        if (idUserOne < idUserTwo) {
            jpaHelper.execute(eM -> {
                Friend friend = new Friend(idUserOne, idUserTwo);
                eM.persist(friend);
                deleteRequest(request);
            });
        } else {
            jpaHelper.execute(eM -> {
                Friend friend = new Friend(idUserTwo, idUserOne);
                eM.persist(friend);
                deleteRequest(request);
            });
        }
    }

    public void deleteRequest(Request request) {
        jpaHelper.execute(eM -> {
            eM.remove(eM.find(Request.class, request.getId()));
        });
    }

    public List<User> getFriendsList(long idMainUser) {
        List<User> userFriendsList = new ArrayList<>();
        //First part check
        String fullQueryOne = "select f from Friend f where f.idUserOne = " + idMainUser;
        List<Friend> listOfFriends = userUtility.executeTypedQuery(fullQueryOne, Friend.class);
        for (Friend el : listOfFriends) {
            User user = userUtility.getUserById(el.getIdUserTwo());
            userFriendsList.add(user);
        }
        //Second part check
        String fullQueryTwo = "select f from Friend f where f.idUserTwo = " + idMainUser;
        List<Friend> listOfFriendsTwo = userUtility.executeTypedQuery(fullQueryTwo, Friend.class);
        for (Friend el : listOfFriendsTwo) {
            User user = userUtility.getUserById(el.getIdUserOne());
            userFriendsList.add(user);
        }
        return userFriendsList;
    }

    public void removeFriend(long idMain, long idFriend) {

        long oneID;         //TODO rewrite simpler?
        long twoID;
        if (idFriend < idMain) {
            oneID = idFriend;
            twoID = idMain;
        } else {
            oneID = idMain;
            twoID = idFriend;
        }

        String fullQuery = "select f from Friend f where f.idUserOne = " + oneID + " AND f.idUserTwo = " + twoID;
        long idFriendship = userUtility.executeTypedQuerySingle(fullQuery, Friend.class).getId();

        jpaHelper.execute(eM -> {
            eM.remove(eM.find(Friend.class, idFriendship));
            User friendToBeRemoved = eM.find(User.class, idFriend);
            System.out.println(friendToBeRemoved.getUserName() + " has been removed from your friend list.");
        });

    }

}
