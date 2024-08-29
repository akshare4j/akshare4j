package com.github.akshare4j.util.http;

public class HttpByteResponse extends HttpResponseBase {

  private byte[] response;

  public byte[] getResponse() {
    return response;
  }

  public void setResponse(byte[] response) {
    this.response = response;
    setMessage("success");
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("HttpByteResponse@").append(hashCode());
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
