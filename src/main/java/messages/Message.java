package messages;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "APP_Message")
public class Message {

    @Id
    @GeneratedValue
    private long id;

    private String textOfMessage;

    private LocalDateTime timestamp;

    private long idSender;          //TODO is there an better option?

    @ManyToOne
    private Conversation conversation;

    public Message() {

    }

    public Message(String textOfMessage, LocalDateTime timestamp, long idSender) {
        this.textOfMessage = textOfMessage;
        this.timestamp = timestamp;
        this.idSender = idSender;

    }

    public String getTextOfMessage() {
        return textOfMessage;
    }

    public void setTextOfMessage(String textOfMessage) {
        this.textOfMessage = textOfMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public long getId() {
        return id;
    }

    public long getIdSender() {
        return idSender;
    }
}
