package com.ss.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import com.ss.message.Constant;
import com.ss.message.MessageConstant;
import com.ss.message.MessageProvider;

public class CacheManagerUtil {
  
  private static final Logger logger = LogManager.getLogger(CacheManagerUtil.class);

  private static PersistentCacheManager createCacheManager() {
    
    // Create CacheManager
    PersistentCacheManager persistentCacheManager =
        CacheManagerBuilder
            .newCacheManagerBuilder().with(
                CacheManagerBuilder.persistence(Constant.CACHE_ROOT_DIRECTORY))
            // Create cache configuration
            .withCache(Constant.CACHE_NAME,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                    ResourcePoolsBuilder.newResourcePoolsBuilder()
                        .disk(10, MemoryUnit.MB, true)))
            .build(true);
    return persistentCacheManager;
  }

  public static boolean storeDataInCache(String data) throws Exception {
    logger.info("Start to store data in the cache");
    boolean isAdded = false;
    try {
      PersistentCacheManager persistentCacheManager = createCacheManager();
      Cache<String, String> cache =
          persistentCacheManager.getCache(Constant.CACHE_NAME, String.class, String.class);
      cache.put(data, data);
      persistentCacheManager.close();
      isAdded = true;
      logger.info("End to store data in the cache");
    } catch (Exception e) {
      logger.error("Error while to store data in the cache: " + e.getMessage());
      throw new Exception(
          MessageProvider.getMessageString(MessageConstant.INTERNAL_SERVER_ERROR, null));
    }
    return isAdded;
  }

  public static boolean getDataFromCache(String data) throws Exception {
    logger.info("Start to get data from the cache");
    boolean isContains = false;
    try {
      PersistentCacheManager persistentCacheManager = createCacheManager();
      Cache<String, String> cache =
          persistentCacheManager.getCache(Constant.CACHE_NAME, String.class, String.class);
      String value = cache.get(data);
      if (value != null && value.length() > 0) {
        isContains = true;
      }
      persistentCacheManager.close();
      logger.info("End to get data from the cache");
      return isContains;
    } catch (Exception e) {
      logger.error("Error while to get data from the cache: " + e.getMessage());
      throw new Exception(
          MessageProvider.getMessageString(MessageConstant.INTERNAL_SERVER_ERROR, null));
    }
  }

  public static void removeDataFromCache(String data) throws Exception {
    logger.info("Start to remove data from the cache");
    try {
      PersistentCacheManager persistentCacheManager = createCacheManager();
      Cache<String, String> cache =
          persistentCacheManager.getCache(Constant.CACHE_NAME, String.class, String.class);
      cache.remove(data);
      persistentCacheManager.close();
      logger.info("End to remove data from the cache");
    } catch (Exception e) {
      logger.error("Error while to remove data from the cache: " + e.getMessage());
      throw new Exception(
          MessageProvider.getMessageString(MessageConstant.INTERNAL_SERVER_ERROR, null));
    }
  }
}