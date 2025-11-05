/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent.model;

import java.util.List;

/**
 * @author leeyazhou
 */
public class TencentStockQt {

  private List<String> fields;
  private String market;

  /**
   * 指数
   */
  private List<Object> zhishu;

  public List<String> getFields() {
    return fields;
  }

  public void setFields(List<String> fields) {
    this.fields = fields;
  }

  public String getMarket() {
    return market;
  }

  public void setMarket(String market) {
    this.market = market;
  }

  public List<Object> getZhishu() {
    return zhishu;
  }

  public void setZhishu(List<Object> zhishu) {
    this.zhishu = zhishu;
  }


}
