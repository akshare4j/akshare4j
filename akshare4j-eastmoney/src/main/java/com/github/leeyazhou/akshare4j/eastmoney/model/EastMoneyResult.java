/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney.model;

/**
 * 
 */
public class EastMoneyResult<T> {
  private T data;
  private String dlmkts;
  private Integer full;
  private Integer lt;
  private Integer rt;
  private Integer rc;
  private Long srv;

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public String getDlmkts() {
    return dlmkts;
  }

  public void setDlmkts(String dlmkts) {
    this.dlmkts = dlmkts;
  }

  public Integer getFull() {
    return full;
  }

  public void setFull(Integer full) {
    this.full = full;
  }

  public Integer getLt() {
    return lt;
  }

  public void setLt(Integer lt) {
    this.lt = lt;
  }

  public Integer getRt() {
    return rt;
  }

  public void setRt(Integer rt) {
    this.rt = rt;
  }

  public Integer getRc() {
    return rc;
  }

  public void setRc(Integer rc) {
    this.rc = rc;
  }

  public Long getSrv() {
    return srv;
  }

  public void setSrv(Long srv) {
    this.srv = srv;
  }


}
