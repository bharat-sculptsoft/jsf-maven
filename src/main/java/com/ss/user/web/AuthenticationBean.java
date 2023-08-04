package com.ss.user.web;

import java.io.Serializable;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import com.ss.message.Constant;
import com.ss.user.service.UserService;
import com.ss.util.FileReaderWriterUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@ManagedBean
@RequestScoped
@Data
@NoArgsConstructor
public class AuthenticationBean implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotBlank(message = "{validation.authenticationBean.email.NotBlank}")
  @Email(regexp = Constant.EMAIL_REGEX, message ="{validation.authenticationBean.email.Regex}")
  private String email;

  @NotBlank(message = "{validation.authenticationBean.password.NotBlank}")
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

      //delete the request details from the file
      deleteUserRequestDetails();

      // Perform any additional cleanup or logout logic
      return Constant.WELCOME_PAGE_REDIRECT_URL;
    } catch (Exception e) {
      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
    }
    return null;
  }

  private void deleteUserRequestDetails() throws Exception {
    FacesContext context = FacesContext.getCurrentInstance();
    ExternalContext externalContext = context.getExternalContext();
    Map<String, String> requestParameterMap = externalContext.getRequestParameterMap();
    String requestToken = requestParameterMap.get(Constant.REQUEST_TOKEN_KEY);
  
    FileReaderWriterUtil.deleteRequestDetails(requestToken);
  }
}