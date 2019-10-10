package com.technology.jep.jepria.server.upload.blob;

import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.server.upload.FileUpload;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.NotImplementedYetException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

/**
 * <pre>
 * OutputStream-обёртка для бина.
 * Используется для загрузки файлов в поля BINARY_FILE.
 * 
 * Пример использования:
 * 
 * // Получим объект, реализующий интерфейс FileUpload
 * BinaryFileUpload upload = new BinaryFileUploadImpl();
 * 
 * // Передаем в метод поток для чтения из файла, объект upload,
 * // имя таблицы, имя поля LOB, имя ключевого поля, значение ключа
 * // в таблице, имя источника данных, имя ресурса.
 * FileUploadStream.uploadFile(
 *        stream
 *        , upload
 *        , TABLE_NAME
 *        , LOB_FIELD_NAME
 *        , KEY_FIELD_NAME
 *        , new BigDecimal(loadTaskId.intValue())
 *        , DATA_SOURCE_JNDI_NAME
 *        , moduleName);
 * </pre>
 */
public class FileUploadStream extends OutputStream {
    
  /**
   * Интерфейс бина, который будет использоваться для записи файла в базу данных.
   */
  private BinaryFileUpload fileUpload;

  /**
   * Конструктор.
   *
   * @param fileUpload  интерфейс бина, который будет использоваться для записи файла в базу данных.
   */      
  public FileUploadStream(BinaryFileUpload fileUpload) throws ApplicationException {
    if (fileUpload == null) {
      throw new IllegalArgumentException("The bean reference is not expected to be null");
    }
    this.fileUpload = fileUpload;
  }

  /**
   * Закрывает выходной поток. Пустая реализация на текущий момент.
   */      
  public void close() throws IOException {

  }

  /**
   * Очищает выходной поток и записывает все буферизированные данные. 
   * На текущий момент вызывает исключение UnsupportedOperationException("Not impemented yet").
   */      
  public void flush() throws IOException {
    throw new UnsupportedOperationException("Not impemented yet");
  }
  /**
   * {@inheritDoc}
   */
  public void write(byte[] b, int off, int len) throws IOException {
    if (b == null) {
      return;
    }
    if (b.length != len || off != 0) {
      byte[] source = b;
      b = new byte[len];
      System.arraycopy(source, off, b, 0, len);
    }
      write(b);
  }
  
  /**
   * {@inheritDoc}
   */
  public void write(byte[] b) throws IOException {
    try {
      fileUpload.continueWrite(b);
    } catch (Exception e) {
      throw new RuntimeException("Exception ocuccured on the remote side", e);
    }
  }
  
  /**
   * {@inheritDoc}
   */
  public void write(int b) throws IOException {
    try {
      byte bytes[] = new byte[] { (byte) b };
      write(bytes);
    } catch (Exception e) {
      throw new RuntimeException("Exception ocuccured on the remote side", e);
    }
  }

  /**
   * Загрузка файла из входного потока в поле Blob таблицы базы данных по простому первичному ключу.
   * 
   * @param fileStream           входной поток чтения файла
   * @param fileUpload          интерфейс выгрузки файла
   * @param tableName           имя таблицы, откуда берем BINARY_FILE
   * @param fileFieldName        имя атрибута в таблице, откуда берем BINARY_FILE
   * @param keyFieldName        PK в таблице tableName
   * @param rowId               идентификатор строки таблицы
   * @param dataSourceJndiName   имя источника данных
   * @param moduleName       имя модуля
   * @throws IOException
   */
  public static void uploadFile(
    InputStream fileStream
    , FileUpload fileUpload
    , String tableName
    , String fileFieldName
    , String keyFieldName
    , Object rowId
    , String dataSourceJndiName
    , String moduleName
    , final boolean transactionable)
    throws IOException {
    if(tableName == null) {
      throw new SystemException("tableName is empty");
    }
    if(fileFieldName == null) {
      throw new SystemException("fileFieldName is empty");
    }
    if(keyFieldName == null) {
      throw new SystemException("keyFieldName is empty");
    }
    if(rowId == null) {
      throw new SystemException("rowId is empty");
    }
    OutputStream writeStream = null;
    InputStream readStream = null;
    try {
      readStream = new BufferedInputStream(fileStream);

      if (transactionable) {
        CallContext.begin(dataSourceJndiName, moduleName);
      }

      final int WRITE_LENGTH = fileUpload.beginWrite(
          tableName
          , fileFieldName
          , keyFieldName
          , rowId
          );
      writeStream = new FileUploadStream((BinaryFileUpload)fileUpload);
      byte[] readBuffer = new byte[WRITE_LENGTH];
      while (true) {
        int size = readStream.read(readBuffer);
        if (size == -1) {
          break;
        } else if (size == WRITE_LENGTH) {
          writeStream.write(readBuffer);
        } else {
          byte[] lastBuffer = new byte[size];
          System.arraycopy(readBuffer, 0, lastBuffer, 0, size);
          writeStream.write(lastBuffer);
        }
      }
    } catch (ApplicationException e) {
      throw new SystemException(e.getMessage(), e);
    } finally {
      if(readStream != null) { 
        readStream.close();
      }
      if(writeStream != null) {
        writeStream.close();
      }
      
      try {
        fileUpload.endWrite();

        if (transactionable) {
          if (fileUpload.isCancelled()) {
            CallContext.rollback();
          } else {
            CallContext.commit();
          }
        }

      } catch (SpaceException e) {
        e.printStackTrace();
      } catch (SQLException ex) {
        throw new SystemException("end write error", ex);
      } finally {
        if (transactionable) {
          CallContext.end();
        }
      }
    }
  }

  /**
   * Загрузка файла из входного потока в поле Blob таблицы базы данных по сложному первичному ключу.
   * 
   * @param inputStream       входной поток чтения файла
   * @param fileUpload      объект, выполняющий загрузку
   * @param tableName         имя таблицы, откуда берем BINARY_FILE
   * @param fileFieldName      имя атрибута в таблице, откуда берем BINARY_FILE
   * @param primaryKeyMap     PK в таблице tableName
   * @param dataSourceJndiName   имя источника данных
   * @param moduleName       имя модуля
   * @throws IOException
   */
  public static void uploadFile(
      InputStream inputStream,
      BinaryFileUpload fileUpload,
      String tableName,
      String fileFieldName,
      Map<String, Object> primaryKeyMap,
      String dataSourceJndiName,
      String moduleName,
      final boolean transactionable) {
    throw new NotImplementedYetException();
  }

}
