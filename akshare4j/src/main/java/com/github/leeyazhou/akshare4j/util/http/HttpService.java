package com.github.leeyazhou.akshare4j.util.http;

import org.apache.http.impl.client.CloseableHttpClient;

public interface HttpService {

  /**
   * GET请求
   *
   * @param context {@link RequestContext} 请求上下文
   * @return 响应字符串
   */
  HttpResponse get(RequestContext context);

  HttpResponse get(RequestContext context, CloseableHttpClient httpClient);

  /**
   * 上传
   *
   * @param context {@link RequestContext} 请求上下文
   * @return 响应字符串
   */
  HttpResponse upload(RequestContext context);

  /**
   * POST请求
   *
   * @param context {@link RequestContext} 请求上下文
   * @return 响应字符串
   */
  HttpResponse post(RequestContext context);

  HttpByteResponse postByte(RequestContext context);

  HttpResponse post(RequestContext context, CloseableHttpClient httpClient);
}
