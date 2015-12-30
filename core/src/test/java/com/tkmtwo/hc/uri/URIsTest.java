package com.tkmtwo.hc.uri;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URI;
import org.junit.Test;


/**
 *
 *
 */
public class URIsTest {


  @Test
  public void testCreate()
  {
    URI arUri = URIs.of("arapi://myaccount:mypassword@MyArHost:1803");
    System.out.println(URIs.confess(arUri));

    UserInfo ui = URIs.getUserInfo(arUri);
    assertEquals("myaccount", ui.getUserName());
    assertEquals("mypassword", ui.getPassword());
  }
  
  @Test
  public void testThis()
    throws Exception
  {
    URI uri = new URI("jdbc:jtds:sqlserver://DbUser:DbPass@DbServerName:1433/DbName");
    System.out.println(URIs.confess(uri));
  } 


  @Test
  public void testFile()
    throws Exception
  {
    System.out.println(URIs.confess(new URI("file://filehost/path/to/file.log")));
    System.out.println(URIs.confess(new URI("file:///path/to/file.log")));
    System.out.println(URIs.confess(new URI("file:file.log")));
    System.out.println(URIs.confess(new URI("file.log")));
  }
  
  @Test
  public void testFileCreate()
    throws Exception
  {
    File f = new File(new URI("file:///C:/tmp/tom.txt"));
    File fTwo = new File(new URI("file:/tom.txt"));
  }



}
