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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multiset;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 *
 */
public class Params {
  private Set<Param> params;
  private ListMultimap<String, String> valueMap;

  public ListMultimap<String, String> getValueMap() {
    if (valueMap == null) {
      valueMap = ArrayListMultimap.create();
    }

    return valueMap;
  }

  public void putAll(Params ps) {
    if (ps == null) {
      return;
    }
    for (Param p : ps.getParams()) {
      getParams().add(p);
    }
    getValueMap().putAll(ps.getValueMap());
  }

  public Set<Param> getParams() {
    if (params == null) {
      params = new HashSet<Param>();
    }
    
    return params;
  }
  public Param getParam(String n) {
    for (Param p : getParams()) {
      if (p.getName().equals(n)) {
        return p;
      }
    }

    return null;
  }


  public Collection<String> removeAll(String n) {
    return getValueMap().removeAll(n);
  }
  public Collection<String> removeAll(Param p) {
    getParams().add(p);
    return removeAll(p.getName());
  }
  
  
  
  
  public boolean remove(String n, String v) {
    boolean b = false;
    while (getValueMap().remove(n, v)) {
      b = true;
    }
    
    return b;
  }
  
  public boolean remove(Param p, String v) {
    getParams().add(p);
    return remove(p.getName(), v);
  }

  
  public boolean putAll(String n, Iterable<String> vs) {
    return getValueMap().putAll(n, vs);
  }
  public boolean put(String n, String v) {
    return getValueMap().put(n, v);
  }
  public boolean put(String n, boolean b) {
    return put(n, String.valueOf(b));
  }
  public boolean put(String n, int i) {
    return put(n, String.valueOf(i));
  }



  public boolean putAll(Param p, Iterable<String> vs) {
    getParams().add(p);
    return putAll(p.getName(), vs);
  }
  public boolean put(Param p, String v) {
    getParams().add(p);
    //return getValueMap().put(p.getName(), v);
    return put(p.getName(), v);
  }
  public boolean put(Param p, boolean b) {
    return put(p, String.valueOf(b));
  }
  public boolean put(Param p, int i) {
    return put(p, String.valueOf(i));
  }



  
  public Set<String> keySet() {
    return getValueMap().keySet();
  }

  
  protected List<String> getValuesInternal(String n) {
    return getValueMap().get(n);
  }
  
  
  public List<String> getValues(String n) {
    Param p = getParam(n);
    if (p == null) {
      return getValueMap().get(n);
    }
    return getValues(p);
  }
  public List<String> getValues(Param p) {
    List<String> rawValues = getValuesInternal(p.getName());

    int rawValuesSize = rawValues.size();
    if (rawValuesSize == 0) {
      return rawValues;
    }

    ImmutableList.Builder<String> ilb = ImmutableList.<String>builder();
    
    switch (p.getParamType()) {
      
    case SCALAR:
      ilb.add(rawValues.get(rawValuesSize - 1));
      break;
      
    case LIST:
      if (p.getJoiner() == null) {
        ilb.addAll(rawValues);
      } else {
        ilb.add(p.getJoiner().join(rawValues));
      }
      break;
      
    case SET:
      Multiset<String> mss = LinkedHashMultiset.create();
      mss.addAll(rawValues);
      
      if (p.getJoiner() == null) {
        for (String s : mss.elementSet()) {
          ilb.add(s);
        }
      } else {
        ilb.add(p.getJoiner().join(mss.elementSet()));
      }
      break;
      
    default:
      ;
      
    }
    
    
    return ilb.build();
  }
  
  
  
}
