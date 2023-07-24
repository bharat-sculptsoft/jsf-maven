package com.ss.message;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class MessageProvider {

	private static final String BUNDLE_NAME = "properties.message";

	/*
	 * this method should use when we have to pass locale explicitly.
	 */
	public static String getMessageString(String key, Object[] params, Locale lc) {

		ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, lc);

		return getMessage(key, params, bundle);
	}

	/*
	 * this method should use inside JSF life cycle,because automatically get the
	 * locale data from view.
	 */
	public static String getMessageString(String key, Object[] params) {

		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, context.getViewRoot().getLocale());

		return getMessage(key, params, bundle);
	}

	private static String getMessage(String key, Object[] params, ResourceBundle bundle) {
		String text = null;
		try {
			text = bundle.getString(key);
		} catch (MissingResourceException e) {
			text = "?? key " + key + " not found ??";
		}

		if (params != null) {
			MessageFormat mf = new MessageFormat(text);
			text = mf.format(params, new StringBuffer(), null).toString();
		}

		return text;
	}
}