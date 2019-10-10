package com.technology.jep.jepria.server.download;

import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_CONTENT_DISPOSITION;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_CONTENT_DISPOSITION_ATTACHMENT;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_CONTENT_DISPOSITION_INLINE;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_EXTENSION;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_FIELD_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_FILE_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_FILE_NAME_PREFIX;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_ID;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_MIME_TYPE;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_RECORD_KEY;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.BINARY_FILE;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.TEXT_FILE;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.server.JepRiaServerConstant;
import com.technology.jep.jepria.server.download.blob.BinaryFileDownloadImpl;
import com.technology.jep.jepria.server.download.blob.FileDownloadStream;
import com.technology.jep.jepria.server.download.clob.FileDownloadReader;
import com.technology.jep.jepria.server.download.clob.TextFileDownloadImpl;
import com.technology.jep.jepria.server.util.JepServerUtil;
import com.technology.jep.jepria.shared.exceptions.UnsupportedException;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.lob.JepLobRecordDefinition;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

@SuppressWarnings("serial")
public class JepDownloadServlet extends HttpServlet {
  /**
   * Логгер.
   */
  protected static Logger logger = Logger.getLogger(JepDownloadServlet.class.getName());  
  /**
   * Символы, недопустимые в имени файла.
   */
  private static final char[] illegalCharacters = "\\/:*?\"<>|\t\n".toCharArray();
  /**
   * Определение записи.
   */
  private JepLobRecordDefinition fileRecordDefinition = null;
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
   * Кодирует строку в формат, подходящий для помещения в заголовок Content-disposition.<br>
   * См. здесь: http://stackoverflow.com/a/611117
   * @param str кодируемая строка
   * @return строка после кодирования
   */
  private static String encodeURIComponent(String str) {
    String result = null;
    try {
      result = URLEncoder.encode(str, "UTF-8")
              .replaceAll("\\+", "%20")
              .replaceAll("\\%21", "!")
              .replaceAll("\\%27", "'")
              .replaceAll("\\%28", "(")
              .replaceAll("\\%29", ")")
              .replaceAll("\\%7E", "~");
    } catch (UnsupportedEncodingException e) {
      // Данное исключение не будет выброшено никогда.
    }
    return result;
  }
  
  /**
   * Заменяет недопустимые символы знаком подчёркивания.
   * @param str строка
   * @return строка с заменёнными недопустимыми символами
   */
  private static String replaceIllegalCharacters(String str) {
    for (char ch : illegalCharacters) {
      str = str.replace(ch, '_');
    }
    return str;
  }
  
  /**
   * Создаёт сервлет для загрузки файлов с сервера.
   * @param fileRecordDefinition определение записи
   * @param dataSourceJndiName JNDI-имя источника данных
   * @param textFileCharset кодировка текстовых файлов
   */
  public JepDownloadServlet(
    JepLobRecordDefinition fileRecordDefinition,
    String dataSourceJndiName,
    Charset textFileCharset) {
    
    this.fileRecordDefinition = fileRecordDefinition;
    this.dataSourceJndiName = dataSourceJndiName;
    this.textFileCharset = textFileCharset;
  }

  /**
   * Создаёт сервлет для загрузки файлов с сервера.<br/>
   * Для текстовых файлов используется кодировка по умолчанию - UTF-8.
   * @param fileRecordDefinition определение записи
   * @param dataSourceJndiName JNDI-имя источника данных
   */
  public JepDownloadServlet(
    JepLobRecordDefinition fileRecordDefinition,
    String dataSourceJndiName){
    
    this(fileRecordDefinition, dataSourceJndiName, JepRiaServerConstant.DEFAULT_ENCODING);
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

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    logger.debug("doGet() request.getQueryString() = " + request.getQueryString());
    
    try {
      response.reset();
      
      String downloadId = request.getParameter(DOWNLOAD_ID);
      String fileName, fileNamePrefix, fileExtension, mimeType, recordKey, contentDisposition, fieldName;
      if (downloadId != null) {
        HttpSession session = request.getSession();
        fileName = (String)session.getAttribute(DOWNLOAD_FILE_NAME + downloadId);
        fileExtension = (String)session.getAttribute(DOWNLOAD_EXTENSION + downloadId);
        fileNamePrefix = (String)session.getAttribute(DOWNLOAD_FILE_NAME_PREFIX + downloadId);
        mimeType = (String)session.getAttribute(DOWNLOAD_MIME_TYPE + downloadId);
        recordKey = (String)session.getAttribute(DOWNLOAD_RECORD_KEY + downloadId);
        fieldName = (String)session.getAttribute(DOWNLOAD_FIELD_NAME + downloadId);
        contentDisposition = (String)session.getAttribute(DOWNLOAD_CONTENT_DISPOSITION + downloadId);
      }
      else {
        fileName = request.getParameter(DOWNLOAD_FILE_NAME);
        fileNamePrefix = request.getParameter(DOWNLOAD_FILE_NAME_PREFIX);
        fileExtension = request.getParameter(DOWNLOAD_EXTENSION);
        mimeType = request.getParameter(DOWNLOAD_MIME_TYPE);
        recordKey = request.getParameter(DOWNLOAD_RECORD_KEY);            
        fieldName = request.getParameter(DOWNLOAD_FIELD_NAME);    
        contentDisposition = request.getParameter(DOWNLOAD_CONTENT_DISPOSITION);
      }
      
      String fileFieldName = getFileFieldName(fieldName);
      String tableName = fileRecordDefinition.getTableName();  
      
      response.setContentType(mimeType + ";charset=" + textFileCharset);
      addAntiCachingHeaders(response);
      
      if(DOWNLOAD_CONTENT_DISPOSITION_INLINE.equals(contentDisposition)){
        response.setHeader("Content-disposition", "inline");
      } else if(DOWNLOAD_CONTENT_DISPOSITION_ATTACHMENT.equals(contentDisposition)){
        setAttachedFileName(response, fileName, fileExtension, fileNamePrefix, recordKey);
      }
      
      JepTypeEnum fileFieldType = getFileFieldType(fieldName);
      
      ServletOutputStream outputStream = response.getOutputStream();
      
      if(fileFieldType == BINARY_FILE) {
        downloadBinary(outputStream,
          tableName,
          fileFieldName,
          recordKey);
      } else if(fileFieldType == TEXT_FILE){
        downloadText(outputStream,
          tableName,
          fileFieldName,
          recordKey);
      } else {
        throw new UnsupportedException(this.getClass() + ".doGet(): " + fileFieldType + " field type does not supported for download.");
      }
    } catch (Throwable th) {
      logger.error("doGet() threw exception: ", th);
      onError(response,
        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
        "doGet(): " + "Download error: " + th.getMessage());
    }
  }
  
