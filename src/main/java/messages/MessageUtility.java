package messages;

import users.User;
import users.UserUtility;
import utility.JpaHelper;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

public class MessageUtility {

    JpaHelper jpaHelper = new JpaHelper();
    UserUtility userUtility = new UserUtility();

    public long startConversation(long userOneId, long userTwoId) {
        AtomicLong id = new AtomicLong();
        jpaHelper.execute(eM -> {
            Conversation conversation = null;
            if (userOneId < userTwoId) {
                conversation = new Conversation(userOneId, userTwoId, 20);
            } else {
                conversation = new Conversation(userTwoId, userOneId, 20);
            }
            eM.persist(conversation);
            id.set(conversation.getId());
        });

        return id.get();
    }

    public void writeMessage(long idConvo, String textMessage, long idSender) {
        jpaHelper.execute(eM -> {
            Conversation conversation = eM.find(Conversation.class, idConvo);
            Message message = new Message(textMessage, LocalDateTime.now(), idSender);
            conversation.addMessage(message);
            eM.persist(conversation);
        });

    }

    public Conversation getConversation(long id) {
        AtomicReference<Conversation> c = new AtomicReference<>();
        jpaHelper.execute(eM -> {
            c.set(eM.find(Conversation.class, id));
        });
        return c.get();
    }

    public void trimMessagesConvo(long idConvo, int sizeMessages) {           //TODO add sizemessage here
        jpaHelper.execute(eM -> {
            //Limiting history to x amount of messages
            List<Message> listMessages = null;
            boolean listTrimmed = false;
            while (!listTrimmed) {
                Conversation convo = eM.find(Conversation.class, idConvo);
                listMessages = convo.getMessages();
                if (listMessages.size() > sizeMessages) {
                    List<Long> idMessages = new ArrayList<>();
                    for (Message el : listMessages) {
                        idMessages.add(el.getId());
                    }
                    Collections.sort(idMessages);
                    Long idMessageToBeDeleted = idMessages.get(0);
                    convo.removeMessage(eM.find(Message.class, idMessageToBeDeleted));

                    eM.remove(eM.find(Message.class, idMessageToBeDeleted));
                } else {
                    listTrimmed = true;
                }
            }
        });

        printMessages(idConvo);

    }

    public void printMessages(long idConvo) {
        jpaHelper.execute(eM -> {
            Conversation convo = eM.find(Conversation.class, idConvo);
            List<Message> listMessages = convo.getMessages();

            for (Message el : listMessages) {
                User user = eM.find(User.class, el.getIdSender());

                //Formatting time stamp
                LocalDateTime ldt = el.getTimestamp();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");
                String formattedTimeStamp = ldt.format(formatter);

                System.out.println("\t-----------  " + colorize(user.getUserName(), GREEN_TEXT()) + "  -----------");
                System.out.println(formattedTimeStamp + "   " + colorize(el.getTextOfMessage() + "\n", BLUE_TEXT()));
            }
        });
    }

    public long doesConversationExist(long userOneId, long userTwoId) {

        long oneID;         //TODO rewrite simpler?
        long twoID;
        if (userOneId < userTwoId) {
            oneID = userOneId;
            twoID = userTwoId;
        } else {
            oneID = userTwoId;
            twoID = userOneId;
        }

        try {
            String fullQuery = "select c from Conversation c where c.userOneID = " + oneID + " AND c.userTwoID = " + twoID;
            long idConvo = userUtility.executeTypedQuerySingle(fullQuery, Conversation.class).getId();
            AtomicInteger sizeOfMessages = new AtomicInteger();
            jpaHelper.execute(eM -> {
               Conversation conversation = eM.find(Conversation.class, idConvo);
               sizeOfMessages.set(conversation.getSizeMessages());

            });

            trimMessagesConvo(idConvo, sizeOfMessages.get());

            return userUtility.executeTypedQuerySingle(fullQuery, Conversation.class).getId();
        } catch (NoResultException nre) {
            System.out.println("Conversation doesnt exist yet, creating a new one.");
            return startConversation(oneID, twoID);
        }
    }

    public void removeMessage(long id) {
        jpaHelper.execute(eM -> {
            eM.remove(eM.find(Message.class, id));
        });
    }

    public void setAmountOfMessages(long idMainUser, int sizeMessages) {
        String fullQuery = "select c from Conversation c where c.userOneID = " + idMainUser + " OR c.userTwoID = " + idMainUser;
        long idCon = userUtility.executeTypedQuerySingle(fullQuery, Conversation.class).getId();
        bulkUpdate("update Conversation c set c.sizeMessages = " + sizeMessages + " where c.id = " + idCon);        //TODO query wont work prob
    }

    public void bulkUpdate(String query) {
        jpaHelper.execute(eM -> {
            Query queryUpdate = eM.createQuery(query);
            int result = queryUpdate.executeUpdate();
        });
    }
}
