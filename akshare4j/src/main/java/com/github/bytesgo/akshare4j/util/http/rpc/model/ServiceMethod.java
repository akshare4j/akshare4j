/**
 * 
 */
package com.github.bytesgo.akshare4j.util.http.rpc.model;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.alibaba.fastjson2.JSON;

/**
 * @author leeyazhou
 *
 */
public class ServiceMethod {

  private Method method;

  private Object instance;

  private Type paramType;
  private final Type resultType;

  private String serviceName;
  private String actionName;

  public ServiceMethod(Method method) {
    this(method, null);
  }

  public ServiceMethod(Method method, Object instance) {
    this.method = method;
    this.instance = instance;
    if (method.getParameterCount() > 0) {
      paramType = method.getParameterTypes()[0];
    }
    this.resultType = method.getGenericReturnType();
  }

  public Object invoke(Object request) throws Exception {
    if (method.getParameterCount() == 0) {
      return method.invoke(instance);
    }
    return method.invoke(instance, request);
  }

  public Object toParam(String requestBody) {
    if (paramType == null) {
      return null;
    }

    if (paramType.getClass().isArray()) {
      return JSON.parseArray(requestBody, new Type[] {paramType});
    }

    return JSON.parseObject(requestBody, paramType);
  }

  public Type getParamType() {
    return paramType;
  }

  public Type getResultType() {
    return resultType;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public void setActionName(String actionName) {
    this.actionName = actionName;
  }

  public String getServiceName() {
    return serviceName;
  }

  public String getActionName() {
    return actionName;
  }

  public String toOperationType() {
    return serviceName + actionName;
  }
}
