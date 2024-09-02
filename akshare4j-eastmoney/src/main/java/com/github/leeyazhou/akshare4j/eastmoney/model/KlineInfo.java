/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 */
public class KlineInfo {

  /**
   * 交易日期
   */
  private Date tradeDate;

  /**
   * 开盘价
   */
  private BigDecimal open;
  /**
   * 最高价
   */
  private BigDecimal high;

  /**
   * 最低价
   */
  private BigDecimal low;

  /**
   * 收盘价
   */
  private BigDecimal close;

  /**
   * 昨收价
   */
  private BigDecimal preClose;

  /**
   * 涨跌额
   */
  private Double change;
  /**
   * 涨跌幅 （未复权，如果是复权请用 <a href="https://tushare.pro/document/2?doc_id=109">通用行情接口</a> ）
   */
  private Double pctChg;

  /**
   * 成交量 （手）
   */
  private BigDecimal vol;

  /**
   * 成交额 （千元）
   */
  private BigDecimal amount; //

  /**
   * 振幅 float64 注意单位: %
   */
  private BigDecimal amplitude;

  /** 换手率 float64 注意单位: % */
  private BigDecimal turnoverRate;

  public Date getTradeDate() {
    return tradeDate;
  }

  public void setTradeDate(Date tradeDate) {
    this.tradeDate = tradeDate;
  }

  public BigDecimal getOpen() {
    return open;
  }

  public void setOpen(BigDecimal open) {
    this.open = open;
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

  public BigDecimal getClose() {
    return close;
  }

  public void setClose(BigDecimal close) {
    this.close = close;
  }

  public BigDecimal getPreClose() {
    return preClose;
  }

  public void setPreClose(BigDecimal preClose) {
    this.preClose = preClose;
  }

  public Double getChange() {
    return change;
  }

  public void setChange(Double change) {
    this.change = change;
  }

  public Double getPctChg() {
    return pctChg;
  }

  public void setPctChg(Double pctChg) {
    this.pctChg = pctChg;
  }

  public BigDecimal getVol() {
    return vol;
  }

  public void setVol(BigDecimal vol) {
    this.vol = vol;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  /**
   * @param amplitude the amplitude to set
   */
  public void setAmplitude(BigDecimal amplitude) {
    this.amplitude = amplitude;
  }

  /**
   * @return the amplitude
   */
  public BigDecimal getAmplitude() {
    return amplitude;
  }

  /**
   * @param turnoverRate the turnoverRate to set
   */
  public void setTurnoverRate(BigDecimal turnoverRate) {
    this.turnoverRate = turnoverRate;
  }

  /**
   * @return the turnoverRate
   */
  public BigDecimal getTurnoverRate() {
    return turnoverRate;
  }
}
