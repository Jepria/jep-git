package com.technology.jep.jepria.server.download.blob;

import com.technology.jep.jepria.server.download.FileDownload;
import com.technology.jep.jepria.server.exceptions.SpaceException;

/**
 * Интерфейс выгрузки бинарного файла.
 */
public interface BinaryFileDownload extends FileDownload {
  /**
   * Чтение очередного блока данных из BINARY_FILE.
   * 
   * @param dataBlock блок данных
   * @throws SpaceException
   */
  int continueRead(byte[] dataBlock) throws SpaceException;
}
