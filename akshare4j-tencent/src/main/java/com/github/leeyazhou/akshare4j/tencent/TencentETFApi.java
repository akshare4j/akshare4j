package com.github.leeyazhou.akshare4j.tencent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.commons.lang3.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.TypeReference;
import com.github.leeyazhou.akshare4j.tencent.model.ApiResult;
import com.github.leeyazhou.akshare4j.tencent.model.ETFListRequestDTO;
import com.github.leeyazhou.akshare4j.tencent.model.ETFListResponseDTO;
import com.github.leeyazhou.akshare4j.tencent.model.HistoryResponseDTO;
import com.github.leeyazhou.akshare4j.tencent.model.TencentETFInfo;
import com.github.leeyazhou.akshare4j.tencent.model.TencentKLineInfo;
import com.github.leeyazhou.akshare4j.tencent.model.TencentKLineRequestDTO;
import com.github.leeyazhou.akshare4j.tencent.model.enums.TencentMarketType;
import com.github.leeyazhou.akshare4j.util.http.HttpResponse;
import com.github.leeyazhou.akshare4j.util.http.HttpService;
import com.github.leeyazhou.akshare4j.util.http.HttpUtil;
import com.github.leeyazhou.akshare4j.util.http.ProxyInfo;
import com.github.leeyazhou.akshare4j.util.http.RequestContext;

/**
 * 腾讯 ETF 接口工具类
 * <p>
 * 提供获取腾讯财经 ETF 排行列表的功能。
 * 
 * @author leeyazhou
 */
public class TencentETFApi {
  private static final Logger logger = LoggerFactory.getLogger(TencentETFApi.class);
  private static final String RANK_URL = "https://proxy.finance.qq.com/cgi/cgi-bin/rank/fund/getList";
  private static final String KLINE_URL = "https://proxy.finance.qq.com/cgi/cgi-bin/stockinfoquery/kline/app/get";
  
  public static final int LIMIT = 50;
  public static final int MAX_PAGES = 30;
  public static final long SLEEP_TIME = 3000L;

  private static HttpService httpService = HttpUtil.getInstance();
  private static String openid;
  private static ProxyInfo proxyInfo;

  /**
   * 设置用户标识符 openid
   * 
   * @param id 用户标识符
   */
  public static void setOpenid(String id) {
    openid = id;
  }

  /**
   * 设置代理信息
   * 
   * @param proxyInfo 代理配置
   */
  public static void setProxyInfo(ProxyInfo proxyInfo) {
    TencentETFApi.proxyInfo = proxyInfo;
  }

  /**
   * 仅用于单元测试注入 Mock 对象
   */
  static void setHttpService(HttpService service) {
    httpService = service;
  }

  private TencentETFApi() {
    // 工具类防止实例化
  }

  /**
   * 查询 ETF 的 K 线详情
   * 
   * @param code ETF 代码，如 "sh563380"
   * @param limit 获取条数
   * @return K 线数据列表
   * @throws IllegalArgumentException 当 openid 未设置时抛出
   */
  public static List<TencentKLineInfo> getKLine(String code, int limit) {
    checkOpenid();
    TencentKLineRequestDTO request = new TencentKLineRequestDTO();
    request.setCode(code);
    request.setKtype("day");
    request.setFqtype("qfq");
    request.setLimit(limit);
    request.setOpenid(openid);
    request.setApp("zxg_xcx");
    request.setT(System.currentTimeMillis());
    request.setScenes(6);
    request.setXcxname("zxg_xcx");
    request.setComeFrom(3);

    RequestContext context = RequestContext.newContext(KLINE_URL);
    context.setProxyInfo(proxyInfo);
    context.setProxy(proxyInfo != null);

    JSONObject params = JSON.parseObject(JSON.toJSONString(request));
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      context.addParam(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
    }

    addKLineHeaders(context);

    logger.info("getKLine 请求参数: {}", context.getParams());
    HttpResponse response = httpService.get(context);
    if (response == null || !response.isOk()) {
      logger.error("查询 K 线接口失败，url: {}, response: {}", KLINE_URL, response);
      return new ArrayList<TencentKLineInfo>();
    }

    ApiResult<HistoryResponseDTO> result = JSON.parseObject(response.getResponse(),
        new TypeReference<ApiResult<HistoryResponseDTO>>() {}, JSONReader.Feature.SupportSmartMatch);

    return (result != null && result.isSuccess() && result.getData() != null) ? result.getData().getNodes()
        : new ArrayList<TencentKLineInfo>();
  }

  /**
   * 获取所有 ETF 列表信息（全量返回）
   * 
   * @return ETF 信息列表
   * @throws IllegalArgumentException 当 openid 未设置时抛出
   */
  public static List<TencentETFInfo> getETFAll() {
    final List<TencentETFInfo> allData = new ArrayList<TencentETFInfo>();
    getETFAll(new Consumer<List<TencentETFInfo>>() {
      @Override
      public void accept(List<TencentETFInfo> tencentETFInfos) {
        allData.addAll(tencentETFInfos);
      }
    });
    return allData;
  }

