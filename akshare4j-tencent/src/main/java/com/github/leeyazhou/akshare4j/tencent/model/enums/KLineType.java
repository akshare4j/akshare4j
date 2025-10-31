/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent.model.enums;

/**
 * @author leeyazhou
 */
public enum KLineType {

  Day("day", "日线"),

  Week("week", "周线");

  private String code;
  private String desc;

  /**
   * 
   */
  private KLineType(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * @return the desc
   */
  public String getDesc() {
    return desc;
  }
}
