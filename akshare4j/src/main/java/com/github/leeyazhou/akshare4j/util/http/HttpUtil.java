/** */
package com.github.leeyazhou.akshare4j.util.http;

import java.io.File;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.leeyazhou.akshare4j.util.http.TimeoutRequestReaper.RequestWrapper;

/** @author leeyazhou */
public class HttpUtil implements HttpService {
  private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
  private static HttpUtil singlton = new HttpUtil();
  public static final int DEFAULT_TIMEOUT = 15000;

  public static final String CHARSET_UTF8 = "UTF-8";
  public static final String CHARSET_GBK = "GBK";

  public static final String KEY_MAX_RETRY_TIMES = "maxRetryTimes";

  public static HttpUtil getInstance() {
    return singlton;
  }

  /**
   * GET请求
   *
   * @param context {@link RequestContext} 请求上下文
   * @return 响应字符串
   */
  @Override
  public HttpResponse get(RequestContext context) {
    return doResponseString(context, HttpMethod.GET);
  }

  @Override
  public HttpResponse get(RequestContext context, CloseableHttpClient httpClient) {
    return doResponseString(context, HttpMethod.GET, httpClient);
  }

  /**
   * POST请求
   *
   * @param context {@link RequestContext} 请求上下文
   * @return 响应字符串
   */
  @Override
  public HttpResponse post(RequestContext context) {
    return doResponseString(context, HttpMethod.POST);
  }

  @Override
  public HttpByteResponse postByte(RequestContext context) {
    return doResponseByte(context, HttpMethod.POST);
  }

  @Override
  public HttpResponse post(RequestContext context, CloseableHttpClient httpClient) {
    return doResponseString(context, HttpMethod.POST, httpClient);
  }

  /**
   * 上传
   *
   * @param context {@link RequestContext} 请求上下文
   * @return 响应字符串
   */
  @Override
  public HttpResponse upload(RequestContext context) {
    return upload(context, null);
  }

  private void fillProxyInfo(HttpRequestBase method, RequestContext context, HttpContext httpContext) {
    int readTimeout = DEFAULT_TIMEOUT, connectTimeout = DEFAULT_TIMEOUT;
    if (context.getReadTimeout() > 0) {
      readTimeout = context.getReadTimeout();
    }
    if (context.getConnectTimeout() > 0) {
      connectTimeout = context.getConnectTimeout();
    }
    RequestConfig.Builder builder = RequestConfig.custom().setConnectionRequestTimeout(connectTimeout)
        .setSocketTimeout(readTimeout).setConnectTimeout(connectTimeout);
    if (context.getCookieSpec() != null) {
      builder.setCookieSpec(context.getCookieSpec());
    }
    if (context.isProxy() && context.getProxyInfo() != null) {
      ProxyInfo proxyInfo = context.getProxyInfo();
      HttpHost proxy = new HttpHost(proxyInfo.getHost(), proxyInfo.getPort());
      builder.setProxy(proxy);
      if (proxyInfo.getUsername() != null) {
        builder.setAuthenticationEnabled(true);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(proxyInfo.getHost(), proxyInfo.getPort()),
            new UsernamePasswordCredentials(proxyInfo.getUsername(), proxyInfo.getPassword()));
        httpContext.setAttribute(HttpClientContext.CREDS_PROVIDER, credsProvider);
      }
      logger.debug("set http proxy, info : {}", proxyInfo);
    }

