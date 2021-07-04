package users;

import friends.Request;
import org.mindrot.jbcrypt.BCrypt;
import utility.InputUtility;
import utility.JpaHelper;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class UserUtility {

    JpaHelper jpaHelper = new JpaHelper();

    public <T> List<T> executeTypedQuery(String query, Class<T> type) {
        AtomicReference<List<T>> outputList = new AtomicReference<>();
        jpaHelper.execute(em -> {
            TypedQuery<T> typedQuery = em.createQuery(query, type);
            outputList.set(typedQuery.getResultList());
        });
        return outputList.get();

    }

    public User executeTypedQuerySingle(String query) {
        AtomicReference<User> user = new AtomicReference<>();
        jpaHelper.execute(em -> {
            TypedQuery<User> typedQuery = em.createQuery(query, User.class);
            user.set(typedQuery.getSingleResult());
        });
        return user.get();

    }

    public <T> T executeTypedQuerySingle(String query, Class<T> typeCOfClass) {
        AtomicReference<T> object = new AtomicReference<>();
        jpaHelper.execute(em -> {
            TypedQuery<T> typedQuery = em.createQuery(query, typeCOfClass);
            object.set(typedQuery.getSingleResult());
        });
        return object.get();

    }

    public User getUserById(long id) {
        AtomicReference<User> user = new AtomicReference<>();
        jpaHelper.execute(em -> {
            user.set(em.find(User.class, id));
        });

        return user.get();
    }

    public User getUserByNameEmail() { // TODO add email as login?
        boolean correct = false;
        System.out.println("Enter User Name or Email");
        String userNameEmail = InputUtility.getInputString();
        User user = null;
        while (!correct){
            try {
                String queryName = "select u from User u where u.userName = " + "'" + userNameEmail + "'";
                user = executeTypedQuerySingle(queryName);
                correct = true;
            } catch (NoResultException nre) {
                try {
                    String queryEmail = "select u from User u where u.email = " + "'" + userNameEmail + "'";
                    user = executeTypedQuerySingle(queryEmail);
                    correct = true;
                } catch (NoResultException nre2) {
                    System.out.println("Your name or email can't be found");
                    System.out.println("Enter User Name or Email");
                    userNameEmail = InputUtility.getInputString();
                }
            }
        }
        return user;
    }

    public List<User> findUsers(String name) {
        String fullQuery = "select u from User u where u.userName like " + "'%" + name + "%'";     //TODO change column names + make sql safe!! no stringbuilding
        return executeTypedQuery(fullQuery, User.class);

    }

    public User findUserAssociatedWithEmail(String emailName) {
        String fullQuery = "select u from User u where u.email = " + "'" + emailName + "'";
        return executeTypedQuerySingle(fullQuery, User.class);

    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void addUser(String userName, String password, String email) {
        JpaHelper jpaHelper = new JpaHelper();

        jpaHelper.execute(eM -> {
            User user = new User(userName, password, email, LocalDateTime.now());
            eM.persist(user);
        });

        System.out.println("User saved");
    }

    public void removeUser(long id) {
        jpaHelper.execute(eM -> {
            eM.remove(eM.find(User.class, id));
        });
    }

    public int checkNameAvailability(String name) {
        try {
            String queryName = "select u from User u where u.userName = " + "'" + name + "'";
            User user = executeTypedQuerySingle(queryName);
        } catch (NoResultException nre) {
            return -1;
        }
        return 0;
    }

    public int checkEmailAvailability(String email) {
        try {
            String queryName = "select u from User u where u.email = " + "'" + email + "'";
            User user = executeTypedQuerySingle(queryName);
        } catch (NoResultException nre) {
            return -1;
        }
        return 0;
    }

    public void removeRequest(long id) {
        jpaHelper.execute(eM -> {
            eM.remove(eM.find(Request.class, id));
        });
    }
}
