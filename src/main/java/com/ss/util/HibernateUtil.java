package com.ss.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.ss.message.Constant;


public class HibernateUtil {

	private HibernateUtil() {
	}

	private static EntityManagerFactory entityManagerFactory;

	public static EntityManager getEntityManager() {
		if (null == entityManagerFactory) {
			entityManagerFactory = Persistence.createEntityManagerFactory(Constant.PERISTANCE_UNIT_NAME);
		}
		return entityManagerFactory.createEntityManager();
	}

	public static void beginTransaction(EntityManager entityManager) {
		entityManager.getTransaction().begin();
	}

	public static void commitTransaction(EntityManager entityManager) {
		entityManager.getTransaction().commit();
	}

	public static void rollbackTransaction(EntityManager entityManager) {
		if (entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().rollback();
		}
	}

	public static void closeEntityManager(EntityManager entityManager) {
		if (null != entityManager && entityManager.isOpen()) {
			entityManager.close();
		}
	}
}