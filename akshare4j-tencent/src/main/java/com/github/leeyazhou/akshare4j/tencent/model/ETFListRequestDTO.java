/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent.model;

import com.alibaba.fastjson2.annotation.JSONField;

/**
 * @author leeyazhou
 */
public class ETFListRequestDTO {


  private String app;// mini_h5
  @JSONField(name = "board_type")
  private String boardType;// etf_all
  @JSONField(name = "sort_type")
  private String sortType;// priceRatio
  private String direct;// down
  private int count;// 50
  private int offset;// 276
  private String comment;// 1
  private String label;// 11,22,21,20,12,23

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



}
