/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent.model;

import com.alibaba.fastjson2.annotation.JSONField;

/**
 * @author leeyazhou
 */
public class StockListReqest {

  @JSONField(name = "board_code")
  private String boardCode;
  @JSONField(name = "_appver")
  private String appVer;

  private String source;
  @JSONField(name = "sort_type")
  private String sortType;
  private String direct;
  private int offset;
  private String app;
  private String scenes;
  private String xcxname;
  @JSONField(name = "come_from")
  private String comeFrom;

  public String getBoardCode() {
    return boardCode;
  }

  public void setBoardCode(String boardCode) {
    this.boardCode = boardCode;
  }

  public String getAppVer() {
    return appVer;
  }

  public void setAppVer(String appVer) {
    this.appVer = appVer;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
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

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public String getApp() {
    return app;
  }

  public void setApp(String app) {
    this.app = app;
  }

  public String getScenes() {
    return scenes;
  }

  public void setScenes(String scenes) {
    this.scenes = scenes;
  }

  public String getXcxname() {
    return xcxname;
  }

  public void setXcxname(String xcxname) {
    this.xcxname = xcxname;
  }

  public String getComeFrom() {
    return comeFrom;
  }

  public void setComeFrom(String comeFrom) {
    this.comeFrom = comeFrom;
  }


}
