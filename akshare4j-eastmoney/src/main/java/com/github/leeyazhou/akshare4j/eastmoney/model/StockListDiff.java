/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney.model;

import java.util.List;
import java.util.Map;

/**
 * 
 */
public class StockListDiff {

  private List<Map<String,String>> diff;
  
  /**
   * @param diff the diff to set
   */
  public void setDiff(List<Map<String, String>> diff) {
    this.diff = diff;
  }
  
  /**
   * @return the diff
   */
  public List<Map<String, String>> getDiff() {
    return diff;
  }
}
