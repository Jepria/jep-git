package com.technology.jep.jepria.server.db.blob;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.server.dao.DaoSupport;
import com.technology.jep.jepria.server.db.LargeObject;
import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * Класс поддерживает запись в поле BINARY_FILE.
 */
public class BinaryLargeObject extends LargeObject {
  private static final int WRITE_LENGTH = 32768;

  private OutputStream output;
  private InputStream input;

  /**
   * Конструктор
   * 
   * @param tableName имя таблицы, в которую выполняется запись
   * @param fileFieldName имя поля, в которую выполняется запись
   * @param keyFieldName имя поля, идентифицирующего строку таблицы
   * @param rowId идентификатор строки таблицы
   */
  public BinaryLargeObject(String tableName, String fileFieldName, String keyFieldName, Object rowId) {
    super(tableName, fileFieldName, keyFieldName, rowId);

    rowId = String.class.isInstance(rowId) ? "'" + rowId + "'" : rowId;
    
    super.sqlClearLob = "update " + tableName + " set " + fileFieldName + "=empty_blob() where " + keyFieldName + "=" + rowId;
  }

  /**
   * Метод начинает запись файла в BINARY_FILE.
   * 
   * @return рекомендуемая величина буфера
   * @throws ApplicationException
   */
  public int beginWrite() throws ApplicationException {
    int result = 0;
    try {
      database = CallContext.getDb();
      // Сбрасываем значение поля
      CallableStatement cs = database.prepare(super.sqlClearLob);
      DaoSupport.setModule(CallContext.getModuleName(), "UploadBLOB");
      cs.execute();
      
      // Получаем поток для записи поля BINARY_FILE
      cs = database.prepare(super.sqlObtainOutputStream);
      ResultSet rs = cs.executeQuery();
      if(rs.next()) {
            Blob blob = (Blob) rs.getBlob(1);
            output = blob.setBinaryStream(0);
            result = WRITE_LENGTH;
      } else {
        throw new ApplicationException("Record of table '" + tableName + "' with id '" + keyFieldName + "' was not found", null);
      }
      return result;
    } catch (SQLException ex) {
      database.rollback();
      ex.printStackTrace();
      throw new ApplicationException("Large object begin write error", ex);
    }
  }
  
  /**
   * Метод начинает чтение файла из BINARY_FILE.
   * 
   * @return рекомендуемая величина буфера
   * @throws ApplicationException
   */
  public int beginRead() throws ApplicationException {
    int result = 0;
    try {
      database = CallContext.getDb();
      
      // Получаем поток для записи поля BINARY_FILE
      CallableStatement cs = database.prepare(super.sqlObtainInputStream);
      DaoSupport.setModule(CallContext.getModuleName(), "DownloadBLOB");
      ResultSet rs = cs.executeQuery();
      if(rs.next()) {
            Blob blob = (Blob) rs.getBlob(1);
            input = blob.getBinaryStream();
            result = WRITE_LENGTH;
      } else {
        throw new ApplicationException("Record of table '" + tableName + "' with id '" + keyFieldName + "' was not found", null);
      }
      return result;
    } catch (SQLException ex) {
      database.rollback();
      ex.printStackTrace();
      throw new ApplicationException("Large object begin write error", ex);
    }
  }

  /**
   * Функция для записи в поле Blob через AsciiStream (пишет данные в поток открытый {@link #beginWrite()}).
   */
  public void continueWrite(byte[] data) throws SpaceException {
    try {
      output.write(data);
    } catch (Exception e) {
      e.printStackTrace();
      throw new SpaceException("Large object continue write error", e);
    }
  }
  
  /**
   * Функция для чтения поля Blob (пишет данные в поток открытый {@link #beginRead()}).
   */
  public int continueRead(byte[] data) throws SpaceException {
    try {
      return input.read(data);
    } catch (IOException e) {
      e.printStackTrace();
      throw new SpaceException("Large object continue read error", e);
    }
  }

  /**
   * Функция завершения записи в поле Blob (закрывает поток открытый {@link #beginWrite()} и вызывает освобождение ресурсов {@link #closeAll()}).
   */
  public void endWrite() throws SpaceException {
    try {
      output.flush();
      output.close();
    } catch (IOException ex) {
      ex.printStackTrace();
      throw new SpaceException("Large object end write error", ex);
    }
  }
  
  /**
   * Функция завершения чтение из поля Blob (закрывает поток открытый {@link #beginRead()} и вызывает освобождение ресурсов {@link #closeAll()}).
   */
  public void endRead() throws SpaceException {
    try {
      if(input != null) {
        input.close();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
      throw new SpaceException("Large object ends read error", ex);
    }
  }

  /**
   * Функция освобождения ресурсов.
   */
  protected void closeAll() {
    try {
      database.closeAll();
      if(input != null) {
        input.close();
      }
      if(output != null) {
        output.close();
      }
    } catch (IOException ex) {
      throw new SystemException("Large object clean up resources error", ex);
    }
  }
}