package com.tkmtwo.hc.uri;

import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

//import java.net.URI;
import org.junit.Test;



/**
 *
 *
 */
public class UserInfoTest {

  @Test
  public void testValid()
  {
    UserInfo ui = UserInfo.of("user:pass");
    assertEquals("user", ui.getUserName());
    assertEquals("pass", ui.getPassword());
    assertEquals("user:*", ui.toString());
  }
  
  @Test
  public void testDefault()
  {
    UserInfo ui = UserInfo.of((String) null);
    assertNull(ui.getUserName());
    assertNull(ui.getPassword());
  }

  @Test
  public void testNull()
  {
    String s = null;
    UserInfo ui = UserInfo.of(s);
    assertNull(ui.getUserName());
    assertNull(ui.getPassword());
  }

  @Test
  public void testBlank()
  {
    String s = "";
    UserInfo ui = UserInfo.of(s);
    assertNull(ui.getUserName());
    assertNull(ui.getPassword());
  }

  
  @Test
  public void testNoColon()
  {
    UserInfo ui = UserInfo.of("user");
    assertEquals("user", ui.getUserName());
    assertNull(ui.getPassword());
  }
  
    
  

}
