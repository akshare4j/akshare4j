/**
 * 
 */
package com.github.bytesgo.akshare4j.eastmoney;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.fastjson2.util.DateUtils;
import com.github.bytesgo.akshare4j.eastmoney.model.CListInfo;
import com.github.bytesgo.akshare4j.eastmoney.model.EastMoneyResult;
import com.github.bytesgo.akshare4j.eastmoney.model.KlineInfo;
import com.github.bytesgo.akshare4j.eastmoney.model.KlineResult;
import com.github.bytesgo.akshare4j.eastmoney.model.enums.Adjust;
import com.github.bytesgo.akshare4j.eastmoney.model.enums.KlinePeriod;
import com.github.bytesgo.akshare4j.util.http.HttpResponse;
import com.github.bytesgo.akshare4j.util.http.HttpUtil;
import com.github.bytesgo.akshare4j.util.http.RequestContext;

/**
 * 
 */
public class StockKlineApi {
  private static final Map<String, String> symbolsMap = new ConcurrentHashMap<String, String>();

  public static synchronized Map<String, String> querySymbolMap() {
    if (symbolsMap.isEmpty() == false) {
      return symbolsMap;
    }
    String url = "https://80.push2.eastmoney.com/api/qt/clist/get";
    JSONObject params = new JSONObject();
    params.put("pn", "1");
    params.put("pz", "50000");
    params.put("po", "1");
    params.put("np", "1");
    params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
    params.put("fltt", "2");
    params.put("invt", "2");
    params.put("fid", "f3");
    params.put("fs", "m:1 t:2,m:1 t:23");
    params.put("fields", "f12");
    params.put("_", "1623833739532");
    RequestContext context = RequestContext.newContext(url);
    context.setParams(params);
    HttpResponse httpResponse = HttpUtil.getInstance().get(context);
    System.out.println(httpResponse.getResponse());
    Map<String, String> result = new HashMap<String, String>();
    EastMoneyResult<CListInfo> clist =
        JSON.parseObject(httpResponse.getResponse(), new TypeReference<EastMoneyResult<CListInfo>>() {}.getType());
    if (clist != null && clist.getData() != null) {
      clist.getData().getDiff().stream().map(d -> d.getF12()).forEach(s -> result.put(s, "1"));;
    }

    params = new JSONObject();
    params.put("pn", "1");
    params.put("pz", "50000");
    params.put("po", "1");
    params.put("np", "1");
    params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
    params.put("fltt", "2");
    params.put("invt", "2");
    params.put("fid", "f3");
    params.put("fs", "m:0 t:81 s:2048");
    params.put("fields", "f12");
    params.put("_", "1623833739532");
    context = RequestContext.newContext(url);
    context.setParams(params);
    httpResponse = HttpUtil.getInstance().get(context);
    clist = JSON.parseObject(httpResponse.getResponse(), new TypeReference<EastMoneyResult<CListInfo>>() {}.getType());
    if (clist != null && clist.getData() != null) {
      clist.getData().getDiff().stream().map(d -> d.getF12()).forEach(s -> result.put(s, "0"));;
    }
    System.out.println(httpResponse.getResponse());
    symbolsMap.putAll(result);
    return result;
  }

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
    params.put("secid", String.format("%s.%s", querySymbolMap().get(symbol), symbol));
    params.put("beg", startDate);
    params.put("end", endDate);
    params.put("_", "1623766962675");
    context.setParams(params);
    HttpResponse httpResponse = HttpUtil.getInstance().get(context);
    EastMoneyResult<KlineResult> result =
        JSON.parseObject(httpResponse.getResponse(), new TypeReference<EastMoneyResult<KlineResult>>() {}.getType());
    System.out.println(httpResponse.getResponse());
    if (result.getData() == null || result.getData().getKlines() == null) {
      return null;
    }
    return result.getData().getKlines().stream().map(klineStr -> {
      String[] data = klineStr.split(",");
      KlineInfo klineInfo = new KlineInfo();
      klineInfo.setTsCode(symbol);
      klineInfo.setTradeDate(DateUtils.parseDate(data[0], "yyyy-MM-dd"));
      klineInfo.setOpen(new BigDecimal(data[1]));
      klineInfo.setClose(new BigDecimal(data[2]));
      klineInfo.setHigh(new BigDecimal(data[3]));
      klineInfo.setLow(new BigDecimal(data[4]));
      klineInfo.setVol(new BigDecimal(data[5]));
      klineInfo.setAmount(new BigDecimal(data[6]));
      //
      klineInfo.setPctChg(new Double(data[8]));
      klineInfo.setChange(new Double(data[9]));

      return klineInfo;
    }).collect(Collectors.toList());
  }

  public static void main(String[] args) {
    String symbol = "601360";
    String start_date = "20240301";
    String end_date = "20240528";
    List<KlineInfo> klines = getKlines(symbol, start_date, end_date, KlinePeriod.daily, Adjust.QFQ);
    System.out.println(JSON.toJSONString(klines));
  }
}
