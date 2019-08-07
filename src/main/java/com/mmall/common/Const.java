package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {
  public static final String CURRENT_USER = "currentUser";
  public static final String USERNAME = "username";
  public static final String EMAIL = "email";

  public interface ProductListOrderBy {
    // 以下划线分割，前面表示排序的字段名，后面表示升/降序
    Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
  }

  public interface Role {
    int ROLE_CUSTOMER = 0; // 普通用户
    int ROLE_ADMIN = 1; // 管理员
  }

  public enum ProductStatusEnum {
    ON_SALE(1, "在线");

    int code;
    String value;

    ProductStatusEnum(int code, String value) {
      this.code = code;
      this.value = value;
    }

    public int getCode() {
      return code;
    }

    public String getValue() {
      return value;
    }
  }
}
