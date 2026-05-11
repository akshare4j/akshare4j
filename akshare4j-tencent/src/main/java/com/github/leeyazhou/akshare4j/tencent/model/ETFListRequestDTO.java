package com.github.leeyazhou.akshare4j.tencent.model;

import com.alibaba.fastjson2.annotation.JSONField;

/**
 * ETF 列表请求 DTO
 * 
 * @author leeyazhou
 */
public class ETFListRequestDTO {

  /**
   * 应用标识
   */
  private String app;

  /**
   * 板块类型
   */
  @JSONField(name = "board_type")
  private String boardType;

  /**
   * 排序字段
   */
  @JSONField(name = "sort_type")
  private String sortType;

  /**
   * 排序方向
   */
  private String direct;

  /**
   * 每页数量
   */
  private int count;

  /**
   * 偏移量
   */
  private int offset;

  /**
   * 评论标识
   */
  private String comment;

  /**
   * 标签过滤
   */
  private String label;

  /**
   * 基金类型
   */
  @JSONField(name = "fund_type")
  private String fundType;

  /**
   * 用户标识
   */
  private String openid;

  public String getApp() {
    return app;
  }

  public void setApp(String app) {
    this.app = app;
  }

  public String getBoardType() {
    return boardType;
  }

  public void setBoardType(String boardType) {
    this.boardType = boardType;
  }

  public String getSortType() {
    return sortType;
  }

  public void setSortType(String sortType) {
    this.sortType = sortType;
  }

  public String getDirect() {
    return direct;
  }

  public void setDirect(String direct) {
    this.direct = direct;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getFundType() {
    return fundType;
  }

  public void setFundType(String fundType) {
    this.fundType = fundType;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }
}
