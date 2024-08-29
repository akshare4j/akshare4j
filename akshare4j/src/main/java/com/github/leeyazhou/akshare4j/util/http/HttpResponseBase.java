/** */
package com.github.leeyazhou.akshare4j.util.http;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;

/** @author leeyazhou */
public class HttpResponseBase {
  private static final Header[] EMPTY = new Header[] {};
  private int code;

  private Header[] headers;
  private Header[] cookieHeaders;
  private String message = "fail";
  private long startTime = System.currentTimeMillis();
  private long costTime;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public Header[] getHeaders() {
    return headers;
  }

  public void setHeaders(Header[] headers) {
    this.headers = headers;
  }

  public Header[] getCookieHeaders() {
    return cookieHeaders;
  }

  public void setCookieHeaders(Header[] cookieHeaders) {
    this.cookieHeaders = cookieHeaders;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public boolean isOk() {
    return 200 == getCode();
  }

  public void setCostTime(long costTime) {
    this.costTime = costTime;
  }

  public long getCostTime() {
    return costTime;
  }

  public void complete() {
    this.costTime = System.currentTimeMillis() - startTime;
  }

  public Header[] getHeaders(final String name) {
    List<Header> headersFound = null;
    // HTTPCORE-361 : we don't use the for-each syntax, i.e.
    // for (Header header : headers)
    // as that creates an Iterator that needs to be garbage-collected
    for (int i = 0; i < this.headers.length; i++) {
      final Header header = this.headers[i];
      if (header.getName().equalsIgnoreCase(name)) {
        if (headersFound == null) {
          headersFound = new ArrayList<Header>();
        }
        headersFound.add(header);
      }
    }
    return headersFound != null ? headersFound.toArray(new Header[headersFound.size()]) : EMPTY;
  }
}