  /**
   * Получает тип скачиваемого файла. <br/>
   * Метод создан для переопределения в потомках, когда нет возможности получить данные из fileRecordDefinition. 
   * @param fieldName Имя поля в fileRecordDefinition
   * @return Тип скачиваемого файла.
   */
  protected JepTypeEnum getFileFieldType(String fieldName) {
    return fileRecordDefinition.getTypeMap().get(fieldName);
  }
  
  /**
   * Получает имя поля в таблице, из которого выгружается файл. <br/>
   * Метод создан для переопределения в потомках, когда нет возможности получить данные из fileRecordDefinition. 
   * @param fieldName Имя поля в fileRecordDefinition
   * @return Имя поля в таблице, из которого выгружается файл.
   */
  protected String getFileFieldName(String fieldName) {
    return fileRecordDefinition.getFieldMap().get(fieldName);
  }

  /**
   * Установка имени выгружаемого файла (в случае, если файл выгружается как вложение).<br>
   * Имя файла кодируется; недопустимые символы заменяются знаками подчёркивания.
   * @param response ответ сервлета
   * @param fileName имя файла
   * @param fileExtension расширение
   * @param fileNamePrefix префикс имени файла
   * @param recordKey ключ записи
   * @throws UnsupportedEncodingException 
   */
  protected void setAttachedFileName(HttpServletResponse response, String fileName, String fileExtension, 
      String fileNamePrefix, String recordKey) throws UnsupportedEncodingException {
    String downloadFileName;
    if(!JepRiaUtil.isEmpty(fileName)){
      downloadFileName = fileName;
    } else if(!JepRiaUtil.isEmpty(fileNamePrefix)){
      downloadFileName = fileNamePrefix + "_" + recordKey;
    } else {
      downloadFileName = recordKey;
    }
    
    if(!JepRiaUtil.isEmpty(fileExtension)){
      fileExtension = "." + fileExtension;
    } else {
      fileExtension = "";
    }

    response.setHeader("Content-disposition", "attachment; filename*=UTF-8''" + encodeURIComponent(replaceIllegalCharacters(downloadFileName)) + replaceIllegalCharacters(fileExtension) + "");
  }

  /**
   * Добавление &quot;типового набора&quot; заголовков для борьбы с кэшированием в разных браузерах.
   * @param response ответ сервлета
   */
  private static void addAntiCachingHeaders(HttpServletResponse response) {
    response.setHeader("Cache-Control", "cache"); //HTTP 1.1
    response.setHeader("Pragma", "no-cache"); //HTTP 1.0
    response.setDateHeader("Expires", 0);
    response.setDateHeader("Last-Modified", System.currentTimeMillis());
  }

  /**
   * Загрузка бинарного файла из базы данных.
   * @param outputStream выходной поток
   * @param tableName имя таблицы
   * @param fileFieldName имя поля в таблице
   * @param recordKey ключ записи
   * @throws IOException
   * @throws NamingException
   */
  private void downloadBinary(
    OutputStream outputStream
    , String tableName
    , String fileFieldName
    , String recordKey
    ) throws IOException, NamingException {
    
    FileDownloadStream.downloadFile(
      outputStream,
      new BinaryFileDownloadImpl(),
      tableName,
      fileFieldName,
      fileRecordDefinition.getKeyFieldName(),
      recordKey,
      this.dataSourceJndiName,
      this.moduleName,
      true);
  }

  /**
   * Загрузка текстового файла из базы данных.
   * @param outputStream выходной поток
   * @param tableName имя таблицы
   * @param fileFieldName имя поля
   * @param recordKey ключ записи
   * @throws IOException
   * @throws NamingException
   */
  private void downloadText(
    OutputStream outputStream
    , String tableName
    , String fileFieldName
    , String recordKey
    ) throws IOException, NamingException {
    
    FileDownloadReader.downloadFile(
      new OutputStreamWriter(outputStream, textFileCharset),
      new TextFileDownloadImpl(),
      tableName,
      fileFieldName,
      fileRecordDefinition.getKeyFieldName(),
      recordKey,
      this.dataSourceJndiName,
      this.moduleName,
      true);
  }
  
  /**
   * Отправка сообщения об ошибке в случае ошибки при загрузке файла.<br/>
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
