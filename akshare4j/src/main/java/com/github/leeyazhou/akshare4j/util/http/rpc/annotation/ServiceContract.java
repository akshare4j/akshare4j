package com.github.leeyazhou.akshare4j.util.http.rpc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceContract {

  /**
   * 服务路径
   * 
   * @return value
   */
  String value();

  /**
   * contentType
   * 
   * @return contentType
   */
  String contentType() default "";

  /**
   * 描述
   * 
   * @return desc
   */
  String desc() default "";
}
