package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateTimeUtil {
  // 使用 joda-time 第三方包

  private static final String FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

  public static Date strToDate(String dateTimeStr, String format) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
    DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
    return dateTime.toDate();
  }

  public static String dateToStr(Date date, String format) {
    if (date == null) {
      return StringUtils.EMPTY;
    }

    DateTime dateTime = new DateTime(date);
    return dateTime.toString(format);
  }

  public static Date strToDate(String dateTimeStr) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(FORMAT_STR);
    DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
    return dateTime.toDate();
  }

  public static String dateToStr(Date date) {
    if (date == null) {
      return StringUtils.EMPTY;
    }

    DateTime dateTime = new DateTime(date);
    return dateTime.toString(FORMAT_STR);
  }
}
































