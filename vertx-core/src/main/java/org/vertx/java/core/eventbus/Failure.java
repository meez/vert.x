/*
 * Copyright 2011-2013 the original author or authors.
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
 */
package org.vertx.java.core.eventbus;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Represents a failure on the event bus.<p>
 *
 * @author <a href="http://github.com/petermd">Peter McDonnell</a>
 */
public class Failure {

  public final static int UNKNOWN = 0;
  // Request 
  public final static int BAD_SYNTAX = 400;
  public final static int FORBIDDEN = 403;
  public final static int NOT_FOUND = 404;
  public final static int REQUEST_TIMEOUT = 408;
  public final static int PRECONDITION_FAILED = 412;
  public final static int CALM_DOWN = 420;
  // Server
  public final static int INTERNAL_ERROR = 500;
  public final static int NOT_IMPLEMENTED = 501;
  public final static int SERVICE_UNAVAILABLE = 503;
  public final static int GATEWAY_TIMEOUT = 504;

  public final int code;

  public final String reason;

  public final String trace;

  public Failure(int code, String reason) {
    assert (reason != null);

    this.code = code;
    this.reason = reason;
    this.trace = "";
  }

  public Failure(int code, String reason, String trace) {
    assert (reason != null);
    assert (trace != null);

    this.code = code;
    this.reason = reason;
    this.trace = trace;
  }

  public Failure(int code, Throwable cause) {
    assert (cause != null);

    this.code = code;
    this.reason = cause.toString();

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    cause.printStackTrace(pw);

    this.trace = sw.toString();
  }

  public String toString() {
    return String.format("ERROR[%d]", code);
  }
}
