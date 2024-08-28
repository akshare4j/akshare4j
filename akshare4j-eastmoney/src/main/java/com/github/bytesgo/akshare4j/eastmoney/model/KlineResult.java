/**
 * 
 */
package com.github.bytesgo.akshare4j.eastmoney.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 */
public class KlineResult {
  // "code": "601360",
  // "decimal": 2,
  // "dktotal": 2939,
  // "klines":
  // [
  // "2024-03-01,8.68,9.55,9.55,8.63,3480811,3241300923.00,10.61,10.15,0.88,4.87",
  // ],
  // "market": 1,
  // "name": "三六零",
  // "preKPrice": 8.67
  private String code;// ": "601360",
  private Integer decimal;// ": 2,
  private Integer dktotal;// ": 2939,
  private List<String> klines;// ":["2024-03-01,8.68,9.55,9.55,8.63,3480811,3241300923.00,10.61,10.15,0.88,4.87",],
  private Integer market;// ": 1,
  private String name;// ": "三六零",
  private BigDecimal preKPrice;// ": 8.67

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Integer getDecimal() {
    return decimal;
  }

  public void setDecimal(Integer decimal) {
    this.decimal = decimal;
  }

  public Integer getDktotal() {
    return dktotal;
  }

  public void setDktotal(Integer dktotal) {
    this.dktotal = dktotal;
  }

  public List<String> getKlines() {
    return klines;
  }

  public void setKlines(List<String> klines) {
    this.klines = klines;
  }

  public Integer getMarket() {
    return market;
  }

  public void setMarket(Integer market) {
    this.market = market;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getPreKPrice() {
    return preKPrice;
  }

  public void setPreKPrice(BigDecimal preKPrice) {
    this.preKPrice = preKPrice;
  }


}
