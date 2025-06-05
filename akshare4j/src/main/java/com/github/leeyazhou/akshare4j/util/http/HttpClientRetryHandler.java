package com.github.leeyazhou.akshare4j.util.http;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.net.ssl.SSLException;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.NoHttpResponseException;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author leeyazhou */
public class HttpClientRetryHandler extends DefaultHttpRequestRetryStrategy {
  private static final Logger logger = LoggerFactory.getLogger(HttpClientRetryHandler.class);
  private int maxRetryTimes;

  public static final int DEFAULT_MAX_RETRY_TIMES = 3;

  private final Set<Class<? extends IOException>> nonRetriableClasses;

  public HttpClientRetryHandler(int maxRetryTimes) {
    this.maxRetryTimes = maxRetryTimes;
    this.nonRetriableClasses = new HashSet<Class<? extends IOException>>();
    List<Class<? extends IOException>> clazzes =
        Arrays.asList(UnknownHostException.class, ConnectException.class, SSLException.class);
    for (final Class<? extends IOException> clazz : clazzes) {
      this.nonRetriableClasses.add(clazz);
    }
  }

  @Override
  public boolean retryRequest(HttpRequest request, IOException exception, int executionCount, HttpContext clientContext) {
    int maxRetryTimes = this.maxRetryTimes;
    Integer maxRetryTimesObj = (Integer) clientContext.getAttribute("maxRetryTimes");
    if (maxRetryTimesObj != null && maxRetryTimesObj > 0) {
      maxRetryTimes = (int) maxRetryTimesObj;
    }

    if (executionCount > maxRetryTimes) {
      logger.warn("{}, retry times up to limit，abort retry: {}/{}, reason: {}", request.getRequestUri(), executionCount,
          maxRetryTimes, exception.getMessage());
      return false;
    }
    if (this.nonRetriableClasses.contains(exception.getClass())) {
      logger.warn("request error，abort retry: {}/{}, reason: {}, requestLine: {}", executionCount, maxRetryTimes,
          exception.getMessage(), request.getRequestUri());
      return false;
    }
    for (final Class<? extends IOException> rejectException : this.nonRetriableClasses) {
      if (rejectException.isInstance(exception)) {
        logger.warn("request error，abort retry: {}/{}, reason: {}, requestLine: {}", executionCount, maxRetryTimes,
            exception.getMessage(), request.getRequestUri());
        return false;
      }
    }
    if (NoHttpResponseException.class.equals(exception.getClass())) {
      logger.warn("server failed to response，retry: {}/{}, reason: {}, requestLine: {}", executionCount, maxRetryTimes,
          exception.getMessage(), request.getRequestUri());
      return true;
    }

    logger.info("fail to send request，retry: {}/{}, reason: {}, requestLine: {}", executionCount, maxRetryTimes,
        exception.getMessage(), request.getRequestUri());
    return true;
  }
}
