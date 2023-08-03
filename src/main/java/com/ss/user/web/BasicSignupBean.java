package com.ss.user.web;

import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ss.exception.ServiceLayerException;
import com.ss.message.Constant;
import com.ss.user.entity.User;
import com.ss.user.service.UserService;
import com.ss.util.PasswordHashingUtil;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@ManagedBean
@RequestScoped
public class BasicSignupBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(BasicSignupBean.class);
	// @Getter @Setter
	private String name;
	// @Getter @Setter
	private String email;
	// @Getter @Setter
	private String phonenumber;
//  private Date birthdate;
	// @Getter @Setter
	private String password;
	private String gender;
	// @Getter @Setter
	private String address;
	// @Getter @Setter
	@ManagedProperty(value = "#{userService}")
	private UserService userService;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String signup() {
		logger.info("signup");
		logger.debug(address, phonenumber, password, name, gender, email);
		try {
			User user = new User();
			user.setName(name);
			user.setEmail(email);
			user.setPhonenumber(phonenumber);
			// user.setBirthdate(birthdate);
			user.setPassword(PasswordHashingUtil.encode(password));
			user.setGender(gender);
			user.setAddress(address);
			userService.signUp(user);

			return Constant.SIGNUP_SUCCESS_PAGE_REDIRECT_URL;
		} catch (ServiceLayerException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
		return null;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}