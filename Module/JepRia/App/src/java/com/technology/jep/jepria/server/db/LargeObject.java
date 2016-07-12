package com.technology.jep.jepria.server.db;

import com.technology.jep.jepria.server.exceptions.SpaceException;

/**
 * Абстрактный класс поддерживает запись в поле LOB.
 */
public abstract class LargeObject {
  protected String sqlClearLob;
  protected String sqlObtainOutputStream;
  protected String sqlObtainInputStream;
  protected Db database;
  
  // Параметры, идентифицирующие изменяемое поле Blob
  protected String tableName = null;
  protected String keyFieldName = null;
  

  /**
   * Освобождение ресурсов
   */
  protected abstract void closeAll();

  /**
   * Завершение записи
   */
  public abstract void endWrite() throws SpaceException;
  
  /**
   * Завершение чтения
   */
  public abstract void endRead() throws SpaceException;

  /**
   * Конструктор
   * 
   * @param tableName имя таблицы, в которую выполняется запись
   * @param fileFieldName имя поля, в которую выполняется запись
   * @param keyFieldName имя поля, идентифицирующего строку таблицы
   * @param rowId идентификатор строки таблицы
   */
  public LargeObject(String tableName, String fileFieldName, String keyFieldName, Object rowId) {
    checkParameters(tableName, fileFieldName, keyFieldName);
    this.tableName = tableName;
    this.keyFieldName = keyFieldName;
  
    rowId = String.class.isInstance(rowId) ? "'" + rowId + "'" : rowId;

    this.sqlObtainOutputStream = "select " + fileFieldName + " from " + tableName + " where " + keyFieldName + "=" + rowId + " for update";
    this.sqlObtainInputStream = "select " + fileFieldName + " from " + tableName + " where " + keyFieldName + "=" + rowId;
  }

  /**
   * Отмена записи.
   */
  public void cancel() {
    closeAll();
  }

  /**
   * Функция проверки наличия обязательных параметров конструктора. При значении null любого параметра выбрасывается исключение.
   *
   * @param tableName имя таблицы, в которую выполняется запись
   * @param fileFieldName имя поля, в которую выполняется запись
   * @param keyFieldName имя поля, идентифицирующего строку таблицы
   */
  private void checkParameters(String tableName, String fileFieldName, String keyFieldName) {
    if (tableName == null) {
      throw new NullPointerException("Table name should be specified");
    }
    if (fileFieldName == null) {
      throw new NullPointerException("Lob field name should be specified");
    }
    if (keyFieldName == null) {
      throw new NullPointerException("Key field name should be specified");
    }
  }
}