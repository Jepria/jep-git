package org.jepria.server.service.apispec;

import java.util.List;
import java.util.Map;

public interface ApiSpec {
  default List<?> parameters() {
    return null;
  }
  /**
   * @return response descriptions mapped to response codes. 
   * A description may be a prepared JSON string,
   * a {@link Map} or a {@link Response}, null-safe
   */
  default Map<Integer, ?> responses() {
    return null;
  }
  default String description() {
    return null;
  }
  default String summary() {
    return null;
  }
}
