/** */
package com.github.leeyazhou.akshare4j.util.http;

import java.io.Closeable;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author leeyazhou */
public class CloseUtil {
  private static final Logger logger = LoggerFactory.getLogger(CloseUtil.class);

  public static void close(Closeable close) {
    if (close != null) {
      try {
        close.close();
      } catch (IOException e) {
        logger.error("关闭" + close + "异常", e);
      }
    }
  }
}
