package org.vertx.java.core;

import org.vertx.java.core.eventbus.Failure;

/** Extension of handler that takes failure event and implements a timeout <p>
 *
 * @author <a href="http://github.com/petermd">Peter McDonnell</a>
 */
public interface HandlerEx<T> extends Handler<T> {
  
  /** Return timeout 
   * 
   * @returns timeout in ms or null to use default
   * 
   **/
  Integer timeout();
  
  /** Something has failed */
  void fail(Failure fail);
}
