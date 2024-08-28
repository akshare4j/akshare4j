/**
 * 
 */
package com.github.bytesgo.akshare4j.util.http.rpc;

import java.lang.reflect.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.bytesgo.akshare4j.util.http.HttpResponse;
import com.github.bytesgo.akshare4j.util.http.HttpResponseBase;
import com.github.bytesgo.akshare4j.util.http.rpc.model.ServiceMethod;
import com.github.bytesgo.akshare4j.util.http.rpc.serializer.Serializer;

/**
 * @author leeyazhou
 *
 */
public class SimpleHttpResponseParser implements HttpResponseParser {
  private static final Logger logger = LoggerFactory.getLogger(SimpleHttpResponseParser.class);
  private HttpRpcConfig httpRpcConfig;
  private Serializer serializer;

  public SimpleHttpResponseParser(HttpRpcConfig httpRpcConfig, Serializer serializer) {
    this.httpRpcConfig = httpRpcConfig;
    this.serializer = serializer;
  }

  @Override
  public Object parse(HttpResponseBase response, ServiceMethod serviceMethod) {
    HttpResponse httpResponse = (HttpResponse) response;
    if (httpResponse.isOk()) {
      if (Void.class.equals(serviceMethod.getResultType())) {
        return null;
      }
      if (httpRpcConfig.isGatewayType()) {
        JSONObject result = JSON.parseObject(httpResponse.getResponse());
        int code = result.getInteger("code");
        if (code == 1000) {
          String data = result.getString("data");
          if (data == null) {
            return null;
          }
          if (serviceMethod.getResultType() != null && serviceMethod.getResultType().getClass().isArray()) {
            return JSON.parseArray(data, new Type[] {serviceMethod.getResultType()});
          } else {
            return serializer.deserialize(data.getBytes(), serviceMethod.getResultType());
          }
        } else {
          logger.warn("http Rpc request, resposeBody: {}", httpResponse.getResponse());
          return null;
        }
      }
      return serializer.deserialize(httpResponse.getResponse().getBytes(), serviceMethod.getResultType());
    }
    return null;
  }

}
