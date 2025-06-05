/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.github.leeyazhou.akshare4j.eastmoney.model.DataWrapper;
import com.github.leeyazhou.akshare4j.eastmoney.model.EastMoneyResult;
import com.github.leeyazhou.akshare4j.eastmoney.model.StockBoardIndustryInfo;
import com.github.leeyazhou.akshare4j.eastmoney.model.StockIndivalInfo;
import com.github.leeyazhou.akshare4j.eastmoney.model.StockInfo;
import com.github.leeyazhou.akshare4j.eastmoney.model.enums.MarketType;
import com.github.leeyazhou.akshare4j.util.http.HttpClientUtil;
import com.github.leeyazhou.akshare4j.util.http.HttpResponse;
import com.github.leeyazhou.akshare4j.util.http.HttpUtil;
import com.github.leeyazhou.akshare4j.util.http.RequestContext;

/**
 * 
 */
public class StockApi {
  private static final Logger logger = LoggerFactory.getLogger(StockApi.class);
  private static final Map<String, String> symbolsMap = new ConcurrentHashMap<String, String>();

  /**
   * 东方财富-股票和市场代码
   * 
   * <a>https://quote.eastmoney.com/center/gridlist.html#hs_a_board</a>
   * 
   * @return symbol map
   */
  public static synchronized Map<String, String> querySymbolMap() {
    if (symbolsMap.isEmpty() == false) {
      return symbolsMap;
    }
    String url = "https://push2.eastmoney.com/api/qt/clist/get";
    // String url = "https://80.push2.eastmoney.com/api/qt/clist/get";
    int pageSize = 20;
    int currentPage = 1;
    Map<String, String> result = new HashMap<String, String>();
    for (;;) {
      JSONObject params = new JSONObject();
      params.put("pn", currentPage++);
      params.put("pz", pageSize);
      params.put("po", "1");
      params.put("np", "1");
      params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
      params.put("fltt", "2");
      params.put("invt", "2");
      params.put("fid", "f3");
      params.put("fs", "m:1 t:2,m:1 t:23");
      params.put("fields", "f12");
      params.put("_", currentTime());

      RequestContext context = RequestContext.newContext(url);
      StockHistoryApi.initHttpHeaders(context);
      context.setParams(params);
      HttpResponse httpResponse = HttpUtil.getInstance().get(context);
      logger.info("querySymbolMap1: {}", httpResponse.getResponse());
      EastMoneyResult<DataWrapper<Map<String, String>>> clist = JSON.parseObject(httpResponse.getResponse(),
          new TypeReference<EastMoneyResult<DataWrapper<Map<String, String>>>>() {}.getType());
      if (clist == null || clist.getData() == null) {
        break;
      }
      clist.getData().getDiff().stream().map(d -> d.get("f12")).forEach(s -> result.put(s, "1"));
      if (clist.getData().getDiff().size() < pageSize) {
        break;
      }
    }
    currentPage = 1;
    for (;;) {
      JSONObject params = new JSONObject();
      params.put("pn", currentPage++);
      params.put("pz", pageSize);
      params.put("pn", currentPage);
      params.put("po", "1");
      params.put("np", "1");
      params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
      params.put("fltt", "2");
      params.put("invt", "2");
      params.put("fid", "f3");
      params.put("fs", "m:0 t:81 s:2048");
      params.put("fields", "f12");
      params.put("_", currentTime());

      RequestContext context = RequestContext.newContext(url);
      StockHistoryApi.initHttpHeaders(context);
      context.setParams(params);
      HttpResponse httpResponse = HttpUtil.getInstance().get(context);
      EastMoneyResult<DataWrapper<Map<String, String>>> clist2 = JSON.parseObject(httpResponse.getResponse(),
          new TypeReference<EastMoneyResult<DataWrapper<Map<String, String>>>>() {}.getType());
      if (clist2 != null && clist2.getData() != null) {
        clist2.getData().getDiff().stream().map(d -> d.get("f12")).forEach(s -> result.put(s, "0"));
      }
      logger.info("querySymbolMap2: {}", httpResponse.getResponse());
      if (clist2.getData().getDiff().size() < pageSize) {
        break;
      }
    }
    symbolsMap.putAll(result);

    return result;
  }


