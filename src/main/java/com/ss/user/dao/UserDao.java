package com.ss.user.dao;

import javax.persistence.EntityManager;

import com.ss.exception.DaoLayerException;
import com.ss.user.entity.User;

public interface UserDao {

	public User findByEmail(EntityManager entityManager,String email) throws DaoLayerException;

	public User findByEmailAndPassword(EntityManager entityManager,String email, String password) throws DaoLayerException;

	public void save(EntityManager entityManager,User user) throws DaoLayerException;

}
