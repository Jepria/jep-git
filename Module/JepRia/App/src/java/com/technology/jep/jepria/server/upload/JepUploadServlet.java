package com.technology.jep.jepria.server.upload;

import static com.technology.jep.jepria.server.JepRiaServerConstant.JEP_RIA_RESOURCE_BUNDLE_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.FILE_SIZE_HIDDEN_FIELD_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.PRIMARY_KEY_HIDDEN_FIELD_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.IS_DELETED_FILE_HIDDEN_FIELD_NAME;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.BINARY_FILE;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.TEXT_FILE;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.InvalidFileNameException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.technology.jep.jepria.server.JepRiaServerConstant;
import com.technology.jep.jepria.server.upload.blob.BinaryFileUploadImpl;
import com.technology.jep.jepria.server.upload.blob.FileUploadStream;
import com.technology.jep.jepria.server.upload.clob.FileUploadWriter;
import com.technology.jep.jepria.server.upload.clob.TextFileUploadImpl;
import com.technology.jep.jepria.server.util.JepServerUtil;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.UnsupportedException;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.history.JepHistoryToken;
import com.technology.jep.jepria.shared.record.lob.JepLobRecordDefinition;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Сервлет для загрузки файлов на сервер.<br/>
 * Осуществляет загрузку текстовых и бинарных файлов в таблицу базы данных.
 *
 */
@SuppressWarnings("serial")
public class JepUploadServlet extends HttpServlet {
  /**
   * Логгер.
   */
  protected static Logger logger = Logger.getLogger(JepUploadServlet.class.getName());  
  /**
   * Определение записи.
   */
  protected JepLobRecordDefinition fileRecordDefinition = null;
  /**
   * JNDI-имя источника данных.
   */
  private String dataSourceJndiName;
  /**
   * Имя модуля, передаваемое в DB.
   */
  private String moduleName;
  
  /**
   * Кодировка текстовых файлов.
   */
  private final Charset textFileCharset;

  /**
   * Признак необходимости производить загрузку в отдельной транзакции.
   */
  private final boolean transactionable;

  /**
   * Создаёт сервлет загрузки файлов на сервер.<br>
   * Для текстовых файлов используется кодировка по умолчанию - UTF-8.
   * @param fileRecordDefinition определение записи
   * @param dataSourceJndiName JNDI-наименование источника данных
   */
  public JepUploadServlet(
      JepLobRecordDefinition fileRecordDefinition,
      String dataSourceJndiName) {
    this(fileRecordDefinition, dataSourceJndiName, JepRiaServerConstant.DEFAULT_ENCODING, true);
  }
  /**
   * Создаёт сервлет загрузки файлов на сервер.
   * @param fileRecordDefinition определение записи
   * @param dataSourceJndiName JNDI-наименование источника данных
   * @param textFileCharset кодировка текстовых файлов
   */
  public JepUploadServlet(
    JepLobRecordDefinition fileRecordDefinition,
    String dataSourceJndiName,
    Charset textFileCharset) {

    this(fileRecordDefinition, dataSourceJndiName, textFileCharset, true);
  }

  /**
   * Создаёт сервлет загрузки файлов на сервер.
   * @param fileRecordDefinition определение записи
   * @param dataSourceJndiName JNDI-наименование источника данных
   * @param textFileCharset кодировка текстовых файлов
   */
  public JepUploadServlet(
          JepLobRecordDefinition fileRecordDefinition,
          String dataSourceJndiName,
          Charset textFileCharset,
          boolean transactionable) {

    this.fileRecordDefinition = fileRecordDefinition;
    this.dataSourceJndiName = dataSourceJndiName;
    this.textFileCharset = textFileCharset;
    this.transactionable = transactionable;
  }

