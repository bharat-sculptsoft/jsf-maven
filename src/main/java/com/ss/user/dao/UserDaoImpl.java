package com.ss.user.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.ss.exception.DaoLayerException;
import com.ss.message.MessageConstant;
import com.ss.message.MessageProvider;
import com.ss.user.entity.User;
import com.ss.util.HibernateUtil;

@ManagedBean(name = "userDao")
@ApplicationScoped
public class UserDaoImpl implements UserDao {

	@Override
	public User findByEmail(String email) throws DaoLayerException {
		EntityManager entityManager = HibernateUtil.getEntityManager();
		try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
			Root<User> user = criteriaQuery.from(User.class);

			criteriaQuery.where(criteriaBuilder.equal(user.get("email"), email));

			return entityManager.createQuery(criteriaQuery).getResultList().stream().findFirst().orElse(null);
		} catch (Exception e) {
			throw new DaoLayerException(MessageProvider.getMessageString(MessageConstant.USER_DATA_FETCH_FAIL, null));
		} finally {
			HibernateUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public User findByEmailAndPassword(String email, String password) throws DaoLayerException {
		EntityManager entityManager = HibernateUtil.getEntityManager();
		try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
			Root<User> user = criteriaQuery.from(User.class);

			Predicate predicate = criteriaBuilder.and(criteriaBuilder.equal(user.get("email"), email),
					criteriaBuilder.equal(user.get("password"), password));
			criteriaQuery.where(predicate);

			return entityManager.createQuery(criteriaQuery).getResultList().stream().findFirst().orElse(null);
		} catch (Exception e) {
			throw new DaoLayerException(MessageProvider.getMessageString(MessageConstant.USER_DATA_FETCH_FAIL, null));
		} finally {
			HibernateUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public void save(User user) throws DaoLayerException {
		EntityManager entityManager = HibernateUtil.beginTransaction();
		try {
			entityManager.persist(user);
			HibernateUtil.commitTransaction(entityManager);
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction(entityManager);
			throw new DaoLayerException(MessageProvider.getMessageString(MessageConstant.USER_DATA_SAVE_FAIL, null));
		} finally {
			HibernateUtil.closeEntityManager(entityManager);
		}
	}

}
