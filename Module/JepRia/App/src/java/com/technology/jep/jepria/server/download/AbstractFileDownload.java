package com.technology.jep.jepria.server.download;

import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.server.db.LargeObject;
import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * Абстрактный базовый класс для реализаций выгрузки файла.
 */
public abstract class AbstractFileDownload implements FileDownload {

  protected CallContext storedContext;
  protected LargeObject largeObject = null;

  protected boolean cancelled = false;

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  /**
   * Метод начинает чтение данных из LOB. 
   * 
   * @param rowId идентификатор строки таблицы
   * @return рекомендуемая величина буфера
   * @throws ApplicationException
   */
  @Override
  public int beginRead(Object rowId) 
    throws ApplicationException {

    return 0;
  }
  
  /**
   * Метод завершает чтение данных из LOB.
   * 
   * @throws SpaceException
   */
  @Override
  public void endRead() throws SpaceException {
    CallContext.attach(storedContext);
    try {
      largeObject.endRead();
    } catch (SpaceException ex) {
      cancel();
      throw ex;
    } catch (Throwable th) {
      th.printStackTrace();
      throw new SystemException("end write error", new RuntimeException(th));
    }
  }
  
  /**
   * Метод отменяет текущую операцию и откатывает транзакцию.
   */
  @Override
  public void cancel() {
    cancelled = true;
    if (storedContext != null) {
      CallContext.attach(storedContext);
    }
    if (largeObject != null) {
      largeObject.cancel();
    }
  }

}
