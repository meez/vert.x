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

package org.vertx.java.core.http.impl;

import org.vertx.java.core.http.MultiMap;

import java.util.*;

/**
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 */
public class DefaultMultiMap implements MultiMap {
  private final Map<String,List<String>> entries = new HashMap<>();

  @Override
  public String get(String name) {
    List<String> values = getAll(name);
    if (values.isEmpty()) {
      return null;
    }
    return values.get(0);
  }

  @Override
  public List<String> getAll(String name) {
    List<String> values = entries.get(name);
    if (values == null) {
      return Collections.emptyList();
    }
    return values;
  }

  @Override
  public List<Map.Entry<String, String>> entries() {
    List<Map.Entry<String, String>> entryList = new ArrayList<>();
    for (Map.Entry<String, List<String>> entry: entries.entrySet()) {
      String key = entry.getKey();
      List<String> values = entry.getValue();
      for (String value: values) {
        entryList.add(new DefaultEntry(key, value));
      }
    }
    return entryList;
  }

  @Override
  public boolean contains(String name) {
    return entries.containsKey(name);
  }

  @Override
  public boolean isEmpty() {
    return entries.isEmpty();
  }

  @Override
  public Set<String> names() {
    return entries.keySet();
  }

  @Override
  public MultiMap add(String name, Object value) {
    List<String> values = entries.get(name);
    if (values == null) {
      values = new ArrayList<>(1);
      entries.put(name, values);
    }
    values.add(value.toString());
    return this;
  }

  @Override
  public MultiMap add(String name, Iterable<?> values) {
    for (Object value: values) {
      add(name, value);
    }
    return this;
  }

  @Override
  public MultiMap set(String name, Object value) {
    List<String> values = entries.get(name);
    if (values == null) {
      values = new ArrayList<>(1);
      entries.put(name, values);
    } else {
      values.clear();
    }
    values.add(value.toString());
    return this;
  }

  @Override
  public MultiMap set(String name, Iterable<?> v) {
    List<String> values = entries.get(name);
    if (!values.isEmpty()) {
      values.clear();
    }
    for (Object value: v) {
      add(name, value);
    }
    return this;
  }

  @Override
  public MultiMap set(MultiMap headers) {
    clear();
    for (Map.Entry<String, String> entry: headers) {
      add(entry.getKey(), entry.getValue());
    }
    return this;
  }

  @Override
  public MultiMap set(Map<String, ? extends Object> headers) {
    clear();
    for (Map.Entry<String, ? extends Object> entry: headers.entrySet()) {
      add(entry.getKey(), entry.getValue());
    }
    return this;
  }

  @Override
  public MultiMap remove(String name) {
    entries.remove(name);
    return this;
  }

  @Override
  public MultiMap clear() {
    entries.clear();
    return this;
  }

  @Override
  public int size() {
    return entries.size();
  }

  @Override
  public Iterator<Map.Entry<String, String>> iterator() {
    return entries().iterator();
  }

  private static final class DefaultEntry implements Map.Entry<String, String>  {
    private final String key;
    private final String value;
    DefaultEntry(String key, String value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public String getKey() {
      return key;
    }

    @Override
    public String getValue() {
      return value;
    }

    @Override
    public String setValue(String value) {
      throw new UnsupportedOperationException("read-only");
    }
  }
}
