package com.technology.jep.jepria.server.upload.clob;

import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.server.upload.FileUpload;

/**
 * Интерфейс выгрузки текстового файла
 */
public interface TextFileUpload extends FileUpload {

  /**
   * Добавление очередного блока данных при записи в CLOB.
   * 
   * @param dataBlock блок данных
   * @throws SpaceException
   */
  void continueWrite(char[] dataBlock) throws SpaceException;
}
