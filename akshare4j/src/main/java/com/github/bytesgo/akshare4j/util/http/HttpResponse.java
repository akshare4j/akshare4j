package com.github.bytesgo.akshare4j.util.http;

public class HttpResponse extends HttpResponseBase {

  private String response;

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
    setMessage("success");
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("HttpResponse@").append(hashCode());
    builder.append("{code=");
    builder.append(getCode());
    builder.append(", message=");
    builder.append(getMessage());
    builder.append(", costTime=");
    builder.append(getCostTime());
    builder.append("}");
    return builder.toString();
  }
}
