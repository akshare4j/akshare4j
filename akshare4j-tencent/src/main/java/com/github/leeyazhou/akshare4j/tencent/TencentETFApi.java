/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader.Feature;
import com.alibaba.fastjson2.TypeReference;
import com.github.leeyazhou.akshare4j.tencent.model.ApiResult;
import com.github.leeyazhou.akshare4j.tencent.model.ETFListRequestDTO;
import com.github.leeyazhou.akshare4j.tencent.model.ETFListResponseDTO;
import com.github.leeyazhou.akshare4j.tencent.model.TencentETFInfo;
import com.github.leeyazhou.akshare4j.tencent.model.enums.TencentMarketType;
import com.github.leeyazhou.akshare4j.util.http.HttpResponse;
import com.github.leeyazhou.akshare4j.util.http.HttpUtil;
import com.github.leeyazhou.akshare4j.util.http.RequestContext;

/**
 * 
 */
public class TencentETFApi {
  private static final Logger logger = LoggerFactory.getLogger(TencentETFApi.class);

  public static List<TencentETFInfo> getETFAll() {
    ETFListRequestDTO request = new ETFListRequestDTO();
    request.setApp("mini_h5");
    request.setBoardType("etf_all");
    request.setSortType("priceRatio");
    request.setDirect("down");
    request.setCount(50);
    request.setOffset(276);
    request.setComment("1");
    request.setLabel("11,22,21,20,12,23");
    List<TencentETFInfo> allData = new ArrayList<>();
    int fetchCount = 30;
    int offset = 0;
    final int limit = 200;
    while (fetchCount-- > 0) {
      request.setOffset(offset);
      List<TencentETFInfo> stockList = doGetBoardRankList(request);
      if (stockList == null || stockList.size() == 0) {
        break;
      }
      allData.addAll(stockList);
      logger.info("getETFAll, offset: {}, stockListSize: {}", offset,
          stockList.size());
      if (stockList.size() < limit) {
        break;
      }
      offset += limit;
      try {
        int sleep = 3000;
        ThreadUtils.sleep(Duration.ofMillis(sleep));
        logger.info("thread sleep {} ms for next fetch...", sleep);
      } catch (InterruptedException e) {
        logger.error("", e);
      }
    }
    return allData.stream().map(etf -> {
      String mt = etf.getCode().substring(0, 2);
      String symbol = etf.getCode().substring(2, etf.getCode().length());
      etf.setMarketType(TencentMarketType.of(mt));
      etf.setSymbol(symbol);
      return etf;
    }).collect(Collectors.toList());
  }

  public static List<TencentETFInfo> doGetBoardRankList(ETFListRequestDTO request) {
    StringBuilder url = new StringBuilder("https://proxy.finance.qq.com/ifzqgtimg/appstock/fund/etf/rank");
    RequestContext context = RequestContext.newContext(url.toString());
    JSONObject requestJSON = JSON.parseObject(JSON.toJSONString(request));
    for (String key : requestJSON.keySet()) {
      context.addParam(key, requestJSON.getString(key));
    }

    context.addHeader("Accept", "application/json, text/plain, */*")//
        .addHeader("Accept-Language", "en-US,en;q=0.9")//
        .addHeader("Connection", "keep-alive")//
        .addHeader("Origin", "https://wzq.tenpay.com")//
        .addHeader("Referer", "https://wzq.tenpay.com/")//
        .addHeader("Sec-Fetch-Dest", "empty")//
        .addHeader("Sec-Fetch-Mode", "cors")//
        .addHeader("xweb_xhr", "1")//
        .addHeader("Sec-Fetch-Site", "cross-site")//
        .addHeader("User-Agent",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36 MicroMessenger/7.0.20.1781(0x6700143B) NetType/WIFI MiniProgramEnv/Mac MacWechat/WMPF MacWechat/3.8.7(0x13080712) UnifiedPCMacWechat(0xf2641210) XWEB/16816");
    logger.info("doGetBoardRankList reuest, url: {}, requestBody: {}", url.toString(), JSON.toJSON(request));
    HttpResponse response = HttpUtil.getInstance().get(context);
    logger.info("doGetBoardRankList response, code: {}, costTime: {}ms, responseBody: {}", response.getCode(),
        response.getCostTime(), response.getResponse());
    if (response.isOk() == false) {
      return null;
    }
    ApiResult<ETFListResponseDTO> apiResponse = JSON.parseObject(response.getResponse(),
        new TypeReference<ApiResult<ETFListResponseDTO>>() {}, Feature.SupportSmartMatch);
    if (apiResponse.isSuccess() == false || apiResponse.getData() == null) {
      return new ArrayList<>();
    }
    return apiResponse.getData().getData();
  }


}
