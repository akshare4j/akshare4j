/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney;

import java.util.List;
import org.junit.jupiter.api.Test;
import com.alibaba.fastjson2.JSON;
import com.github.leeyazhou.akshare4j.eastmoney.model.KlineInfo;
import com.github.leeyazhou.akshare4j.eastmoney.model.enums.Adjust;
import com.github.leeyazhou.akshare4j.eastmoney.model.enums.KlinePeriod;
import com.github.leeyazhou.akshare4j.eastmoney.model.enums.MarketType;

/**
 * 
 */
class StockHistoryApiTest {
  public static final String symbol = "601360";

  @Test
  void testQuerySymbolMap() {}

  @Test
  void testGetKlines() {
    String start_date = "20240301";
    String end_date = "20240528";
    List<KlineInfo> klines =
        StockHistoryApi.getKlines(symbol, MarketType.HK, start_date, end_date, KlinePeriod.daily, Adjust.QFQ);
    System.out.println(JSON.toJSONString(klines));

  }

  @Test
  void teststock_us_hist() {
    String usSymbol = "106.TTE";
    String end_date = "20240528";
    List<KlineInfo> klines = StockHistoryApi.stock_us_hist(usSymbol, end_date, 100, KlinePeriod.daily, Adjust.QFQ);
    System.out.println(JSON.toJSONString(klines));

  }

}
