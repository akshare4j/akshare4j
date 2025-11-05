/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent;

import java.util.List;
import org.junit.jupiter.api.Test;
import com.alibaba.fastjson2.JSON;
import com.github.leeyazhou.akshare4j.tencent.model.TencentStockInfo;

/**
 * @author leeyazhou
 */
class TencentStockApiTest {

  @Test
  void testGetKlines() {
    List<TencentStockInfo> stockList = TencentStockApi.getBoardRankList();
    System.out.println("stockList size: " + stockList.size());
    for (TencentStockInfo stock : stockList) {
      System.out.println("stock: " + JSON.toJSONString(stock));
    }
  }

  @Test
  void testDoGetStockList() {}

  @Test
  void testQueryStockInfo() {
    TencentStockApi.queryStockInfo("sz301361", "os-ppuOrnTEM8Zki353-okWCEP7I", "323ed30b7d96019f05eedb268c443283");
  }

}