  /**
   * Инициализация сервлета.
   * Переопределение метода обусловлено установкой имени модуля. Выполнить данную операцию
   * в конструкторе невозможно, т.к. <code>getServletContext()</code> в конструкторе 
   * выбрасывает <code>NullPointerException</code>.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    moduleName = JepServerUtil.getModuleName(getServletConfig());
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    logger.debug("doPost()");
    if (ServletFileUpload.isMultipartContent(request)) {
      // Создание фабрики элементов дисковых файлов
      FileItemFactory factory = new DiskFileItemFactory();
      // Создание обработчика загрузки файла
      ServletFileUpload upload = new ServletFileUpload(factory);
      try {
        // Последовательный upload
        // TODO Параллельный эффективнее, но насколько это актуально ?
        List<FileItem> items = upload.parseRequest(request);
        if (items.size() >= 2) { // Должно быть два параметра: файл и привязка к полю записи таблицы БД
          
          //Привязки к полю записи таблицы БД по первичному ключу
          Map<String, Object> primaryKeyMap = getPrimaryKeyMap(items);
          
          FileItem fileSizeFormField = getFormField(items, FILE_SIZE_HIDDEN_FIELD_NAME);
          String fileSizeAsString = fileSizeFormField.getString();
          Integer specifiedFileSize = JepRiaUtil.isEmpty(fileSizeAsString) ? null : Integer.decode(fileSizeAsString) * 1024; // in Kbytes
          
          StringBuilder errorMessage = new StringBuilder();
          
          for (FileItem fileItem : items) {
            if (!fileItem.isFormField()) {
              // check file size if necessary
              long fileSize = fileItem.getSize();
              if (!JepRiaUtil.isEmpty(specifiedFileSize) && fileSize > specifiedFileSize) {
                ResourceBundle resource = ResourceBundle.getBundle(JEP_RIA_RESOURCE_BUNDLE_NAME);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/plain; charset=UTF-8");
                response.getWriter().print(MessageFormat.format(resource.getString("errors.file.uploadFileSizeError"), specifiedFileSize, fileSize));
                response.flushBuffer();
                return;
              }
              
              String fileName = null;
              try {
                fileName = fileItem.getName();
              } catch(InvalidFileNameException e) {
                fileName = e.getName();
              }
              
              FileItem isDeletedFormField = getFormField(items, IS_DELETED_FILE_HIDDEN_FIELD_NAME);
              boolean isDeleted = isDeletedFormField != null && 
                  Boolean.valueOf(isDeletedFormField.getString());
              
              if (!JepRiaUtil.isEmpty(fileName) || isDeleted) {
                String fileFieldName = getFileFieldName(fileItem);
                JepTypeEnum fileFieldType = getFileFieldType(fileItem);
                String tableName = fileRecordDefinition.getTableName();
                try {
                  if (fileFieldType == BINARY_FILE) {
                    uploadBinary(
                      fileItem.getInputStream(),
                      tableName,
                      fileFieldName,
                      primaryKeyMap);
                  } else if (fileFieldType == TEXT_FILE) {
                    uploadText(
                      fileItem.getInputStream(),
                      tableName,
                      fileFieldName,
                      primaryKeyMap);
                  } else {
                    throw new UnsupportedException(this.getClass() + ".doPost(): " + fileFieldType + " field type does not supported for upload.");
                  }
                } catch(Throwable th) {
                  String message = "doPost().upload error in '" + fileFieldName + "' field: " + th.getMessage();
                  logger.error(message, th);
                  errorMessage.append(message);
                  errorMessage.append("\n");
                }
              }
            }
          }

          response.setStatus(HttpServletResponse.SC_CREATED);
          if (errorMessage.length() == 0) {
            response.getWriter().print("Upload success");
          } else {
            response.getWriter().print(errorMessage);
          }
          response.flushBuffer();
        } else {
          FileItem formField = getFormField(items, PRIMARY_KEY_HIDDEN_FIELD_NAME);
          // Параметр привязки к полю записи таблицы БД
          throw new IllegalArgumentException("Parameters number should be 3, but we have: "
                  + items.size() + ". "
                  + (formField == null ? "FormField" : "File")
                  + " item is absent.");

        }
      } catch (Exception ex) {
        ex.printStackTrace();
        onError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            "doPost(): " + "Upload error: " + ex.getMessage());
      }
    } else {
      onError(response, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
          "doPost(): Request contents type is not supported by the servlet.");
    }
  }
  
  /**
   * Получает тип загружаемого файла. <br/>
   * Метод создан для переопределения в потомках, когда нет возможности получить данные из fileRecordDefinition. 
   * @param fileItem Интерфейс выгрузки файла.
   * @return Тип загружаемого файла.
   */
  protected JepTypeEnum getFileFieldType(FileItem fileItem) {
    return fileRecordDefinition.getTypeMap().get(fileItem.getFieldName());
  }
  
