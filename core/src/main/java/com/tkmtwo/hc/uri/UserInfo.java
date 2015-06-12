package com.tkmtwo.hc.uri;

import static com.google.common.base.TkmTwoStrings.isBlank;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.escape.Escaper;
import com.google.common.net.PercentEscaper;
import java.io.Serializable;
import java.util.List;

/**
 * Small, immutable class to represent the "user-info" portion of a URI.
 *
 *
 */
public final class UserInfo
  implements Serializable {
  
  private static final long serialVersionUID = 1L;
  private static final Splitter colonSplitter = Splitter.on(':');
  private static final Escaper userInfoEscaper =
    new PercentEscaper("-._~"                //RFC 3986 "unreserved"
                       + "!$&'()*+,;="       //RFC 3986 "sub-delims"
                       + ":",                //RFC 3986 OK for user-info portion
                       false);

  

  private final String userName;
  private final String password;

  private UserInfo() {
    userName = null;
    password = null;
  }

  private UserInfo(String u, String p) {
    userName = u;
    password = p;
  }
  
  
  
  public static UserInfo of(UserInfo ui) {
    return new UserInfo(ui.getUserName(), ui.getPassword());
  }
  
  
  public static UserInfo of(String s) {
    if (isBlank(s)) {
      return new UserInfo();
    }
    
    List<String> ss = colonSplitter.splitToList(s);
    if (ss.size() > 2) {
      throw new IllegalArgumentException(String.format("String %s has more than two tokens.", s));
    } else if (ss.size() == 2) {
      return new UserInfo(ss.get(0), ss.get(1));
    } else if (ss.size() == 1) {
      return new UserInfo(ss.get(0), null);
    }
    
    return new UserInfo();
  }

    
  public static UserInfo of(String u, String p) {
    return new UserInfo(u, p);
  }

  public UserInfo withUserName(String u) {
    return new UserInfo(u, getPassword());
  }
  public UserInfo withPassword(String p) {
    return new UserInfo(getUserName(), p);
  }
  
  public String getUserName() { return userName; }
  public String getPassword() { return password; }
  
  
  
  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (!(o instanceof UserInfo)) { return false; }
    
    UserInfo impl = (UserInfo) o;
    
    return
      Objects.equal(getUserName(), impl.getUserName())
      && Objects.equal(getPassword(), impl.getPassword());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getUserName(), getPassword());
  }
  


  @Override
  public String toString() {
    return String.format("%s:%s", getUserName(), "*");
  }



    
  
}
