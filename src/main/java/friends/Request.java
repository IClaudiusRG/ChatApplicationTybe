package friends;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APP_Request")
public class Request {

    @Id
    @GeneratedValue
    private long id;

    private long idSender;

    private long idReceiver;

    private boolean pending;

    public Request() {

    }

    public Request(long idSender, long idReceiver, boolean pending){
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.pending = pending;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public long getId() {
        return id;
    }

    public long getIdReceiver() {
        return idReceiver;
    }

    public long getIdSender() {
        return idSender;
    }
}
