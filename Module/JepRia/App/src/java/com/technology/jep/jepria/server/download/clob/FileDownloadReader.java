package com.technology.jep.jepria.server.download.clob;

import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.server.download.FileDownload;
import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.SQLException;

/**
 * <pre>
 * Reader-обёртка для бина. 
 * Используется для выгрузки текстовых файлов из поля CLOB.
 * 
 * Пример использования:
 * 
 * // Получим объект, реализующий интерфейс FileDownload
 * TextFileDownload download = new TextFileDownloadImpl();
 * 
 * // Передаем в метод поток для записи в файл, объект download,
 * // имя таблицы, имя поля LOB, имя ключевого поля, значение ключа
 * // в таблице, имя источника данных, имя ресурса.
 * FileUploadWriter.downloadFile(
 *        stream
 *        , download
 *        , TABLE_NAME
 *        , LOB_FIELD_NAME
 *        , KEY_FIELD_NAME
 *        , new BigDecimal(loadTaskId.intValue())
 *        , DATA_SOURCE_JNDI_NAME);
 * </pre>
 */
public class FileDownloadReader extends Reader {
  
  /**
   * Интерфейс бина, который будет использоваться для чтения файла из базы данных.
   */
  private TextFileDownload fileDownload;
  
  /**
   * Конструктор.
   *
   * @param fileDownload  интерфейс бина, который будет использоваться для чтения файла из базы данных.
   */
  public FileDownloadReader(TextFileDownload fileDownload) throws ApplicationException {
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
  
  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    try {
      return fileDownload.continueRead(cbuf);
    } catch (Exception e) {
      throw new RuntimeException("Exception ocuccured on the remote side", e);
    }
  }
  
  /**
   * Загрузка файла в выходной поток из поля Clob таблицы базы данных.
   * 
   * @param writer           выходной поток записи файла
   * @param fileDownload        интерфейс загрузки файла
   * @param tableName           имя таблицы, откуда берем СLOB
   * @param fileFieldName        имя атрибута в таблице, откуда берем СLOB
   * @param keyFieldName        PK в таблице tableName
   * @param rowId               идентификатор строки таблицы
   * @param dataSourceJndiName   имя источника данных
   * @throws IOException
   */
  public static void downloadFile(
      Writer writer
      , FileDownload fileDownload
      , String tableName
      , String fileFieldName
      , String keyFieldName
      , Object rowId
      , String dataSourceJndiName
      , String moduleName
      , final boolean transactionable)
      throws IOException {

    Reader readStream = null;
    BufferedWriter bufferedWriter = null;
    try {
      // Здесь выполняется преобразование из байтов в символы
      bufferedWriter = new BufferedWriter(writer);

      if (transactionable) {
        CallContext.begin(dataSourceJndiName, moduleName);
      }

      final int WRITE_LENGTH = fileDownload.beginRead(
          tableName
          , fileFieldName
          , keyFieldName
          , rowId
          );
      readStream = new FileDownloadReader((TextFileDownload)fileDownload);
      char[] readBuffer = new char[WRITE_LENGTH];
      while (true) {
        int size = readStream.read(readBuffer);
        if (size == -1) {
          break;
        } else if (size == WRITE_LENGTH) {
          bufferedWriter.write(readBuffer);
        } else {
          char[] lastBuffer = new char[size];
          System.arraycopy(readBuffer, 0, lastBuffer, 0, size);
          bufferedWriter.write(lastBuffer);
        }
      }
    } catch (ApplicationException e) {
      throw new SystemException(e.getMessage(), e);
    } finally {
      if(readStream != null) {
        readStream.close();
      }
      if(bufferedWriter != null) {
        bufferedWriter.close();
      }
      if(fileDownload != null) {
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
      if(writer != null) {
        try {
          writer.flush();
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
