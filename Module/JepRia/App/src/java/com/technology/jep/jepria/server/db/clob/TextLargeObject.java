package com.technology.jep.jepria.server.db.clob;

import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.server.dao.DaoSupport;
import com.technology.jep.jepria.server.db.LargeObject;
import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.server.util.JepServerUtil;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс поддерживает запись в поле CLOB.
 */
public class TextLargeObject extends LargeObject {
  private static final int WRITE_LENGTH = 32768;
  private Writer writer;
  private Reader reader;

  /**
   * Конструктор
   * 
   * @param tableName имя таблицы, в которую выполняется запись
   * @param fileFieldName имя поля, в которую выполняется запись
   * @param keyFieldName имя поля, идентифицирующего строку таблицы
   * @param rowId идентификатор строки таблицы
   */
  public TextLargeObject(String tableName, String fileFieldName, String keyFieldName, Object rowId) {
    super(tableName, fileFieldName, keyFieldName, rowId);
    
    rowId = String.class.isInstance(rowId) ? "'" + rowId + "'" : rowId;

    super.sqlClearLob = "update " + tableName + " set " + fileFieldName + "=empty_clob() where " + keyFieldName + "=" + rowId;
  }
  
  /**
   * Метод начинает запись в CLOB.
   * 
   * @return рекомендуемая величина буфера
   * @throws ApplicationException
   */
  public int beginWrite() throws ApplicationException {
    int result = 0;
    try {
      database = CallContext.getDb();
      // Очищаем значение поля
      CallableStatement cs = database.prepare(super.sqlClearLob);
      DaoSupport.setModule(CallContext.getModuleName(), "UploadCLOB");
      cs.execute();
      
      // Получаем поток для записи поля СLOB
      cs = database.prepare(super.sqlObtainOutputStream);
      ResultSet rs = cs.executeQuery();
      if(rs.next()) {
        Clob clob = (Clob) rs.getClob(1);
        writer = clob.setCharacterStream(0);
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
   * Метод начинает чтение из CLOB.
   * 
   * @return рекомендуемая величина буфера
   * @throws ApplicationException
   */
  public int beginRead() throws ApplicationException {
    int result = 0;
    try {
      database = CallContext.getDb();

      // Получаем поток для записи поля СLOB
      CallableStatement cs = database.prepare(super.sqlObtainInputStream);
      DaoSupport.setModule(CallContext.getModuleName(), "DownloadBLOB");
      ResultSet rs = cs.executeQuery();
      if(rs.next()) {
        Clob clob = (Clob) rs.getClob(1);

        JepServerUtil.disableClobPrefetch(clob);

        if (clob != null) {
          // non-empty clob
          reader = clob.getCharacterStream();
        } else {
          // empty clob

          // TODO это грубый костыль в проектировании!
          // Здесь нужно сделать {reader = null; return 0;}, но поскольку далее везде доступ к переменной reader нигде не проверяется на null, проще сделать пустой reader
          reader = new StringReader("");
        }

        result = WRITE_LENGTH;
      } else {
        throw new ApplicationException("Record of table '" + tableName + "' with id '" + keyFieldName + "' was not found", null);
      }
      return result;
    } catch (SQLException ex) {
      database.rollback();
      ex.printStackTrace();
      throw new ApplicationException("Large object begin read error", ex);
    }
  }

  /**
   * Функция для записи в поле Clob через CharacterStream (пишет данные в поток открытый {@link #beginWrite()}).
   */
  public void continueWrite(char[] data) throws SpaceException {
    try {
      writer.write(data);
    } catch (Exception e) {
      e.printStackTrace();
      throw new SpaceException("Large object continue write error", e);
    }
  }
  
  /**
   * Функция для чтения из поля Clob через CharacterStream (читает данные из потока открытого {@link #beginRead()}).
   */
  public int continueRead(char[] data) throws SpaceException {
    try {
      return reader.read(data);
    } catch (Exception e) {
      e.printStackTrace();
      throw new SpaceException("Large object continue write error", e);
    }
  }

  /**
   * Функция завершения записи в поле Clob (закрывает поток открытый {@link #beginWrite()} и вызывает освобождение ресурсов {@link #closeAll()}).
   */
  @Override
  public void endWrite() throws SpaceException {
    try {
      writer.flush();
      writer.close();
    } catch (IOException ex) {
      ex.printStackTrace();
      throw new SpaceException("Large object end write error", ex);
    }
  }
  
  /**
   * Функция завершения чтения из поля Clob (закрывает поток открытый {@link #beginRead()} и вызывает освобождение ресурсов {@link #closeAll()}).
   */
  @Override
  public void endRead() throws SpaceException {
    try {
      reader.close();
    } catch (IOException ex) {
      ex.printStackTrace();
      throw new SpaceException("Large object end write error", ex);
    }
  }

  /**
   * Функция освобождения ресурсов.
   */
  @Override
  protected void closeAll() {
    try {
      database.closeAll();
      reader.close();
      writer.close();
    } catch (IOException ex) {
      throw new SystemException("Large object clean up resources error", ex);
    }
  }
}