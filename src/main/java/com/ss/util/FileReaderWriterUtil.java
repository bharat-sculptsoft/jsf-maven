package com.ss.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ss.message.MessageConstant;
import com.ss.message.MessageProvider;

public class FileReaderWriterUtil {

  private static final Logger logger = LogManager.getLogger(FileReaderWriterUtil.class);

  public static boolean readRequestDetails(String requestToken) throws Exception {
    logger.info("Start to read request data from the file");
    boolean isContains = false;
    try {
      String filePath = getFilePath();
      StringBuilder content = new StringBuilder();
      BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

      String line;
      while ((line = bufferedReader.readLine()) != null) {
        if (line.equals(requestToken)) {
          isContains = true;
        }
        content.append(line).append("\n");
      }
      bufferedReader.close();
      logger.info("End to read request data from the file");
    } catch (Exception e) {
      logger.error("Error while read request data from the file: " + e.getMessage());
      throw new Exception(
          MessageProvider.getMessageString(MessageConstant.INTERNAL_SERVER_ERROR, null));
    }
    return isContains;
  }

  public static boolean writeRequestDetails(String requestToken) throws Exception {
    logger.info("Start to write request data in the file");
    boolean isContains = false;
    try {
      String filePath = getFilePath();
      StringBuilder content = new StringBuilder();
      BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

      String line;
      while ((line = bufferedReader.readLine()) != null) {
        if (line.equals(requestToken)) {
          isContains = true;
        }
        content.append(line).append("\n");
      }
      bufferedReader.close();

      if (!isContains) {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
        bufferedWriter.write(content.toString() + requestToken);
        bufferedWriter.newLine();
        bufferedWriter.close();
      }
      logger.info("End to write request data in the file");
    } catch (Exception e) {
      logger.error("Error while write request data in the file: " + e.getMessage());
      throw new Exception(
          MessageProvider.getMessageString(MessageConstant.INTERNAL_SERVER_ERROR, null));
    }
    return isContains;
  }

  public static void deleteRequestDetails(String csrfToken) throws Exception {
    logger.info("Start to delete request data from the file");
    List<String> csrfTokens = new ArrayList<>();
    try {
      String line;
      String filePath = getFilePath();
      BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
      while ((line = bufferedReader.readLine()) != null) {
        if (!line.equals(csrfToken)) {
          csrfTokens.add(line);
        }
      }
      bufferedReader.close();

      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
      for (String token : csrfTokens) {
        bufferedWriter.write(token);
        bufferedWriter.newLine();
      }
      bufferedWriter.close();
      logger.info("End to delete request data from the file");
    } catch (Exception e) {
      logger.error("Error while delete request data from the file: " + e.getMessage());
      throw new Exception(
          MessageProvider.getMessageString(MessageConstant.INTERNAL_SERVER_ERROR, null));
    }
  }

  private static String getFilePath() throws IOException {
    File file = new File("..\\test.txt");
    if(!file.exists()) {
    	file.createNewFile();
    }
    return file.getAbsolutePath();
  }
}