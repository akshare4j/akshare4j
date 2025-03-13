/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney.model.enums;

/**
 * 
 */
public enum MarketType {
  
  SZ("0"),

  SH("1"),


  HK("128"),

  US("105");

  private String code;

  private MarketType(String code) {
    this.code = code;
  }

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

  public static MarketType of(String code) {
    for (MarketType marketType : values()) {
      if (marketType.getCode().equals(code)) {
        return marketType;
      }
    }
    return null;
  }
}
