/**
 * 
 */
package com.github.leeyazhou.akshare4j.tencent;

import java.util.List;
import org.junit.jupiter.api.Test;
import com.alibaba.fastjson2.JSON;
import com.github.leeyazhou.akshare4j.tencent.model.KLineData;
import com.github.leeyazhou.akshare4j.tencent.model.enums.FqType;
import com.github.leeyazhou.akshare4j.tencent.model.enums.KLineType;

/**
 * @author leeyazhou
 */
class TencentStockHistoryApiTest {

  @Test
  void testFetchCompleteKlineData() {
    List<KLineData> fetchCompleteKlineData =
        TencentStockHistoryApi.getKlineDatas("sh603005", KLineType.Day, FqType.QFQ);
    System.out.println("size=" + fetchCompleteKlineData.size());
    for (KLineData kLineData : fetchCompleteKlineData) {
      System.out.println(JSON.toJSONString(kLineData));
    }
  }

  @Test
  void testFetchLatestKlineData() {}

  @Test
  void testFetchKlineBatch() {}

}
