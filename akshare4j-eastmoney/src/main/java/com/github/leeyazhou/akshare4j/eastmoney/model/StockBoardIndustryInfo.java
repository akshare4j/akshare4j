/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney.model;

import java.math.BigDecimal;

/**
 * 
 */
public class StockBoardIndustryInfo {
  // "排名",
  // "板块名称",
  // "板块代码",
  // "最新价",
  // "涨跌额",
  // "涨跌幅",
  // "总市值",
  // "换手率",
  // "上涨家数",
  // "下跌家数",
  // "领涨股票",
  // "领涨股票-涨跌幅",
  private String sort;
  private String boardName;
  private String boardSymbol;
  private BigDecimal latestPrice;
  /** 涨跌幅/% */
  private BigDecimal priceChange;

  /** 涨跌额 */
  private BigDecimal priceChangeAmount;

  /** 市值 */
  private BigDecimal totalMarketValue;

  /** 换手率/% */
  private BigDecimal turnoverRatio;

  /**
   * 上涨家数
   */
  private Integer riseNum;

  /**
   * 下跌家数
   */
  private Integer fallNum;

  /**
   * 领涨股票
   */
  private String riseMaxStockSymbol;

  /**
   * 领涨股票-涨跌幅
   */
  private BigDecimal riseMaxStockPriceChange;

  public String getSort() {
    return sort;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }

  public String getBoardName() {
    return boardName;
  }

  public void setBoardName(String boardName) {
    this.boardName = boardName;
  }

  public String getBoardSymbol() {
    return boardSymbol;
  }

  public void setBoardSymbol(String boardSymbol) {
    this.boardSymbol = boardSymbol;
  }

  public BigDecimal getLatestPrice() {
    return latestPrice;
  }

  public void setLatestPrice(BigDecimal latestPrice) {
    this.latestPrice = latestPrice;
  }

  public BigDecimal getPriceChange() {
    return priceChange;
  }

  public void setPriceChange(BigDecimal priceChange) {
    this.priceChange = priceChange;
  }

  public BigDecimal getPriceChangeAmount() {
    return priceChangeAmount;
  }

  public void setPriceChangeAmount(BigDecimal priceChangeAmount) {
    this.priceChangeAmount = priceChangeAmount;
  }

  public BigDecimal getTotalMarketValue() {
    return totalMarketValue;
  }

  public void setTotalMarketValue(BigDecimal totalMarketValue) {
    this.totalMarketValue = totalMarketValue;
  }

  public BigDecimal getTurnoverRatio() {
    return turnoverRatio;
  }

  public void setTurnoverRatio(BigDecimal turnoverRatio) {
    this.turnoverRatio = turnoverRatio;
  }

  public Integer getRiseNum() {
    return riseNum;
  }

  public void setRiseNum(Integer riseNum) {
    this.riseNum = riseNum;
  }

  public Integer getFallNum() {
    return fallNum;
  }

  public void setFallNum(Integer fallNum) {
    this.fallNum = fallNum;
  }

  public String getRiseMaxStockSymbol() {
    return riseMaxStockSymbol;
  }

  public void setRiseMaxStockSymbol(String riseMaxStockSymbol) {
    this.riseMaxStockSymbol = riseMaxStockSymbol;
  }

  public BigDecimal getRiseMaxStockPriceChange() {
    return riseMaxStockPriceChange;
  }

  public void setRiseMaxStockPriceChange(BigDecimal riseMaxStockPriceChange) {
    this.riseMaxStockPriceChange = riseMaxStockPriceChange;
  }


}
