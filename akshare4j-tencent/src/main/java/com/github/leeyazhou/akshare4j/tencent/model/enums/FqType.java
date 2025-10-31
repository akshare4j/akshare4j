/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent.model.enums;

/**
 * @author leeyazhou
 */
public enum FqType {

  QFQ("qfq", "前复权"),

  HFQ("hfq", "后复权"),

  NONE("none", "不复权");

  private String code;
  private String desc;

  /**
   * 
   */
  private FqType(String code, String desc) {
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
