package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.lib.Dao;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

@Dao
public class UserDaoImpl implements UserDao {
    @Override
    public User add(User user) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            System.out.println("INFO: User successfully saved to database: " + user.getEmail());
            return user;
        } catch (ConstraintViolationException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("ERROR: Constraint violation while saving user: " 
                    + user.getEmail());
            throw new RuntimeException("User with email " + user.getEmail() + " already exists", e);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("ERROR: Failed to save user to database: " + user.getEmail() 
                    + " - " + e.getMessage());
            throw new RuntimeException("Can't save User into DB: " + user.getEmail(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Optional<User> result = session.createQuery(
                    "FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email).uniqueResultOptional();
            if (result.isPresent()) {
                System.out.println("INFO: User found by email: " + email);
            } else {
                System.out.println("INFO: No user found with email: " + email);
            }
            return result;
        } catch (Exception e) {
            System.err.println("ERROR: Failed to find user by email: " + email 
                    + " - " + e.getMessage());
            throw new RuntimeException("Can't find User by email: " + email, e);
        }
    }
}
