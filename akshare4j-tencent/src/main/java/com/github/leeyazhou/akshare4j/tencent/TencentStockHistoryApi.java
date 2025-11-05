/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.github.leeyazhou.akshare4j.tencent.model.ApiResult;
import com.github.leeyazhou.akshare4j.tencent.model.HistoryResponseDTO;
import com.github.leeyazhou.akshare4j.tencent.model.TencentKLineInfo;
import com.github.leeyazhou.akshare4j.tencent.model.TencentStockQt;
import com.github.leeyazhou.akshare4j.tencent.model.enums.TencentAdjust;
import com.github.leeyazhou.akshare4j.tencent.model.enums.TencentKlinePeriod;
import com.github.leeyazhou.akshare4j.tencent.model.enums.TencentMarketType;
import com.github.leeyazhou.akshare4j.util.http.HttpResponse;
import com.github.leeyazhou.akshare4j.util.http.HttpUtil;
import com.github.leeyazhou.akshare4j.util.http.RequestContext;

/**
 * 
 */
public class TencentStockHistoryApi {
  private static final Logger logger = LoggerFactory.getLogger(TencentStockHistoryApi.class);
  public static final String yyyyMMdd = "yyyy-MM-dd";
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      new DateTimeFormatterBuilder().appendPattern(yyyyMMdd).toFormatter();

  public static HistoryResponseDTO getKlines(String symbol, TencentMarketType marketType, TencentKlinePeriod ktype,
      TencentAdjust fqtype, String toDate, String endTime, int limit) {
    List<TencentKLineInfo> allData = new ArrayList<>();
    TencentStockQt stockQt = null;
    int fetchCount = 3;
    limit = limit <= 0 ? 370 : limit;
    while (fetchCount-- > 0) {
      HistoryResponseDTO historyResponseDTO = doFetchKlineDatas(symbol, marketType, ktype, fqtype, toDate, endTime, limit);
      if (historyResponseDTO == null || historyResponseDTO.getNodes() == null) {
        break;
      }
      if (stockQt == null) {
        stockQt = historyResponseDTO.getQt();
      }
      List<TencentKLineInfo> klines = historyResponseDTO.getNodes();
      if (klines == null || klines.size() == 0) {
        break;
      }
      allData.addAll(klines);

      if (klines.size() < limit) {
        break;
      }
      toDate = DateTime.parse(klines.get(0).getDate(), DATE_TIME_FORMATTER).plusDays(-1).toString(yyyyMMdd);
    }
    allData = allData.stream().sorted(Comparator.comparing(TencentKLineInfo::getDate)).distinct()
        .collect(Collectors.toList());
    HistoryResponseDTO resp = new HistoryResponseDTO();
    resp.setNodes(allData);
    resp.setQt(stockQt);
    return resp;
  }

  public static HistoryResponseDTO fetchKlineBatch(String code, TencentMarketType marketType, TencentKlinePeriod ktype,
      TencentAdjust fqtype, String toDate, String endTime, int limit) throws Exception {
    return doFetchKlineDatas(code, marketType, ktype, fqtype, toDate, endTime, limit);
  }

  private static HistoryResponseDTO doFetchKlineDatas(String code, TencentMarketType marketType, TencentKlinePeriod ktype,
      TencentAdjust fqtype, String toDate, String endTime, int limit) {

    StringBuilder url = new StringBuilder();
    url.append("https://proxy.finance.qq.com/cgi/cgi-bin/stockinfoquery/kline/app/get");
    url.append("?code=").append(marketType.getCode() + code)//
        .append("&ktype=").append(ktype.getCode())//
        .append("&fqtype=").append(fqtype.getCode())//
        .append("&limit=").append(limit)//
        .append("&openid=").append("stockfe")//
        .append("&app=").append("wzqxcx")//
        .append("&scenes=").append("6")//
        .append("&xcxname=").append("wzqxcx")//
        .append("&come_from=").append("3")//
        .append("&t=").append(System.currentTimeMillis());

    if (toDate != null) {
      url.append("&toDate=").append(toDate);
    }
    if (endTime != null) {
      url.append("&endTime=").append(endTime);
    }

    RequestContext context = RequestContext.newContext(url.toString());
    context.addHeader("Accept", "application/json, text/plain, */*")//
        .addHeader("Accept-Language", "en-US,en;q=0.9")//
        .addHeader("Connection", "keep-alive")//
        .addHeader("Origin", "https://wzq.tenpay.com")//
        .addHeader("Referer", "https://wzq.tenpay.com/")//
        .addHeader("Sec-Fetch-Dest", "empty")//
        .addHeader("Sec-Fetch-Mode", "cors")//
        .addHeader("Sec-Fetch-Site", "cross-site")//
        .addHeader("User-Agent",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 NetType/WIFI MicroMessenger/6.8.0(0x16080000) MacWechat/3.8.10(0x13080a10) XWEB/1227 Flue");
    logger.info("fetchKlineBatchWithNodes reuest, url: {}", url.toString());
    HttpResponse response = HttpUtil.getInstance().get(context);
    logger.info("fetchKlineBatchWithNodes response, code: {}, costTime: {}ms, responseBody: {}", response.getCode(),
        response.getCostTime(), response.getResponse());
    if (response.isOk() == false) {
      return null;
    }
    ApiResult<HistoryResponseDTO> apiResponse =
        JSON.parseObject(response.getResponse(), new TypeReference<ApiResult<HistoryResponseDTO>>() {});
    if (apiResponse.isSuccess() == false || apiResponse.getData() == null) {
      return null;
    }
    return apiResponse.getData();
  }


}
