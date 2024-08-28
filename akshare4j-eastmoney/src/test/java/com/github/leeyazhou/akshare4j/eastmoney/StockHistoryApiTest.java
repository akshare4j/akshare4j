/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney;

import java.util.List;
import org.junit.jupiter.api.Test;
import com.alibaba.fastjson2.JSON;
import com.github.leeyazhou.akshare4j.eastmoney.StockHistoryApi;
import com.github.leeyazhou.akshare4j.eastmoney.model.KlineInfo;
import com.github.leeyazhou.akshare4j.eastmoney.model.enums.Adjust;
import com.github.leeyazhou.akshare4j.eastmoney.model.enums.KlinePeriod;

/**
 * 
 */
class StockHistoryApiTest {

  @Test
  void testQuerySymbolMap() {
  }

  @Test
  void testGetKlines() {

    String symbol = "601360";
    String start_date = "20240301";
    String end_date = "20240528";
    List<KlineInfo> klines = StockHistoryApi.getKlines(symbol, start_date, end_date, KlinePeriod.daily, Adjust.QFQ);
    System.out.println(JSON.toJSONString(klines));

  }

}
