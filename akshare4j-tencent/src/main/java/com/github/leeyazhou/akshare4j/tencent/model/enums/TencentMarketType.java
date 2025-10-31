/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent.model.enums;

/**
 * 
 */
public enum TencentMarketType {
  
  SZ("sz"),

  SH("sh"),


  HK("hk"),

  US("us");

  private String code;

  private TencentMarketType(String code) {
    this.code = code;
  }

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

  public static TencentMarketType of(String code) {
    for (TencentMarketType tencentMarketType : values()) {
      if (tencentMarketType.getCode().equals(code)) {
        return tencentMarketType;
      }
    }
    return null;
  }
}
