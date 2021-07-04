package friends;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APP_Friend")
public class Friend {

    @Id
    @GeneratedValue
    private long id;

    private long idUserOne;

    private long idUserTwo;

    public Friend() {

    }

    public Friend(long idUserOne, long idUserTwo) {
        this.idUserOne = idUserOne;
        this.idUserTwo = idUserTwo;
    }

    public long getId() {
        return id;
    }

    public long getIdUserOne() {
        return idUserOne;
    }

    public long getIdUserTwo() {
        return idUserTwo;
    }
}
