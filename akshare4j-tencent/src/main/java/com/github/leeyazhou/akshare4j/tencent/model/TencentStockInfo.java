/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent.model;

import java.math.BigDecimal;
import com.github.leeyazhou.akshare4j.tencent.model.enums.TencentMarketType;

/**
 * @author leeyazhou
 */
public class TencentStockInfo {

  private String code;
  
  /**
   * 换手率
   */
  private BigDecimal hsl;
  private String name;
  private String state;
  private String stockType;
  
  /**
   * 成交量
   */
  private BigDecimal volume;
  
  /**
   * 涨跌额
   */
  private BigDecimal zd;
  
  /**
   * 涨跌幅百分比
   */
  private BigDecimal zdf;

  private TencentMarketType marketType;
  private String symbol;

  /**
   * 最新价？
   */
  private BigDecimal zxj;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public BigDecimal getHsl() {
    return hsl;
  }

  public void setHsl(BigDecimal hsl) {
    this.hsl = hsl;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getStockType() {
    return stockType;
  }

  public void setStockType(String stockType) {
    this.stockType = stockType;
  }

  public BigDecimal getVolume() {
    return volume;
  }

  public void setVolume(BigDecimal volume) {
    this.volume = volume;
  }

  public BigDecimal getZd() {
    return zd;
  }

  public void setZd(BigDecimal zd) {
    this.zd = zd;
  }

  public BigDecimal getZdf() {
    return zdf;
  }

  public void setZdf(BigDecimal zdf) {
    this.zdf = zdf;
  }

  public BigDecimal getZxj() {
    return zxj;
  }

  public void setZxj(BigDecimal zxj) {
    this.zxj = zxj;
  }

  public TencentMarketType getMarketType() {
    return marketType;
  }

  public void setMarketType(TencentMarketType marketType) {
    this.marketType = marketType;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

}
