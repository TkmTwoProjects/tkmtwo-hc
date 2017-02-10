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


import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 *
 *
 *
 */
public class RestTemplates {
  
  
  public static RestTemplate build(String uname, String passwd) {
    return build(HttpClients.build(uname, passwd));
  }
  
  public static RestTemplate build(String uname, String passwd,
                                   String ntlmDomain, String ntmlWorkstation) {
    return build(HttpClients.build(uname, passwd, ntlmDomain, ntmlWorkstation));
  }
  
  public static RestTemplate build(HttpClient hc) {
    return build(new HttpComponentsClientHttpRequestFactory(hc));
  }
  
  public static RestTemplate build(HttpComponentsClientHttpRequestFactory requestFactory) {
    List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
    return build(requestFactory, messageConverters);
  }
  
  public static RestTemplate build(HttpComponentsClientHttpRequestFactory requestFactory,
                                   List<HttpMessageConverter<?>> messageConverters) {
    RestTemplate rt = null;
    if (messageConverters == null || messageConverters.isEmpty()) {
      rt = new RestTemplate();
    } else {
      rt = new RestTemplate(messageConverters);
    }
    rt.setRequestFactory(requestFactory);
    return rt;
  }

}
