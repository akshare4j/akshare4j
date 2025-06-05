package com.github.leeyazhou.akshare4j.util.http;

import java.io.Serializable;
import java.nio.charset.Charset;
import org.apache.hc.core5.util.CharArrayBuffer;

/**
 * 
 * @author leeyazhou
 *
 */
public enum HttpContentType implements Serializable {

  /**
   * application/atom+xml
   */
  APPLICATION_ATOM_XML("application/atom+xml", Charset.forName(HttpConstants.CHARSET_UTF8)),

  /**
   * application/x-www-form-urlencoded
   */
  APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded", Charset.forName(HttpConstants.CHARSET_UTF8)),

  /**
   * application/json
   */
  APPLICATION_JSON("application/json", Charset.forName(HttpConstants.CHARSET_UTF8)),

  /**
   * application/octet-stream
   */
  APPLICATION_OCTET_STREAM("application/octet-stream", (Charset) null),

  /**
   * application/soap+xml
   */
  APPLICATION_SOAP_XML("application/soap+xml", Charset.forName(HttpConstants.CHARSET_UTF8)),

  /**
   * application/svg+xml
   */
  APPLICATION_SVG_XML("application/svg+xml", Charset.forName(HttpConstants.CHARSET_UTF8)),

  /**
   * application/xhtml+xml
   */
  APPLICATION_XHTML_XML("application/xhtml+xml", Charset.forName(HttpConstants.CHARSET_UTF8)),

  /**
   * application/xml
   */
  APPLICATION_XML("application/xml", Charset.forName(HttpConstants.CHARSET_UTF8)),

  /**
   * multipart/form-data
   */
  MULTIPART_FORM_DATA("multipart/form-data", Charset.forName(HttpConstants.CHARSET_UTF8)),

  /**
   * text/html
   */
  TEXT_HTML("text/html", Charset.forName(HttpConstants.CHARSET_UTF8)),

  /**
   * text/plain
   */
  TEXT_PLAIN("text/plain", Charset.forName(HttpConstants.CHARSET_UTF8)),

  /**
   * text/xml
   */
  TEXT_XML("text/xml", Charset.forName(HttpConstants.CHARSET_UTF8));



  private final String mimeType;
  private final Charset charset;

  private HttpContentType(final String mimeType) {
    this(mimeType, Charset.forName(HttpConstants.CHARSET_UTF8));
  }

  private HttpContentType(final String mimeType, final Charset charset) {
    this.mimeType = mimeType;
    this.charset = charset;
  }


  public String getMimeType() {
    return this.mimeType;
  }

  public Charset getCharset() {
    return this.charset;
  }

  @Override
  public String toString() {
    final CharArrayBuffer buf = new CharArrayBuffer(64);
    buf.append(this.mimeType);
    if (this.charset != null) {
      buf.append("; charset=");
      buf.append(this.charset.name());
    }
    return buf.toString();
  }

  public String toHeaderValue() {
    final CharArrayBuffer buf = new CharArrayBuffer(64);
    buf.append(this.mimeType);
    if (this.charset != null) {
      buf.append("; charset=");
      buf.append(this.charset.name());
    }
    return buf.toString();
  }


}
