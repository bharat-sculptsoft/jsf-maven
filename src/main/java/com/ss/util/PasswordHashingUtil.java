package com.ss.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashingUtil {

	public static String encode(String password) {
		String salt = BCrypt.gensalt();
		return BCrypt.hashpw(password, salt);
	}

	public static boolean match(String password, String hashedPassword) {
		return BCrypt.checkpw(password, hashedPassword);
	}

}