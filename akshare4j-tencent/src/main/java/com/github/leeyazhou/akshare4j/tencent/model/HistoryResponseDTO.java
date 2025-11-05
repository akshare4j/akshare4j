package com.github.leeyazhou.akshare4j.tencent.model;

import java.util.List;

public class HistoryResponseDTO {
  private List<TencentKLineInfo> nodes;

  private TencentStockQt qt;

  /**
   * @param nodes the nodes to set
   */
  public void setNodes(List<TencentKLineInfo> nodes) {
    this.nodes = nodes;
  }

  /**
   * @return the nodes
   */
  public List<TencentKLineInfo> getNodes() {
    return nodes;
  }

  /**
   * @param qt the qt to set
   */
  public void setQt(TencentStockQt qt) {
    this.qt = qt;
  }

  /**
   * @return the qt
   */
  public TencentStockQt getQt() {
    return qt;
  }
}
