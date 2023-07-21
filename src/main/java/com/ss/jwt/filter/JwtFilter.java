package com.ss.jwt.filter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ss.common.utility.BeanUtil;
import com.ss.common.utility.MessageUtil;
import com.ss.jwt.auth.AuthenticationBean;
import com.ss.jwt.util.JwtUtil;
import com.ss.user.dao.UserDao;
import com.ss.user.pojo.User;

public class JwtFilter implements Filter, Serializable {
	public static final String LOGIN_PAGE = "/login";

	private static final String USER_DAO_BEAN_NAME = "userDaoImpl";
	private static final String AUTH_BEAN_NAME = "authenticationBean";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		BeanUtil.createApplicationBeanIfNotPresent(filterConfig.getServletContext(), USER_DAO_BEAN_NAME);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String token = extractTokenFromRequest(httpRequest);
		
		
		if (isLoginUrls(httpRequest)) {
			chain.doFilter(request, response);
			return;
		}
		if (null != token) {
			try {
				validateJwtTokenWithUser(request, response, chain, httpRequest, httpResponse, token);
			} catch (Exception e) {
				e.printStackTrace();
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
			}
		} else {
			httpResponse.sendRedirect(httpRequest.getContextPath() + LOGIN_PAGE);
			// httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or
			// invalid token");
		}
		
	}

	private void validateJwtTokenWithUser(ServletRequest request, ServletResponse response, FilterChain chain,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, String token)
			throws IOException, ServletException {
		if (JwtUtil.validateToken(token)) {
			User loggedInUser = getUserDetailsFromToken(httpRequest, token);

			if (null != loggedInUser) {
				AddAuthBeanDetailIfNotFound(httpRequest, loggedInUser);
				chain.doFilter(request, response);
			} else {
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found..!");
			}
		} else {
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Your session is expired..!");
		}
	}

	private void AddAuthBeanDetailIfNotFound(HttpServletRequest httpRequest, User loggedInUser) {
		BeanUtil.createRequestBeanIfNotPresent(httpRequest, AUTH_BEAN_NAME,loggedInUser);
		AuthenticationBean authBean = (AuthenticationBean) BeanUtil.getSessionBeanFromContext(httpRequest,
				AUTH_BEAN_NAME);
		System.out.println(authBean.toString());
		//authBean.setLoggedInUser(loggedInUser);
	}

	private User getUserDetailsFromToken(HttpServletRequest httpRequest, String token) {
		String userEmail = JwtUtil.getSubject(token);
		UserDao userDao = (UserDao) BeanUtil.getApplicationBeanFromContext(httpRequest, USER_DAO_BEAN_NAME);
		return userDao.findByEmail(userEmail);
	}

	private boolean isLoginUrls(HttpServletRequest httpRequest) {

		return httpRequest.getRequestURI().contains(LOGIN_PAGE);

	}

	private String extractTokenFromRequest(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
		return Arrays.stream(cookies).filter(e -> MessageUtil.JWT_TOKEN_NAME.equals(e.getName())).findAny()
				.map(Cookie::getValue).orElse(null);

	}

	@Override
	public void destroy() {
	}

}
