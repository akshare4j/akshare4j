package com.github.akshare4j.util.http.rpc;

public class HttpRpcException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private Integer code;

  private String message;

  public HttpRpcException(String message) {
    this.message = message;
  }

  public HttpRpcException(String message, Integer code) {
    this.message = message;
    this.code = code;
  }

  public HttpRpcException(String message, Throwable e) {
    super(message, e);
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public Integer getCode() {
    return code;
  }


}
