package com.ntatvr.core.helps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionHelpers {

  public static <T extends Comparable<T>> Set<T> getSortedSet(final Set<T> needToBeSort) {
    if (CollectionUtils.isEmpty(needToBeSort)) {
      return new HashSet<>();
    }

    final List<T> sortedList = new ArrayList<>(needToBeSort);
    Collections.sort(sortedList);
    return new LinkedHashSet<>(sortedList);
  }

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
    return collection.stream()
        .filter(Objects::nonNull);
  }

  /**
   * Filters the <code>collection</code> by the target class type <code>clazz</code> and converts it to a {@link Stream}.
   *
   * @param object of object
   * @param clazz to filter...
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
