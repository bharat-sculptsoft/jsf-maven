package com.ss.user.service;

import com.ss.exception.ServiceLayerException;
import com.ss.user.entity.User;

public interface UserService {

	boolean authenticate(String email,String password) throws ServiceLayerException;

	void logoutUser() throws ServiceLayerException;
	
	void signUp(User user) throws ServiceLayerException;
}
