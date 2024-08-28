/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney.model;

import java.util.List;

/**
 * 
 */
public class CListInfo {

  private List<DiffInfo> diff;

  /**
   * @param diff the diff to set
   */
  public void setDiff(List<DiffInfo> diff) {
    this.diff = diff;
  }

  /**
   * @return the diff
   */
  public List<DiffInfo> getDiff() {
    return diff;
  }
}
