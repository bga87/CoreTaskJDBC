package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Table;
import java.util.Collections;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static EntityManagerFactory entityManagerFactory;
    private final String tableName = User.class.getAnnotation(Table.class).name();

    public UserDaoHibernateImpl() {
        try {
            entityManagerFactory =  Util.getEntityManagerFactory();
        } catch (Exception ex) {
            System.err.printf("При попытке соединения с БД возникла ошибка:%n");
            System.err.println("\t==>" + ex.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void createUsersTable() {
        EntityManager entityManager = null;
        EntityTransaction tx = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            entityManager.createNativeQuery(
                    "CREATE TABLE IF NOT EXISTS " + tableName + "(id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(20) NOT NULL, lastname VARCHAR(35) NOT NULL, age TINYINT(3) UNSIGNED);")
                    .executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        EntityManager entityManager = null;
        EntityTransaction tx = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            entityManager.createNativeQuery("DROP TABLE IF EXISTS " + tableName + ';').executeUpdate();
            tx.commit();
        }  catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        EntityManager entityManager = null;
        EntityTransaction tx = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            entityManager.persist(new User(name, lastName, age));
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        EntityManager entityManager = null;
        EntityTransaction tx = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            entityManager.remove(entityManager.getReference(User.class, id));
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        List<User> result = Collections.emptyList();

        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            result = entityManager.createQuery("from User", User.class).getResultList();
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return result;
    }

    @Override
    public void cleanUsersTable() {
        EntityManager entityManager = null;
        EntityTransaction tx = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            entityManager.createQuery("delete from User").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}