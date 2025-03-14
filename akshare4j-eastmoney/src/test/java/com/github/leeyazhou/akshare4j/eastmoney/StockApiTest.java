/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import com.alibaba.fastjson2.JSON;
import com.github.leeyazhou.akshare4j.eastmoney.model.StockBoardIndustryInfo;
import com.github.leeyazhou.akshare4j.eastmoney.model.StockIndivalInfo;
import com.github.leeyazhou.akshare4j.eastmoney.model.StockInfo;
import com.github.leeyazhou.akshare4j.eastmoney.model.enums.MarketType;

/**
 * 
 */
class StockApiTest {
  public static final String symbol = "601360";

  @Test
  void testQuerySymbolMap() {
    Map<String, String> symbolMap = StockApi.querySymbolMap();
    System.out.println(JSON.toJSONString(symbolMap));
    System.out.println(symbolMap.size());
  }

  @Test
  public void teststock_individual_info_em() {
    StockIndivalInfo stock_individual_info_em = StockApi.stock_individual_info_em(symbol, MarketType.SZ);
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
    // System.out.println(JSON.toJSONString(stocks));
    System.out.println(stocks.size());
    stocks = stocks.stream().filter(s -> s.getOpen() != null && s.getOpen().compareTo(BigDecimal.ZERO) > 0)
        .collect(Collectors.toList());
    // System.out.println(JSON.toJSONString(stocks));
    System.out.println(stocks.size());
  }

  @Test
  public void testStock_board_industry_name_em() {
    List<StockBoardIndustryInfo> stocks = StockApi.stock_board_industry_name_em();
    System.out.println(JSON.toJSONString(stocks));
    System.out.println(stocks.size());
  }

  @Test
  public void testStock_board_industry_cons_em() {
    List<StockInfo> stocks = StockApi.stock_board_industry_cons_em("BK1027");

    System.out.println(JSON.toJSONString(stocks));
    System.out.println(stocks.size());

  }
}
