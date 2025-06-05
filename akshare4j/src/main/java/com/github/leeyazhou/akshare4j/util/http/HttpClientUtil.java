/** */
package com.github.leeyazhou.akshare4j.util.http;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.hc.client5.http.DnsResolver;
import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

/** @author leeyazhou */
public class HttpClientUtil {
  private static HttpClientUtil singlton = new HttpClientUtil();

  private HttpClientUtil() {}

  public static HttpClientUtil getInstance() {
    return singlton;
  }

  private CloseableHttpClient defaultHttpClient;

  PoolingHttpClientConnectionManager createConnectionManager(int maxTotal, DnsSelector dnsSelector) {
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

    TlsSocketStrategy tlsSocketStrategy =
        new DefaultClientTlsStrategy(SslContextUtil.getSslContext(), NoopHostnameVerifier.INSTANCE);
    PoolingHttpClientConnectionManagerBuilder connectionManagerBuilder =
        PoolingHttpClientConnectionManagerBuilder.create();
    connectionManagerBuilder.setTlsSocketStrategy(tlsSocketStrategy);

    connectionManagerBuilder.setDnsResolver(dnsResolver);
    if (maxTotal > 0) {
      connectionManagerBuilder.setMaxConnTotal(maxTotal);
      connectionManagerBuilder.setMaxConnPerRoute(maxTotal);
    }

    ConnectionConfig.Builder connectionConfigBuilder = ConnectionConfig.custom()
        .setConnectTimeout(Timeout.ofMilliseconds(HttpConstants.DEFAULT_TIMEOUT)).setTimeToLive(TimeValue.ofSeconds(10))
        .setSocketTimeout(Timeout.ofMilliseconds(HttpConstants.DEFAULT_TIMEOUT));
    connectionConfigBuilder.setValidateAfterInactivity(TimeValue.ofMilliseconds(50));
    connectionManagerBuilder.setDefaultConnectionConfig(connectionConfigBuilder.build());
    connectionManagerBuilder.setDefaultSocketConfig(SocketConfig.custom()
        .setSoTimeout(Timeout.ofMilliseconds(HttpConstants.DEFAULT_TIMEOUT)).setTcpNoDelay(true).build());
    return connectionManagerBuilder.build();
  }


  public CloseableHttpClient getHttpClient() {
    if (defaultHttpClient == null) {
      synchronized (HttpConstants.CHARSET_UTF8) {
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
    HttpClientBuilder builder = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom()
        .setConnectionRequestTimeout(Timeout.ofMilliseconds(HttpConstants.DEFAULT_TIMEOUT)).build());
    PoolingHttpClientConnectionManager connectionManager = createConnectionManager(maxTotal, dnsSelector);
    builder.setConnectionManager(connectionManager);

    builder.evictExpiredConnections().evictIdleConnections(TimeValue.ofSeconds(10)).evictExpiredConnections();
    return builder.build();
  }
}
