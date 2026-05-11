package com.github.leeyazhou.akshare4j.tencent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.github.leeyazhou.akshare4j.tencent.model.TencentETFInfo;
import com.github.leeyazhou.akshare4j.tencent.model.TencentKLineInfo;
import com.github.leeyazhou.akshare4j.tencent.model.enums.TencentAdjust;
import com.github.leeyazhou.akshare4j.tencent.model.enums.TencentKlinePeriod;
import com.github.leeyazhou.akshare4j.util.http.HttpResponse;
import com.github.leeyazhou.akshare4j.util.http.HttpService;
import com.github.leeyazhou.akshare4j.util.http.HttpUtil;
import com.github.leeyazhou.akshare4j.util.http.ProxyInfo;

/**
 * TencentETFApi 单元测试 目标：覆盖率 > 90%
 */
class TencentETFApiTest {

  private HttpService httpService;

  @BeforeEach
  void setUp() {
    httpService = mock(HttpService.class);
    // 注入 Mock 对象
    TencentETFApi.setHttpService(httpService);
    // 默认设置 openid，避免基础测试失败
    TencentETFApi.setOpenid("test-openid");
  }

  @AfterEach
  void tearDown() {
    // 恢复默认对象，避免影响其他测试
    TencentETFApi.setHttpService(HttpUtil.getInstance());
    TencentETFApi.setOpenid(null);
  }

  @Test
  @DisplayName("未设置 openid 时应抛出异常")
  void testOpenid_Required() {
    TencentETFApi.setOpenid(null);
    String endDate = DateFormatUtils.format(new DateTime().toDate(), "yyyyMMdd");
    assertThrows(IllegalArgumentException.class, () -> TencentETFApi.getETFAll());
    assertThrows(IllegalArgumentException.class,
        () -> TencentETFApi.queryETFHistory("sh", "159982", endDate, 1, TencentKlinePeriod.Day, TencentAdjust.QFQ));
  }

  @Test
  @DisplayName("静态方法成功获取多页 ETF 列表")
  void testGetETFAll_Success() {
    // 模拟第一页响应（50条）
    HttpResponse res1 = createMockResponse(0, 50, true);
    // 模拟第二页响应（10条，触发结束）
    HttpResponse res2 = createMockResponse(50, 10, true);

    when(httpService.get(any())).thenReturn(res1).thenReturn(res2);

    List<TencentETFInfo> result = TencentETFApi.getETFAll();

    assertNotNull(result);
    assertEquals(60, result.size());
    assertEquals("sz", result.get(0).getMarketType().getCode());
    assertEquals("000000", result.get(0).getSymbol());

    verify(httpService, times(2)).get(any());
  }

  @Test
  @DisplayName("通过 Consumer 按页消费 ETF 列表")
  void testGetETFAll_Consumer() {
    HttpResponse res1 = createMockResponse(0, 50, true);
    HttpResponse res2 = createMockResponse(50, 10, true);
    when(httpService.get(any())).thenReturn(res1).thenReturn(res2);

    final List<TencentETFInfo> collected = new java.util.ArrayList<>();
    TencentETFApi.getETFAll(new java.util.function.Consumer<List<TencentETFInfo>>() {
      @Override
      public void accept(List<TencentETFInfo> page) {
        collected.addAll(page);
      }
    });

    assertEquals(60, collected.size());
    verify(httpService, times(2)).get(any());
  }

  @Test
  @DisplayName("首页数据为空时提前结束")
  void testGetETFAll_EmptyFirstPage() {
    HttpResponse res = createMockResponse(0, 0, true);
    when(httpService.get(any())).thenReturn(res);

    List<TencentETFInfo> result = TencentETFApi.getETFAll();

    assertTrue(result.isEmpty());
    verify(httpService, times(1)).get(any());
  }

