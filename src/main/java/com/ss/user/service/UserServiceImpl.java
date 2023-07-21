package com.ss.user.service;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import com.ss.common.exception.DaoLayerException;
import com.ss.common.exception.NotFoundException;
import com.ss.common.exception.ServiceLayerException;
import com.ss.common.util.MessageConstant;
import com.ss.common.util.MessageProvider;
import com.ss.jwt.util.JwtUtil;
import com.ss.user.dao.UserDao;
import com.ss.user.entity.User;

import lombok.Data;

@ManagedBean(name = "userService")
@ApplicationScoped
@Data
public class UserServiceImpl implements UserService {

	@ManagedProperty(value = "#{userDao}")
	private UserDao userDao;

	@Override
	public boolean authenticate(String email, String password) throws ServiceLayerException {

		try {
			User user = userDao.findByEmailAndPassword(email, password);

			if (null == user) {
				throw new NotFoundException(MessageProvider.getMessageString(MessageConstant.USER_NOT_FOUND, null));
			}

			String token = JwtUtil.generateToken(user.getEmail());
			JwtUtil.storeTokenAtClientSide(token);
			return true;
		} catch (DaoLayerException | NotFoundException e) {
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
			JwtUtil.removeTokenAtClientSide();
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceLayerException(
					MessageProvider.getMessageString(MessageConstant.INTERNAL_SERVER_ERROR, null));

		}
	}

}
