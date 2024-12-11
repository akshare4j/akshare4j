/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney;

import java.util.List;
import org.junit.jupiter.api.Test;
import com.alibaba.fastjson2.JSON;
import com.github.leeyazhou.akshare4j.eastmoney.model.KlineInfo;
import com.github.leeyazhou.akshare4j.eastmoney.model.StockInfo;
import com.github.leeyazhou.akshare4j.eastmoney.model.enums.Adjust;
import com.github.leeyazhou.akshare4j.eastmoney.model.enums.KlinePeriod;

/**
 * 
 */
class ETFApiTest {

  @Test
  void testQueryETFList() {
    List<StockInfo> etfData = ETFApi.queryETFList();
    System.out.println("etfData: " + etfData.size());
  }

  @Test
  void testQueryETFHistory() {
    // {"f2":1.079,"f3":2.18,"f4":0.023,"f5":288251,"f6":31050688.268,"f7":0.57,"f8":3.49,"f10":0.5,"f12":"159502","f13":0,"f14":"标普生物科技ETF","f15":1.08,"f16":1.074,"f17":1.074,"f18":1.056}
    List<KlineInfo> etfData = ETFApi.queryETFHistory("0", "159608", "20500000", 500, KlinePeriod.daily, Adjust.QFQ);
    System.out.println("etfData: " + JSON.toJSONString(etfData));
  }

}
