package com.example.budgetwolt.hibernateControl;

import com.example.budgetwolt.fxControllers.FxUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javafx.scene.control.Alert;

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
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(entityClass);
            Root<T> root = query.from(entityClass);
            query.select(root);
            Query q = entityManager.createQuery(query);
            list = q.getResultList();
        } catch (Exception e) {
            FxUtil.generateAlert(Alert.AlertType.ERROR, "ERROR", "EXCEPTION", e.getMessage());
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return list;
    }
}
