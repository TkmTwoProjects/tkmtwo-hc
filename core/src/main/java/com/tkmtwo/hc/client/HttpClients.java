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

import static com.google.common.base.TkmTwoJointers.COMMA_SPLITTER;
import static com.google.common.base.TkmTwoStrings.isBlank;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import java.util.List;
import javax.net.ssl.SSLContext;
import org.apache.http.client.config.RequestConfig;

/**
 *
 *
 */
public class HttpClients {
  
  private static final Logger logger = LoggerFactory.getLogger(HttpClients.class);

  public static final int CONN_TIMEOUT = 60 * 1000;
  public static final int CONN_REQUEST_TIMEOUT = 5 * 1000;
  public static final int CONN_SOCKET_TIMEOUT = 30 * 1000;

  public static final int CONN_MAX_TOTAL = 200;
  public static final int CONN_MAX_PER_ROUTE = 100;


  public static HttpClient build(String uname, String passwd) {
    
    return build(uname,                      //String uname,
                 passwd,                     //String passwd,
                 null,                       //String ntlmDomain,
                 null,                       //String ntlmWorkstation,
                 CONN_TIMEOUT,               //int connectTimeout,
                 CONN_REQUEST_TIMEOUT,       //int connectionRequestTimeout,
                 CONN_SOCKET_TIMEOUT,        //int socketTimeout,
                 CONN_MAX_TOTAL,             //int maxConnections,
                 CONN_MAX_PER_ROUTE,         //int maxConnectionsPerRoute,
                 null,                       //String sslProtocols,
                 null,                       //String proxyHost,
                 -1);                        //int proxyPort
    
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
  
  
  
  public static HttpClient build(String uname, String passwd,
                                 String ntlmDomain, String ntlmWorkstation,
                                 int connectTimeout, int connectionRequestTimeout, int socketTimeout,
                                 int maxConnections, int maxConnectionsPerRoute,
                                 String sslProtocols,
                                 String proxyHost, int proxyPort) {
    
    logger.trace("Building BASIC HttpClient.");
    
    SSLContext sslContext = SSLContexts.createSystemDefault();
    
    SSLConnectionSocketFactory sslSocketFactory = null;
    if (isBlank(sslProtocols)) {
      logger.trace("Using default SSLConnectionSocketFactory.");
      sslSocketFactory = new SSLConnectionSocketFactory(sslContext);
    } else {
      logger.trace("Using default SSLConnectionSocketFactory with protocols '{}'.", sslProtocols);
      List<String> protoList = COMMA_SPLITTER.splitToList(sslProtocols);
      String[] protoArray = protoList.toArray(new String[protoList.size()]);
      sslSocketFactory = 
        new SSLConnectionSocketFactory(sslContext,
                                       //new String[]{"TLSv1"},
                                       protoArray,
                                       null,
                                       new NoopHostnameVerifier());
    }
    
    Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
      .register("http", PlainConnectionSocketFactory.INSTANCE)
      .register("https", sslSocketFactory)
      .build();
    
    //
    // HttpClientConnectionManager
    //
    HttpClientConnectionManager connectionManager = null;
    PoolingHttpClientConnectionManager pccm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
    pccm.setMaxTotal(CONN_MAX_TOTAL);
    pccm.setDefaultMaxPerRoute(CONN_MAX_PER_ROUTE);
    connectionManager = pccm;
    
    
    //
    //CredentialsProvider
    //
    CredentialsProvider credentialsProvider = null;
    if (!isBlank(uname)) {
      credentialsProvider = new BasicCredentialsProvider();
      
      if (!isBlank(ntlmWorkstation)
          || !isBlank(ntlmDomain)) {
        NTCredentials ntc = new NTCredentials(uname,
                                              passwd,
                                              ntlmWorkstation,
                                              ntlmDomain);
        logger.trace("Adding NTCredentials '{}' to credentials.", ntc.toString());
        credentialsProvider.setCredentials(AuthScope.ANY, ntc);
        
      } else {
        UsernamePasswordCredentials upc = new UsernamePasswordCredentials(uname,
                                                                          passwd);
        logger.trace("Adding UsernamePasswordCredentials '{}' to credentials.", upc.toString());
        credentialsProvider.setCredentials(AuthScope.ANY, upc);
      }
    }
    
    //
    // Timeouts
    //
    RequestConfig requestConfig =
      RequestConfig
      .custom()
      .setConnectTimeout(connectTimeout)
      .setConnectionRequestTimeout(connectionRequestTimeout)
      .setSocketTimeout(socketTimeout)
      .build();
    
    //
    // Build it up
    //
    HttpClientBuilder hcBuilder = HttpClientBuilder.create();
    hcBuilder.setConnectionManager(connectionManager);
    hcBuilder.setRedirectStrategy(new LaxRedirectStrategy());
    hcBuilder.setDefaultRequestConfig(requestConfig);
    
    if (credentialsProvider != null) {
      logger.trace("Adding credentials provider...adding '{}'", credentialsProvider);
      hcBuilder.setDefaultCredentialsProvider(credentialsProvider);
    }
    
    if (!isBlank(proxyHost)) {
      HttpHost httpProxyHost = new HttpHost(proxyHost,
                                            proxyPort);
      hcBuilder.setProxy(httpProxyHost);
      logger.trace("HTTP proxy set to {}:{}", proxyHost, String.valueOf(proxyPort));
    }
    
    
    return hcBuilder.build();
  }
  
  
  
  
}


