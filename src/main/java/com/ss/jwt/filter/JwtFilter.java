package com.ss.jwt.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ss.common.util.Constant;
import com.ss.common.util.MessageConstant;
import com.ss.common.util.MessageProvider;
import com.ss.jwt.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		try {
			if (isLoginUrls(httpRequest)) {
				chain.doFilter(request, response);
				return;
			}
			String token = extractTokenFromRequest(httpRequest);
			if (token != null && JwtUtil.validateToken(token)) {

				// TODO : set userDetails in request from JWT

				// Token is valid, proceed with the request
				chain.doFilter(request, response);
			} else {
				// Token is invalid or not present, redirect to login page or show an error
				MessageProvider.getMessageString(MessageConstant.JWT_INVALID_TOKEN, null, httpRequest.getLocale());
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
			}

		} catch (ExpiredJwtException e) {
			MessageProvider.getMessageString(MessageConstant.JWT_TOKEN_EXPIRED, null, httpRequest.getLocale());

			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");

		} catch (Exception e) {
			MessageProvider.getMessageString(MessageConstant.JWT_INVALID_TOKEN, null, httpRequest.getLocale());
			// httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");

		}

	}

	private boolean isLoginUrls(HttpServletRequest httpRequest) {

		String defaultURL = httpRequest.getContextPath() + "/";
		String loginURL = httpRequest.getContextPath() + "/login";
		String loginHtmlURL = httpRequest.getContextPath() + "/login.xhtml";

		return (httpRequest.getRequestURI().equals(defaultURL) || httpRequest.getRequestURI().equals(loginURL)
				|| httpRequest.getRequestURI().equals(loginHtmlURL));

	}

	private String extractTokenFromRequest(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
		return Arrays.stream(cookies).filter(e -> Constant.JWT_TOKEN_NAME.equals(e.getName())).findAny()
				.map(Cookie::getValue).orElse(null);

	}

	@Override
	public void destroy() {
	}

}