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

package com.tkmtwo.hc.uri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;

import com.google.common.base.Joiner;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


/**
 *
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class ParamsTest {
  
  private Param scalarParam;

  private Param listParam;
  private Param joinedListParam;
  private Param setParam;
  private Param joinedSetParam;
  
  @Before
  public void setUp() {
    scalarParam = new Param("scalar", ParamType.SCALAR);

    setParam = new Param("set", ParamType.SET);
    joinedSetParam = new Param("set", ParamType.SET, Joiner.on(',').skipNulls());

    listParam = new Param("list", ParamType.LIST);
    joinedListParam = new Param("list", ParamType.LIST, Joiner.on(',').skipNulls());

  }


  @Test
  public void test000Plain() {
    Params ps = new Params();
    ps.put("colors", "red");
    ps.put("colors", "green");
    ps.put("colors", "blue");
    ps.put("stooges", "moe");
    ps.put("stooges", "larry");
    ps.put("stooges", "curly");
    ps.put("stooges", "curlyjoe");
    
    List<String> colors = ps.getValues("colors");
    assertNotNull(colors);
    assertEquals(3, colors.size());
    assertEquals("red", colors.get(0));
    assertEquals("green", colors.get(1));
    assertEquals("blue", colors.get(2));

    List<String> stooges = ps.getValues("stooges");
    assertNotNull(stooges);
    assertEquals(4, stooges.size());
    assertEquals("moe", stooges.get(0));
    assertEquals("larry", stooges.get(1));
    assertEquals("curly", stooges.get(2));
    assertEquals("curlyjoe", stooges.get(3));

    //remove curly, everyone else stays
    assertTrue(ps.remove("stooges", "curly"));
    stooges = ps.getValues("stooges");
    assertNotNull(stooges);
    assertEquals(3, stooges.size());
    assertEquals("moe", stooges.get(0));
    assertEquals("larry", stooges.get(1));
    assertEquals("curlyjoe", stooges.get(2));

    Collection<String> firedStooges = ps.removeAll("stooges");
    assertNotNull(firedStooges);
    assertEquals(3, firedStooges.size());
    assertTrue(ps.getValues("stooges").isEmpty());

  }
  
  
  @Test
  public void test0010Scalar() {
    Params ps = new Params();
    assertTrue(ps.put(scalarParam, "red"));
    assertTrue(ps.put(scalarParam, "green"));
    assertTrue(ps.put(scalarParam, "blue"));
    
    List<String> colors = ps.getValues(scalarParam);
    assertNotNull(colors);
    assertEquals(1, colors.size());
    assertEquals("blue", colors.get(0));
  }
    

  
  
  
  
  @Test
  public void test0020List() {
    Params ps = new Params();
    assertTrue(ps.put(listParam, "red"));
    assertTrue(ps.put(listParam, "green"));
    assertTrue(ps.put(listParam, "blue"));
    
    List<String> colors = ps.getValues(listParam);
    assertNotNull(colors);
    assertEquals(3, colors.size());
    
    assertTrue(ps.put(listParam, "bright-red"));
    assertTrue(ps.put(listParam, "bright-green"));
    assertTrue(ps.put(listParam, "bright-blue"));
    
    List<String> plusBrightColors = ps.getValues(listParam);
    assertNotNull(plusBrightColors);
    assertEquals(6, plusBrightColors.size());
    
    
    
    
    
    assertTrue(ps.put(listParam, "red"));
    assertTrue(ps.put(listParam, "green"));
    assertTrue(ps.put(listParam, "blue"));
    assertTrue(ps.put(listParam, "bright-red"));
    assertTrue(ps.put(listParam, "bright-green"));
    assertTrue(ps.put(listParam, "bright-blue"));
    
    List<String> moreColors = ps.getValues(listParam);
    assertNotNull(moreColors);
    assertEquals(12, moreColors.size());
    
  }    
  
  



  @Test
  public void test0021ListJoined() {
    Params ps = new Params();
    assertTrue(ps.put(joinedListParam, "red"));
    assertTrue(ps.put(joinedListParam, "green"));
    assertTrue(ps.put(joinedListParam, "blue"));
    
    List<String> colors = ps.getValues(joinedListParam);
    assertNotNull(colors);
    assertEquals(1, colors.size());
    assertEquals("red,green,blue", colors.get(0));
    
    assertTrue(ps.put(joinedListParam, "bright-red"));
    assertTrue(ps.put(joinedListParam, "bright-green"));
    assertTrue(ps.put(joinedListParam, "bright-blue"));
    
    List<String> plusBrightColors = ps.getValues(joinedListParam);
    assertNotNull(plusBrightColors);
    assertEquals(1, plusBrightColors.size());
    assertEquals("red,green,blue,bright-red,bright-green,bright-blue", plusBrightColors.get(0));
    
    
    
    
    
    
    assertTrue(ps.put(joinedListParam, "red"));
    assertTrue(ps.put(joinedListParam, "green"));
    assertTrue(ps.put(joinedListParam, "blue"));
    assertTrue(ps.put(joinedListParam, "bright-red"));
    assertTrue(ps.put(joinedListParam, "bright-green"));
    assertTrue(ps.put(joinedListParam, "bright-blue"));
    
    List<String> moreColors = ps.getValues(joinedListParam);
    assertNotNull(moreColors);
    assertEquals(1, moreColors.size());
    assertEquals("red,green,blue,bright-red,bright-green,bright-blue,red,green,blue,bright-red,bright-green,bright-blue",
                 moreColors.get(0));



    
    ps.remove(joinedListParam, "red");
    ps.remove(joinedListParam, "bright-green");

    List<String> lessColors = ps.getValues(joinedListParam);
    assertNotNull(lessColors);
    assertEquals(1, lessColors.size());
    assertEquals("green,blue,bright-red,bright-blue,green,blue,bright-red,bright-blue", //red bright-green
                 lessColors.get(0));

    
  }
  
  

  
  
  
  @Test
  public void test0030Set() {
    Params ps = new Params();
    assertTrue(ps.put(setParam, "red"));
    assertTrue(ps.put(setParam, "green"));
    assertTrue(ps.put(setParam, "blue"));
    
    List<String> colors = ps.getValues(setParam);
    assertNotNull(colors);
    assertEquals(3, colors.size());
    
    assertTrue(ps.put(setParam, "bright-red"));
    assertTrue(ps.put(setParam, "bright-green"));
    assertTrue(ps.put(setParam, "bright-blue"));
    
    List<String> plusBrightColors = ps.getValues(setParam);
    assertNotNull(plusBrightColors);
    assertEquals(6, plusBrightColors.size());
    
    
    assertTrue(ps.put(setParam, "red"));
    assertTrue(ps.put(setParam, "green"));
    assertTrue(ps.put(setParam, "blue"));
    assertTrue(ps.put(setParam, "bright-red"));
    assertTrue(ps.put(setParam, "bright-green"));
    assertTrue(ps.put(setParam, "bright-blue"));
  }    



  @Test
  public void test0031SetJoined() {
    Params ps = new Params();
    assertTrue(ps.put(joinedSetParam, "red"));
    assertTrue(ps.put(joinedSetParam, "green"));
    assertTrue(ps.put(joinedSetParam, "blue"));

    List<String> colors = ps.getValues(joinedSetParam);
    assertEquals(1, colors.size());
    assertEquals("red,green,blue", colors.get(0));



    
    assertTrue(ps.put(joinedSetParam, "bright-red"));
    assertTrue(ps.put(joinedSetParam, "bright-green"));
    assertTrue(ps.put(joinedSetParam, "bright-blue"));
    
    List<String> plusBrightColors = ps.getValues(joinedSetParam);
    assertNotNull(plusBrightColors);
    assertEquals(1, plusBrightColors.size());
    assertEquals("red,green,blue,bright-red,bright-green,bright-blue", plusBrightColors.get(0));
    
    
    
    
    ps.remove(joinedSetParam, "red");
    ps.remove(joinedSetParam, "bright-green");

    List<String> lessColors = ps.getValues(joinedSetParam);
    assertNotNull(lessColors);
    assertEquals(1, lessColors.size());
    assertEquals("green,blue,bright-red,bright-blue",
                 lessColors.get(0));




  }    

  


  
  
}
