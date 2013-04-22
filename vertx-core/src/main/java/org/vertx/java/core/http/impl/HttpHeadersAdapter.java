package org.vertx.java.core.http.impl;

import org.vertx.java.core.http.HttpHeaders;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 */
public final class HttpHeadersAdapter implements HttpHeaders {
  private final io.netty.handler.codec.http.HttpHeaders headers;

  public HttpHeadersAdapter(io.netty.handler.codec.http.HttpHeaders headers) {
    this.headers = headers;
  }

  @Override
  public String get(String name) {
    return headers.get(name);
  }

  @Override
  public List<String> getAll(String name) {
    return headers.getAll(name);
  }

  @Override
  public List<Map.Entry<String, String>> entries() {
    return headers.entries();
  }

  @Override
  public boolean contains(String name) {
    return headers.contains(name);
  }

  @Override
  public boolean isEmpty() {
    return headers.isEmpty();
  }

  @Override
  public Set<String> names() {
    return headers.names();
  }

  @Override
  public HttpHeaders add(String name, Object value) {
    headers.add(name, value);
    return this;
  }

  @Override
  public HttpHeaders add(String name, Iterable<?> values) {
    headers.add(name, values);
    return this;
  }

  @Override
  public HttpHeaders set(String name, Object value) {
    headers.set(name, value);
    return this;
  }

  @Override
  public HttpHeaders set(String name, Iterable<?> values) {
    headers.set(name, values);
    return this;
  }

  @Override
  public HttpHeaders set(HttpHeaders httpHeaders) {
    clear();
    for (Map.Entry<String, String> entry: httpHeaders) {
      add(entry.getKey(), entry.getValue());
    }
    return this;
  }

  @Override
  public HttpHeaders remove(String name) {
    headers.remove(name);
    return this;
  }

  @Override
  public HttpHeaders clear() {
    headers.clear();
    return this;
  }

  @Override
  public Iterator<Map.Entry<String, String>> iterator() {
    return headers.iterator();
  }

  @Override
  public int size() {
    return entries().size();
  }

  @Override
  public HttpHeaders set(Map<String, ? extends Object> headers) {
    for (Map.Entry<String, ? extends Object> entry: headers.entrySet()) {
      add(entry.getKey(), entry.getValue());
    }
    return this;
  }
}
