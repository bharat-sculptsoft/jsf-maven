package com.ss.user.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.ss.exception.DaoLayerException;
import com.ss.message.Constant;
import com.ss.message.MessageConstant;
import com.ss.message.MessageProvider;
import com.ss.user.entity.User;

import lombok.Data;

@ManagedBean(name = "userDao")
@ApplicationScoped
@Data
public class UserDaoImpl implements UserDao {

	@Override
	public User findByEmail(EntityManager entityManager, String email) throws DaoLayerException {
		try {

			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
			Root<User> user = criteriaQuery.from(User.class);

			criteriaQuery.where(criteriaBuilder.equal(user.get(Constant.EMAIL), email));

			return entityManager.createQuery(criteriaQuery).getResultList().stream().findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoLayerException(MessageProvider.getMessageString(MessageConstant.USER_DATA_FETCH_FAIL, null));
		}
	}

	@Override
	public User findByEmailAndPassword(EntityManager entityManager, String email, String password)
			throws DaoLayerException {
		try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
			Root<User> user = criteriaQuery.from(User.class);

			Predicate predicate = criteriaBuilder.and(criteriaBuilder.equal(user.get(Constant.EMAIL), email),
					criteriaBuilder.equal(user.get(Constant.PASSWORD), password));
			criteriaQuery.where(predicate);

			return entityManager.createQuery(criteriaQuery).getResultList().stream().findFirst().orElse(null);
		} catch (Exception e) {
			throw new DaoLayerException(MessageProvider.getMessageString(MessageConstant.USER_DATA_FETCH_FAIL, null));
		}
	}

	@Override
	public void save(EntityManager entityManager, User user) throws DaoLayerException {
		try {
			entityManager.persist(user);
			entityManager.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoLayerException(MessageProvider.getMessageString(MessageConstant.USER_DATA_SAVE_FAIL, null));
		}
	}
}