/** */
package com.github.leeyazhou.akshare4j.util.http;

import java.util.HashMap;
import java.util.Map;

/** @author leeyazhou */
public class RequestContext {
  private String url;
  private Map<String, Object> params = new HashMap<>();
  private Map<String, Object> attachements = new HashMap<>();
  private Map<String, String> headers = new HashMap<>();
  private String charset = HttpUtil.CHARSET_UTF8;
  private HttpContentType contentType;
  private boolean proxy;

  /** 允许重定向 */
  private boolean redirectsEnabled = true;

  private String requestBody;
  private byte[] requestBodyByte;
  private String uploadFile;
  private ProxyInfo proxyInfo;
  private int connectTimeout;
  private int readTimeout;
  private int maxTotalTimeout;

  /** see {@link org.apache.http.client.config.CookieSpecs} */
  private String cookieSpec;

  private int maxRetryTimes;

  private boolean urlFormEncoded;
  /**
   * 构建请求上下文
   *
   * @param url 请求地址
   * @return {@link RequestContext}
   */
  public static RequestContext newContext(String url) {
    return new RequestContext(url);
  }

  /**
   * 构建请求上下文
   *
   * @param url 请求地址
   */
  public RequestContext(String url) {
    this.url = url;
  }

  public RequestContext setParams(Map<String, Object> params) {
    this.params = params;
    return this;
  }

  public Map<String, Object> getParams() {
    return params;
  }

  public String getUrl() {
    return url;
  }

  public RequestContext setHeaders(Map<String, String> headers) {
    this.headers.putAll(headers);
    return this;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public RequestContext addParam(String name, String value) {
    this.params.put(name, value);
    return this;
  }

  public RequestContext addAttachement(String name, Object value) {
    this.attachements.put(name, value);
    return this;
  }

  public Map<String, Object> getAttachements() {
    return attachements;
  }

  public RequestContext addHeader(String name, String value) {
    this.headers.put(name, value);
    return this;
  }

  public RequestContext addHeaders(Map<String, String> headers) {
    this.headers.putAll(headers);
    return this;
  }

  public RequestContext setCharset(String charset) {
    this.charset = charset;
    return this;
  }

  public String getCharset() {
    return charset;
  }

  /**
   * 是否使用代理
   *
   * @param proxy 是否使用代理
   * @return {@link RequestContext}
   */
  public RequestContext setProxy(boolean proxy) {
    this.proxy = proxy;
    return this;
  }

  /**
   * 是否使用代理
   *
   * @return 是否使用代理
   */
  public boolean isProxy() {
    return proxy;
  }

  /**
   * 是否重定向
   *
   * @param redirectsEnabled 是否重定向
   * @return {@link RequestContext}
   */
  public RequestContext setRedirectsEnabled(boolean redirectsEnabled) {
    this.redirectsEnabled = redirectsEnabled;
    return this;
  }

  public boolean isRedirectsEnabled() {
    return redirectsEnabled;
  }

  public RequestContext setRequestBody(String requestBody) {
    this.requestBody = requestBody;
    return this;
  }

  public String getRequestBody() {
    return requestBody;
  }

  /**
   * 请求体是否有内容
   *
   * @return true / false
   */
  public boolean hasRequestBody() {
    return requestBody != null || requestBodyByte != null;
  }

  public void setRequestBodyByte(byte[] requestBodyByte) {
    this.requestBodyByte = requestBodyByte;
  }

  public byte[] getRequestBodyByte() {
    return requestBodyByte;
  }

  public void setUploadFile(String uploadFile) {
    this.uploadFile = uploadFile;
  }

  public String getUploadFile() {
    return uploadFile;
  }

  public ProxyInfo getProxyInfo() {
    return proxyInfo;
  }

  public RequestContext setProxyInfo(ProxyInfo proxyInfo) {
    this.proxyInfo = proxyInfo;
    return this;
  }

  public RequestContext setCookieSpec(String cookieSpec) {
    this.cookieSpec = cookieSpec;
    return this;
  }

  public String getCookieSpec() {
    return cookieSpec;
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public RequestContext setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  public int getReadTimeout() {
    return readTimeout;
  }

  public RequestContext setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
    return this;
  }

  public int getMaxRetryTimes() {
    return maxRetryTimes;
  }

  public RequestContext setMaxRetryTimes(int maxRetryTimes) {
    this.maxRetryTimes = maxRetryTimes;
    return this;
  }

  public void setMaxTotalTimeout(int maxTotalTimeout) {
    this.maxTotalTimeout = maxTotalTimeout;
  }

  public int getMaxTotalTimeout() {
    return maxTotalTimeout;
  }

  /**
   * @param contentType the contentType to set
   */
  public void setContentType(HttpContentType contentType) {
    this.contentType = contentType;
  }

  /**
   * @return the contentType
   */
  public HttpContentType getContentType() {
    return contentType;
  }
  
  /**
   * @param urlFormEncoded the urlFormEncoded to set
   */
  public void setUrlFormEncoded(boolean urlFormEncoded) {
    this.urlFormEncoded = urlFormEncoded;
  }
  
  /**
   * @return the urlFormEncoded
   */
  public boolean isUrlFormEncoded() {
    return urlFormEncoded;
  }
}
