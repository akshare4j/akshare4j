/**
 * 
 */
package com.github.leeyazhou.akshare4j.eastmoney;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.fastjson2.util.DateUtils;
import com.github.leeyazhou.akshare4j.eastmoney.model.EastMoneyResult;
import com.github.leeyazhou.akshare4j.eastmoney.model.KlineInfo;
import com.github.leeyazhou.akshare4j.eastmoney.model.KlineResult;
import com.github.leeyazhou.akshare4j.eastmoney.model.enums.Adjust;
import com.github.leeyazhou.akshare4j.eastmoney.model.enums.KlinePeriod;
import com.github.leeyazhou.akshare4j.util.http.HttpClientUtil;
import com.github.leeyazhou.akshare4j.util.http.HttpResponse;
import com.github.leeyazhou.akshare4j.util.http.HttpUtil;
import com.github.leeyazhou.akshare4j.util.http.RequestContext;

/**
 * 
 */
public class StockHistoryApi {
  private static final Logger logger = LoggerFactory.getLogger(StockHistoryApi.class);

  /**
   * 东方财富网-行情首页-沪深京 A 股-每日行情
   * 
   * https://quote.eastmoney.com/concept/sh603777.html?from=classic
   * 
   * @param symbol symbol
   * @param startDate format: yyyy-MM-dd
   * @param endDate format: yyyy-MM-dd
   * @param klinePeriod {@link KlinePeriod}
   * @param adjust {@link Adjust}
   * @return KlineInfo
   */
  public static List<KlineInfo> getKlines(String symbol, String startDate, String endDate, KlinePeriod klinePeriod,
      Adjust adjust) {
    String url = "https://push2his.eastmoney.com/api/qt/stock/kline/get";
    RequestContext context = RequestContext.newContext(url);
    JSONObject params = new JSONObject();
    params.put("fields1", "f1,f2,f3,f4,f5,f6");
    params.put("fields2", "f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61,f116");
    params.put("ut", "7eea3edcaed734bea9cbfc24409ed989");
    params.put("klt", klinePeriod.getCode());
    params.put("fqt", adjust.getCode());
    params.put("secid", String.format("%s.%s", StockApi.querySymbolMap().get(symbol), symbol));
    params.put("beg", startDate);
    params.put("end", endDate);
    params.put("_", currentTime());
    context.setParams(params);
    try (CloseableHttpClient httpClient = HttpClientUtil.getInstance().createHttpClient(2)) {
      HttpResponse httpResponse = HttpUtil.getInstance().get(context, httpClient);
      EastMoneyResult<KlineResult> result =
          JSON.parseObject(httpResponse.getResponse(), new TypeReference<EastMoneyResult<KlineResult>>() {}.getType());
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

  /**
   * 东方财富网-行情-美股-每日行情
   * 
   * https://quote.eastmoney.com/us/ENTX.html#fullScreenChart
   * 
   * @param symbol 股票代码; 此股票代码需要通过调用 ak.stock_us_spot_em() 的 `代码` 字段获取
   * @param endDate endDate
   * @param limit limit
   * @param klinePeriod {@link KlinePeriod}
   * @param adjust {@link Adjust}
   * @return KlineInfo
   */
  public static List<KlineInfo> stock_us_hist(String symbol, String endDate, int limit, KlinePeriod klinePeriod,
      Adjust adjust) {
    String url = "https://63.push2his.eastmoney.com/api/qt/stock/kline/get";
    RequestContext context = RequestContext.newContext(url);
    JSONObject params = new JSONObject();
    params.put("secid", symbol);
    params.put("ut", "fa5fd1943c7b386f172d6893dbfba10b");
    params.put("fields1", "f1,f2,f3,f4,f5,f6");
    params.put("fields2", "f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61");
    params.put("klt", klinePeriod.getCode());
    params.put("fqt", adjust.getCode());
    params.put("end", endDate);
    params.put("lmt", limit);
    params.put("_", currentTime());
    context.setParams(params);
    try (CloseableHttpClient httpClient = HttpClientUtil.getInstance().createHttpClient(2)) {
      HttpResponse httpResponse = HttpUtil.getInstance().get(context, httpClient);
      EastMoneyResult<KlineResult> result =
          JSON.parseObject(httpResponse.getResponse(), new TypeReference<EastMoneyResult<KlineResult>>() {}.getType());
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

  private static String currentTime() {
    return String.valueOf(System.currentTimeMillis());
  }
}