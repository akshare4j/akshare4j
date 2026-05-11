package com.github.leeyazhou.akshare4j.tencent.model;

import com.github.leeyazhou.akshare4j.tencent.model.enums.TencentMarketType;

/**
 * 腾讯 ETF 信息领域模型（贫血模型）
 * 
 * @author leeyazhou
 */
public class TencentETFInfo {

  /**
   * 股票代码，如 "sz159945"
   */
  private String code;

  /**
   * 份额折算比例
   */
  private String cwjsg;

  /**
   * 份额折算金额
   */
  private String cwjsgje;

  /**
   * 基金名称
   */
  private String fundName;

  /**
   * 规模
   */
  private String gm;

  /**
   * 简称
   */
  private String name;

  /**
   * 状态
   */
  private String state;

  /**
   * 股票类型，如 "ETF"
   */
  private String stockType;

  /**
   * 换手率
   */
  private String turnover;

  /**
   * 成交量
   */
  private String volume;

  /**
   * 溢价率
   */
  private String yjl;

  /**
   * 涨跌幅
   */
  private String zdf;

  /**
   * 20日涨跌幅
   */
  private String zdfD20;

  /**
   * 5日涨跌幅
   */
  private String zdfD5;

  /**
   * 累计涨跌幅
   */
  private String zdfY;

  /**
   * 最新价
   */
  private String zxj;

  /**
   * 市场类型（转换后）
   */
  private TencentMarketType marketType;

  /**
   * 纯数字代码（转换后）
   */
  private String symbol;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCwjsg() {
    return cwjsg;
  }

  public void setCwjsg(String cwjsg) {
    this.cwjsg = cwjsg;
  }

  public String getCwjsgje() {
    return cwjsgje;
  }

  public void setCwjsgje(String cwjsgje) {
    this.cwjsgje = cwjsgje;
  }

  public String getFundName() {
    return fundName;
  }

  public void setFundName(String fundName) {
    this.fundName = fundName;
  }

  public String getGm() {
    return gm;
  }

  public void setGm(String gm) {
    this.gm = gm;
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

  public String getTurnover() {
    return turnover;
  }

  public void setTurnover(String turnover) {
    this.turnover = turnover;
  }

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public String getYjl() {
    return yjl;
  }

  public void setYjl(String yjl) {
    this.yjl = yjl;
  }

  public String getZdf() {
    return zdf;
  }

  public void setZdf(String zdf) {
    this.zdf = zdf;
  }

  public String getZdfD20() {
    return zdfD20;
  }

  public void setZdfD20(String zdfD20) {
    this.zdfD20 = zdfD20;
  }

  public String getZdfD5() {
    return zdfD5;
  }

  public void setZdfD5(String zdfD5) {
    this.zdfD5 = zdfD5;
  }

  public String getZdfY() {
    return zdfY;
  }

  public void setZdfY(String zdfY) {
    this.zdfY = zdfY;
  }

  public String getZxj() {
    return zxj;
  }

  public void setZxj(String zxj) {
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
