/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney.model;

import java.util.List;

/**
 * 
 */
public class DataWrapper<T> {

  private int total;
  private List<T> diff;

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public List<T> getDiff() {
    return diff;
  }

  public void setDiff(List<T> diff) {
    this.diff = diff;
  }



}
