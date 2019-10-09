package com.technology.jep.jepria.server.download.clob;

import com.technology.jep.jepria.server.db.clob.TextLargeObject;
import com.technology.jep.jepria.server.download.AbstractFileDownload;
import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * Реализует запись в CLOB.
 */
public class TextFileDownloadImpl extends AbstractFileDownload implements TextFileDownload {
  /**
   * Метод начинает чтение данных из LOB. 
   * 
   * @param rowId идентификатор строки таблицы
   * @return рекомендуемая величина буфера
   * @throws ApplicationException
   */
  @Override
  public int beginRead(
      String tableName
      , String fileFieldName
      , String keyFieldName
      , Object rowId
      )
      throws ApplicationException {

    int result = -1;
    try {

      super.largeObject = new TextLargeObject(tableName, fileFieldName, keyFieldName, rowId);
      result = ((TextLargeObject)super.largeObject).beginRead();
    } catch (ApplicationException ex) {
      cancel();
      throw ex;
    } catch (IllegalStateException ex) {
      ex.printStackTrace();
      throw new SystemException("begin write error", ex);
    } finally {
      storedContext = CallContext.detach();
    }

    return result;
  }
  
  /**
   * Чтение очередного блока данных из CLOB.
   * 
   * @param dataBlock блок данных
   * @throws SpaceException
   */
  @Override
  public int continueRead(char[] dataBlock) throws SpaceException {
    CallContext.attach(storedContext);
    boolean cancelled = true;
    int result = 0;
    try {
      result = ((TextLargeObject)super.largeObject).continueRead(dataBlock);
      cancelled = false;
    } catch (SpaceException e) {
      throw e;
    } catch (Exception e) {
      throw new SpaceException("continue read error", (Exception) e);
    } finally {
      if (cancelled) {
        cancel();
      }
      storedContext = CallContext.detach();
    }
    
    return result;
  }
}
