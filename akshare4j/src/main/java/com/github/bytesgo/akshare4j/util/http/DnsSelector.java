/** */
package com.github.bytesgo.akshare4j.util.http;

/** @author leeyazhou */
@FunctionalInterface
public interface DnsSelector {

  String select(String host);
}
