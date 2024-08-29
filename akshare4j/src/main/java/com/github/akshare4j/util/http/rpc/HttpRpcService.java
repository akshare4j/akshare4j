package com.github.akshare4j.util.http.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.http.HttpHeaders;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.akshare4j.util.http.HttpContentType;
import com.github.akshare4j.util.http.HttpMethod;
import com.github.akshare4j.util.http.HttpResponse;
import com.github.akshare4j.util.http.HttpResponseBase;
import com.github.akshare4j.util.http.HttpUtil;
import com.github.akshare4j.util.http.RequestContext;
import com.github.akshare4j.util.http.rpc.annotation.ServiceAction;
import com.github.akshare4j.util.http.rpc.annotation.ServiceContract;
import com.github.akshare4j.util.http.rpc.model.ServiceMethod;
import com.github.akshare4j.util.http.rpc.serializer.Serializer;

/**
 * http rpc service
 *
 * @author leeyazhou
 */
public class HttpRpcService implements InvocationHandler, HttpRpcInvoker {
  private static final Logger logger = LoggerFactory.getLogger(HttpRpcService.class);
  private final HttpRpcConfig httpRpcConfig;
  private Class<?> serviceType;
  private String serviceName = "/";
  private final Serializer serializer;
  private final HttpResponseParser responseParser;
  private CloseableHttpClient httpClient;
  public static final String KEY_HEADER_RPC_TIMEOUT = "http-rpc-timeout";
  private AtomicInteger logIndex = new AtomicInteger();

  public HttpRpcService(HttpRpcConfig httpRpcConfig, Class<?> serviceType, Serializer serializer,
      HttpResponseParser responseParser) {
    this.httpRpcConfig = httpRpcConfig;
    this.serviceType = serviceType;
    this.serializer = serializer;
    this.responseParser = responseParser;
    ServiceContract serviceContract = serviceType.getAnnotation(ServiceContract.class);
    if (serviceContract != null) {
      serviceName = serviceContract.value();
    }
  }

  /**
   * @param httpClient the httpClient to set
   */
  public void setHttpClient(CloseableHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    HttpRpcContext rpcContext = HttpRpcContext.get();
    try {
      if (method.getName().equals("toString")) {
        return serviceType + "@" + this.toString();
      }
      if (method.getName().equals("hashCode")) {
        return this.hashCode();
      }

      Object request = null;
      ServiceAction serviceAction = method.getAnnotation(ServiceAction.class);
      if (args != null) {
        if (args.length > 1 || args[0] == null) {
          throw new HttpRpcException("请求参数个数应该为一个且不能为空, 类：" + serviceType + ", 方法：" + method.getName());
        }
        request = args[0];
      }

      ServiceMethod serviceMethod = new ServiceMethod(method, null);
      serviceMethod.setServiceName(serviceName);
      serviceMethod.setActionName(serviceAction.value());
      rpcContext.setHttpMethod(serviceAction.method());

      String url = buildUrl(method, serviceAction, rpcContext);

      RequestContext context = newRequestContext(url, rpcContext, serviceMethod);
      buildRequestData(request, context, rpcContext);
      boolean logEnabled = logIndex.incrementAndGet() % 100 <= httpRpcConfig.getLogRatio();
      rpcContext.setLogEnabled(logEnabled);
      if (logEnabled) {
        logger.info("http Rpc request, requestBody: {}", JSON.toJSONString(request));
      }
      HttpResponseBase httpResponse = doInvoke(context, rpcContext);
      return responseParser.parse(httpResponse, serviceMethod);
    } finally {
      HttpRpcContext.remove();
    }
  }

  private void buildRequestData(Object request, RequestContext context, HttpRpcContext rpcContext) {
    if (request != null) {
      if (HttpMethod.GET.equals(rpcContext.getHttpMethod())) {
        JSONObject params = JSON.parseObject(JSON.toJSONString(request));
        context.setParams(params);
      } else {
        byte[] requestBody = serializer.serialize(request);
        context.setRequestBodyByte(requestBody);
      }

    }
  }

  @Override
  public HttpResponseBase doInvoke(RequestContext context, HttpRpcContext rpcContext) {
    HttpResponse httpResponse = null;
    if (HttpMethod.GET.equals(rpcContext.getHttpMethod())) {
      httpResponse = HttpUtil.getInstance().get(context, this.httpClient);
    } else {
      httpResponse = HttpUtil.getInstance().post(context, this.httpClient);
    }
    if (rpcContext.isLogEnabled()) {
      logger.info("http Rpc response, httpCode: {}, costTime: {}ms, responseBody: {}", httpResponse.getCode(),
          httpResponse.getCostTime(), httpResponse.getResponse());
    }
    return httpResponse;
  }

  private String buildUrl(Method method, ServiceAction serviceAction, HttpRpcContext rpcContext) {
    String serverType = HttpRpcConfig.DEFAULT_SERVER_TYPE;
    if (rpcContext.getServerType() != null) {
      serverType = rpcContext.getServerType();
    }
    String url = httpRpcConfig.getServiceBaseUrl(serverType);
    if (!httpRpcConfig.isGatewayType()) {
      String actionPath = method.getName();
      if (serviceAction != null && (serviceAction.value() == null || serviceAction.value().length() == 0)) {
        actionPath = serviceAction.value();
      }
      url = url + serviceName + actionPath;
    }
    return url;
  }

  private RequestContext newRequestContext(String url, HttpRpcContext rpcContext, ServiceMethod serviceMethod) {
    RequestContext context = RequestContext.newContext(url);
    context.setContentType(HttpContentType.APPLICATION_JSON);
    context.setProxy(false);
    context.addHeader(HttpHeaders.CONTENT_TYPE, HttpContentType.APPLICATION_JSON.toHeaderValue());
    context.addHeader(HttpHeaders.CONNECTION, "keep-alive");
    context.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");

    if (rpcContext.getConnectTimeout() > 0) {
      context.setConnectTimeout(rpcContext.getConnectTimeout());
    }
    if (rpcContext.getRetryTimes() > 0) {
      context.setMaxRetryTimes(rpcContext.getRetryTimes());
    }
    int readTimeout = HttpUtil.DEFAULT_TIMEOUT;
    if (rpcContext.getReadTimeout() > 0) {
      readTimeout = rpcContext.getReadTimeout();
    }
    context.setReadTimeout(readTimeout);
    for (Map.Entry<String, String> entry : rpcContext.getHeaders().entrySet()) {
      context.addHeader(entry.getKey(), entry.getValue());
    }
    return context;
  }
}
