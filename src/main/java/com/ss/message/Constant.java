package com.ss.message;

/*this class is used to get the internal constant value
*/
public class Constant {

	private Constant() {
	}

	public static final String JWT_TOKEN_NAME = "jwtToken";
	public static final String USER_PALYLOAD = "userPayload";

	public static final String SUCCESS_PAGE_REDIRECT_URL = "success?faces-redirect=true";
	public static final String CONFIRM_PAGE_REDIRECT_URL = "success?faces-redirect=true";
	public static final String LOGIN_PAGE_REDIRECT_URL = "login?faces-redirect=true";

	public static final String EMAIL_REGEX = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}";

	public static final String SIGNUP_SUCCESS_PAGE_REDIRECT_URL = "signupsuccess?faces-redirect=true";
	
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
}
