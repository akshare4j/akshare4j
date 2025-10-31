/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.github.leeyazhou.akshare4j.tencent.model.ApiResult;
import com.github.leeyazhou.akshare4j.tencent.model.KLineData;
import com.github.leeyazhou.akshare4j.tencent.model.KLineResponse;
import com.github.leeyazhou.akshare4j.tencent.model.enums.FqType;
import com.github.leeyazhou.akshare4j.tencent.model.enums.KLineType;
import com.github.leeyazhou.akshare4j.util.http.HttpResponse;
import com.github.leeyazhou.akshare4j.util.http.HttpUtil;
import com.github.leeyazhou.akshare4j.util.http.RequestContext;

/**
 * 
 */
public class TencentStockHistoryApi {
  private static final Logger logger = LoggerFactory.getLogger(TencentStockHistoryApi.class);

  public static List<KLineData> getKlineDatas(String symbol, KLineType ktype, FqType fqtype) {
    List<KLineData> allData = new ArrayList<>();
    String toDate = null;
    int fetchCount = 3;
    int limit = 370;
    while (fetchCount-- > 0) {
      List<KLineData> batchData = doFetchKlineDatas(symbol, ktype, fqtype, toDate, null, limit);
      allData.addAll(batchData);

      if (batchData.size() < limit) {
        break;
      }
    }
    return allData;
  }

  public static List<KLineData> fetchLatestKlineData(String code, KLineType ktype, FqType fqtype, Long latestTimestamp)
      throws Exception {
    if (latestTimestamp == null) {
      return fetchKlineBatch(code, ktype, fqtype, null, null, 370);
    }
    return fetchKlineBatch(code, ktype, fqtype, null, null, 100);
  }

  public static List<KLineData> fetchKlineBatch(String code, KLineType ktype, FqType fqtype, String toDate,
      String endTime, int limit) throws Exception {
    return doFetchKlineDatas(code, ktype, fqtype, toDate, endTime, limit);
  }

  private static List<KLineData> doFetchKlineDatas(String code, KLineType ktype, FqType fqtype, String toDate,
      String endTime, int limit) {
    long timestamp = System.currentTimeMillis();

    StringBuilder url = new StringBuilder();
    url.append("https://proxy.finance.qq.com/cgi/cgi-bin/stockinfoquery/kline/app/get");
    url.append("?code=").append(code)//
        .append("&ktype=").append(ktype.getCode())//
        .append("&fqtype=").append(fqtype.getCode())//
        .append("&limit=").append(limit)//
        .append("&openid=").append("stockfe")//
        .append("&app=").append("wzqxcx")//
        .append("&scenes=").append("6")//
        .append("&xcxname=").append("wzqxcx")//
        .append("&come_from=").append("3")//
        .append("&t=").append(timestamp);

    if (toDate != null) {
      url.append("&toDate=").append(encode(toDate));
    }
    if (endTime != null) {
      url.append("&endTime=").append(encode(endTime));
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
    ApiResult<KLineResponse> apiResponse =
        JSON.parseObject(response.getResponse(), new TypeReference<ApiResult<KLineResponse>>() {});
    if (apiResponse.isSuccess() == false || apiResponse.getData() == null) {
      return new ArrayList<>();
    }
    return apiResponse.getData().getNodes();
  }



  private static String encode(String s) {
    try {
      return URLEncoder.encode(Objects.toString(s, ""), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return s;
  }



}
