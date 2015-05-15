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
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;

import com.google.common.base.Joiner;
import java.net.URI;
//import java.util.Collection;
//import java.util.List;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


/**
 *
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class URIBuilderTest {

  private String baseUri;
  
  private Param scalarParam;

  private Param listParam;
  private Param joinedListParam;
  
  private Param setParam;
  private Param joinedSetParam;
  
  @Before
  public void setUp() {
    baseUri = "scheme://host/path";
    
    scalarParam = new Param("scalar", ParamType.SCALAR);

    setParam = new Param("set", ParamType.SET);
    joinedSetParam = new Param("set", ParamType.SET, Joiner.on(',').skipNulls());

    listParam = new Param("list", ParamType.LIST);
    joinedListParam = new Param("list", ParamType.LIST, Joiner.on(',').skipNulls());

  }


  @Test
  public void test0000NoParams() {
    URI uri = URIBuilder.fromUri(baseUri).build();
    assertEquals(baseUri,
                 uri.toString());
    
  }

  @Test
  public void test0010Plain() {
    URI uri =
      URIBuilder
      .fromUri(baseUri)
      .withParam("name", "moe")
      .withParam("name", "larry")
      .withParam("name", "curly")
      .withParam("name", "curly joe")
      .build();

    assertEquals(baseUri + "?name=moe&name=larry&name=curly&name=curly+joe",
                 uri.toString());
  }

  @Test
  public void test0020Scalar() {
    Params ps = new Params();
    ps.put(scalarParam, "moe");
    
    assertEquals(baseUri + "?scalar=moe",
                 URIBuilder
                 .fromUri(baseUri)
                 .withParams(ps)
                 .build().toString());
    
    assertEquals(baseUri + "?scalar=larry",
                 URIBuilder
                 .fromUri(baseUri)
                 .withParams(ps)
                 .withParam("scalar", "larry")
                 .build().toString());
    
    assertEquals(baseUri + "?scalar=moe",
                 URIBuilder
                 .fromUri(baseUri)
                 .withParams(ps)
                 .withParam("scalar", "larry")
                 .withParam("scalar", "moe")
                 .build().toString());
    
    
    
    //More inline
    assertEquals(baseUri + "?scalar=moe",
                 URIBuilder
                 .fromUri(baseUri)
                 .withParam(scalarParam, "moe")
                 .build().toString());
    assertEquals(baseUri + "?scalar=larry",
                 URIBuilder
                 .fromUri(baseUri)
                 .withParam(scalarParam, "moe")
                 .withParam(scalarParam, "larry")
                 .build().toString());
    assertEquals(baseUri + "?scalar=moe",
                 URIBuilder
                 .fromUri(baseUri)
                 .withParam(scalarParam, "moe")
                 .withParam(scalarParam, "larry")
                 .withParam(scalarParam, "moe")
                 .build().toString());
    
    
    
  }
  
  @Test
  public void test0030List() {
    Params ps = new Params();
    ps.put(listParam, "moe");
    ps.put(listParam, "larry");

    
    assertEquals(baseUri + "?list=moe&list=larry",
                 URIBuilder
                 .fromUri(baseUri)
                 .withParams(ps)
                 .build().toString());
    assertEquals(baseUri + "?list=moe&list=larry&list=curly",
                 URIBuilder
                 .fromUri(baseUri)
                 .withParams(ps)
                 .withParam(listParam, "curly")
                 .build().toString());

    assertEquals(baseUri + "?list=moe%2Clarry%2Ccurly%2Ccurly+joe",
                 URIBuilder
                 .fromUri(baseUri)
                 .withParam("list", "moe")
                 .withParam("list", "larry")
                 .withParam("list", "curly")
                 .withParam(joinedListParam, "curly joe")
                 .build().toString());
  }

  @Test
  public void test0040Set() {
    Params ps = new Params();
    ps.put(setParam, "moe");
    ps.put(setParam, "larry");
    ps.put(setParam, "larry");
    
    assertEquals(baseUri + "?set=moe&set=larry",
                 URIBuilder
                 .fromUri(baseUri)
                 .withParams(ps)
                 .build().toString());
    assertEquals(baseUri + "?set=moe&set=larry&set=curly",
                 URIBuilder
                 .fromUri(baseUri)
                 .withParams(ps)
                 .withParam(setParam, "curly")
                 .withParam(setParam, "curly")
                 .withParam(setParam, "curly")
                 .build().toString());

    assertEquals(baseUri + "?set=moe%2Clarry%2Ccurly%2Ccurly+joe",
                 URIBuilder
                 .fromUri(baseUri)
                 .withParam("set", "moe")
                 .withParam("set", "moe")
                 .withParam("set", "larry")
                 .withParam("set", "larry")
                 .withParam("set", "curly")
                 .withParam("set", "curly")
                 .withParam(joinedSetParam, "curly joe")
                 .build().toString());
  }

    
    
  @Test
  public void test0050Mix() {
    Params ps = new Params();
    assertTrue(ps.put(scalarParam, "red"));
    assertTrue(ps.put(scalarParam, "green"));
    assertTrue(ps.put(scalarParam, "blue"));

    assertTrue(ps.put(joinedSetParam, "moe"));
    assertTrue(ps.put(joinedSetParam, "larry"));
    assertTrue(ps.put(joinedSetParam, "curly"));
    assertTrue(ps.put(joinedSetParam, "moe"));
    assertTrue(ps.put(joinedSetParam, "larry"));
    assertTrue(ps.put(joinedSetParam, "curly"));
    assertTrue(ps.put(joinedSetParam, "curly"));
    assertTrue(ps.put(joinedSetParam, "curly"));

    assertTrue(ps.put(listParam, "one"));
    assertTrue(ps.put(listParam, "two"));
    assertTrue(ps.put(listParam, "three"));

    URI uri = URIBuilder.fromUri(baseUri).withParams(ps).build();
    assertEquals("scheme://host/path?scalar=blue&set=moe%2Clarry%2Ccurly&list=one&list=two&list=three",
                 uri.toString());
  }
  
  
  
  
}
