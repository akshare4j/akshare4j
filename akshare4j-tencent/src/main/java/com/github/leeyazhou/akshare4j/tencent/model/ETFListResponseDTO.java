/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent.model;

import java.util.List;
import com.alibaba.fastjson2.annotation.JSONField;

/**
 * @author leeyazhou
 */
public class ETFListResponseDTO {
  @JSONField(name = "rank_list")
  private List<TencentETFInfo> data;

  /**
   * @param data the data to set
   */
  public void setData(List<TencentETFInfo> data) {
    this.data = data;
  }

  /**
   * @return the data
   */
  public List<TencentETFInfo> getData() {
    return data;
  }
}
