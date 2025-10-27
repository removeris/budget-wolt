package com.example.budgetwolt.hibernateControl;

import com.example.budgetwolt.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.ArrayList;
import java.util.List;

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

    public <T> void update(T entity) {
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } catch(Exception e) {
            // later
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    public <T> void delete(T entity) {
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            T managedEntity = entityManager.merge(entity);
            entityManager.remove(managedEntity);
            entityManager.getTransaction().commit();
        } catch(Exception e) {
            System.out.println(e);
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    public <T> T getEntityById(Class<T> entityClass, int id) {

        T entity = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entity = entityManager.find(entityClass, id);
            entityManager.getTransaction().commit();
        } catch(Exception e) {
            System.out.println("ERROR!");
        } finally {
            if (entityManager != null)
                entityManager.close();
        }

        return entity;
    }

    public <T> List<T> getAllRecords(Class<T> entityClass) {
        List<T> list = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            CriteriaQuery query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(entityClass));
            Query q = entityManager.createQuery(query);
            list = q.getResultList();
        } catch (Exception e) {
            // alert
        }

        return list;
    }
}
