package com.technology.jep.jepria.server.download.blob;

import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.server.download.FileDownload;
import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * <pre>
 * InputStream-обёртка для бина.
 * Используется для выгрузки файлов из поля BINARY_FILE.
 * 
 * Пример использования:
 * 
 * // Получим объект, реализующий интерфейс FileDownload
 * BinaryFileDownload download = new BinaryFileDownloadImpl();
 * 
 * // Передаем в метод поток для записи в файл, объект download,
 * // имя таблицы, имя поля LOB, имя ключевого поля, значение ключа
 * // в таблице, имя источника данных, имя ресурса.
 * FileUploadStream.downloadFile(
 *        stream
 *        , download
 *        , TABLE_NAME
 *        , LOB_FIELD_NAME
 *        , KEY_FIELD_NAME
 *        , new BigDecimal(loadTaskId.intValue())
 *        , DATA_SOURCE_JNDI_NAME);
 * </pre>
 */
public class FileDownloadStream extends InputStream {
  /**
   * Интерфейс бина, который будет использоваться для чтения файла из базы данных.
   */
  private BinaryFileDownload fileDownload;
  
  /**
   * Конструктор.
   *
   * @param fileDownload  интерфейс бина, который будет использоваться для чтения файла из базы данных.
   */  
  public FileDownloadStream(BinaryFileDownload fileDownload) throws ApplicationException {
    if (fileDownload == null) {
      throw new IllegalArgumentException("The bean reference is not expected to be null");
    }
    this.fileDownload = fileDownload;
  }
  
  /**
   * Закрывает входной поток. Пустая реализация на текущий момент.
   */
  @Override
  public void close() throws IOException {
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    if (b == null) {
      return -1;
    }
    if (b.length != len || off != 0) {
      byte[] source = b;
      b = new byte[len];
      System.arraycopy(source, off, b, 0, len);
    }
    return read(b);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int read(byte[] b) throws IOException {
    try {
      return fileDownload.continueRead(b);
    } catch (SpaceException e) {
      throw new RuntimeException("Exception ocuccured on the remote side", e);
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int read() throws IOException {
    byte[] bytes = new byte[1];
    try {
      fileDownload.continueRead(bytes);
    } catch (SpaceException e) {
      throw new RuntimeException("Exception ocuccured on the remote side", e);
    }
    
    return bytes[0];
  }
  
  /**
   * Выгрузка файла в выходной поток из поля BINARY_FILE таблицы базы данных.
   * 
   * @param fileStream       выходной поток записи файла
   * @param fileDownload      интерфейс выгрузки файла
   * @param tableName         имя таблицы, откуда берем BINARY_FILE
   * @param fileFieldName      имя атрибута в таблице, откуда берем BINARY_FILE
   * @param keyFieldName      PK в таблице tableName
   * @param rowId         идентификатор строки таблицы
   * @param dataSourceJndiName   имя источника данных
   * @throws IOException
   */
  public static void downloadFile(
      OutputStream fileStream
      , FileDownload fileDownload
      , String tableName
      , String fileFieldName
      , String keyFieldName
      , Object rowId
      , String dataSourceJndiName
      , String moduleName
      , final boolean transactionable)
      throws IOException {

    OutputStream writeStream = null;
    InputStream readStream = null;
    try {
      writeStream = new BufferedOutputStream(fileStream);

      if (transactionable) {
        CallContext.begin(dataSourceJndiName, moduleName);
      }

      final int WRITE_LENGTH = fileDownload.beginRead(
          tableName
          , fileFieldName
          , keyFieldName
          , rowId
          );
      readStream = new FileDownloadStream((BinaryFileDownload)fileDownload);
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
      // Завершение работы
    } catch (ApplicationException e) {
      throw new SystemException(e.getMessage(), e);
    } finally {
      if (readStream != null) {
        readStream.close();
      }
      if (writeStream != null) {
        writeStream.close();
      }
      
      try {
        fileDownload.endRead();

        if (transactionable) {
          if (fileDownload.isCancelled()) {
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
}
