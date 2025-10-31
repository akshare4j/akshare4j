/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent.model;

import java.util.List;

/**
 * @author leeyazhou
 */
public class StockListResponse {

  private List<TencentStockInfo> rankList;

  /**
   * @param rankList the rankList to set
   */
  public void setRankList(List<TencentStockInfo> rankList) {
    this.rankList = rankList;
  }

  /**
   * @return the rankList
   */
  public List<TencentStockInfo> getRankList() {
    return rankList;
  }
}

