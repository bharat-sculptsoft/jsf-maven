package com.ss.product.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ss.exception.DaoLayerException;
import com.ss.message.Constant;
import com.ss.message.MessageConstant;
import com.ss.message.MessageProvider;
import com.ss.product.entity.ProductDetails;
import com.ss.product.service.ProductServiceImpl;
import com.ss.user.entity.User;

import lombok.Data;

@ManagedBean(name = "productDao")
@ApplicationScoped
@Data
public class ProductDaoImpl implements ProductDao {
	private static final Logger logger = LogManager.getLogger(ProductDaoImpl.class);
	@Override
	public void save(EntityManager entityManager,ProductDetails product) throws DaoLayerException {
		try {
			logger.debug("ProductDaoImpl: Save: "+product.toString());
			//product.setId(1);
			entityManager.persist(product);
			logger.debug("ProductDaoImpl: Save2: "+product.toString());
			entityManager.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoLayerException(MessageProvider.getMessageString(MessageConstant.USER_DATA_SAVE_FAIL, null));
		}
	}
	@Override
	public List<ProductDetails> findAll(EntityManager entityManager) throws DaoLayerException {
		try {

		    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<ProductDetails> criteriaQuery = criteriaBuilder.createQuery(ProductDetails.class);
			Root<ProductDetails> rootEntry = criteriaQuery.from(ProductDetails.class);
			CriteriaQuery<ProductDetails> all=criteriaQuery.select(rootEntry);
			TypedQuery<ProductDetails> allQuery = entityManager.createQuery(all);
			return allQuery.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoLayerException(MessageProvider.getMessageString(MessageConstant.USER_DATA_FETCH_FAIL, null));
		}
	}

}