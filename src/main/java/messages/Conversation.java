package messages;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "APP_Conversation")
public class Conversation {

    @Id
    @GeneratedValue
    private long id;

    private long userOneID;

    private long userTwoID;

    private int sizeMessages = 20;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.PERSIST)
    private List<Message> messages = new ArrayList<>();

    public Conversation () {

    }

    public Conversation(long userOneID, long userTwoID, int sizeMessages) {
        this.userOneID = userOneID;
        this.userTwoID = userTwoID;
        this.sizeMessages = sizeMessages;
    }

    public long getUserOneID() {
        return userOneID;
    }

    public void setUserOneID(long userOneID) {
        this.userOneID = userOneID;
    }

    public long getUserTwoID() {
        return userTwoID;
    }

    public void setUserTwoID(long userTwoID) {
        this.userTwoID = userTwoID;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public long getId() {
        return id;
    }

    public void addMessage(Message message){
        messages.add(message);
        message.setConversation(this);
    }

    public void removeMessage(Message message){
        messages.remove(message);
        message.setConversation(null);
    }

    public int getSizeMessages() {
        return sizeMessages;
    }

    public void setSizeMessages(int sizeMessages) {
        this.sizeMessages = sizeMessages;
    }
}
