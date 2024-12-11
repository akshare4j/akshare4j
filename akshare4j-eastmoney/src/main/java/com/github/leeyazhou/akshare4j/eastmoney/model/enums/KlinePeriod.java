/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney.model.enums;

/**
 * 
 */
public enum KlinePeriod {
  daily("101"),

  weekly("102"),

  monthly("103"),


  ;

  private String code;

  private KlinePeriod(String code) {
    this.code = code;
  }

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }
}
