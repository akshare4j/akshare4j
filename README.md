# akshare4j
akshare4j

## Getting Started

```
<dependency>
	<groupId>com.github.leeyazhou</groupId>
	<artifactId>akshare4j-eastmoney</artifactId>
	<version>${project.version}</version>
</dependency>
```

## Unit Test

```
class StockHistoryApiTest {
  public static final String symbol = "601360";

  @Test
  void testQuerySymbolMap() {}

  @Test
  void testGetKlines() {
    String start_date = "20240301";
    String end_date = "20240528";
    List<KlineInfo> klines = StockHistoryApi.getKlines(symbol, start_date, end_date, KlinePeriod.daily, Adjust.QFQ);
    System.out.println(JSON.toJSONString(klines));

  }

  @Test
  void teststock_us_hist() {
    String usSymbol = "106.TTE";
    String end_date = "20240528";
    List<KlineInfo> klines = StockHistoryApi.stock_us_hist(usSymbol, end_date, 100, KlinePeriod.daily, Adjust.QFQ);
    System.out.println(JSON.toJSONString(klines));

  }

  @Test
  public void teststock_individual_info_em() {
    StockIndivalInfo stock_individual_info_em = StockHistoryApi.stock_individual_info_em(symbol);
    System.out.println(JSON.toJSONString(stock_individual_info_em));
  }

  @Test
  public void teststock_zh_a_spot_em() {
    List<StockInfo> stocks = StockHistoryApi.stock_zh_a_spot_em();
    System.out.println(JSON.toJSONString(stocks));
    System.out.println(stocks.size());
  }

  @Test
  public void teststock_us_spot_em() {
    List<StockInfo> stocks = StockHistoryApi.stock_us_spot_em();
    System.out.println(JSON.toJSONString(stocks));
    System.out.println(stocks.size());
  }
}
```