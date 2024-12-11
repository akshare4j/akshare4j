package com.github.leeyazhou.akshare4j.eastmoney.model;

import java.math.BigDecimal;

/**
 * 股票管理对象 tb_stock
 *
 * @author leeyazhou
 */
public class StockInfo {

  /**
   * 
   */
  public StockInfo() {}

  public StockInfo(String symbol) {
    this.symbol = symbol;
  }

  /** ID */
  private Long id;

  /** 代码 */
  private String symbol;

  /**
   * 市场
   */
  private String market;

  /** 名称 */
  private String name;

  /** 最新价 */
  private BigDecimal latestPrice;

  /** 涨跌幅/% */
  private BigDecimal priceChange;

  /** 涨跌额 */
  private BigDecimal priceChangeAmount;

  /** 成交量 */
  private BigDecimal volume;

  /** 成交额 */
  private BigDecimal volumeAmount;

  /** 振幅/% */
  private BigDecimal amplitude;

  /** 最高价 */
  private BigDecimal high;

  /** 最低价 */
  private BigDecimal low;

  /** 开盘价 */
  private BigDecimal open;

  /** 昨收 */
  private BigDecimal closeYestoday;

  /** 量比 */
  private BigDecimal volumeRatio;

  /** 换手率/% */
  private BigDecimal turnoverRatio;

  /** 市值 */
  private BigDecimal totalMarketValue;
  /** 市值 */
  private BigDecimal tradeMarketValue;

  /**
   * 上市状态
   */
  private String listStatus;

  /**
   * 上市日期
   */
  private String listDate;

  /**
   * 退市日期
   */
  private String deListDate;

  /**
   * 市场类型: SH, SZ, HK
   */
  private String marketType;

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setLatestPrice(BigDecimal latestPrice) {
    this.latestPrice = latestPrice;
  }

  public BigDecimal getLatestPrice() {
    return latestPrice;
  }

  public void setPriceChange(BigDecimal priceChange) {
    this.priceChange = priceChange;
  }

  public BigDecimal getPriceChange() {
    return priceChange;
  }

  public void setPriceChangeAmount(BigDecimal priceChangeAmount) {
    this.priceChangeAmount = priceChangeAmount;
  }

  public BigDecimal getPriceChangeAmount() {
    return priceChangeAmount;
  }

  public void setVolume(BigDecimal volume) {
    this.volume = volume;
  }

  public BigDecimal getVolume() {
    return volume;
  }

  public void setVolumeAmount(BigDecimal volumeAmount) {
    this.volumeAmount = volumeAmount;
  }

  public BigDecimal getVolumeAmount() {
    return volumeAmount;
  }

  public void setAmplitude(BigDecimal amplitude) {
    this.amplitude = amplitude;
  }

  public BigDecimal getAmplitude() {
    return amplitude;
  }

  public void setHigh(BigDecimal high) {
    this.high = high;
  }

  public BigDecimal getHigh() {
    return high;
  }

  public void setLow(BigDecimal low) {
    this.low = low;
  }

  public BigDecimal getLow() {
    return low;
  }

  public void setOpen(BigDecimal open) {
    this.open = open;
  }

  public BigDecimal getOpen() {
    return open;
  }

  public void setCloseYestoday(BigDecimal closeYestoday) {
    this.closeYestoday = closeYestoday;
  }

  public BigDecimal getCloseYestoday() {
    return closeYestoday;
  }

  public void setVolumeRatio(BigDecimal volumeRatio) {
    this.volumeRatio = volumeRatio;
  }

  public BigDecimal getVolumeRatio() {
    return volumeRatio;
  }

  public void setTurnoverRatio(BigDecimal turnoverRatio) {
    this.turnoverRatio = turnoverRatio;
  }

  public BigDecimal getTurnoverRatio() {
    return turnoverRatio;
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

  public String getListStatus() {
    return listStatus;
  }

  public void setListStatus(String listStatus) {
    this.listStatus = listStatus;
  }

  public String getListDate() {
    return listDate;
  }

  public void setListDate(String listDate) {
    this.listDate = listDate;
  }

  public String getDeListDate() {
    return deListDate;
  }

  public void setDeListDate(String deListDate) {
    this.deListDate = deListDate;
  }

  public String getMarketType() {
    return marketType;
  }

  public void setMarketType(String marketType) {
    this.marketType = marketType;
  }

  /**
   * @param market the market to set
   */
  public void setMarket(String market) {
    this.market = market;
  }

  /**
   * @return the market
   */
  public String getMarket() {
    return market;
  }

}
