package com.technology.jep.jepria.server.upload.blob;

import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.server.upload.FileUpload;

/**
 * Интерфейс загрузки бинарного файла
 */
public interface BinaryFileUpload extends FileUpload {

  /**
   * Добавление очередного блока данных при записи в BINARY_FILE.
   * 
   * @param dataBlock блок данных
   * @throws SpaceException
   */
  void continueWrite(byte[] dataBlock) throws SpaceException;
}
