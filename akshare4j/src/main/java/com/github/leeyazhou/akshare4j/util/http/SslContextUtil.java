package com.github.leeyazhou.akshare4j.util.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leeyazhou
 *
 */
public class SslContextUtil {
  private static final Logger logger = LoggerFactory.getLogger(SslContextUtil.class);
  private static SSLContext sslContext;

  public static SSLContext getSslContext() {
    if (sslContext == null) {
      sslContext = createSslContext();
    }
    return sslContext;
  }

  public static SSLContext createSslContext() {
    try {
      SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
      SSLContext sslContext = sslContextBuilder.loadTrustMaterial(null, new TrustStrategy() {
        // 信任所有
        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
          return true;
        }
      }).build();
      sslContext.getServerSessionContext().setSessionCacheSize(HttpConstants.DEFAULT_TOTAL_MAX);
      sslContext.getServerSessionContext().setSessionTimeout(HttpConstants.DEFAULT_SESSION_TIMEOUT_IN_SECOND);
      sslContext.getClientSessionContext().setSessionCacheSize(HttpConstants.DEFAULT_TOTAL_MAX);
      sslContext.getClientSessionContext().setSessionTimeout(HttpConstants.DEFAULT_SESSION_TIMEOUT_IN_SECOND);
      return sslContext;
    } catch (Exception e) {
      logger.error("", e);
    }
    return null;
  }

}
