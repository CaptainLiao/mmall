package com.mmall.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {

  private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

  private static Properties prop;

  static {
    String filename = "mmall.properties";
    prop = new Properties();
    try {
      prop.load(new InputStreamReader(Properties.class.getResourceAsStream(filename), "UTF-8"));
    } catch (IOException e) {
      logger.error("读取mmall.properties配置文件失败", e);
    }

  }

  public static String getProperty(String key) {
    String value = prop.getProperty(key.trim());
    if (StringUtils.isBlank(value)) {
      return null;
    }

    return value.trim();
  }

  public static String getProperty(String key, String defaultValue) {
    String value = prop.getProperty(key.trim());
    if (StringUtils.isBlank(value)) {
      value = defaultValue;
    }

    return value.trim();
  }

}



















