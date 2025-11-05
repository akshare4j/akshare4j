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
import com.github.leeyazhou.akshare4j.tencent.model.HistoryResponseDTO;
import com.github.leeyazhou.akshare4j.tencent.model.QueryStockInfoRequestDTO;
import com.github.leeyazhou.akshare4j.tencent.model.StockListReqest;
import com.github.leeyazhou.akshare4j.tencent.model.StockListResponse;
import com.github.leeyazhou.akshare4j.tencent.model.TencentStockInfo;
import com.github.leeyazhou.akshare4j.tencent.model.TencentStockQt;
import com.github.leeyazhou.akshare4j.tencent.model.enums.TencentMarketType;
import com.github.leeyazhou.akshare4j.util.http.HttpResponse;
import com.github.leeyazhou.akshare4j.util.http.HttpUtil;
import com.github.leeyazhou.akshare4j.util.http.RequestContext;

/**
 * 
 */
public class TencentStockApi {
  private static final Logger logger = LoggerFactory.getLogger(TencentStockApi.class);

  /**
   * 获取指数成分股列表
   * 
   * @return
   */
  public static List<TencentStockInfo> getBoardRankList() {
    // sh000985
    return getBoardRankList("sh000985");
  }

  public static List<TencentStockInfo> getBoardRankList(String boardCode) {
    StockListReqest request = new StockListReqest();
    request.setAppVer("11.11");
    request.setSource("wzqxcx");
    request.setSortType("priceRatio");
    request.setDirect("down");
    request.setApp("wzqxcx");
    request.setScenes("6");
    request.setXcxname("wzqxcx");
    request.setComeFrom("3");
    request.setBoardCode(boardCode);
    List<TencentStockInfo> allData = new ArrayList<>();
    int fetchCount = 30;
    int offset = 0;
    final int limit = 200;
    while (fetchCount-- > 0) {
      request.setOffset(offset);
      List<TencentStockInfo> stockList = doGetBoardRankList(request);
      if (stockList == null || stockList.size() == 0) {
        break;
      }
      allData.addAll(stockList);
      logger.info("getBoardRankList, boardCode: {}, offset: {}, stockListSize: {}", boardCode, offset,
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
    return allData.stream().map(stock -> {
      String mt = stock.getCode().substring(0, 2);
      String symbol = stock.getCode().substring(2, stock.getCode().length());
      stock.setMarketType(TencentMarketType.of(mt));
      stock.setSymbol(symbol);
      return stock;
    }).collect(Collectors.toList());
  }

  public static List<TencentStockInfo> doGetBoardRankList(StockListReqest request) {
    StringBuilder url = new StringBuilder("https://proxy.finance.qq.com/cgi/cgi-bin/rank/hs/getBoardRankList");
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
    logger.info("doGetStockList reuest, url: {}, requestBody: {}", url.toString(), JSON.toJSON(request));
    HttpResponse response = HttpUtil.getInstance().get(context);
    logger.info("doGetStockList response, code: {}, costTime: {}ms, responseBody: {}", response.getCode(),
        response.getCostTime(), response.getResponse());
    if (response.isOk() == false) {
      return null;
    }
    ApiResult<StockListResponse> apiResponse = JSON.parseObject(response.getResponse(),
        new TypeReference<ApiResult<StockListResponse>>() {}, Feature.SupportSmartMatch);
    if (apiResponse.isSuccess() == false || apiResponse.getData() == null) {
      return new ArrayList<>();
    }
    return apiResponse.getData().getRankList();
  }

  /**
   * sz301361
   * 
   * @param symbol 例如：sz301361
   * @return
   */
  public static TencentStockQt queryStockInfo(String symbol, String openid, String xSaSign) {
    QueryStockInfoRequestDTO request = new QueryStockInfoRequestDTO();
    request.setApp("wzqxcx");
    request.setCode(symbol);
    request.setFsType("d1");
    request.setNeedDataType("fs,qt,attribute");
    request.setOpenid(openid);
    request.setT(String.valueOf(System.currentTimeMillis()));
    request.setxAppid("wzqxcx");
    request.setxSaV("2");
    request.setxSaSign(xSaSign);
    request.setxTimestamp(String.valueOf(System.currentTimeMillis()/1000));
    StringBuilder url = new StringBuilder("https://proxy.finance.qq.com/cgi/cgi-bin/stockinfoquery/fs/app/get");
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
    logger.info("queryStockInfo reuest, url: {}, requestBody: {}", url.toString(), JSON.toJSON(request));
    HttpResponse response = HttpUtil.getInstance().get(context);
    logger.info("queryStockInfo response, code: {}, costTime: {}ms, responseBody: {}", response.getCode(),
        response.getCostTime(), response.getResponse());
    if (response.isOk() == false) {
      return null;
    }
    ApiResult<HistoryResponseDTO> apiResponse = JSON.parseObject(response.getResponse(),
        new TypeReference<ApiResult<HistoryResponseDTO>>() {}, Feature.SupportSmartMatch);
    if (apiResponse.isSuccess() == false || apiResponse.getData() == null) {
      return null;
    }
    return apiResponse.getData().getQt();

  }

}
