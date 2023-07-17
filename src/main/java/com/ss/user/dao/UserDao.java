package com.ss.user.dao;

import com.ss.user.pojo.User;

public interface UserDao {

	public User findByEmail(String email);

	public User findByEmailAndPassword(String email, String password);

	public void save(User user);

}
