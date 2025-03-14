package com.github.leeyazhou.akshare4j.util.http.rpc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.github.leeyazhou.akshare4j.util.http.HttpMethod;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceAction {

  /**
   * 服务路径
   * 
   * @return value
   */
  String value();

  HttpMethod method() default HttpMethod.POST;

  /**
   * 描述
   * 
   * @return desc
   */
  String desc() default "";
}