  /**
   * 东方财富-个股-股票信息
   * 
   * https://quote.eastmoney.com/concept/sh603777.html?from=classic
   * 
   * @param symbol symbol
   */
  public static StockIndivalInfo stock_individual_info_em(String symbol, MarketType marketType) {
    // "f57": "股票代码",
    // "f58": "股票简称",
    // "f84": "总股本",
    // "f85": "流通股",
    // "f127": "行业",
    // "f116": "总市值",
    // "f117": "流通市值",
    // "f189": "上市时间",
    String url = "http://push2.eastmoney.com/api/qt/stock/get";
    RequestContext context = RequestContext.newContext(url);
    StockHistoryApi.initHttpHeaders(context);
    JSONObject params = new JSONObject();
    params.put("ut", "fa5fd1943c7b386f172d6893dbfba10b");
    params.put("fltt", "2");
    params.put("invt", "2");
    params.put("fields",
        "f120,f121,f122,f174,f175,f59,f163,f43,f57,f58,f169,f170,f46,f44,f51,f168,f47,f164,f116,f60,f45,f52,f50,f48,f167,f117,f71,f161,f49,f530,f135,f136,f137,f138,f139,f141,f142,f144,f145,f147,f148,f140,f143,f146,f149,f55,f62,f162,f92,f173,f104,f105,f84,f85,f183,f184,f185,f186,f187,f188,f189,f190,f191,f192,f107,f111,f86,f177,f78,f110,f262,f263,f264,f267,f268,f255,f256,f257,f258,f127,f199,f128,f198,f259,f260,f261,f171,f277,f278,f279,f288,f152,f250,f251,f252,f253,f254,f269,f270,f271,f272,f273,f274,f275,f276,f265,f266,f289,f290,f286,f285,f292,f293,f294,f295");
    // params.put("secid", String.format("%s.%s", querySymbolMap().get(symbol), symbol));
    params.put("secid", String.format("%s.%s", marketType.getCode(), symbol));
    params.put("_", currentTime());
    context.setParams(params);
    try (CloseableHttpClient httpClient = HttpClientUtil.getInstance().createHttpClient(2)) {
      HttpResponse httpResponse = HttpUtil.getInstance().get(context, httpClient);
      EastMoneyResult<Map<String, String>> eastMoneyResult = JSON.parseObject(httpResponse.getResponse(),
          new TypeReference<EastMoneyResult<Map<String, String>>>() {}.getType());
      Map<String, String> result = eastMoneyResult.getData();
      StockIndivalInfo stockIndivalInfo = new StockIndivalInfo();
      stockIndivalInfo.setSymbol(symbol);
      stockIndivalInfo.setName(result.get("f58"));
      stockIndivalInfo.setListDate(result.get("f189"));
      stockIndivalInfo.setTotalMarketVol(new BigDecimal(result.get("f84")));
      stockIndivalInfo.setTradeMarketVol(new BigDecimal(result.get("f85")));
      stockIndivalInfo.setTotalMarketValue(new BigDecimal(result.get("f116")));
      stockIndivalInfo.setTradeMarketValue(new BigDecimal(result.get("f117")));
      stockIndivalInfo.setIndustry(result.get("f127"));
      return stockIndivalInfo;
    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
  }

  /**
   * 东方财富网-沪深京 A 股-实时行情
   * 
   * https://quote.eastmoney.com/center/gridlist.html#hs_a_board
   */
  public static List<StockInfo> stock_zh_a_spot_em() {
    String url = "https://push2.eastmoney.com/api/qt/clist/get";
    RequestContext context = RequestContext.newContext(url);
    StockHistoryApi.initHttpHeaders(context);
    JSONObject params = new JSONObject();
    int pageSize = 20;
    int currentPage = 1;
    params.put("pz", pageSize);
    params.put("po", "1");
    params.put("np", "1");
    params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
    params.put("fltt", "2");
    params.put("invt", "2");
    params.put("fid", "f3");
    params.put("fs", "m:0 t:6,m:0 t:80,m:1 t:2,m:1 t:23,m:0 t:81 s:2048");
    params.put("fields",
        "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152");
    params.put("_", currentTime());
    context.setParams(params);
    try (CloseableHttpClient httpClient = HttpClientUtil.getInstance().createHttpClient(2)) {
      List<StockInfo> stockInfos = new ArrayList<StockInfo>();
      for (;;) {
        params.put("pn", currentPage++);
        HttpResponse httpResponse = HttpUtil.getInstance().get(context, httpClient);
        logger.info("stock_zh_a_spot_em, currentPage: {}, response: {}", currentPage, httpResponse.getResponse());
        EastMoneyResult<DataWrapper<Map<String, String>>> eastMoneyResult = JSON.parseObject(httpResponse.getResponse(),
            new TypeReference<EastMoneyResult<DataWrapper<Map<String, String>>>>() {}.getType());
        if (eastMoneyResult == null || eastMoneyResult.getData() == null) {
          break;
        }

        // "_",
        // "最新价",f2
        // "涨跌幅",f3
        // "涨跌额",f4
        // "成交量",f5
        // "成交额",f6
        // "振幅",f7
        // "换手率",f8
        // "市盈率-动态",f9
        // "量比",f10
        // "5分钟涨跌",f11
        // "代码",f12
        // "_",f13
        // "名称",f14
        // "最高",f15
        // "最低",f16
        // "今开",f17
        // "昨收",f18
        // "总市值",f20
        // "流通市值",f21
        // "涨速",f22
        // "市净率",f23
        // "60日涨跌幅",f24
        // "年初至今涨跌幅",f25
        // "-",f62
        // "-",f115
        // "-",f128
        // "-",f140
        // "-",f141
        // "-",f136
        // "-",f152
        List<StockInfo> tempStocks = eastMoneyResult.getData().getDiff().stream().map(dif -> {
          try {
            StockInfo stockInfo = new StockInfo();
            stockInfo.setLatestPrice(new BigDecimal(defaultString(dif.get("f2"))));
            stockInfo.setPriceChange(new BigDecimal(defaultString(dif.get("f3"))));
            stockInfo.setPriceChangeAmount(new BigDecimal(defaultString(dif.get("f4"))));
            stockInfo.setVolume(new BigDecimal(defaultString(dif.get("f5"))));
            stockInfo.setVolumeAmount(new BigDecimal(defaultString(dif.get("f6"))));
            stockInfo.setAmplitude(new BigDecimal(defaultString(dif.get("f7"))));
            stockInfo.setTurnoverRatio(new BigDecimal(defaultString(dif.get("f8"))));
            stockInfo.setSymbol(dif.get("f12"));
            MarketType marketType = MarketType.of(dif.get("f13"));
            if (marketType != null) {
              stockInfo.setMarketType(marketType.name());
            }
            stockInfo.setName(dif.get("f14"));
            stockInfo.setOpen(new BigDecimal(defaultString(dif.get("f17"))));
            stockInfo.setCloseYestoday(new BigDecimal(defaultString(dif.get("f18"))));
            stockInfo.setHigh(new BigDecimal(defaultString(dif.get("f15"))));
            stockInfo.setLow(new BigDecimal(defaultString(dif.get("f16"))));
            stockInfo.setTotalMarketValue(new BigDecimal(defaultString(dif.get("f20"))));
            stockInfo.setTradeMarketValue(new BigDecimal(defaultString(dif.get("f21"))));
            return stockInfo;
          } catch (Exception e) {
            logger.error(JSON.toJSONString(dif), e);
            throw e;
          }
        }).collect(Collectors.toList());
        if (tempStocks == null) {
          break;
        }
        stockInfos.addAll(tempStocks);
        if (tempStocks.size() < pageSize) {
          break;
        }
      }
      return stockInfos.stream().distinct().collect(Collectors.toList());
    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
  }

  /**
   * 东方财富网-港股-实时行情
   * 
   * https://quote.eastmoney.com/center/gridlist.html#hk_stocks
   */
  public static List<StockInfo> stock_hk_spot_em() {
    String url = "https://72.push2.eastmoney.com/api/qt/clist/get";
    RequestContext context = RequestContext.newContext(url);
    StockHistoryApi.initHttpHeaders(context);
    JSONObject params = new JSONObject();
    int pageSize = 20;
    int currentPage = 1;
    params.put("pz", pageSize);
    params.put("po", "1");
    params.put("np", "1");
    params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
    params.put("fltt", "2");
    params.put("invt", "2");
    params.put("fid", "f3");
    params.put("fs", "m:128 t:3,m:128 t:4,m:128 t:1,m:128 t:2");
    params.put("fields",
        "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152");
    params.put("_", "1624010056945");
    params.put("_", currentTime());
    context.setParams(params);
    try (CloseableHttpClient httpClient = HttpClientUtil.getInstance().createHttpClient(2)) {
      List<StockInfo> stockInfos = new ArrayList<StockInfo>();
      for (;;) {
        params.put("pn", currentPage++);
        HttpResponse httpResponse = HttpUtil.getInstance().get(context, httpClient);
        logger.info("stock_hk_spot_em, currentPage: {}, response: {}", currentPage, httpResponse.getResponse());
        EastMoneyResult<DataWrapper<Map<String, String>>> eastMoneyResult = JSON.parseObject(httpResponse.getResponse(),
            new TypeReference<EastMoneyResult<DataWrapper<Map<String, String>>>>() {}.getType());
        if (eastMoneyResult == null || eastMoneyResult.getData() == null) {
          break;
        }

        // "_",
        // "最新价",f2
        // "涨跌幅",f3
        // "涨跌额",f4
        // "成交量",f5
        // "成交额",f6
        // "振幅",f7
        // "换手率",f8
        // "市盈率-动态",f9
        // "量比",f10
        // "5分钟涨跌",f11
        // "代码",f12
        // "_",f13
        // "名称",f14
        // "最高",f15
        // "最低",f16
        // "今开",f17
        // "昨收",f18
        // "总市值",f20
        // "流通市值",f21
        // "涨速",f22
        // "市净率",f23
        // "60日涨跌幅",f24
        // "年初至今涨跌幅",f25
        // "-",f62
        // "-",f115
        // "-",f128
        // "-",f140
        // "-",f141
        // "-",f136
        // "-",f152
        List<StockInfo> stockInfosTemp = eastMoneyResult.getData().getDiff().stream().map(dif -> {
          try {
            StockInfo stockInfo = new StockInfo();
            stockInfo.setLatestPrice(new BigDecimal(defaultString(dif.get("f2"))));
            stockInfo.setPriceChange(new BigDecimal(defaultString(dif.get("f3"))));
            stockInfo.setPriceChangeAmount(new BigDecimal(defaultString(dif.get("f4"))));
            stockInfo.setVolume(new BigDecimal(defaultString(dif.get("f5"))));
            stockInfo.setVolumeAmount(new BigDecimal(defaultString(dif.get("f6"))));
            stockInfo.setAmplitude(new BigDecimal(defaultString(dif.get("f7"))));
            stockInfo.setTurnoverRatio(new BigDecimal(defaultString(dif.get("f8"))));
            stockInfo.setSymbol(dif.get("f12"));
            MarketType marketType = MarketType.of(dif.get("f13"));
            if (marketType != null) {
              stockInfo.setMarketType(marketType.name());
            }
            stockInfo.setName(dif.get("f14"));
            stockInfo.setOpen(new BigDecimal(defaultString(dif.get("f17"))));
            stockInfo.setCloseYestoday(new BigDecimal(defaultString(dif.get("f18"))));
            stockInfo.setHigh(new BigDecimal(defaultString(dif.get("f15"))));
            stockInfo.setLow(new BigDecimal(defaultString(dif.get("f16"))));
            stockInfo.setTotalMarketValue(new BigDecimal(defaultString(dif.get("f20"))));
            stockInfo.setTradeMarketValue(new BigDecimal(defaultString(dif.get("f21"))));
            return stockInfo;
          } catch (Exception e) {
            logger.error(JSON.toJSONString(dif), e);
            throw e;
          }
        }).collect(Collectors.toList());
        if (stockInfosTemp == null || stockInfosTemp.size() == 0) {
          break;
        }
        stockInfos.addAll(stockInfosTemp);
        if (stockInfosTemp.size() < pageSize) {
          break;
        }
      }
      return stockInfos.stream().distinct().collect(Collectors.toList());
    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
  }

  /**
   * 东方财富网-美股-实时行情
   * 
   * 美股-实时行情; 延迟 15 min
   * 
   * https://quote.eastmoney.com/center/gridlist.html#us_stocks
   */
  public static List<StockInfo> stock_us_spot_em() {
    String url = "https://72.push2.eastmoney.com/api/qt/clist/get";
    RequestContext context = RequestContext.newContext(url);
    StockHistoryApi.initHttpHeaders(context);
    JSONObject params = new JSONObject();
    int pageSize = 20;
    int currentPage = 1;
    params.put("pz", pageSize);
    params.put("po", "1");
    params.put("np", "1");
    params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
    params.put("fltt", "2");
    params.put("invt", "2");
    params.put("fid", "f3");
    params.put("fs", "m:105,m:106,m:107");
    params.put("fields",
        "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f33,f11,f62,f128,f136,f115,f152");
    params.put("_", currentTime());
    context.setParams(params);
    try (CloseableHttpClient httpClient = HttpClientUtil.getInstance().createHttpClient(2)) {
      List<StockInfo> stockInfos = new ArrayList<StockInfo>();
      for (;;) {
        params.put("pn", currentPage++);
        HttpResponse httpResponse = HttpUtil.getInstance().get(context, httpClient);
        logger.info("stock_us_spot_em, currentPage: {}, response: {}", currentPage, httpResponse.getResponse());
        EastMoneyResult<DataWrapper<Map<String, String>>> eastMoneyResult = JSON.parseObject(httpResponse.getResponse(),
            new TypeReference<EastMoneyResult<DataWrapper<Map<String, String>>>>() {}.getType());
        if (eastMoneyResult == null || eastMoneyResult.getData() == null) {
          break;
        }
        // "_",
        // "最新价",f2
        // "涨跌幅",f3
        // "涨跌额",f4
        // "成交量",f5
        // "成交额",f6
        // "振幅",f7
        // "换手率",f8
        // "_",f9
        // "_",f10
        // "_",f11
        // "简称",f12
        // "编码",f13
        // "名称",f14
        // "最高价",f15
        // "最低价",f16
        // "开盘价",f17
        // "昨收价",f18
        // "总市值",f20
        // "_",f21
        // "_",f22
        // "_",f23
        // "_",f24
        // "_",f25
        // "_",f26
        // "_",f33
        // "_",f62
        // "市盈率",f115
        // "_",f128
        // "_",f140
        // "_",f141
        // "_",f136
        // "_",f152
        List<StockInfo> stockInfosTemp = eastMoneyResult.getData().getDiff().stream().map(dif -> {
          try {
            StockInfo stockInfo = new StockInfo();
            stockInfo.setLatestPrice(new BigDecimal(defaultString(dif.get("f2"))));
            stockInfo.setPriceChange(new BigDecimal(defaultString(dif.get("f3"))));
            stockInfo.setPriceChangeAmount(new BigDecimal(defaultString(dif.get("f4"))));
            stockInfo.setVolume(new BigDecimal(defaultString(dif.get("f5"))));
            stockInfo.setVolumeAmount(new BigDecimal(defaultString(dif.get("f6"))));
            stockInfo.setAmplitude(new BigDecimal(defaultString(dif.get("f7"))));
            stockInfo.setTurnoverRatio(new BigDecimal(defaultString(dif.get("f8"))));
            stockInfo.setSymbol(dif.get("f12"));
            MarketType marketType = MarketType.of(dif.get("f13"));
            if (marketType != null) {
              stockInfo.setMarketType(marketType.name());
            }
            stockInfo.setName(dif.get("f14"));
            stockInfo.setOpen(new BigDecimal(defaultString(dif.get("f17"))));
            stockInfo.setCloseYestoday(new BigDecimal(defaultString(dif.get("f18"))));
            stockInfo.setHigh(new BigDecimal(defaultString(dif.get("f15"))));
            stockInfo.setLow(new BigDecimal(defaultString(dif.get("f16"))));
            stockInfo.setTotalMarketValue(new BigDecimal(defaultString(dif.get("f20"))));
            stockInfo.setTradeMarketValue(new BigDecimal(defaultString(dif.get("f21"))));
            return stockInfo;
          } catch (Exception e) {
            logger.error(JSON.toJSONString(dif), e);
            throw e;
          }
        }).collect(Collectors.toList());
        if (stockInfosTemp == null) {
          break;
        }
        stockInfos.addAll(stockInfosTemp);
        if (stockInfosTemp.size() < pageSize) {
          break;
        }
      }
      return stockInfos.stream().distinct().collect(Collectors.toList());
    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
  }

  /**
   * 东方财富-行业板块<br>
   * 接口: stock_board_industry_name_em
   * 
   * 目标地址: https://quote.eastmoney.com/center/boardlist.html#industry_board
   * 
   * 描述: 东方财富-沪深京板块-行业板块
   * 
   * 限量: 单次返回当前时刻所有行业板的实时行情数据
   * 
   * @return
   */
  public static List<StockBoardIndustryInfo> stock_board_industry_name_em() {
    String url = "https://17.push2.eastmoney.com/api/qt/clist/get";
    RequestContext context = RequestContext.newContext(url);
    StockHistoryApi.initHttpHeaders(context);
    JSONObject params = new JSONObject();
    int pageSize = 20;
    int currentPage = 1;
    params.put("pz", pageSize);
    params.put("po", "1");
    params.put("np", "1");
    params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
    params.put("fltt", "2");
    params.put("invt", "2");
    params.put("fid", "f3");
    params.put("fs", "m:90 t:2 f:!50");
    params.put("fields",
        "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f33,f11,f62,f128,f136,f115,f152,f124,f107,f104,f105,f140,f141,f207,f208,f209,f222");
    params.put("_", currentTime());


    context.setParams(params);
    try (CloseableHttpClient httpClient = HttpClientUtil.getInstance().createHttpClient(2)) {
      List<StockBoardIndustryInfo> stockInfos = new ArrayList<>();
      for (;;) {
        params.put("pn", currentPage++);
        HttpResponse httpResponse = HttpUtil.getInstance().get(context, httpClient);
        logger.info("stock_board_industry_name_em, currentPage: {}, response: {}", currentPage,
            httpResponse.getResponse());
        EastMoneyResult<DataWrapper<Map<String, String>>> eastMoneyResult = JSON.parseObject(httpResponse.getResponse(),
            new TypeReference<EastMoneyResult<DataWrapper<Map<String, String>>>>() {}.getType());
        if (eastMoneyResult == null || eastMoneyResult.getData() == null) {
          break;
        }
        // "排名",
        // "-",
        // "最新价",
        // "涨跌幅",
        // "涨跌额",
        // "-",
        // "_",
        // "-",
        // "换手率",
        // "-",
        // "-",
        // "-",
        // "板块代码",
        // "-",
        // "板块名称",
        // "-",
        // "-",
        // "-",
        // "-",
        // "总市值",
        // "-",
        // "-",
        // "-",
        // "-",
        // "-",
        // "-",
        // "-",
        // "-",
        // "上涨家数",
        // "下跌家数",
        // "-",
        // "-",
        // "-",
        // "领涨股票",
        // "-",
        // "-",
        // "领涨股票-涨跌幅",
        // "-",
        // "-",
        // "-",
        // "-",
        // "-",
        List<StockBoardIndustryInfo> stockInfosTemp = eastMoneyResult.getData().getDiff().stream().map(dif -> {
          try {
            StockBoardIndustryInfo stockInfo = new StockBoardIndustryInfo();
            stockInfo.setBoardName(dif.get("f14"));
            stockInfo.setBoardSymbol(dif.get("f12"));
            stockInfo.setRiseNum(Integer.parseInt(dif.get("f104")));
            stockInfo.setFallNum(Integer.parseInt(dif.get("f105")));
            stockInfo.setRiseMaxStockSymbol(dif.get("f104"));
            stockInfo.setRiseMaxStockPriceChange(new BigDecimal(defaultString(dif.get("f136"))));
            stockInfo.setFallNum(Integer.parseInt(dif.get("f105")));
            stockInfo.setLatestPrice(new BigDecimal(defaultString(dif.get("f2"))));
            stockInfo.setPriceChange(new BigDecimal(defaultString(dif.get("f3"))));
            stockInfo.setPriceChangeAmount(new BigDecimal(defaultString(dif.get("f4"))));
            stockInfo.setTurnoverRatio(new BigDecimal(defaultString(dif.get("f8"))));
            // MarketType marketType = MarketType.of(dif.get("f13"));
            // if (marketType != null) {
            // stockInfo.setMarketType(marketType.name());
            // }
            // stockInfo.setName(dif.get("f14"));
            // stockInfo.setOpen(new BigDecimal(defaultString(dif.get("f17"))));
            // stockInfo.setCloseYestoday(new BigDecimal(defaultString(dif.get("f18"))));
            // stockInfo.setHigh(new BigDecimal(defaultString(dif.get("f15"))));
            // stockInfo.setLow(new BigDecimal(defaultString(dif.get("f16"))));
            stockInfo.setTotalMarketValue(new BigDecimal(defaultString(dif.get("f20"))));
            // stockInfo.setTradeMarketValue(new BigDecimal(defaultString(dif.get("f21"))));
            return stockInfo;
          } catch (Exception e) {
            logger.error(JSON.toJSONString(dif), e);
            throw e;
          }
        }).collect(Collectors.toList());
        if (stockInfosTemp == null) {
          break;
        }
        stockInfos.addAll(stockInfosTemp);
        if (stockInfosTemp.size() < pageSize) {
          break;
        }
      }
      return stockInfos.stream().distinct().collect(Collectors.toList());
    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
  }

  /**
   * 接口: stock_board_industry_cons_em
   * 
   * 目标地址: https://data.eastmoney.com/bkzj/BK1027.html
   * 
   * 描述: 东方财富-沪深板块-行业板块-板块成份
   * 
   * 限量: 单次返回指定 symbol 的所有成份股
   * 
   * @return
   */
  public static List<StockInfo> stock_board_industry_cons_em(String symbol) {
    final String url = "https://29.push2.eastmoney.com/api/qt/clist/get";
    RequestContext context = RequestContext.newContext(url);
    StockHistoryApi.initHttpHeaders(context);
    JSONObject params = new JSONObject();
    int pageSize = 100;
    int currentPage = 1;
    params.put("pz", pageSize);
    params.put("pn", "1");
    params.put("po", "1");
    params.put("np", "1");
    params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
    params.put("fltt", "2");
    params.put("invt", "2");
    params.put("fid", "f3");
    params.put("fs", String.format("b:%s f:!50", symbol));
    params.put("fields",
        "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152,f45");
    params.put("_", currentTime());
    context.setParams(params);
    try (CloseableHttpClient httpClient = HttpClientUtil.getInstance().createHttpClient(2)) {
      List<StockInfo> stockInfos = new ArrayList<StockInfo>();
      for (;;) {
        params.put("pn", currentPage++);
        HttpResponse httpResponse = HttpUtil.getInstance().get(context, httpClient);
        logger.info("stock_zh_a_spot_em, currentPage: {}, response: {}", currentPage, httpResponse.getResponse());
        EastMoneyResult<DataWrapper<Map<String, String>>> eastMoneyResult = JSON.parseObject(httpResponse.getResponse(),
            new TypeReference<EastMoneyResult<DataWrapper<Map<String, String>>>>() {}.getType());
        if (eastMoneyResult == null || eastMoneyResult.getData() == null) {
          break;
        }

        // "_",
        // "最新价",f2
        // "涨跌幅",f3
        // "涨跌额",f4
        // "成交量",f5
        // "成交额",f6
        // "振幅",f7
        // "换手率",f8
        // "市盈率-动态",f9
        // "量比",f10
        // "5分钟涨跌",f11
        // "代码",f12
        // "_",f13
        // "名称",f14
        // "最高",f15
        // "最低",f16
        // "今开",f17
        // "昨收",f18
        // "总市值",f20
        // "流通市值",f21
        // "涨速",f22
        // "市净率",f23
        // "60日涨跌幅",f24
        // "年初至今涨跌幅",f25
        // "-",f62
        // "-",f115
        // "-",f128
        // "-",f140
        // "-",f141
        // "-",f136
        // "-",f152
        List<StockInfo> tempStocks = eastMoneyResult.getData().getDiff().stream().map(dif -> {
          try {
            StockInfo stockInfo = new StockInfo();
            stockInfo.setLatestPrice(new BigDecimal(defaultString(dif.get("f2"))));
            stockInfo.setPriceChange(new BigDecimal(defaultString(dif.get("f3"))));
            stockInfo.setPriceChangeAmount(new BigDecimal(defaultString(dif.get("f4"))));
            stockInfo.setVolume(new BigDecimal(defaultString(dif.get("f5"))));
            stockInfo.setVolumeAmount(new BigDecimal(defaultString(dif.get("f6"))));
            stockInfo.setAmplitude(new BigDecimal(defaultString(dif.get("f7"))));
            stockInfo.setTurnoverRatio(new BigDecimal(defaultString(dif.get("f8"))));
            stockInfo.setSymbol(dif.get("f12"));
            MarketType marketType = MarketType.of(dif.get("f13"));
            if (marketType != null) {
              stockInfo.setMarketType(marketType.name());
            }
            stockInfo.setName(dif.get("f14"));
            stockInfo.setOpen(new BigDecimal(defaultString(dif.get("f17"))));
            stockInfo.setCloseYestoday(new BigDecimal(defaultString(dif.get("f18"))));
            stockInfo.setHigh(new BigDecimal(defaultString(dif.get("f15"))));
            stockInfo.setLow(new BigDecimal(defaultString(dif.get("f16"))));
            stockInfo.setTotalMarketValue(new BigDecimal(defaultString(dif.get("f20"))));
            stockInfo.setTradeMarketValue(new BigDecimal(defaultString(dif.get("f21"))));
            return stockInfo;
          } catch (Exception e) {
            logger.error(JSON.toJSONString(dif), e);
            throw e;
          }
        }).collect(Collectors.toList());
        if (tempStocks == null) {
          break;
        }
        stockInfos.addAll(tempStocks);
        if (tempStocks.size() < pageSize) {
          break;
        }
      }
      return stockInfos.stream().distinct().collect(Collectors.toList());
    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
  }

  private static String defaultString(String value) {
    if ("-".equals(value)) {
      return "0";
    }
    return value;
  }

  private static String currentTime() {
    return String.valueOf(System.currentTimeMillis());
  }
}