    builder.setRedirectsEnabled(context.isRedirectsEnabled());
    method.setConfig(builder.build());
    if (context.getMaxRetryTimes() > 0) {
      httpContext.setAttribute(KEY_MAX_RETRY_TIMES, context.getMaxRetryTimes());
    }
  }

  public CloseableHttpResponse doResponse(RequestContext context, HttpMethod method) {
    return doResponse(context, method, null);
  }

  public CloseableHttpResponse doResponse(RequestContext context, HttpMethod method, CloseableHttpClient httpClient) {
    return doResponse(context, method, httpClient, null);
  }

  /**
   * 获取response内容
   *
   * @param context 请求上下文
   * @param method 请求方法
   * @return {@link CloseableHttpResponse}
   */
  public CloseableHttpResponse doResponse(RequestContext context, HttpMethod method, CloseableHttpClient httpClient,
      HttpResponseBase httpResponse) {
    String url = context.getUrl();
    if (url == null || url.length() == 0) {
      logger.warn("请求地址不能为空， url: {}", url);
      return null;
    }
    RequestWrapper requestWrapper = null;
    HttpRequestBase httpRequest = null;
    try {
      if (HttpMethod.GET.equals(method)) {
        httpRequest = new HttpGet(url);
      } else if (HttpMethod.POST.equals(method)) {
        httpRequest = new HttpPost(url);
      } else if (HttpMethod.PUT.equals(method)) {
        httpRequest = new HttpPut(url);
      } else if (HttpMethod.DELETE.equals(method)) {
        httpRequest = new HttpDelete(url);
      }
      HttpContext httpContext = new BasicHttpContext();
      requestWrapper = new RequestWrapper(httpRequest, httpContext, context.getMaxTotalTimeout());
      TimeoutRequestReaper.registerRequest(requestWrapper);
      if (context.getParams() != null && !context.getParams().isEmpty()) {
        Map<String, Object> params = context.getParams();
        List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
          Object value = entry.getValue();
          if (value != null) {
            pairs.add(new BasicNameValuePair(entry.getKey(), value.toString()));
          }
        }
        if (pairs.size() > 0) {
          if (HttpMethod.GET.equals(method)) {
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, context.getCharset()));
            httpRequest.setURI(URI.create(url));
          } else if (HttpMethod.POST.equals(method)) {
            HttpEntity entity = new UrlEncodedFormEntity(pairs, context.getCharset());
            ((HttpPost) httpRequest).setEntity(entity);
          }
        }
      } else if (context.hasRequestBody()) {
        HttpEntity entity = null;
        if (context.getRequestBodyByte() != null) {
          if (context.getRequestBodyByte() != null) {
            entity = new ByteArrayEntity(context.getRequestBodyByte());
          } else {
            entity = new ByteArrayEntity(context.getRequestBody().getBytes());
          }
        } else {
          entity = new StringEntity(context.getRequestBody(), CHARSET_UTF8);
        }
        ((HttpPost) httpRequest).setEntity(entity);
      }

      for (Map.Entry<String, String> entry : context.getHeaders().entrySet()) {
        httpRequest.addHeader(entry.getKey(), entry.getValue());
      }

      for (Map.Entry<String, Object> attachement : context.getAttachements().entrySet()) {
        httpContext.setAttribute(attachement.getKey(), attachement.getValue());
      }
      fillProxyInfo(httpRequest, context, httpContext);
      if (httpClient == null) {
        httpClient = HttpClientUtil.getInstance().getHttpClient();
      }

      CloseableHttpResponse response = httpClient.execute(httpRequest, httpContext);
      if (response != null && response.getStatusLine().getStatusCode() != 200) {
        logger.info("响应状态：{}, url: {}", response.getStatusLine(), url);
      }
      return response;
    } catch (SocketException e) {
      logger.warn(e.getMessage());
      if (httpResponse != null) {
        httpResponse.setMessage(e.getMessage());
      }
    } catch (ConnectTimeoutException e) {
      logger.warn("connect timeout, url: {}, timeout:{}ms, 参数：{}", url,
          (context.getConnectTimeout() > 0 ? context.getConnectTimeout() : DEFAULT_TIMEOUT), context.getParams());
      if (httpResponse != null) {
        httpResponse.setMessage(e.getMessage());
      }
    } catch (SocketTimeoutException e) {
      logger.warn("read timeout, url: {}, timeout:{}ms, 参数：{}", url,
          (context.getReadTimeout() > 0 ? context.getReadTimeout() : DEFAULT_TIMEOUT), context.getParams());
      if (httpResponse != null) {
        httpResponse.setMessage(e.getMessage());
      }
    } catch (Exception e) {
      httpRequest.abort();
      logger.error("请求异常, url: " + url + ", 参数：" + context.getParams(), e);
      if (httpResponse != null) {
        httpResponse.setMessage(e.getMessage());
      }
    } finally {
      TimeoutRequestReaper.removeRequest(requestWrapper);
    }
    return null;
  }

  public HttpResponse upload(RequestContext context, InputStream inputStream) {
    HttpResponse httpResponse = new HttpResponse();
    try {
      HttpPost httppost = new HttpPost(context.getUrl());
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
      if (inputStream != null) {
        builder.addBinaryBody("file", inputStream);
      } else {
        builder.addBinaryBody("file", new File(context.getUploadFile()));
      }
      HttpEntity reqEntity = builder.build();
      httppost.setEntity(reqEntity);
      CloseableHttpResponse response = HttpClientUtil.getInstance().getHttpClient().execute(httppost);
      try {
        logger.info("upload status : {}, url : {}", response.getStatusLine(), context.getUrl());
        HttpEntity resEntity = response.getEntity();
        String responseEntityStr = null;
        if (resEntity != null) {
          responseEntityStr = EntityUtils.toString(response.getEntity());
        }
        logger.info("响应数据: {}", responseEntityStr);
        httpResponse.setCode(response.getStatusLine().getStatusCode());
        httpResponse.setResponse(responseEntityStr);
      } finally {
        if (response != null) {
          EntityUtils.consumeQuietly(response.getEntity());
        }
        CloseUtil.close(response);
      }
    } catch (Exception e) {
      logger.error("请求异常url : " + context.getUrl(), e);
    }
    return httpResponse;
  }

  public HttpResponse doResponseString(RequestContext context, HttpMethod method) {
    return doResponseString(context, method, null);
  }

  public HttpByteResponse doResponseByte(RequestContext context, HttpMethod method) {
    return doResponseByte(context, method, null);
  }

  /**
   * @param context {@link RequestContext} 请求上下文
   * @param method 请求方法
   * @return 响应字符串
   */
  private HttpResponse doResponseString(RequestContext context, HttpMethod method, CloseableHttpClient httpClient) {
    HttpResponse httpResponse = new HttpResponse();
    CloseableHttpResponse response = null;
    try {
      response = doResponse(context, method, httpClient, httpResponse);
      if (response == null) {
        return httpResponse;
      }
      String reponseStr = EntityUtils.toString(response.getEntity(), context.getCharset());
      httpResponse.setResponse(reponseStr);
      httpResponse.setCode(response.getStatusLine().getStatusCode());
      httpResponse.setHeaders(response.getAllHeaders());
    } catch (Exception e) {
      logger.error("请求异常url : " + context.getUrl(), e);
      httpResponse.setMessage("fail:" + e.getMessage());
    } finally {
      if (response != null) {
        EntityUtils.consumeQuietly(response.getEntity());
      }
      CloseUtil.close(response);
      httpResponse.complete();
    }
    return httpResponse;
  }

  public HttpByteResponse doResponseByte(RequestContext context, HttpMethod method, CloseableHttpClient httpClient) {
    HttpByteResponse httpResponse = new HttpByteResponse();
    CloseableHttpResponse response = null;
    try {
      response = doResponse(context, method, httpClient, httpResponse);
      if (response == null) {
        return httpResponse;
      }
      byte[] reponse = EntityUtils.toByteArray(response.getEntity());
      httpResponse.setResponse(reponse);
      httpResponse.setCode(response.getStatusLine().getStatusCode());
      httpResponse.setHeaders(response.getAllHeaders());
    } catch (Exception e) {
      logger.error("请求异常url : " + context.getUrl(), e);
      httpResponse.setMessage("fail:" + e.getMessage());
    } finally {
      if (response != null) {
        EntityUtils.consumeQuietly(response.getEntity());
      }
      CloseUtil.close(response);
      httpResponse.complete();
    }
    return httpResponse;
  }
}
