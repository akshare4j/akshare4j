/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent.model;

import com.alibaba.fastjson2.annotation.JSONField;

/**
 * @author leeyazhou
 */
public class QueryStockInfoRequestDTO {


  /* wzqxcx */
  private String app;

  /* sz301361 */
  private String code;

  /* d1 */
  @JSONField(name = "fs.type")
  private String fsType;

  /* fs,qt,attribute */
  private String needDataType;

  /* os-ppuOrnTEM8Zki353-okWCEP7I */
  private String openid;

  /* 1762352229991 */
  private String t;

  /* wzqxcx */
  @JSONField(name = "x-appid")
  private String xAppid;

  /* 2 */
  @JSONField(name = "x-sa-v")
  private String xSaV;

  /* e6fa62c7827d5b40bcc43c28225cc640 */
  @JSONField(name = "x-sa-sign")
  private String xSaSign;

  /* 1762352229 */
  @JSONField(name = "x-timestamp")
  private String xTimestamp;

  public String getApp() {
    return app;
  }

  public void setApp(String app) {
    this.app = app;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getFsType() {
    return fsType;
  }

  public void setFsType(String fsType) {
    this.fsType = fsType;
  }

  public String getNeedDataType() {
    return needDataType;
  }

  public void setNeedDataType(String needDataType) {
    this.needDataType = needDataType;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getT() {
    return t;
  }

  public void setT(String t) {
    this.t = t;
  }

  public String getxAppid() {
    return xAppid;
  }

  public void setxAppid(String xAppid) {
    this.xAppid = xAppid;
  }

  public String getxSaV() {
    return xSaV;
  }

  public void setxSaV(String xSaV) {
    this.xSaV = xSaV;
  }

  public String getxSaSign() {
    return xSaSign;
  }

  public void setxSaSign(String xSaSign) {
    this.xSaSign = xSaSign;
  }

  public String getxTimestamp() {
    return xTimestamp;
  }

  public void setxTimestamp(String xTimestamp) {
    this.xTimestamp = xTimestamp;
  }


}
