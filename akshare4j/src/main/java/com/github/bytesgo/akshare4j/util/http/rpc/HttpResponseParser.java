/**
 * 
 */
package com.github.bytesgo.akshare4j.util.http.rpc;

import com.github.bytesgo.akshare4j.util.http.HttpResponseBase;
import com.github.bytesgo.akshare4j.util.http.rpc.model.ServiceMethod;

/**
 * @author leeyazhou
 *
 */
@FunctionalInterface
public interface HttpResponseParser {

  Object parse(HttpResponseBase httpResponse, ServiceMethod serviceMethod);
}
