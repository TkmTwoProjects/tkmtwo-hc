package com.tkmtwo.hc.uri;



import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 */
public class URIs {

  public static URI of(String s) {
    URI uri = null;
    try {
      uri = new URI(s);
      return uri;
    } catch (URISyntaxException ex) {
      throw new IllegalArgumentException("String " + s + " is not a valid URI.", ex);
    }
  }
  
  public static UserInfo getUserInfo(URI uri) {
    if (uri == null) { return null; }
    return parseUserInfo(uri.getUserInfo());
  }
  
  public static UserInfo parseUserInfo(String s) {
    return UserInfo.of(s);
  }
  
  public static String confess(URI uri) {
    StringBuffer sb = new StringBuffer();
    sb
      .append(String.format("BEGIN URI: %s%n", uri.toString()))
      .append(String.format("%25s: %s%n", "isOpaque", String.valueOf(uri.isOpaque())))
      .append(String.format("%25s: %s%n", "isAbsolute", String.valueOf(uri.isAbsolute())))
      .append(String.format("%25s: %s%n", "scheme", uri.getScheme()))
      .append(String.format("%25s: %s%n", "scheme-specific-part", uri.getSchemeSpecificPart()))
      .append(String.format("%25s: %s%n", "authority", uri.getAuthority()))
      .append(String.format("%25s: %s%n", "user-info", uri.getUserInfo()))
      .append(String.format("%25s: %s%n", "host", uri.getHost()))
      .append(String.format("%25s: %s%n", "port", String.valueOf(uri.getPort())))
      .append(String.format("%25s: %s%n", "path", uri.getPath()))
      .append(String.format("%25s: %s%n", "query", uri.getQuery()))
      .append(String.format("%25s: %s%n", "fragment", uri.getFragment()))
      .append(String.format("END URI%n"));
    return sb.toString();
  }
      
  
}
