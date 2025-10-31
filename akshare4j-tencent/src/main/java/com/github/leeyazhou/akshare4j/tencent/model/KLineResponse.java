package com.github.leeyazhou.akshare4j.tencent.model;

import java.util.List;

public class KLineResponse {
  private List<TencentKLineInfo> nodes;

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
}
