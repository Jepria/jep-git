package com.technology.jep.jepria.server.download.clob;

import com.technology.jep.jepria.server.download.FileDownload;
import com.technology.jep.jepria.server.exceptions.SpaceException;

/**
 * Интерфейс загрузки текстового файла.
 */
public interface TextFileDownload extends FileDownload {
  /**
   * Чтение очередного блока данных из CLOB.
   * 
   * @param dataBlock блок данных
   * @throws SpaceException
   */
  int continueRead(char[] dataBlock) throws SpaceException;
}
