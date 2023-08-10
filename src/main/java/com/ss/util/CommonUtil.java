package com.ss.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CommonUtil {

  public static String encode(String dataString) throws UnsupportedEncodingException {
    return URLEncoder.encode(dataString, StandardCharsets.UTF_8.name());
  }
}

