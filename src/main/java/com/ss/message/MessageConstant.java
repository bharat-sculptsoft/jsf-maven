package com.ss.message;

/*
 * this class is used to get the message from properties files.
*/
public class MessageConstant {

	public static final String INTERNAL_SERVER_ERROR = "internal.server.error";

	public static final String JWT_TOKEN_EXPIRED = "jwt.token.expired";
	public static final String JWT_INVALID_TOKEN = "jwt.token.invalid";

	public static final String USER_NOT_FOUND = "user.data.not.found";
	public static final String USER_DATA_FETCH_FAIL = "user.data.fetch.fail";
	public static final String USER_DATA_SAVE_FAIL = "user.data.save.fail";

	public static final String INCORRECT_USERNAME_PASSWORD = "incorrect.username.or.password";

	
	//validation message 
	public static final String EMAIL_NOT_BLANK_VALIDATION_MESSAGE = "Email should not be blank";
	public static final String EMAIL_REGEX_VALIDATION_MESSAGE = "Enter valid email address";
	public static final String PASSWORD_NOT_BLANK_VALIDATION_MESSAGE = "Password should not be blank";

}
