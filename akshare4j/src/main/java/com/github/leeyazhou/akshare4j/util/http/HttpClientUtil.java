/** */
package com.github.leeyazhou.akshare4j.util.http;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author leeyazhou */
public class HttpClientUtil {
  public static final String appName = "traffic-tools-http";
  private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
  private static HttpClientUtil singlton = new HttpClientUtil();
  public static final int DEFAULT_TIMEOUT = 15000;
  public static final int DEFAULT_TOTAL_MAX = 4096;
  public static final int DEFAULT_SESSION_TIMEOUT_IN_SECOND = 30 * 60;

  public static final String CHARSET_UTF8 = "UTF-8";

  private HttpClientUtil() {}

  public static HttpClientUtil getInstance() {
    return singlton;
  }

  private CloseableHttpClient defaultHttpClient;
  private SSLContext sslContext;

  private SSLContext getSslContext() {
    if (sslContext == null) {
      synchronized (CHARSET_UTF8) {
        if (sslContext == null) {
          try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
              // 信任所有
              public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
              }
            }).build();
            sslContext.getServerSessionContext().setSessionCacheSize(DEFAULT_TOTAL_MAX);
            sslContext.getServerSessionContext().setSessionTimeout(DEFAULT_SESSION_TIMEOUT_IN_SECOND);
            sslContext.getClientSessionContext().setSessionCacheSize(DEFAULT_TOTAL_MAX);
            sslContext.getClientSessionContext().setSessionTimeout(DEFAULT_SESSION_TIMEOUT_IN_SECOND);
            this.sslContext = sslContext;
          } catch (Exception e) {
            logger.error("", e);
          }
        }
      }
    }
    return sslContext;
  }

  Registry<ConnectionSocketFactory> sockRegistry() {
    RegistryBuilder<ConnectionSocketFactory> sockRegistryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
    SSLContext sslContext = getSslContext();
    sockRegistryBuilder.register("http", PlainConnectionSocketFactory.INSTANCE);
    sockRegistryBuilder.register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE));
    return sockRegistryBuilder.build();
  }

  PoolingHttpClientConnectionManager createConnectionManager(int maxTotal, DnsSelector dnsSelector) {
    Registry<ConnectionSocketFactory> sockRegistry = sockRegistry();
    DnsResolver dnsResolver = null;
    if (dnsSelector != null) {
      dnsResolver = new SystemDefaultDnsResolver() {
        @Override
        public InetAddress[] resolve(String host) throws UnknownHostException {
          if (dnsSelector != null) {
            String dns = dnsSelector.select(host);
            if (dns != null) {
              return new InetAddress[] {InetAddress.getByName(dns)};
            }
          }
          return super.resolve(host);
        }
      };
    }
    PoolingHttpClientConnectionManager cm =
        new PoolingHttpClientConnectionManager(sockRegistry, null, null, dnsResolver, 60, TimeUnit.SECONDS);
    if (maxTotal > 0) {
      cm.setMaxTotal(maxTotal);
      cm.setDefaultMaxPerRoute(maxTotal);
    }
    cm.setValidateAfterInactivity(2000);
    cm.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(DEFAULT_TIMEOUT).setTcpNoDelay(true).build());
    return cm;
  }

  public RequestConfig getDefaultRequestConfig() {
    RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(DEFAULT_TIMEOUT)
        .setConnectionRequestTimeout(DEFAULT_TIMEOUT).setSocketTimeout(DEFAULT_TIMEOUT).build();
    return defaultRequestConfig;
  }

  public CloseableHttpClient getHttpClient() {
    if (defaultHttpClient == null) {
      synchronized (CHARSET_UTF8) {
        if (defaultHttpClient == null) {
          this.defaultHttpClient = createHttpClient(4096);
        }
      }
    }
    return defaultHttpClient;
  }

  public CloseableHttpClient createHttpClient() {
    return createHttpClient(-1);
  }

  public CloseableHttpClient createHttpClient(int maxTotal) {
    return createHttpClient(maxTotal, null);
  }

  public CloseableHttpClient createHttpClient(int maxTotal, DnsSelector dnsSelector) {
    HttpClientBuilder builder = HttpClientBuilder.create().setDefaultRequestConfig(getDefaultRequestConfig());
    PoolingHttpClientConnectionManager connectionManager = createConnectionManager(maxTotal, dnsSelector);
    builder.setConnectionManager(connectionManager);

    builder.evictExpiredConnections().evictIdleConnections(10, TimeUnit.SECONDS)
        .setConnectionTimeToLive(60, TimeUnit.SECONDS).evictExpiredConnections()
        .setRetryHandler(new HttpClientRetryHandler(3));
    return builder.build();
  }
}
