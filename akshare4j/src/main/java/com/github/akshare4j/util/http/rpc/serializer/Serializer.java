/** */
package com.github.akshare4j.util.http.rpc.serializer;

import java.lang.reflect.Type;

/**
 * Serializer
 *
 * @author leeyazhou
 */
public interface Serializer {

  /**
   * serialize obj
   *
   * @param data data
   * @return byte data
   */
  byte[] serialize(Object data);

  /**
   * deserialize data
   *
   * @param <T> type
   * @param data data
   * @param type type
   * 
   * @return deserialized response
   */
  <T> T deserialize(byte[] data, Type type);
}