  @Test
  @DisplayName("HTTP 请求失败处理")
  void testGetETFAll_HttpError() {
    HttpResponse res = new HttpResponse();
    res.setCode(500);
    when(httpService.get(any())).thenReturn(res);

    List<TencentETFInfo> result = TencentETFApi.getETFAll();

    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("HTTP 返回 null 场景处理")
  void testGetETFAll_HttpNull() {
    when(httpService.get(any())).thenReturn(null);

    List<TencentETFInfo> result = TencentETFApi.getETFAll();

    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("业务 API 返回失败码处理")
  void testGetETFAll_ApiBusinessError() {
    String errorJson = "{\"code\": -1, \"msg\": \"system error\", \"data\": null}";
    HttpResponse res = new HttpResponse();
    res.setCode(200);
    res.setResponse(errorJson);
    when(httpService.get(any())).thenReturn(res);

    List<TencentETFInfo> result = TencentETFApi.getETFAll();

    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("成功获取 ETF K线数据")
  void testGetKLine_Success() {
    String json =
        "{\"code\": 0, \"msg\": \"ok\", \"data\": {\"nodes\": [{\"date\": \"2024-05-10\", \"open\": 1.0, \"close\": 1.1}]}}";
    HttpResponse res = new HttpResponse();
    res.setCode(200);
    res.setResponse(json);
    when(httpService.get(any())).thenReturn(res);
    String endDate = DateFormatUtils.format(new DateTime().toDate(), "yyyyMMdd");
    List<TencentKLineInfo> result =
        TencentETFApi.queryETFHistory("sh", "159982", endDate, 1, TencentKlinePeriod.Day, TencentAdjust.QFQ);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("2024-05-10", result.get(0).getDate());
  }

  @Test
  @DisplayName("K线查询失败场景处理")
  void testGetKLine_Fail() {
    when(httpService.get(any())).thenReturn(null);
    String endDate = DateFormatUtils.format(new DateTime().toDate(), "yyyyMMdd");
    List<TencentKLineInfo> result =
        TencentETFApi.queryETFHistory("sh", "159982", endDate, 1, TencentKlinePeriod.Day, TencentAdjust.QFQ);

    assertTrue(result.isEmpty());
  }

  /**
   * 构造模拟响应
   */
  private HttpResponse createMockResponse(int offset, int count, boolean success) {
    StringBuilder dataJson = new StringBuilder();
    for (int i = 0; i < count; i++) {
      dataJson.append(String.format("{\"code\": \"sz%06d\", \"name\": \"ETF_%d\"}", offset + i, offset + i));
      if (i < count - 1)
        dataJson.append(",");
    }

    String json = String.format("{\"code\": %d, \"msg\": \"ok\", \"data\": {\"data\": [%s]}}", success ? 0 : -1,
        dataJson.toString());

    HttpResponse res = new HttpResponse();
    res.setCode(200);
    res.setResponse(json);
    return res;
  }

  @Test
  public void testGetEtfAll() {
    TencentETFApi.setHttpService(HttpUtil.getInstance());
    TencentETFApi.setOpenid("os-ppuOrnTEM8Zki353-okWCEP7I");
    ProxyInfo proxyInfo = new ProxyInfo("192.168.31.244", 9091, "", "");
    TencentETFApi.setProxyInfo(proxyInfo);
    TencentETFApi.getETFAll(etfList -> {
      System.out.println("etfList size: " + etfList.size());
      for (TencentETFInfo etf : etfList) {
        assertNotNull(etf.getCode());
        assertNotNull(etf.getName());
      }
    });
  }

  @Test
  public void testGetKline() {
    TencentETFApi.setHttpService(HttpUtil.getInstance());
    TencentETFApi.setOpenid("os-ppuOrnTEM8Zki353-okWCEP7I");
//    ProxyInfo proxyInfo = new ProxyInfo("192.168.31.244", 9091, "", "");
//    TencentETFApi.setProxyInfo(proxyInfo);
    String endDate = DateFormatUtils.format(new DateTime().toDate(), "yyyyMMdd");
    TencentETFApi.queryETFHistory("sh", "159982", endDate, 500, TencentKlinePeriod.Day, TencentAdjust.QFQ);
  }
}
