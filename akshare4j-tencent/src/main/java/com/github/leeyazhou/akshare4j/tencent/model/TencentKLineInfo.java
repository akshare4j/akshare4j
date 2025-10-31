package com.github.leeyazhou.akshare4j.tencent.model;

import java.math.BigDecimal;
import com.alibaba.fastjson2.annotation.JSONField;

public class TencentKLineInfo {
  private BigDecimal open;
  @JSONField(alternateNames = "last")
  private BigDecimal close;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal volume;
  private BigDecimal exchange;
  private BigDecimal exchangeRaw;
  @JSONField(ordinal = 0)
  private String date;


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



}
