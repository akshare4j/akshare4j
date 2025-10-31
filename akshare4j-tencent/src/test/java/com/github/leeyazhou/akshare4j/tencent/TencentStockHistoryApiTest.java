/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;
import com.alibaba.fastjson2.JSON;
import com.github.leeyazhou.akshare4j.tencent.model.TencentKLineInfo;
import com.github.leeyazhou.akshare4j.tencent.model.enums.TencentAdjust;
import com.github.leeyazhou.akshare4j.tencent.model.enums.TencentKlinePeriod;
import com.github.leeyazhou.akshare4j.tencent.model.enums.TencentMarketType;

/**
 * @author leeyazhou
 */
class TencentStockHistoryApiTest {

  @Test
  void testFetchCompleteKlineData() {
    String endDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
    List<TencentKLineInfo> fetchCompleteKlineData = TencentStockHistoryApi.getKlines("603005", TencentMarketType.SH,
        TencentKlinePeriod.Day, TencentAdjust.QFQ, null, endDate, 0);
    System.out.println("size=" + fetchCompleteKlineData.size());
    for (TencentKLineInfo tencentKLineInfo : fetchCompleteKlineData) {
      System.out.println(JSON.toJSONString(tencentKLineInfo));
    }
  }

  @Test
  void testFetchLatestKlineData() {}

  @Test
  void testFetchKlineBatch() {}

}
