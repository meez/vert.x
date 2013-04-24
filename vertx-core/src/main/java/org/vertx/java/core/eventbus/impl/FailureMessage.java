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
package org.vertx.java.core.eventbus.impl;

import org.jboss.netty.util.CharsetUtil;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Failure;
import org.vertx.java.core.eventbus.Message;

/** FailureMessage 
 *
 * Custom message type for transferring failures 
 * 
 **/
public class FailureMessage extends BaseMessage<Failure> {

  private byte[] encReason;
  private byte[] encTrace;

  FailureMessage(boolean send, String address, Failure body) {
    super(send, address, body);
  }

  public FailureMessage(Buffer readBuff) {
    super(readBuff);
  }
	
  public String toString() {
	return String.format("Failure(%s:%s)",body.code,body.reason);
  }

  protected void readBody(int pos, Buffer readBuff) {
    boolean isNull=readBuff.getByte(pos) == (byte)0;
    if (!isNull) {
      pos++;
      // Type
      int code=readBuff.getInt(pos);
      pos += 4;
      // Reason 
      int strLength=readBuff.getInt(pos);
      pos += 4;
      String reason=readBuff.getString(pos,pos+strLength);
      pos += strLength;
      // Trace
      strLength=readBuff.getInt(pos);
      pos += 4;
      String trace=readBuff.getString(pos,pos+strLength);
      pos += strLength;
      // Complete
      body=new Failure(code,reason,trace);
    }
  }

  protected void writeBody(Buffer buff) {
    if (body == null) {
      buff.appendByte((byte)0);
    } else {
      buff.appendByte((byte)1);
      buff.appendInt(body.code);
      buff.appendInt(encReason.length);
      buff.appendBytes(encReason);
      buff.appendInt(encTrace.length);
      buff.appendBytes(encTrace);
    }
    encReason = encTrace = null;
  }

  protected int getBodyLength() {
    if (body == null) {
      return 1;
    } else {
	    encReason=body.reason.getBytes(CharsetUtil.UTF_8);
  	  encTrace=(body.trace!=null)?body.trace.getBytes(CharsetUtil.UTF_8):new byte[0];
      return 1 + 4 + (4 + encReason.length) + (4 + encTrace.length);
    }
  }

  protected Message copy() {
    // No need to copy since everything is immutable
    return this;
  }

  protected byte type() {
    return MessageFactory.TYPE_FAILURE;
  }

  protected BaseMessage createReplyMessage(Failure reply) {
	  throw new UnsupportedOperationException("Cannot reply to a failure");
  }
}
