/**
 * 
 */
package com.github.akshare4j.util.http.rpc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.akshare4j.util.http.rpc.model.ServiceMethod;

/**
 * @author leeyazhou
 *
 */
public class ServiceContractCache {

  private static final Logger logger = LoggerFactory.getLogger(ServiceContractCache.class);
  private static final ConcurrentMap<String, ServiceMethod> serviceMethodCache = new ConcurrentHashMap<>();

  public static void addServiceMethod(ServiceMethod serviceMethod) {
    logger.info("add service: " + serviceMethod.toOperationType());
    serviceMethodCache.put(serviceMethod.toOperationType(), serviceMethod);
  }

  public static ServiceMethod findServiceMethod(String cacheKey) {
    return serviceMethodCache.get(cacheKey);
  }
}
