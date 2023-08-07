package com.ss.filter;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ss.message.Constant;
import com.ss.message.MessageConstant;
import com.ss.message.MessageProvider;
import com.ss.util.CommonUtil;
import com.ss.util.FileReaderWriterUtil;
import com.ss.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;

@WebFilter("/*")
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
      String sessionToken =
          (String) httpRequest.getSession().getAttribute(Constant.SESSION_TOKEN_KEY);
      String requestToken = httpRequest.getParameter(Constant.REQUEST_TOKEN_KEY);
      
      if(requestToken != null) {
        requestToken = CommonUtil.encode(requestToken);
      }

      if (sessionToken != null && requestToken != null && sessionToken.equals(requestToken)) {
        FileReaderWriterUtil.writeRequestDetails(requestToken);
      } else if (requestToken != null) {
        Boolean isContains = FileReaderWriterUtil.readRequestDetails(requestToken);
        if (isContains.equals(Boolean.TRUE)) {
          httpRequest.getSession().setAttribute(Constant.SESSION_TOKEN_KEY, requestToken);
        }
      }

      if (isLoginUrls(httpRequest)) {
        chain.doFilter(request, response);
        return;
      }

      String token = extractTokenFromRequest(httpRequest);
      if (token != null && JwtUtil.validateToken(token)) {
        httpRequest.setAttribute(Constant.USER_PALYLOAD, JwtUtil.getSubjectFromToken(token));
        // Token is valid, proceed with the request
        chain.doFilter(request, response);
      } else {
        // Token is invalid or not present, redirect to login page or show an error
        MessageProvider.getMessageString(MessageConstant.JWT_INVALID_TOKEN, null,
            httpRequest.getLocale());
        httpResponse.sendRedirect(httpRequest.getContextPath() + "/welcome.xhtml");
      }
    } catch (ExpiredJwtException e) {
      JwtUtil.removeTokenfromCookie(httpResponse);
      MessageProvider.getMessageString(MessageConstant.JWT_TOKEN_EXPIRED, null,
          httpRequest.getLocale());
      httpResponse.sendRedirect(httpRequest.getContextPath() + "/welcome.xhtml");
    } catch (Exception e) {
      MessageProvider.getMessageString(MessageConstant.JWT_INVALID_TOKEN, null,
          httpRequest.getLocale());
      // httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
      httpResponse.sendRedirect(httpRequest.getContextPath() + "/welcome.xhtml");
    }
  }

  private boolean isLoginUrls(HttpServletRequest httpRequest) {
    String defaultURL = httpRequest.getContextPath() + "/";
    String loginURL = httpRequest.getContextPath() + "/login";
    String loginHtmlURL = httpRequest.getContextPath() + "/login.xhtml";
    String newSignupURL = httpRequest.getContextPath() + "/newsignup";
    String welcomeURL = httpRequest.getContextPath() + "/welcome";
    String welcomeHtmlURL = httpRequest.getContextPath() + "/welcome.xhtml";
    String signupURL = httpRequest.getContextPath() + "/signup";

    return (httpRequest.getRequestURI().equals(defaultURL)
        || httpRequest.getRequestURI().equals(loginURL)
        || httpRequest.getRequestURI().equals(loginHtmlURL)
        || httpRequest.getRequestURI().equals(newSignupURL)
        || httpRequest.getRequestURI().equals(signupURL)
        || httpRequest.getRequestURI().equals(welcomeURL)
        || httpRequest.getRequestURI().equals(welcomeHtmlURL));
  }

  private String extractTokenFromRequest(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null)
      return null;
    return Arrays.stream(cookies).filter(e -> Constant.JWT_TOKEN_NAME.equals(e.getName())).findAny()
        .map(Cookie::getValue).orElse(null);
  }

  @Override
  public void destroy() {}
}