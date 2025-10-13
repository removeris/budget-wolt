package com.example.budgetwolt.hibernateControl;

import com.example.budgetwolt.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class GenericHibernate {
    EntityManagerFactory entityManagerFactory;

    EntityManager entityManager;

    public GenericHibernate(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public <T> void create(T entity) {
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();

        } catch(Exception e) {
            // alerts if error
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }
}
