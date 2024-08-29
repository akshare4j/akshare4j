/** */
package com.github.akshare4j.util.http;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/** @author leeyazhou */
public class ProxyInfo {
  private String id;
  private String host;
  private int port;
  private String username;
  private String password;

  private boolean needDial;
  private AtomicInteger counter = new AtomicInteger(Integer.MAX_VALUE - 1);
  private Map<String, String> attachements;

  public ProxyInfo() {}

  public ProxyInfo(String host, int port, String username, String password) {
    this(host, port, username, password, false);
  }

  public ProxyInfo(String host, int port, String username, String password, boolean needDial) {
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = password;
    this.needDial = needDial;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  /** @param needDial the needDial to set */
  public void setNeedDial(boolean needDial) {
    this.needDial = needDial;
  }

  public boolean isNeedDial() {
    return needDial;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int increateIndex() {
    return counter.incrementAndGet();
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public Map<String, String> getAttachements() {
    return attachements;
  }

  public void addAttachement(String name, String value) {
    if (attachements == null) {
      attachements = new HashMap<String, String>();
    }
    attachements.put(name, value);
  }

  public String getAttachement(String name) {
    if (attachements == null) {
      return null;
    }
    return attachements.get(name);
  }

  @Override
  public String toString() {
    return "ProxyInfo [host=" + host + ", port=" + port + ", username=" + username + ", password=********]";
  }
}
