package com.ss.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CommonUtil {

  public static String encode(String dataString) {
    return URLEncoder.encode(dataString, StandardCharsets.UTF_8);
  }
}
