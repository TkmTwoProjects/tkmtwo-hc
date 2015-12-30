/*
 *
 * Copyright 2015 Tom Mahaffey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.tkmtwo.hc.client;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Properties;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.web.client.RestTemplate;

/**
 *
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class RestTemplatesTest {
  
  private Properties properties = new Properties();
  
  @Before
  public void setUp() throws Exception {
    properties = new Properties();

    try {
      //properties.load(ClassLoader.getSystemResourceAsStream("com/tkmtwo/hc/client/RestTemplatesTesthc-test.properties"));
      properties.load(ClassLoader.getSystemResourceAsStream("com/tkmtwo/hc/client/RestTemplatesTest.properties"));
    } catch (Exception ex) {
      fail("Error reading properties: " + ex.getMessage());
    }
  }
  
  
  
  @Test
  public void test000String() {
    RestTemplate rt = RestTemplates.build(properties.getProperty("uname"),
                                          properties.getProperty("passwd"));
    
    String locationResponseBody = rt.getForObject(properties.getProperty("locationUri"),
                                                  String.class);
    System.out.println("I got: " + locationResponseBody);
  }

}
