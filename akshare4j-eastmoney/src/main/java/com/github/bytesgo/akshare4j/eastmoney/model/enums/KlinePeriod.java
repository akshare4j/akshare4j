/**
 * 
 */
package com.github.bytesgo.akshare4j.eastmoney.model.enums;

/**
 * 
 */
public enum KlinePeriod {
  daily("101"), 
  
  monthly("103"), 
  
  weekly("102");

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