  /**
   * 获取所有 ETF 列表信息，并通过 Consumer 及时消费每一页数据，避免占用过多内存。
   * 
   * @param consumer 数据消费者，接收每一页抓取到的 ETF 列表
   * @throws IllegalArgumentException 当 openid 未设置时抛出
   */
  public static void getETFAll(Consumer<List<TencentETFInfo>> consumer) {
    checkOpenid();
    ETFListRequestDTO request = createBaseRequest();

    for (int i = 0; i < MAX_PAGES; i++) {
      request.setOffset(i * LIMIT);
      List<TencentETFInfo> pageData = fetchEtfListPageData(request);

      if (pageData == null || pageData.isEmpty()) {
        break;
      }

      for (TencentETFInfo etf : pageData) {
        enrichInfo(etf);
      }

      if (consumer != null) {
        consumer.accept(pageData);
      }

      logger.info("已抓取并消费 ETF 数据，偏移量: {}, 本次抓取: {}", request.getOffset(), pageData.size());

      if (pageData.size() < LIMIT) {
        break;
      }
      waitNextFetch();
    }
  }

  private static void checkOpenid() {
    if (openid == null || openid.trim().isEmpty()) {
      throw new IllegalArgumentException("使用腾讯接口前必须先通过 TencentETFApi.setOpenid(String) 设置用户标识符 openid");
    }
  }

  /**
   * 获取单页数据
   */
  private static List<TencentETFInfo> fetchEtfListPageData(ETFListRequestDTO request) {
    RequestContext context = RequestContext.newContext(RANK_URL);
    context.setProxyInfo(proxyInfo);
    context.setProxy(proxyInfo != null);

    JSONObject params = JSON.parseObject(JSON.toJSONString(request));
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      context.addParam(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
    }

    addHeaders(context);

    logger.info("fetchEtfListPageData 请求参数: {}", context.getParams());
    HttpResponse response = httpService.get(context);
    if (response == null || !response.isOk()) {
      logger.error("腾讯 ETF 列表接口请求失败，url: {}, response: {}", RANK_URL, response);
      return null;
    }
    logger.info("fetchEtfListPageData 响应: {}", response.getResponse());
    ApiResult<ETFListResponseDTO> result = JSON.parseObject(response.getResponse(),
        new TypeReference<ApiResult<ETFListResponseDTO>>() {}, JSONReader.Feature.SupportSmartMatch);

    return (result != null && result.isSuccess() && result.getData() != null) ? result.getData().getData() : null;
  }

  private static void addHeaders(RequestContext context) {
    context.addHeader("Host", "proxy.finance.qq.com");
    context.addHeader("Connection", "keep-alive");
    context.addHeader("Accept", "application/json, text/plain, */*");
    context.addHeader("Origin", "https://wzq.tenpay.com");
    context.addHeader("Sec-Fetch-Site", "cross-site");
    context.addHeader("Sec-Fetch-Mode", "cors");
    context.addHeader("Sec-Fetch-Dest", "empty");
    context.addHeader("Referer", "https://wzq.tenpay.com/");
    context.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
    context.addHeader("User-Agent",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36 MicroMessenger/7.0.20.1781(0x6700143B) NetType/WIFI MiniProgramEnv/Mac MacWechat/WMPF MacWechat/3.8.7(0x13080712) UnifiedPCMacWechat(0xf2641934) XWEB/19824 miniProgram/wx4ffb369b6881ee5e");
  }

  private static void addKLineHeaders(RequestContext context) {
    context.addHeader("Host", "proxy.finance.qq.com");
    context.addHeader("Connection", "keep-alive");
    context.addHeader("xweb_xhr", "1");
    context.addHeader("Accept", "*/*");
    context.addHeader("Sec-Fetch-Site", "cross-site");
    context.addHeader("Sec-Fetch-Mode", "cors");
    context.addHeader("Sec-Fetch-Dest", "empty");
    context.addHeader("Referer", "https://servicewechat.com/wx4ffb369b6881ee5e/918/page-frame.html");
    context.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
    context.addHeader("Content-Type", "application/json");
    context.addHeader("User-Agent",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36 MicroMessenger/7.0.20.1781(0x6700143B) NetType/WIFI MiniProgramEnv/Mac MacWechat/WMPF MacWechat/3.8.7(0x13080712) UnifiedPCMacWechat(0xf2641934) XWEB/19824");
  }

  private static ETFListRequestDTO createBaseRequest() {
    ETFListRequestDTO req = new ETFListRequestDTO();
    req.setApp("wzq");
    req.setBoardType("etf_all");
    req.setSortType("priceRatio");
    req.setDirect("down");
    req.setCount(LIMIT);
    req.setLabel("11,22,21,20,12,23");
    req.setFundType("inner");
    req.setOpenid(openid);
    return req;
  }

  private static void enrichInfo(TencentETFInfo etf) {
    String code = etf.getCode();
    if (code != null && code.length() > 2) {
      etf.setMarketType(TencentMarketType.of(code.substring(0, 2)));
      etf.setSymbol(code.substring(2));
    }
  }

  private static void waitNextFetch() {
    try {
      ThreadUtils.sleep(Duration.ofMillis(SLEEP_TIME));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.warn("抓取过程被中断");
    }
  }
}
