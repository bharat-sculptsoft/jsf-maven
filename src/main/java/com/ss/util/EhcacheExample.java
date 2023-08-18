package com.ss.util;

import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.xml.exceptions.XmlConfigurationException;

public class EhcacheExample {
  public static void main(String[] args) throws XmlConfigurationException {
    PersistentCacheManager persistentCacheManager = 
        CacheManagerBuilder.newCacheManagerBuilder()
        .with(CacheManagerBuilder.persistence("C:\\Mehul\\Workspace\\JSF")) 
        .withCache("jsfCache", CacheConfigurationBuilder
            .newCacheConfigurationBuilder(String.class, String.class,
                ResourcePoolsBuilder.newResourcePoolsBuilder()
                .heap(100, EntryUnit.ENTRIES)
                .disk(10, MemoryUnit.MB, true))
            )
        .build(true);

    // Create cache
    Cache<String, String> myCache1 = persistentCacheManager.getCache("jsfCache", String.class, String.class);

    // Put and retrieve data from cache
    myCache1.put("key", "value");
    myCache1.put("name", "kano");
    myCache1.put("key1", "value");
    myCache1.put("name1", "kano");
    myCache1.put("key2", "value");
    myCache1.put("name2", "kano");
    myCache1.put("key3", "value");
    myCache1.put("name3", "kano");
    myCache1.put("key4", "value");
    myCache1.put("name4", "kano");
    myCache1.put("key5", "value");
    myCache1.put("name5", "kano");
    
    String value1 = myCache1.get("key");
    System.out.println("Value1 from cache: " + value1);

    String value2 = myCache1.get("name");
    System.out.println("Value2 from cache: " + value2);
    
    persistentCacheManager.close();
  }
}