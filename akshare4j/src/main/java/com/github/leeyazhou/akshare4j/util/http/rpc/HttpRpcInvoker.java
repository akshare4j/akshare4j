/**
 * 
 */
package com.github.leeyazhou.akshare4j.util.http.rpc;

import com.github.leeyazhou.akshare4j.util.http.HttpResponseBase;
import com.github.leeyazhou.akshare4j.util.http.RequestContext;

/**
 * http rpc invoker
 * 
 * @author leeyazhou
 *
 */
public interface HttpRpcInvoker {

  HttpResponseBase doInvoke(RequestContext context, HttpRpcContext rpcContext);
}
