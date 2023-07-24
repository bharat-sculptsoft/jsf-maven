package com.ss.user.web;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import com.ss.exception.ServiceLayerException;
import com.ss.message.Constant;
import com.ss.user.service.UserService;

import lombok.Data;
import lombok.NoArgsConstructor;

@ManagedBean
@RequestScoped
@Data
@NoArgsConstructor
public class AuthenticationBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private String email;
  private String password;

  @ManagedProperty(value = "#{userService}")
  private UserService userService;

  public String login() {
    try {
      if(userService.authenticate(email, password)) {
        return Constant.SUCCESS_PAGE_REDIRECT_URL;
      }
    } catch (ServiceLayerException e) {
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
    }catch (ServiceLayerException e) {
      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
    }
    return null;
  }
}