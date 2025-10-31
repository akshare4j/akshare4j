package com.github.leeyazhou.akshare4j.tencent.model;

import java.math.BigDecimal;
import com.alibaba.fastjson2.annotation.JSONField;

public class TencentKLineInfo {

  @JSONField(ordinal = 0)
  private String date;
  /**
   * 开盘价
   */
  private BigDecimal open;

  /**
   * 收盘价
   */
  @JSONField(alternateNames = "last")
  private BigDecimal close;

  /**
   * 最高价
   */
  private BigDecimal high;

  /**
   * 最低价
   */
  private BigDecimal low;

  /**
   * 交易量
   */
  private BigDecimal volume;

  /**
   * 交易额
   */
  private BigDecimal amount;

  /**
   * 换手率
   */
  private BigDecimal exchange;
  private BigDecimal exchangeRaw;


  public BigDecimal getOpen() {
    return open;
  }

  public void setOpen(BigDecimal open) {
    this.open = open;
  }

  public BigDecimal getClose() {
    return close;
  }

  public void setClose(BigDecimal close) {
    this.close = close;
  }

  public BigDecimal getHigh() {
    return high;
  }

  public void setHigh(BigDecimal high) {
    this.high = high;
  }

  public BigDecimal getLow() {
    return low;
  }

  public void setLow(BigDecimal low) {
    this.low = low;
  }

  public BigDecimal getVolume() {
    return volume;
  }

  public void setVolume(BigDecimal volume) {
    this.volume = volume;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public BigDecimal getExchange() {
    return exchange;
  }

  public void setExchange(BigDecimal exchange) {
    this.exchange = exchange;
  }

  public BigDecimal getExchangeRaw() {
    return exchangeRaw;
  }

  public void setExchangeRaw(BigDecimal exchangeRaw) {
    this.exchangeRaw = exchangeRaw;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }



}
