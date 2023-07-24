package com.ss.user.service;


import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ss.exception.DaoLayerException;
import com.ss.exception.ServiceLayerException;
import com.ss.exception.ValidationFailedException;
import com.ss.message.MessageConstant;
import com.ss.message.MessageProvider;
import com.ss.user.dao.UserDao;
import com.ss.user.entity.User;
import com.ss.util.JwtUtil;
import com.ss.util.PasswordHashingUtil;

import lombok.Data;

@ManagedBean(name = "userService")
@ApplicationScoped
@Data
public class UserServiceImpl implements UserService {

	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	@ManagedProperty(value = "#{userDao}")
	private UserDao userDao;
//
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public boolean authenticate(String email, String password) throws ServiceLayerException {

		try {
			logger.trace("We've just greeted the user!");
			logger.debug("We've just greeted the user! 1");
			logger.info("We've just greeted the user!");
			logger.warn("We've just greeted the user!");
			logger.error("We've just greeted the user!");
			logger.fatal("We've just greeted the user!");
			User user = userDao.findByEmail(email);

			if (null != user && PasswordHashingUtil.match(password, user.getPassword())) {
				String token = JwtUtil.generateToken(user.getEmail());
				JwtUtil.storeTokenInCookie(token);
				return true;
			}else {
				throw new ValidationFailedException(MessageProvider.getMessageString(MessageConstant.INCORRECT_USERNAME_PASSWORD, null));
			}

		} catch (DaoLayerException | ValidationFailedException e) {
			e.printStackTrace();
			throw new ServiceLayerException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceLayerException(
					MessageProvider.getMessageString(MessageConstant.INTERNAL_SERVER_ERROR, null));
		}

	}

	@Override
	public void logoutUser() throws ServiceLayerException {

		try {
			FacesContext context= FacesContext.getCurrentInstance();
			HttpServletResponse httpServletResponse = (HttpServletResponse) context.getExternalContext().getResponse();
			JwtUtil.removeTokenfromCookie(httpServletResponse);
			context.getExternalContext().invalidateSession();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceLayerException(
					MessageProvider.getMessageString(MessageConstant.INTERNAL_SERVER_ERROR, null));

		}
	}
	
}
