/*
 * Copyright 2011-2012 the original author or authors.
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

package vertx.tests.core.eventbus;

import org.vertx.java.core.Handler;
import org.vertx.java.core.ReplyHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Failure;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;
import org.vertx.java.testframework.TestUtils;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class LocalClient extends EventBusAppBase {

  private static final Logger log = LoggerFactory.getLogger(LocalClient.class);

  @Override
  public void start() {
    super.start();
  }

  @Override
  public void stop() {
    super.stop();
  }

  protected boolean isLocal() {
    return true;
  }

  public void testPubSub() {
    Buffer buff = TestUtils.generateRandomBuffer(1000);
    data.put("buffer", buff);
    eb.publish("some-address", buff);
  }

  public void testPubSubMultipleHandlers() {
    Buffer buff = TestUtils.generateRandomBuffer(1000);
    eb.send("some-address", buff);
    data.put("buffer", buff);
    eb.publish("some-address", buff);
  }

  public void testPointToPoint() {
    Buffer buff = TestUtils.generateRandomBuffer(1000);
    data.put("buffer", buff);
    Set<String> addresses = vertx.sharedData().getSet("addresses");
    for (String address: addresses) {
      eb.send(address, buff);
    }
  }

  public void testPointToPointRoundRobin() {
    final Buffer buff = TestUtils.generateRandomBuffer(1000);
    data.put("buffer", buff);
    //Each peer should get two messages
    for (int i = 0; i < 8; i++) {
      eb.send("some-address", buff);
    }
  }

  public void testReply() {
    Buffer buff = TestUtils.generateRandomBuffer(1000);
    data.put("buffer", buff);
    Set<String> addresses = vertx.sharedData().getSet("addresses");
    for (final String address: addresses) {
      eb.send(address, buff, new Handler<Message<Buffer>>() {
        public void handle(Message<Buffer> reply) {
          tu.azzert(("reply" + address).equals(reply.body.toString()));
          tu.testComplete();
        }
      });
    }
  }

  public void testBadHandler() {
    Set<String> addresses = vertx.sharedData().getSet("addresses");
    for (final String address: addresses) {
      eb.send(address, "bad", new ReplyHandler<Message<String>>() {
        public void handle(Message<String> reply) {
          // Response will never arrive
          tu.azzert(false);
        }
        public void fail(Failure fail) {
          log.info("Expected failure "+fail.code+":"+fail.reason+"\n"+fail.trace);
          tu.azzert(fail.code==Failure.INTERNAL_ERROR);
          tu.azzert(fail.reason.indexOf("synthetic-error")>=0);
          tu.testComplete();
        }
      });
    }
  }

  public void testSlowHandler() {
    Set<String> addresses = vertx.sharedData().getSet("addresses");
    int idx=0;
    for (final String address: addresses) {
      // Alternate slow requests
      if (idx++%2==0) {
        // 2500ms with 2000ms timeout -> fail
        eb.send(address, 2500, new ReplyHandler<Message<Integer>>(2000) {
          public void handle(Message<Integer> reply) {
            // Handler should never be called
            tu.azzert(false);
          }
          public void fail(Failure fail) {
            log.info("Expected failure "+fail);
            tu.azzert(fail.code==Failure.REQUEST_TIMEOUT);
            tu.testComplete();
          }
        });
      }
      else {
        // 2000ms with 2500ms timeout -> pass
        eb.send(address, 2000, new ReplyHandler<Message<Integer>>(2500) {
          public void handle(Message<Integer> reply) {
            tu.azzert(reply.body==2000);
            tu.testComplete();
          }
          public void fail(Failure fail) {
            // Handler should never be called
            tu.azzert(false);
          }
        });
      }
    }
  }
  
  public void testNoHandler() throws Exception {
    eb.send("no-handler","bar",new ReplyHandler<Message<String>>() {
      public void handle(Message<String> msg) {
        // Handler will never be called
        tu.azzert(false);
      }
      public void fail(Failure fail) {
        log.info("Expected failure "+fail);
        tu.azzert(fail.code==Failure.NOT_IMPLEMENTED);
        tu.testComplete();
      }
    });
  }

  public void testLocal1() {
    testLocal(true);
  }

  public void testLocal2() {
    testLocal(false);
  }

  public void testLocal(boolean localMethod) {
    final int numHandlers = 10;
    final String address = UUID.randomUUID().toString();
    final AtomicInteger count = new AtomicInteger(0);
    final Buffer buff = TestUtils.generateRandomBuffer(1000);
    for (int i = 0; i < numHandlers; i++) {

      Handler<Message<Buffer>> handler = new Handler<Message<Buffer>>() {
        boolean handled;

        public void handle(Message<Buffer> msg) {
          tu.checkContext();
          tu.azzert(!handled);
          tu.azzert(TestUtils.buffersEqual(buff, msg.body));
          int c = count.incrementAndGet();
          tu.azzert(c <= numHandlers);
          eb.unregisterHandler(address, this);
          if (c == numHandlers) {
            tu.testComplete();
          }
          handled = true;
        }
      };
      if (localMethod) {
        eb.registerLocalHandler(address, handler);
      } else {
        eb.registerHandler(address, handler);
      }
    }

    eb.publish(address, buff);
  }

  public void testRegisterNoAddress() {
    final String msg = "foo";
    final AtomicReference<String> idRef = new AtomicReference<>();
    String id = UUID.randomUUID().toString();
    eb.registerHandler(id, new Handler<Message<String>>() {
      boolean handled = false;
      public void handle(Message<String> received) {
        tu.azzert(!handled);
        tu.azzert(msg.equals(received.body));
        handled = true;
        eb.unregisterHandler(idRef.get(), this);
        vertx.setTimer(100, new Handler<Long>() {
          public void handle(Long timerID) {
            tu.testComplete();
          }
        });
      }
    });
    idRef.set(id);
    for (int i = 0; i < 10; i++) {
      eb.send(id, "foo");
    }
  }

}
