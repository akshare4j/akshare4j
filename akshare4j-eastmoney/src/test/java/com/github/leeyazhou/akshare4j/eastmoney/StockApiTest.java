/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import com.alibaba.fastjson2.JSON;
import com.github.leeyazhou.akshare4j.eastmoney.model.StockIndivalInfo;
import com.github.leeyazhou.akshare4j.eastmoney.model.StockInfo;

/**
 * 
 */
class StockApiTest {
  public static final String symbol = "601360";

  @Test
  void testQuerySymbolMap() {}

  @Test
  public void teststock_individual_info_em() {
    StockIndivalInfo stock_individual_info_em = StockApi.stock_individual_info_em(symbol);
    System.out.println(JSON.toJSONString(stock_individual_info_em));
  }

  @Test
  public void teststock_zh_a_spot_em() {
    List<StockInfo> stocks = StockApi.stock_zh_a_spot_em();
    System.out.println(JSON.toJSONString(stocks));
    System.out.println(stocks.size());
  }

  @Test
  public void teststock_us_spot_em() {
    List<StockInfo> stocks = StockApi.stock_us_spot_em();
    System.out.println(JSON.toJSONString(stocks));
    System.out.println(stocks.size());
  }

  @Test
  public void testStock_hk_spot_em() {
    List<StockInfo> stocks = StockApi.stock_hk_spot_em();
    System.out.println(JSON.toJSONString(stocks));
    System.out.println(stocks.size());
    stocks = stocks.stream().filter(s -> s.getOpen() != null && s.getOpen().compareTo(BigDecimal.ZERO) > 0)
        .collect(Collectors.toList());
    System.out.println(JSON.toJSONString(stocks));
    System.out.println(stocks.size());
  }
}
