package com.ss.user.web;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern.Flag;

import com.ss.message.Constant;
import com.ss.message.MessageConstant;
import com.ss.user.service.UserService;

import lombok.Data;
import lombok.NoArgsConstructor;

@ManagedBean
@RequestScoped
@Data
@NoArgsConstructor
public class AuthenticationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = MessageConstant.EMAIL_NOT_BLANK_VALIDATION_MESSAGE)
	@Email(regexp = Constant.EMAIL_REGEX, flags = Flag.CASE_INSENSITIVE, message = MessageConstant.EMAIL_REGEX_VALIDATION_MESSAGE)
	private String email;

	@NotBlank(message = MessageConstant.PASSWORD_NOT_BLANK_VALIDATION_MESSAGE)
	private String password;

	@ManagedProperty(value = "#{userService}")
	private UserService userService;

	public String login() {
		try {
			if (userService.authenticate(email, password)) {
				return Constant.SUCCESS_PAGE_REDIRECT_URL;
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
		return null;

	}

	public String logout() {
		try {
			userService.logoutUser();
			// Perform any additional cleanup or logout logic
			return Constant.LOGIN_PAGE_REDIRECT_URL;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
		return null;
	}

}
