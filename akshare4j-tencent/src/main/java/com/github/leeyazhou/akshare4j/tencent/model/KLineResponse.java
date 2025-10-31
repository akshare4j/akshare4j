package com.github.leeyazhou.akshare4j.tencent.model;

import java.util.List;

public class KLineResponse {
  private List<KLineData> nodes;

  /**
   * @param nodes the nodes to set
   */
  public void setNodes(List<KLineData> nodes) {
    this.nodes = nodes;
  }

  /**
   * @return the nodes
   */
  public List<KLineData> getNodes() {
    return nodes;
  }
}
