package org.vertx.java.core;

import org.vertx.java.core.eventbus.Failure;

/** Base-class implementation of HandlerEx <p>
 *
 * @author <a href="http://github.com/petermd">Peter McDonnell</a>
 */
public abstract class ReplyHandler<T> implements HandlerEx<T> {
  
  private final Integer timeout;
  
  public ReplyHandler() {
    this.timeout=null;
  }
  
  public ReplyHandler(int timeout) {
    this.timeout=timeout;
  }

  public Integer timeout() {
    return this.timeout;
  }

  public void fail(Failure fail) {
    // Override to handle failures
  }

  public abstract void handle(T event);
}
