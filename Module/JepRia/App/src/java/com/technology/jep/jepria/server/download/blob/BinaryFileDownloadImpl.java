package com.technology.jep.jepria.server.download.blob;

import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.server.db.blob.BinaryLargeObject;
import com.technology.jep.jepria.server.download.AbstractFileDownload;
import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

/**
 * Класс, реализующий выгрузку (download) бинарного файла.
 */
public class BinaryFileDownloadImpl extends AbstractFileDownload implements BinaryFileDownload {
  
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

      super.largeObject = new BinaryLargeObject(tableName, fileFieldName, keyFieldName, rowId);
      result = ((BinaryLargeObject)super.largeObject).beginRead();
    } catch (ApplicationException ex) {
      cancel();
      throw ex;
    } finally {
      storedContext = CallContext.detach();
    }

    return result;
  }
  
  /**
   * Чтение очередного блока данных из BINARY_FILE.
   * 
   * @param dataBlock блок данных
   * @throws SpaceException
   */
  @Override
  public int continueRead(byte[] dataBlock) throws SpaceException {
    CallContext.attach(storedContext);
    boolean cancelled = true;
    int result = 0;
    try {
      result = ((BinaryLargeObject)super.largeObject).continueRead(dataBlock);
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
