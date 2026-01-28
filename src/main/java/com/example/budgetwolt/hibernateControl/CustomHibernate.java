package com.example.budgetwolt.hibernateControl;

import com.example.budgetwolt.models.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomHibernate extends GenericHibernate {
    public CustomHibernate(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public User validateLogin(String username, String password) {
        User user = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root).where(cb.equal(root.get("username"), username));

            Query q = entityManager.createQuery(query);

            user = (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(entityManager != null) {
                entityManager.close();
            }
        }

        if (user != null) {
            System.out.println("HELLO PEOPLE!!!!!\n\n");
        }

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    public boolean isUniqueUsername(String username) {
        User user = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root).where(cb.equal(root.get("username"), username));
            Query q = entityManager.createQuery(query);

            user = (User) q.getSingleResult();
        } catch (NoResultException e) {
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if(entityManager != null) {
                entityManager.close();
            }
        }

        return false;
    }

    public List<User> searchUsers(String username, String name, String surname, String phoneNumber) {
        List<User> users = new ArrayList<>();

        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            List<Predicate> predicates = new ArrayList<>();

            if (username != null && !username.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
            }

            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (surname != null && !surname.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("surname")), "%" + surname.toLowerCase() + "%"));
            }

            if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                predicates.add(cb.like(root.get("phoneNumber"), "%" + phoneNumber + "%"));
            }

            query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

            users = entityManager.createQuery(query).getResultList();

        } catch (Exception e) {
            // Some error
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return users;
    }

    public void deleteFoodOrder(int id) {
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            FoodOrder foodOrder = entityManager.find(FoodOrder.class, id);

            if (foodOrder != null) {
                BasicUser buyer = foodOrder.getBuyer();
                buyer.getMyOrders().remove(foodOrder);
                entityManager.merge(buyer);
            }

            entityManager.remove(foodOrder);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public List<FoodOrder> searchOrders(Restaurant restaurant, OrderStatus orderStatus, BasicUser client, LocalDate fromDate, LocalDate toDate) {
        List<FoodOrder> orders = new ArrayList<>();

        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<FoodOrder> query = cb.createQuery(FoodOrder.class);
            Root<FoodOrder> root = query.from(FoodOrder.class);

            List<Predicate> predicates = new ArrayList<>();

            if (restaurant != null) {
                predicates.add(cb.equal(root.get("restaurant"), restaurant));
            }
            if (orderStatus != null) {
                predicates.add(cb.equal(root.get("status"), orderStatus));
            }
            if (client != null) {
                predicates.add(cb.equal(root.get("buyer"), client));
            }

            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateCreated"), fromDate));
            }
            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dateCreated"), toDate));
            }

            if (!predicates.isEmpty()) {
                query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
            } else {
                query.select(root);
            }

            orders = entityManager.createQuery(query).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
        return orders;
    }
}
