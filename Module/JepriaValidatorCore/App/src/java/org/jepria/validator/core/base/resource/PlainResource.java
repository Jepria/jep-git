package org.jepria.validator.core.base.resource;

import java.io.InputStream;

public interface PlainResource extends Resource {
  
  /**
   * @return новый поток чтения ресурса
   */
  InputStream newInputStream();
}
