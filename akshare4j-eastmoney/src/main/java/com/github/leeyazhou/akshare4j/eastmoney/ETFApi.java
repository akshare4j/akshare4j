/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.fastjson2.util.DateUtils;
import com.github.leeyazhou.akshare4j.eastmoney.model.DataWrapper;
import com.github.leeyazhou.akshare4j.eastmoney.model.EastMoneyResult;
import com.github.leeyazhou.akshare4j.eastmoney.model.KlineInfo;
import com.github.leeyazhou.akshare4j.eastmoney.model.KlineResult;
import com.github.leeyazhou.akshare4j.eastmoney.model.StockInfo;
import com.github.leeyazhou.akshare4j.eastmoney.model.enums.Adjust;
import com.github.leeyazhou.akshare4j.eastmoney.model.enums.KlinePeriod;
import com.github.leeyazhou.akshare4j.util.http.HttpClientUtil;
import com.github.leeyazhou.akshare4j.util.http.HttpResponse;
import com.github.leeyazhou.akshare4j.util.http.HttpUtil;
import com.github.leeyazhou.akshare4j.util.http.RequestContext;

/**
 * 
 */
public class ETFApi {
  private static final Logger logger = LoggerFactory.getLogger(ETFApi.class);

  /**
   * eastmoney-ETF
   * 
   * <a>https://quote.eastmoney.com/center/gridlist.html#fund_etf</a>
   * 
   * @return symbol map
   */
  public static synchronized List<StockInfo> queryETFList() {
    String url = "https://26.push2.eastmoney.com/api/qt/clist/get";
    JSONObject params = new JSONObject();
    params.put("pn", "1");
    params.put("pz", "2000");
    params.put("po", "1");
    params.put("np", "1");
    params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
    params.put("fltt", "2");
    params.put("invt", "2");
    params.put("fid", "f3");
    params.put("fs", "b:MK0021,b:MK0022,b:MK0023,b:MK0024");
    params.put("fields", "f2,f3,f4,f5,f6,f7,f8,f10,f12,f13,f14,f15,f16,f17,f18");
    params.put("_", "1623833739532");
    RequestContext context = RequestContext.newContext(url);
    StockHistoryApi.initHttpHeaders(context);
    context.setParams(params);
    HttpResponse httpResponse = HttpUtil.getInstance().get(context);
    logger.info("queryETFList: {}", httpResponse.getResponse());
    EastMoneyResult<DataWrapper<Map<String, String>>> clist = JSON.parseObject(httpResponse.getResponse(),
        new TypeReference<EastMoneyResult<DataWrapper<Map<String, String>>>>() {}.getType());
    if (clist != null && clist.getData() != null && clist.getData().getDiff() != null) {
      return clist.getData().getDiff().stream().map(dif -> {
        StockInfo stockInfo = new StockInfo();
        stockInfo.setLatestPrice(new BigDecimal(defaultString(dif.get("f2"))));
        stockInfo.setPriceChange(new BigDecimal(defaultString(dif.get("f3"))));
        stockInfo.setPriceChangeAmount(new BigDecimal(defaultString(dif.get("f4"))));
        stockInfo.setVolume(new BigDecimal(defaultString(dif.get("f5"))));
        stockInfo.setVolumeAmount(new BigDecimal(defaultString(dif.get("f6"))));
        stockInfo.setAmplitude(new BigDecimal(defaultString(dif.get("f7"))));
        stockInfo.setTurnoverRatio(new BigDecimal(defaultString(dif.get("f8"))));
        stockInfo.setSymbol(dif.get("f12"));
        stockInfo.setMarket(dif.get("f13"));
        stockInfo.setName(dif.get("f14"));
        stockInfo.setOpen(new BigDecimal(defaultString(dif.get("f17"))));
        stockInfo.setCloseYestoday(new BigDecimal(defaultString(dif.get("f18"))));
        stockInfo.setHigh(new BigDecimal(defaultString(dif.get("f15"))));
        stockInfo.setLow(new BigDecimal(defaultString(dif.get("f16"))));
        return stockInfo;

      }).collect(Collectors.toList());
    }
    return new ArrayList<>();
  }

  /**
   * eastmoney-ETF
   * 
   * <a>https://wap.eastmoney.com/quote/stock/0.159608.html</a>
   * 
   * @return symbol map
   */
  public static synchronized List<KlineInfo> queryETFHistory(String market, String symbol, String endDate, int limit,
      KlinePeriod klinePeriod, Adjust adjust) {
    if (endDate == null) {
      endDate = "20500000";
    }
    String url = "https://push2his.eastmoney.com/api/qt/stock/kline/get";
    RequestContext context = RequestContext.newContext(url);
    StockHistoryApi.initHttpHeaders(context);
    JSONObject params = new JSONObject();
    params.put("secid", String.format("%s.%s", market, symbol));
    params.put("ut", "f057cbcbce2a86e2866ab8877db1d059");
    params.put("fields1", "f1,f2,f3,f4,f5,f6,f7,f8");
    params.put("fields2", "f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61");
    params.put("klt", klinePeriod.getCode());
    params.put("fqt", adjust.getCode());
    params.put("iscca", "1");
    params.put("end", endDate);
    params.put("lmt", limit);
    params.put("forcect", "1");
    // params.put("_", currentTime());
    context.setParams(params);
    logger.info("queryETFHistory, requestBody: {}", JSON.toJSONString(params));
    try (CloseableHttpClient httpClient = HttpClientUtil.getInstance().createHttpClient(2)) {
      HttpResponse httpResponse = HttpUtil.getInstance().get(context, httpClient);
      EastMoneyResult<KlineResult> result =
          JSON.parseObject(httpResponse.getResponse(), new TypeReference<EastMoneyResult<KlineResult>>() {}.getType());
      logger.info("queryETFHistory requestBody: {}", httpResponse.getResponse());
      if (result.getData() == null || result.getData().getKlines() == null) {
        return null;
      }
      return result.getData().getKlines().stream().map(klineStr -> {
        String[] data = klineStr.split(",");
        KlineInfo klineInfo = new KlineInfo();
        klineInfo.setTradeDate(DateUtils.parseDate(data[0], "yyyy-MM-dd"));
        klineInfo.setOpen(new BigDecimal(data[1]));
        klineInfo.setClose(new BigDecimal(data[2]));
        klineInfo.setHigh(new BigDecimal(data[3]));
        klineInfo.setLow(new BigDecimal(data[4]));
        klineInfo.setVol(new BigDecimal(data[5]));
        klineInfo.setAmount(new BigDecimal(data[6]));
        klineInfo.setAmplitude(new BigDecimal(data[7]));
        klineInfo.setPctChg(new Double(data[8]));
        klineInfo.setChange(new Double(data[9]));
        klineInfo.setTurnoverRate(new BigDecimal(data[10]));
        return klineInfo;
      }).collect(Collectors.toList());
    } catch (Exception e) {
      logger.error("", e);
    }
    return null;
  }

  private static String defaultString(String value) {
    if ("-".equals(value)) {
      return "0";
    }
    return value;
  }

}
