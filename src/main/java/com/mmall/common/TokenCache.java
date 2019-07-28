package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TokenCache {
  private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

  // 使用 google guava 作为本地缓存
  // LRU 算法
  private static LoadingCache<String, String> loadingCache = CacheBuilder
      .newBuilder()
      .initialCapacity(1000)
      .maximumSize(10000)
      .expireAfterAccess(12, TimeUnit.HOURS)
      .build(new CacheLoader<String, String>() {
        // 默认的数据加载实现，当调用 get 取值的时候，如果 key 么有对应的值，就调用这个方法进行加载
        @Override
        public String load(String s) throws Exception {
          return "null";
        }
      });

  public static void setKey(String key, String value) {
    loadingCache.put(key, value);
  }

  public static String getKey(String key) {
    String value = null;
    try {
      value = loadingCache.get(key);
      return "null".equals(value) ? null : value;
    } catch (Exception e) {
      logger.error("localCache error", e);
    }
    return null;
  }
}














