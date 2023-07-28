package com.ss.user.web;

import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.ss.exception.ServiceLayerException;
import com.ss.message.Constant;
import com.ss.user.entity.User;
import com.ss.user.service.UserService;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@ManagedBean
@RequestScoped
public class SignupBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private String name;
  private String email;
  private String phonenumber;
  private Date birthdate;
  private String gender;
  private String address;
  
  @ManagedProperty(value = "#{userService}")
  private UserService userService;

  public String signup() {
    try {
      User user = new User();
      user.setName(name);
      user.setEmail(email);
      user.setPhonenumber(phonenumber);
      user.setBirthdate(birthdate);
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
}