  /**
   * Получает имя поля в таблице, в которое загружается файл. <br/>
   * Метод создан для переопределения в потомках, когда нет возможности получить данные из fileRecordDefinition. 
   * @param fileItem Интерфейс выгрузки файла.
   * @return Имя поля в таблице, в которое загружается файл.
   */
  protected String getFileFieldName(FileItem fileItem) {
    return fileRecordDefinition.getFieldMap().get(fileItem.getFieldName());
  }
  
  /**
   * Возвращает карту первичных ключей.
   * @param items Список FileItem из запроса.
   * @return Карта первичных ключей.
   * @throws UnsupportedEncodingException
   * @throws ApplicationException
   */
  protected Map<String, Object> getPrimaryKeyMap(List<FileItem> items) throws UnsupportedEncodingException, ApplicationException {
    // Параметр привязки к полю записи таблицы БД
    FileItem primaryKeyFormField = getFormField(items, PRIMARY_KEY_HIDDEN_FIELD_NAME);
    String primaryKeyToken = primaryKeyFormField.getString("UTF-8");
    Map<String, Object> primaryKeyMap = JepHistoryToken.buildMapFromToken(primaryKeyToken);
    return primaryKeyMap;
  }
  
  /**
   * Загрузка на сервер бинарного файла.
   * @param inputStream поток данных
   * @param tableName имя таблицы, в которую загружается файл
   * @param fileFieldName имя поля, в которое загружается файл
   * @param primaryKeyMap первичный ключ
   * @throws IOException
   * @throws Exception
   */
  protected void uploadBinary(
    InputStream inputStream
    , String tableName
    , String fileFieldName
    , Map<String, Object> primaryKeyMap
    ) throws IOException, Exception {
    
      if (primaryKeyMap.size() == 1) {
        FileUploadStream.uploadFile(
          inputStream,
          new BinaryFileUploadImpl(),
          tableName,
          fileFieldName,
          fileRecordDefinition.getKeyFieldName(),
          primaryKeyMap.values().toArray()[0],
          this.dataSourceJndiName,
          this.moduleName,
          transactionable);
      } else {
        FileUploadStream.uploadFile(
          inputStream,
          new BinaryFileUploadImpl(),
          tableName,
          fileFieldName,
          primaryKeyMap,
          this.dataSourceJndiName,
          this.moduleName,
          transactionable);
      }
    
  }

  /**
   * Загрузка на сервер текстового файла.
   * @param inputStream поток данных
   * @param tableName имя таблицы, в которую загружается файл
   * @param fileFieldName имя поля, в которое загружается файл
   * @param primaryKeyMap первичный ключ
   * @throws IOException
   * @throws Exception
   */
  protected void uploadText(
    InputStream inputStream
    , String tableName
    , String fileFieldName
    , Map<String, Object> primaryKeyMap
    ) throws IOException, Exception {
    
      if (primaryKeyMap.size() == 1) {
        FileUploadWriter.uploadFile(
          new InputStreamReader(inputStream, textFileCharset),
          new TextFileUploadImpl(),
          tableName,
          fileFieldName,
          fileRecordDefinition.getKeyFieldName(),
          primaryKeyMap.values().toArray()[0],
          this.dataSourceJndiName,
          this.moduleName,
          transactionable);
      } else {
        FileUploadWriter.uploadFile(
          new InputStreamReader(inputStream, textFileCharset),
          new TextFileUploadImpl(),
          tableName,
          fileFieldName,
          primaryKeyMap,
          this.dataSourceJndiName,
          this.moduleName,
          transactionable);
      }
    
  }
  
  /**
   * Получение интерфейса выгрузки файлов.<br/>
   * Предполагается, что может быть только одно поле формы с указанным наименованием 
   * @param items список интерфейсов выгрузки файлов
   * @param fieldName наименование поля 
   */
  protected FileItem getFormField(List<FileItem> items, String fieldName) {
        for (FileItem item : items) {
          if (item.isFormField() && fieldName.equalsIgnoreCase(item.getFieldName())) {
            return item;
          }
        }
    return null;
  }

  /**
   * Отправка сообщения об ошибке в случае неуспешной загрузки.<br/>
   * При необходимости данный метод может быть переопределён в классе-наследнике.
   * @param response результат работы сервлета (ответ)
   * @param error HTTP-код ошибки
   * @param message текст сообщения об ошибке
   * @throws IOException
   */
  protected void onError(HttpServletResponse response, int error, String message) throws IOException {
    logger.error(message);
    response.sendError(error, message);
  }

}
