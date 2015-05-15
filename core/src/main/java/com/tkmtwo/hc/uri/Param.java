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

import com.google.common.base.Joiner;
import java.util.Objects;


/**
 *
 *
 */
public final class Param {
  
  private String name;
  private Joiner joiner;
  private ParamType paramType = ParamType.SET;

  public Param(String s) {
    name = s;
    joiner = null;
  }
  
  public Param(String s, Joiner j) {
    name = s;
    joiner = j;
  }
  public Param(String s, ParamType pt) {
    name = s;
    paramType = pt;
    joiner = null;
  }
  public Param(String s, ParamType pt, Joiner j) {
    name = s;
    paramType = pt;
    joiner = j;
  }
  
  public String getName() {
    return name;
  }
  public Joiner getJoiner() {
    return joiner;
  }
  public ParamType getParamType() {
    return paramType;
  }

  
  /*  
  public static Collection<String> removeAll(ListMultimap<String, String> lm) {
    return lm.removeAll(getName());
  }
  public static boolean remove(ListMultimap<String, String> lm, String value) {
    return lm.remove(getName(), value);
  }
  public static boolean put(ListMultimap<String, String> lm, String value) {
    return lm.put(getName(), value);
  }
  */
  
  //public static List<String> getValues(ListMultimap<String, String> lm) {
  
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Param)) {
      return false;
    }
    
    Param impl = (Param) o;
    
    return Objects.equals(getName(), impl.getName());
  }
  
  
  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }
  
}
