package com.github.leeyazhou.akshare4j.util.http.rpc;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.leeyazhou.akshare4j.util.http.HttpMethod;

public class HttpRpcContext {
  private static final Logger logger = LoggerFactory.getLogger(HttpRpcContext.class);
  private static final ThreadLocal<HttpRpcContext> MOBILE_CONTEXT_LOCAL = new ThreadLocal<>();

  private Map<String, String> headers = new HashMap<>();
  private int connectTimeout;
  private int readTimeout;
  private int retryTimes;
  private String serverType;
  private boolean logEnabled;
  private HttpMethod httpMethod;

  public void addHeader(String name, String value) {
    headers.put(name, value);
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public void setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  public int getReadTimeout() {
    return readTimeout;
  }

  public void setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  /** @param serverType the serverType to set */
  public void setServerType(String serverType) {
    this.serverType = serverType;
  }

  /** @return the serverType */
  public String getServerType() {
    return serverType;
  }

  /**
   * @param retryTimes the retryTimes to set
   */
  public void setRetryTimes(int retryTimes) {
    this.retryTimes = retryTimes;
  }

  /**
   * @return the retryTimes
   */
  public int getRetryTimes() {
    return retryTimes;
  }

  /**
   * @param logEnabled the logEnabled to set
   */
  public void setLogEnabled(boolean logEnabled) {
    this.logEnabled = logEnabled;
  }

  /**
   * @param httpMethod the httpMethod to set
   */
  public void setHttpMethod(HttpMethod httpMethod) {
    this.httpMethod = httpMethod;
  }

  /**
   * @return the httpMethod
   */
  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  /**
   * @return the logEnabled
   */
  public boolean isLogEnabled() {
    return logEnabled;
  }

  public static HttpRpcContext get() {
    HttpRpcContext context = MOBILE_CONTEXT_LOCAL.get();
    if (context == null) {
      context = new HttpRpcContext();
      MOBILE_CONTEXT_LOCAL.set(context);
      logger.debug("init train rpc context: {}", context);
    }
    return context;
  }

  public static void remove() {
    logger.debug("remove train rpc context: {}", MOBILE_CONTEXT_LOCAL.get());
    MOBILE_CONTEXT_LOCAL.remove();
  }
}
