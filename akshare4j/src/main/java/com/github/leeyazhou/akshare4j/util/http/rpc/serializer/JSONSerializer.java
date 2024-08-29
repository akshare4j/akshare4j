/** */
package com.github.leeyazhou.akshare4j.util.http.rpc.serializer;

import java.lang.reflect.Type;

import com.alibaba.fastjson2.JSON;

/** @author leeyazhou */
public class JSONSerializer implements Serializer {

  @Override
  public byte[] serialize(Object data) {
    return JSON.toJSONBytes(data);
  }

  @Override
  public <T> T deserialize(byte[] data, Type type) {
    return JSON.parseObject(data, type);
  }
}
