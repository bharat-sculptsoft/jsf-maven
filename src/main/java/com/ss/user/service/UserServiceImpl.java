package com.ss.user.service;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.ss.exception.DaoLayerException;
import com.ss.exception.NotFoundException;
import com.ss.exception.ServiceLayerException;
import com.ss.message.MessageConstant;
import com.ss.message.MessageProvider;
import com.ss.user.dao.UserDao;
import com.ss.user.entity.User;
import com.ss.util.JwtUtil;

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
			JwtUtil.storeTokenInCookie(token);
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
