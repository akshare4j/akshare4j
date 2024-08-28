package com.github.bytesgo.akshare4j.util.http.rpc.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Result<T> implements Serializable {
  private static final long serialVersionUID = 1L;

  private int code = 1000;

  private String message = "OK";
  private T data;
  private String traceid;

  public int getCode() {
    return code;
  }

  public Result<T> setCode(int code) {
    this.code = code;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public Result<T> setMessage(String message) {
    this.message = message;
    return this;
  }

  public T getData() {
    return data;
  }

  public Result<T> setData(T data) {
    this.data = data;
    return this;
  }

  @SuppressWarnings("unchecked")
  public Result<T> addAttribute(String name, Object attr) {
    if (this.data == null) {
      this.data = (T) new HashMap<String, Object>();
    }
    ((Map<String, Object>) data).put(name, attr);
    return this;
  }

  public static <T> Result<T> success() {
    return new Result<>();
  }

  public static <T> Result<T> success(T data) {
    return new Result<T>().setData(data);
  }

  public static <T> Result<T> fail() {
    return new Result<T>().setCode(5000).setMessage("fail");
  }

  public static <T> Result<T> fail(int code, String message) {
    return new Result<T>().setCode(code).setMessage(message);
  }

  public static <T> Result<T> fail(String message) {
    return new Result<T>().setCode(5000).setMessage(message);
  }

  public String getTraceid() {
    // if (this.traceid == null) {
    // this.traceid = TraceIdContext.get();
    // }
    return traceid;
  }

  public void setTraceid(String traceid) {
    this.traceid = traceid;
  }
}
