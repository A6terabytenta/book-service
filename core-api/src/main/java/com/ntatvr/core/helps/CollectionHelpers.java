package com.ntatvr.core.helps;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionHelpers {

  /**
   * Ignore null elements and converts it to a {@link Stream}.
   *
   * @param collection of <T>
   * @return a Stream<T>
   */
  public static <T> Stream<T> nonNullStream(final Collection<T> collection) {
    if (CollectionUtils.isEmpty(collection)) {
      return Stream.empty();
    }
    return collection.stream().filter(Objects::nonNull);
  }

  /**
   * Filters the <code>collection</code> by the target class type <code>clazz</code> and converts it to a
   * {@link Stream}.
   *
   * @param object of object
   * @param clazz  to filter...
   * @return a Stream object from the filtered collection on class <T>
   */
  public static <T> Stream<T> toFilteredStream(final Object object, final Class<T> clazz) {
    if (Objects.isNull(object) || !Collection.class.isAssignableFrom(object.getClass())) {
      return Stream.empty();
    }
    final Collection<?> toCollection = (Collection<?>) object;
    return nonNullStream(toCollection)
        .filter(clazz::isInstance)
        .map(clazz::cast);
  }
}
