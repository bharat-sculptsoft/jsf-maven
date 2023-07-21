package com.ss.common.utility;

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

	private static EntityManagerFactory entityManagerFactory;

	public static EntityManager getEntityManager() {
		if (entityManagerFactory == null) {
			entityManagerFactory = Persistence.createEntityManagerFactory("jsf-maven-project");
		}
		return entityManagerFactory.createEntityManager();
	}

	public static EntityManager beginTransaction() {
		EntityManager entityManager = getEntityManager();
		entityManager.getTransaction().begin();
		return entityManager;
	}

	public static void commitTransaction(EntityManager em) {
		em.getTransaction().commit();
	}
	
	public static void rollbackTransaction(EntityManager em) {
		em.getTransaction().rollback();
	}

}
