package com.example.budgetwolt.hibernateControl;

import com.example.budgetwolt.models.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class CustomHibernate extends GenericHibernate {
    public CustomHibernate(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public User getUserByCredentials(String username, String password) {
        User user = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root).where(cb.and(cb.equal(root.get("username"), username)), cb.equal(root.get("password"), password));
            Query q = entityManager.createQuery(query);

            user = (User) q.getSingleResult();
        } catch (Exception e) {
            // alert spot
        } finally {
            if(entityManager != null) {
                entityManager.close();
            }
        }

        return user;
    }
}
