package users;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "APP_Users")
public class User {

    @Id
    @GeneratedValue
    private long uniqueId;

    private String userName;

    private String email;

    private String password;

    //TODO LocalDateTime replace with just LocalDate?
    private LocalDateTime dateOfRegistration;

    public User(){

    }

    public User(String userName, String password, String email, LocalDateTime dateOfRegistration){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.dateOfRegistration = dateOfRegistration;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDateTime dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public void sendFriendRequest(User user) {
        System.out.println("Do you want to be friends?"); //TODO
    }
}
