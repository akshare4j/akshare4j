/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney.model.enums;

/**
 * 
 */
public enum Adjust {
  QFQ("1"),

  HFQ("2"),

  None("0");

  private String code;

  /**
   * 
   */
  private Adjust(String code) {
    this.code = code;
  }

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }
}
