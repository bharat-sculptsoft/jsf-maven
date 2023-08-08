package com.ss.product.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.ss.exception.DaoLayerException;
import com.ss.product.entity.ProductDetails;
import com.ss.user.entity.User;

public interface ProductDao {

	public void save(EntityManager entityManager,ProductDetails product) throws DaoLayerException;
	public List<ProductDetails> findAll(EntityManager entityManager) throws DaoLayerException;
}
