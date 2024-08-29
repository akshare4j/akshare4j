/** */
package com.github.leeyazhou.akshare4j.util.http;

/** @author leeyazhou */
@FunctionalInterface
public interface DnsSelector {

  String select(String host);
}
