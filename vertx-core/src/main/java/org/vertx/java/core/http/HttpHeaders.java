package org.vertx.java.core.http;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 */
public interface HttpHeaders extends  Iterable<Map.Entry<String, String>> {

  /**
   * Returns the value of a header with the specified name.  If there are
   * more than one values for the specified name, the first value is returned.
   *
   * @param name The name of the header to search
   * @return The first header value or {@code null} if there is no such header
   */
  String get(String name);

  /**
   * Returns the values of headers with the specified name
   *
   * @param name The name of the headers to search
   * @return A immutable {@link List} of header values which will be empty if no values
   *         are found
   */
  List<String> getAll(String name);

  /**
   * Returns all headers that this message contains.
   *
   * @return A immutable {@link List} of the header name-value entries, which will be
   *         empty if no pairs are found
   */
  List<Map.Entry<String, String>> entries();

  /**
   * Checks to see if there is a header with the specified name
   *
   * @param name The name of the header to search for
   * @return True if at least one header is found
   */
  boolean contains(String name);

  /**
   * Checks if no header exists.
   */
  boolean isEmpty();

  /**
   * Gets a immutable {@link Set} of all header names that this message contains
   *
   * @return A {@link Set} of all header names
   */
  Set<String> names();

  /**
   * Adds a new header with the specified name and value.
   *
   * If the specified value is not a {@link String}, it is converted
   * into a {@link String} by {@link Object#toString()}, except in the cases
   * of {@link java.util.Date} and {@link java.util.Calendar}, which are formatted to the date
   * format defined in <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.3.1">RFC2616</a>.
   *
   * @param name The name of the header being added
   * @param value The value of the header being added
   *
   * @return {@code this}
   */
  HttpHeaders add(String name, Object value);

  /**
   * Adds a new header with the specified name and values.
   *
   * This getMethod can be represented approximately as the following code:
   * <pre>
   * for (Object v: values) {
   *     if (v == null) {
   *         break;
   *     }
   *     headers.add(name, v);
   * }
   * </pre>
   *
   * @param name The name of the headers being set
   * @param values The values of the headers being set
   * @return {@code this}
   */
  HttpHeaders add(String name, Iterable<?> values);


  /**
   * Sets a header with the specified name and value.
   *
   * If there is an existing header with the same name, it is removed.
   * If the specified value is not a {@link String}, it is converted into a
   * {@link String} by {@link Object#toString()}, except for {@link java.util.Date}
   * and {@link java.util.Calendar}, which are formatted to the date format defined in
   * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.3.1">RFC2616</a>.
   *
   * @param name The name of the header being set
   * @param value The value of the header being set
   * @return {@code this}
   */
  HttpHeaders set(String name, Object value);

  /**
   * Sets a header with the specified name and values.
   *
   * If there is an existing header with the same name, it is removed.
   * This getMethod can be represented approximately as the following code:
   * <pre>
   * headers.remove(name);
   * for (Object v: values) {
   *     if (v == null) {
   *         break;
   *     }
   *     headers.add(name, v);
   * }
   * </pre>
   *
   * @param name The name of the headers being set
   * @param values The values of the headers being set
   * @return {@code this}
   */
  HttpHeaders set(String name, Iterable<?> values);

  /**
   * Cleans the current header entries and copies all header entries of the specified {@code headers}.
   *
   * @return {@code this}
   */
  HttpHeaders set(HttpHeaders headers);

  /**
   * Removes the header with the specified name.
   *
   * @param name The name of the header to remove
   * @return {@code this}
   */
  HttpHeaders remove(String name);

  /**
   * Removes all headers from this {@link HttpHeaders}.
   *
   * @return {@code this}
   */
   HttpHeaders clear();
}
