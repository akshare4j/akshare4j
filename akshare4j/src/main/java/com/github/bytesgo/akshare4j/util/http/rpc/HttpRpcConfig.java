package com.github.bytesgo.akshare4j.util.http.rpc;

import java.util.HashMap;
import java.util.Map;

public class HttpRpcConfig {
  public static final String DEFAULT_SERVER_TYPE = "DefaultServerType";
  private Map<String, String> serverUrls = new HashMap<>();
  private String serviceBaseUrl;

  private boolean gatewayType;

  private int logRatio = 100;

  public String getServiceBaseUrl() {
    return serviceBaseUrl;
  }

  public HttpRpcConfig setServiceBaseUrl(String serviceBaseUrl) {
    this.serviceBaseUrl = serviceBaseUrl;
    serverUrls.put(DEFAULT_SERVER_TYPE, serviceBaseUrl);
    return this;
  }

  public HttpRpcConfig addServiceBaseUrl(String serverType, String serviceBaseUrl) {
    serverUrls.put(serverType, serviceBaseUrl);
    return this;
  }

  public String getServiceBaseUrl(String serverType) {
    return serverUrls.get(serverType);
  }

  public boolean isGatewayType() {
    return gatewayType;
  }

  public HttpRpcConfig setGatewayType(boolean gatewayType) {
    this.gatewayType = gatewayType;
    return this;
  }

  public HttpRpcConfig setLogRatio(int logRatio) {
    this.logRatio = logRatio;
    return this;
  }

  public int getLogRatio() {
    return logRatio;
  }
}
