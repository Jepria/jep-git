package com.technology.jep.jepria.server.upload;

import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

/**
 * Интерфейс выгрузки файла
 */
public interface FileUpload {

  /**
   * Начало длинной транзакции записи файла в LOB.
   * 
   * @param tableName     имя таблицы, в которую выполняется запись
   * @param fileFieldName   имя поля, в которое выполняется запись
   * @param keyFieldName   имя поля, идентифицирующего строку таблицы
   * @param rowId         идентификатор строки таблицы
   * @return рекомендуемый размер буфера
   * @throws ApplicationException 
   */
  int beginWrite(
    String tableName
    , String fileFieldName
    , String keyFieldName
    , Object rowId
    )
    throws ApplicationException;

  /**
   * Функция-обертка для {@link #beginWrite(String tableName, String fileFieldName, String keyFieldName, Object rowId)}.
   * В классе реализации в конкретном модуле данный метод перегружаем вызывая в нем 
   * {@link #beginWrite(String, String, String, Object)}
   * с подставленными из констант класса реализации параметрами:<br/>
   * <code>
   * tableName,<br/>
   * fileFieldName,<br/>
   * keyFieldName,<br/>
   * dataSourceJndiName<br/>
   * </code>.
   * 
   * @param rowId         идентификатор строки таблицы
   * @return рекомендуемый размер буфера
   * @throws ApplicationException 
   */
  int beginWrite(
    Object rowId) 
    throws ApplicationException;

  /**
   * Окончание выгрузки.
   *
   * @throws SpaceException
   */
  void endWrite() throws SpaceException;

  /**
   * Откат длинной транзакции.
   */
  void cancel();

  /**
   * Whether the writing has been cancelled (interrupted) or ended successfully (to decide whether to do rollback or commit at the end)
   */
  boolean isCancelled();
}
