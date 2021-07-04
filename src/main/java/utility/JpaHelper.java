package utility;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class JpaHelper {

    public void execute(Consumer<EntityManager> action) {
        execute(em -> {
            action.accept(em);
            return null;
        });
    }

    public <T> T execute(Function<EntityManager, T> action) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            emf = Persistence.createEntityManagerFactory("chatapp");
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            return action.apply(em);
        } finally {
            if ((tx != null) && (tx.isActive())) {
                tx.commit();
            }
            if (em != null)
                em.close();
            if (emf != null)
                emf.close();
        }
    }

    public <T> T executeWithTx(BiFunction<EntityManager, EntityTransaction, T> action) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            emf = Persistence.createEntityManagerFactory("chatapp");
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            return action.apply(em, tx);
        } finally {
            if ((tx != null) && (tx.isActive())) {
                if (tx.getRollbackOnly()) {
                    tx.rollback();
                } else {
                    tx.commit();
                }
            }
            if (em != null)
                em.close();
            if (emf != null)
                emf.close();
        }
    }

}
