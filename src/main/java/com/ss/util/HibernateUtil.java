package com.ss.util;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean
@ApplicationScoped
public class HibernateUtil {

  private HibernateUtil() {
  }

  private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jsf-maven-project");

  public static EntityManager getEntityManager() {
    return entityManagerFactory.createEntityManager();
  }
  
  public static void beginTransaction(EntityManager entityManager) {
    entityManager.getTransaction().begin();
  }

  public static void commitTransaction(EntityManager entityManager) {
    entityManager.getTransaction().commit();
  }
  
  public static void rollbackTransaction(EntityManager entityManager) {
    if(entityManager.getTransaction().isActive()) {
      entityManager.getTransaction().rollback();
    } 
  }

  public static void closeEntityManager(EntityManager entityManager) {
    if(entityManager != null && entityManager.isOpen()) {
      entityManager.close();
    }
  }
}