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


import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Iterator;


/**
 *
 *
 */
public class URIBuilder {
  
  private final String baseUri;
  private Params params;
  
  private String getBaseUri() {
    return baseUri;
  }
  private Params getParams() {
    if (params == null) {
      params = new Params();
    }
    
    return params;
  }



  
  private URIBuilder(String base) {
    baseUri = base;
    params = new Params();
  }
  
  public static URIBuilder fromUri(String base) {
    return new URIBuilder(base);
  }
  
  public static URIBuilder fromUri(URI baseUri) {
    return new URIBuilder(baseUri.toString());
  }

  public URIBuilder withParam(String n, String v) {
    getParams().put(n, v);
    return this;
  }
  public URIBuilder withParam(Param p, String v) {
    getParams().put(p, v);
    return this;
  }



  public URIBuilder withParams(Params p) {
    getParams().putAll(p);
    return this;
  }
  
  public URI build() {
    try {
      StringBuilder sb = new StringBuilder();

      for (Iterator<String> paramKeys = getParams().keySet().iterator(); paramKeys.hasNext();) {
        String paramKey = paramKeys.next();
        
        for (Iterator<String> paramValues = getParams().getValues(paramKey).iterator(); paramValues.hasNext();) {
          String paramValue = paramValues.next();
          sb.append(formEncode(paramKey)).append("=").append(formEncode(paramValue));
          if (paramValues.hasNext()) {
            sb.append("&");
          }
        }
        
        if (paramKeys.hasNext()) {
          sb.append("&");
        }
        
      }
      
      String queryDelimiter = "?";
      if (URI.create(getBaseUri()).getQuery() != null) {
        queryDelimiter = "&";
      }
      return new URI(getBaseUri() + (sb.length() > 0 ? queryDelimiter + sb.toString() : ""));
    } catch (URISyntaxException e) {
      throw new URIBuilderException("Unable to build URI: Bad URI syntax", e);
    }
  }
  
  private String formEncode(String data) {
    try {
      return URLEncoder.encode(data, "UTF-8");
    } catch (UnsupportedEncodingException uee) {
      throw new IllegalStateException(uee);
    }
  }
}
