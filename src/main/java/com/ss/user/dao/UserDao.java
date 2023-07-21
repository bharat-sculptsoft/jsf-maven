package com.ss.user.dao;

import com.ss.common.exception.DaoLayerException;
import com.ss.user.entity.User;

public interface UserDao {

	public User findByEmail(String email) throws DaoLayerException ;

	public User findByEmailAndPassword(String email, String password) throws DaoLayerException ;

	public void save(User user) throws DaoLayerException ;

}
