package com.ss.user.dao.impl;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import com.ss.common.utility.HibernateUtil;
import com.ss.user.dao.UserDao;
import com.ss.user.pojo.User;

@ManagedBean
@ApplicationScoped
public class UserDaoImpl implements UserDao {

	@Override
	public User findByEmail(String email) {
		EntityManager entityManager = HibernateUtil.beginTransaction();
		try {
			String queryString = "SELECT u FROM User u WHERE u.email = :email";
			User result= entityManager.createQuery(queryString,User.class).setParameter("email", email).getSingleResult();
			HibernateUtil.commitTransaction(entityManager);
			return result;
		}catch (Exception e) {
			HibernateUtil.rollbackTransaction(entityManager);
			throw e;
		}
	}
	@Override
	public User findByEmailAndPassword(String email, String password) {
		EntityManager entityManager = HibernateUtil.beginTransaction();
		try {
			String queryString = "SELECT u FROM User u WHERE u.email = :email and u.password=: password";
			User result= entityManager.createQuery(queryString,User.class).setParameter("email", email).setParameter("password", password).getResultList().stream().findFirst().orElse(null);
			HibernateUtil.commitTransaction(entityManager);
			return result;
		}catch (Exception e) {
			HibernateUtil.rollbackTransaction(entityManager);
			throw e;
		}
	}

	@Override
	public void save(User user) {
		
		EntityManager entityManager = HibernateUtil.beginTransaction();

		try {
			entityManager.persist(user);
			HibernateUtil.commitTransaction(entityManager);

		}catch (Exception e) {
			HibernateUtil.rollbackTransaction(entityManager);
			throw e;
		}
		
	}

	
	/*
	 * private void beginTransaction() { try {
	 * entityManager.getTransaction().begin(); } catch (IllegalStateException e) {
	 * rollBackTransaction(); } }
	 * 
	 * private void commitTransaction() { try {
	 * entityManager.getTransaction().commit(); } catch (IllegalStateException |
	 * RollbackException e) { rollBackTransaction(); } }
	 * 
	 * private void rollBackTransaction() { try {
	 * entityManager.getTransaction().rollback(); } catch (IllegalStateException |
	 * PersistenceException e) { e.printStackTrace(); } }
	 */
}
