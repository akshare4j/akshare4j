package com.github.leeyazhou.akshare4j.tencent.model;

/**
 * 腾讯 K 线请求 DTO
 * 
 * @author leeyazhou
 */
public class TencentKLineRequestDTO {

  private String code;
  private String ktype; // day, week, month
  private String fqtype; // qfq, hfq
  private int limit;
  private String openid;
  private String app;
  private long t;
  private int scenes;
  private String xcxname;
  @com.alibaba.fastjson2.annotation.JSONField(name = "come_from")
  private int comeFrom;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getKtype() {
    return ktype;
  }

  public void setKtype(String ktype) {
    this.ktype = ktype;
  }

  public String getFqtype() {
    return fqtype;
  }

  public void setFqtype(String fqtype) {
    this.fqtype = fqtype;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getApp() {
    return app;
  }

  public void setApp(String app) {
    this.app = app;
  }

  public long getT() {
    return t;
  }

  public void setT(long t) {
    this.t = t;
  }

  public int getScenes() {
    return scenes;
  }

  public void setScenes(int scenes) {
    this.scenes = scenes;
  }

  public String getXcxname() {
    return xcxname;
  }

  public void setXcxname(String xcxname) {
    this.xcxname = xcxname;
  }

  public int getComeFrom() {
    return comeFrom;
  }

  public void setComeFrom(int comeFrom) {
    this.comeFrom = comeFrom;
  }
}
