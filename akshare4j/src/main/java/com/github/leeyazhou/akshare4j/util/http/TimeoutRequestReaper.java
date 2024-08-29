package com.github.leeyazhou.akshare4j.util.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A daemon thread used to periodically check connection pools for idle connections.
 */
public final class TimeoutRequestReaper extends Thread {
  private static final Logger logger = LoggerFactory.getLogger(TimeoutRequestReaper.class);
  private static final int REAP_INTERVAL_MILLISECONDS = 3 * 1000;
  private static ConcurrentMap<Integer, RequestWrapper> requestCache = new ConcurrentHashMap<>();

  private static TimeoutRequestReaper instance;

  private volatile boolean shuttingDown;

  private TimeoutRequestReaper() {
    super("idle-request-reaper");
    setDaemon(true);
  }

  public static synchronized boolean registerRequest(RequestWrapper request) {
    if (instance == null) {
      synchronized (TimeoutRequestReaper.class) {
        if (instance == null) {
          instance = new TimeoutRequestReaper();
          instance.start();
        }
      }
    }
    if (request == null)
      return false;
    if (request.getId() % 1000 == 1) {
      logger.info("add request id: {}", request.getId());
    }
    requestCache.putIfAbsent(request.getId(), request);
    return true;
  }

  public static void removeRequest(RequestWrapper request) {
    if (request == null)
      return;
    if (request.getId() % 1000 == 1) {
      logger.info("remove request id: {}, maxTimeout: {}, maxRetryTimes: {}, cached request size: {}", request.getId(),
          request.getMaxTimeout(), request.getMaxRetryTimes(), requestCache.size());
    }
    requestCache.remove(request.getId());
  }

  private void markShuttingDown() {
    shuttingDown = true;
  }

  @Override
  public void run() {
    while (true) {
      if (shuttingDown) {
        logger.debug("Shutting down reaper thread.");
        return;
      }

      try {
        Thread.sleep(REAP_INTERVAL_MILLISECONDS);
      } catch (InterruptedException e) {
      }

      try {
        for (Map.Entry<Integer, RequestWrapper> entry : requestCache.entrySet()) {
          try {
            RequestWrapper requestWrapper = entry.getValue();
            if (requestWrapper.isTimeout()) {
              removeRequest(requestWrapper);
              logger.info("request processed time: {}ms, id: {}",
                  System.currentTimeMillis() - requestWrapper.getStartTime(), requestWrapper.getId());
              requestWrapper.abort();
            }
          } catch (Exception ex) {
            logger.warn("Unable to abort timeout request", ex);
          }
        }
      } catch (Throwable t) {
        logger.warn("Reaper thread: ", t);
      }
    }
  }

  public static synchronized boolean shutdown() {
    if (instance != null) {
      instance.markShuttingDown();
      instance.interrupt();
      requestCache.clear();
      instance = null;
      return true;
    }
    return false;
  }

  public static synchronized int size() {
    return requestCache.size();
  }

  private static final AtomicInteger index = new AtomicInteger();

  static class RequestWrapper {
    private final int id;
    private final HttpRequestBase request;
    private final long startTime = System.currentTimeMillis();
    private long maxTimeout;
    private final HttpContext httpContext;
    private int maxRetryTimes;

    public RequestWrapper(HttpRequestBase request, HttpContext httpContext) {
      this.id = index.incrementAndGet();
      this.request = request;
      this.httpContext = httpContext;
    }

    public RequestWrapper(HttpRequestBase request, HttpContext httpContext, int maxTotalTimeout) {
      this(request, httpContext);
      this.maxTimeout = maxTotalTimeout;
    }

    public void abort() {
      this.request.abort();
      logger.info("abort request, id: {}, maxTimeout: {}, maxRetryTimes: {}", id, getMaxTimeout(), getMaxRetryTimes());
    }

    public int getId() {
      return id;
    }

    public boolean isTimeout() {
      final long end = System.currentTimeMillis();
      if (end - getStartTime() >= getMaxTimeout()) {
        return true;
      }

      return false;
    }

    public long getMaxTimeout() {
      if (maxTimeout <= 0) {
        RequestConfig requestConfig = request.getConfig();
        if (requestConfig != null) {
          this.maxTimeout = request.getConfig().getSocketTimeout() + request.getConfig().getConnectionRequestTimeout()
              + request.getConfig().getConnectTimeout();
        }
      }
      if (maxTimeout <= 0) {
        this.maxTimeout = HttpUtil.DEFAULT_TIMEOUT * 3;
      }
      return maxTimeout;
    }

    public void setMaxTimeout(long maxTimeout) {
      this.maxTimeout = maxTimeout;
    }

    public int getMaxRetryTimes() {
      if (maxRetryTimes == 0) {
        Integer maxRetryTimesObj = (Integer) httpContext.getAttribute(HttpUtil.KEY_MAX_RETRY_TIMES);
        if (maxRetryTimesObj != null && maxRetryTimesObj > 0) {
          maxRetryTimes = (int) maxRetryTimesObj;
        }
      }
      if (maxRetryTimes == 0) {
        this.maxRetryTimes = HttpClientRetryHandler.DEFAULT_MAX_RETRY_TIMES;
      }
      return maxRetryTimes;
    }

    public long getStartTime() {
      return startTime;
    }
  }
}
