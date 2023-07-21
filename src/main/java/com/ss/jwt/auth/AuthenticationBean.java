package com.ss.jwt.auth;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.ss.common.utility.MessageUtil;
import com.ss.jwt.util.JwtUtil;
import com.ss.user.dao.UserDao;
import com.ss.user.pojo.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@ManagedBean
@SessionScoped
@Data
@NoArgsConstructor
public class AuthenticationBean implements Serializable {
//Test comments by mayur jain
	private String email;
	private String password;
	private User loggedInUser;

	@ManagedProperty(value = "#{userDaoImpl}")
	private UserDao userDao;
//
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
//	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public String login() {

		User user = userDao.findByEmailAndPassword(email, password);
		if (user != null) {
			String token = JwtUtil.generateToken(email);
			// tokenBean.setToken(token); // store at server side
			storeTokenAtClientSide(token); // store at client side
			loggedInUser = user;
			return MessageUtil.CARD_PAGE_REDIRECT_URL;
			//return MessageUtil.SUCCESS_PAGE_REDIRECT_URL;
		} else {
			return MessageUtil.LOGIN_PAGE_REDIRECT_URL;
		}
	}

	public String logout() {
		removeTokenAtClientSide();
		// Perform any additional cleanup or logout logic
		return MessageUtil.LOGIN_PAGE_REDIRECT_URL;
	}

	private void storeTokenAtClientSide(String token) {
		// Store the token on the client side (e.g., in a cookie)
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.addCookie(new Cookie(MessageUtil.JWT_TOKEN_NAME, token));
	}

	private void removeTokenAtClientSide() {
		// Store the token on the client side (e.g., in a cookie)
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		Cookie c = new Cookie(MessageUtil.JWT_TOKEN_NAME, null);
		c.setMaxAge(0);
		response.addCookie(c);

	}
	@Override
	public String toString() {
		return "AuthenticationBean [email=" + email + ", password=" + password + ", loggedInUser=" + loggedInUser
				+ ", userDao=" + userDao + "]";
	}
	
}
