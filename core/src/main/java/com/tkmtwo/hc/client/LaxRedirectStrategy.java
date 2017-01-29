/*
 *
 * Copyright 2017 Tom Mahaffey
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

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultRedirectStrategy;

/**
 *
 *
 */
public class LaxRedirectStrategy
  extends DefaultRedirectStrategy {
  
  /**
   * Redirectable methods.
   */
  private static final String[] REDIRECT_METHODS = new String[] {
    HttpGet.METHOD_NAME,
    HttpPost.METHOD_NAME,
    HttpPut.METHOD_NAME,
    HttpHead.METHOD_NAME,
    HttpDelete.METHOD_NAME
  };
  
  @Override
  protected boolean isRedirectable(final String method) {
    for (final String m: REDIRECT_METHODS) {
      if (m.equalsIgnoreCase(method)) {
        return true;
      }
    }
    return false;
  }
  
}
