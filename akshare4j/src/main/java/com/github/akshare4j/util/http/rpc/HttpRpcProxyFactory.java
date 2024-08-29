package com.github.akshare4j.util.http.rpc;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.http.impl.client.CloseableHttpClient;
import com.github.akshare4j.util.http.rpc.serializer.JSONSerializer;
import com.github.akshare4j.util.http.rpc.serializer.Serializer;

public class HttpRpcProxyFactory {
  private static ConcurrentMap<Class<?>, Object> proxyCache = new ConcurrentHashMap<>();
  private final HttpRpcConfig httpRpcConfig;
  private Serializer serializer = new JSONSerializer();
  private HttpResponseParser responseParser;
  private CloseableHttpClient httpClient;

  public HttpRpcProxyFactory(HttpRpcConfig httpRpcConfig) {
    this.httpRpcConfig = httpRpcConfig;
    this.responseParser = new SimpleHttpResponseParser(httpRpcConfig, serializer);
  }

  /**
   * @param serializer the serializer to set
   */
  public void setSerializer(Serializer serializer) {
    this.serializer = serializer;
  }

  /**
   * @param responseParser the responseParser to set
   */
  public void setResponseParser(HttpResponseParser responseParser) {
    this.responseParser = responseParser;
  }

  /**
   * @param httpClient the httpClient to set
   */
  public void setHttpClient(CloseableHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public <T> T getProxy(Class<T> clazz) {
    return getProxy(clazz, HttpRpcProxyFactory.class.getClassLoader());
  }


  @SuppressWarnings("unchecked")
  public <T> T getProxy(Class<T> clazz, ClassLoader classLoader) {
    T result = (T) proxyCache.get(clazz);
    if (result == null) {
      synchronized (proxyCache) {
        result = (T) proxyCache.get(clazz);
        if (result == null) {
          HttpRpcService httpRpcService = new HttpRpcService(httpRpcConfig, clazz, serializer, responseParser);
          httpRpcService.setHttpClient(httpClient);
          result = (T) Proxy.newProxyInstance(classLoader, new Class<?>[] {clazz}, httpRpcService);
          proxyCache.put(clazz, result);
        }
      }
    }
    return result;
  }

}
