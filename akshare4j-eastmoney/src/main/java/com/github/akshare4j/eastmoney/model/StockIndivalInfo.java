/**
 * 
 */
package com.github.akshare4j.eastmoney.model;

import java.math.BigDecimal;

/**
 * 
 */
public class StockIndivalInfo {
  // "f57": "股票代码",
  // "f58": "股票简称",
  // "f84": "总股本",
  // "f85": "流通股",
  // "f127": "行业",
  // "f116": "总市值",
  // "f117": "流通市值",
  // "f189": "上市时间",
  private String symbol;
  private String name;
  private BigDecimal totalMarketVol;
  private BigDecimal tradeMarketVol;
  private BigDecimal totalMarketValue;
  private BigDecimal tradeMarketValue;
  private String listDate;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getTotalMarketVol() {
    return totalMarketVol;
  }

  public void setTotalMarketVol(BigDecimal totalMarketVol) {
    this.totalMarketVol = totalMarketVol;
  }

  public BigDecimal getTradeMarketVol() {
    return tradeMarketVol;
  }

  public void setTradeMarketVol(BigDecimal tradeMarketVol) {
    this.tradeMarketVol = tradeMarketVol;
  }

  public BigDecimal getTotalMarketValue() {
    return totalMarketValue;
  }

  public void setTotalMarketValue(BigDecimal totalMarketValue) {
    this.totalMarketValue = totalMarketValue;
  }

  public BigDecimal getTradeMarketValue() {
    return tradeMarketValue;
  }

  public void setTradeMarketValue(BigDecimal tradeMarketValue) {
    this.tradeMarketValue = tradeMarketValue;
  }

  public String getListDate() {
    return listDate;
  }

  public void setListDate(String listDate) {
    this.listDate = listDate;
  }


}
