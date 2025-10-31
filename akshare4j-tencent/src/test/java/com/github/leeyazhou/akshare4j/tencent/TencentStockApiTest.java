/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent;

import static org.junit.jupiter.api.Assertions.fail;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.alibaba.fastjson2.JSON;
import com.github.leeyazhou.akshare4j.tencent.model.TencentStockInfo;
import com.github.leeyazhou.akshare4j.tencent.model.enums.TencentMarketType;

/**
 * @author leeyazhou
 */
class TencentStockApiTest {

  @Test
  void testGetKlines() {
    List<TencentStockInfo> stockList = TencentStockApi.getStockList(TencentMarketType.SH);
    System.out.println("stockList size: " + stockList.size());
    for (TencentStockInfo stock : stockList) {
      System.out.println("stock: " + JSON.toJSONString(stock));
    }
  }

  /**
   * Test method for
   * {@link com.github.leeyazhou.akshare4j.tencent.TencentStockApi#doGetStockList(com.github.leeyazhou.akshare4j.tencent.model.StockListReqest)}.
   */
  @Test
  void testDoGetStockList() {
    fail("Not yet implemented");
  }

}
