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

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;


/**
 *
 *
 */
public class HttpClients {

  public static final int CONN_TIMEOUT = 60 * 1000;
  public static final int CONN_MAX_TOTAL = 20;
  public static final int CONN_MAX_PER_ROUTE = 2;


  public static HttpClient build(String uname, String passwd) {
    return build(uname, passwd,
                 CONN_TIMEOUT, CONN_MAX_TOTAL, CONN_MAX_PER_ROUTE);
  }


  public static HttpClient build(String uname, String passwd,
                                 int connTimeout, int connMaxTotal, int connMaxPerRoute) {
    
    PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager();
    pcm.setMaxTotal((connMaxTotal > 0) ? connMaxTotal : CONN_MAX_TOTAL);
    pcm.setDefaultMaxPerRoute((connMaxPerRoute > 0) ? connMaxPerRoute : CONN_MAX_PER_ROUTE);

    CredentialsProvider cp = new BasicCredentialsProvider();
    UsernamePasswordCredentials creds = new UsernamePasswordCredentials(uname, passwd);
    cp.setCredentials(AuthScope.ANY, creds);

    HttpClient hc = HttpClientBuilder
      .create()
      .setConnectionManager(pcm)
      .setDefaultCredentialsProvider(cp)
      .build();
    return hc;
  }
  
  
  
}

    
