package com.ss.common.utility;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.ss.jwt.auth.AuthenticationBean;
import com.ss.user.dao.impl.UserDaoImpl;
import com.ss.user.pojo.User;

public class BeanUtil {
	private BeanUtil() {
		
	}

	public static void createApplicationBeanIfNotPresent(ServletContext servletContext,String beanName) {

		UserDaoImpl UserDaoBean = (UserDaoImpl) servletContext.getAttribute(beanName);
		if (null == UserDaoBean) {
			UserDaoBean = new UserDaoImpl();
			servletContext.setAttribute(beanName, UserDaoBean);
		}
		
	}

	public static void createRequestBeanIfNotPresent(HttpServletRequest httpRequest,String beanName,User loggedInUser) {
		AuthenticationBean authBean = (AuthenticationBean) getSessionBeanFromContext(httpRequest, beanName);
		if (null == authBean) {
			authBean = new AuthenticationBean();
			authBean.setLoggedInUser(loggedInUser);
			httpRequest.getSession().setAttribute(beanName, authBean);
		}
		
	}

	public static Object getApplicationBeanFromContext(HttpServletRequest httpRequest, String beanName) {
		return httpRequest.getSession().getServletContext().getAttribute(beanName);
	}

	public static Object getSessionBeanFromContext(HttpServletRequest httpRequest, String beanName) {
		return httpRequest.getSession().getAttribute(beanName);
	}
}
