package com.technology.jep.jepriatoolkit.util;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_DETAILS;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.BIGDECIMAL;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.BINARY_FILE;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.BOOLEAN;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.DATE;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.INTEGER;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.STRING;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.TIME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ALL_TEXT_ENCODE_TASK_TARGET;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_SETTING_FILE_ENDING;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.APPLICATION_STRUCTURE_FILE_PATH_TASK_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUILD_AND_DEPLOY_TASK_TARGET;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUILD_CONFIG_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUILD_FILE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUTTON_IDENTIFICATOR_SUFFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.CLEAN_TASK_TARGET;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.CREATE_STRUCTURE_TASK_TARGET;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.CURRENT_DIRECTORY_ENVIRONMENT_VARIABLE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DEFAULT_HTTP_PORT;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DISPLAY_VALUE_SUFFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DOT;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.END_OF_LINE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FILE_STRUCTURE_PATTERN;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.HTTP_PROTOCOL;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.IDENTIFICATOR_CODE_SUFFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.IDENTIFICATOR_SUFFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.INHERITS_MAIN_GWT_XML_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.JEP_CHECKBOX_FIELD;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.JEP_COMBOBOX_FIELD;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.JEP_DATE_FIELD;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.JEP_FILE_FIELD;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.JEP_IMAGE_FIELD;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.JEP_LIST_FIELD;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.JEP_MONEY_FIELD;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.JEP_NUMBER_FIELD;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.JEP_TEXT_AREA_FIELD;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.JEP_TEXT_FIELD;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.JEP_TIME_FIELD;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.LEFT_CURLY_BRACKET;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MODULE_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.OPMN_PROTOCOL;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PATH_SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PRESENTER_BOBY_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.REGEXP_FOR_BLANK;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.UNDEFINED_INT;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.UNDERSCORE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.UTF_8;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WHITE_SPACE;
import static java.text.MessageFormat.format;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.taskdefs.Echo;
import org.apache.tools.ant.taskdefs.Mkdir;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepriatoolkit.ApplicationDefinition;
import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.ModuleField;

@SuppressWarnings("unchecked")
public final class JepRiaToolkitUtil {

  private static final String WIN_CHARSET = "windows-1251";
  
  /**
   * Функция чтения из файла
   * 
   * @param filePath  путь до файла, из которого производится считывание
   * @param charset  кодировка
   * @return содержимое файла
   * @throws FileNotFoundException
   */
  public static String readFromFile(String filePath, String charset) throws FileNotFoundException {
    Scanner br = new Scanner(new File(filePath), isEmpty(charset) ? UTF_8 : charset);
    StringBuilder sb = new StringBuilder();
    try {
      while (br.hasNextLine()) {
        sb.append(br.nextLine() + END_OF_LINE);
      }
    } finally {
      br.close();
    }
    return sb.toString();
  }
  
  /**
   * Функция чтения из архива jar
   * 
   * @param jarPath  путь до файла в архиве
   * @param charset  кодировка
   * @return содержимое файла
   * @throws FileNotFoundException
   */
  public static String readFromJar(String jarPath, String charset) {
    InputStream stream = JepRiaToolkitUtil.class.getResourceAsStream(jarPath);
    Scanner br = new Scanner(stream, isEmpty(charset) ? UTF_8 : charset);
    StringBuilder sb = new StringBuilder();
    try {
      while (br.hasNextLine()) {
        sb.append(br.nextLine() + END_OF_LINE);
      }
    } finally {
      br.close();
    }
    return sb.toString();
  }
  
  /**
   * Функция записи строки в файл
   * 
   * @param fileContent необходимое для записи содержимое
   * @param filePath    путь до файла, в который будет произведена запись содержимого
   * @param charset     кодировка
   */
  public static void writeToFile(String fileContent, String filePath, String charset, boolean overwrite) 
  {
    if (overwrite || !new File(filePath).exists())
      writeToFile(fileContent, filePath, charset);
  }
  
  /**
   * Функция записи строки в файл
   * 
   * @param fileContent необходимое для записи содержимое
   * @param filePath    путь до файла, в который будет произведена запись содержимого
   * @param charset     кодировка
   */
  public static void writeToFile(String fileContent, String filePath,
      String charset) {
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(new OutputStreamWriter(
          new FileOutputStream(filePath),
          isEmpty(charset) ? UTF_8 : charset));

      StringTokenizer tokenizer = new StringTokenizer(fileContent,
          END_OF_LINE);

      while (tokenizer.hasMoreTokens()) {
        String content = tokenizer.nextToken();
        writer.println(content);
      }

      writer.close();
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, "File '", normalizePath(filePath), "' is incorrect! ", e.getLocalizedMessage()));
    }
  }

  /**
   * Функция записи строки в файл
   * 
   * @param fileContent  необходимое для записи содержимое
   * @param filePath     путь до файла, в который будет произведена запись содержимого
   */
  public static void writeToFile(String fileContent, String filePath) {
    writeToFile(fileContent, filePath, null);
  }

  /**
   * Создание структуры документа
   * 
   * @return объекта созданной структуры
   * @throws ParserConfigurationException
   */
  public static final Document createDOM()
      throws ParserConfigurationException {
    DocumentBuilder db = createDocumentBuilder();
    Document doc = db.newDocument();
    return doc;

  }

  /**
   * Получение структуры документа
   * 
   * @param fileNameOrPath наименование файла настроек или пути для генерации структуры
   * @return документ
   * @throws IOException
   * @throws SAXException
   * @throws ParserConfigurationException
   */
  public static final Document getDOM(String fileNameOrPath)
      throws IOException, SAXException, ParserConfigurationException {
    DocumentBuilder db = createDocumentBuilder();
    Document doc = db.parse(fileNameOrPath);
    return doc;

  }

  /**
   * Создание билдера для построения DOM-структуры
   * 
   * @return билдер
   * @throws ParserConfigurationException
   */
  public static final DocumentBuilder createDocumentBuilder()
      throws ParserConfigurationException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true); // never forget this!
    dbf.setIgnoringElementContentWhitespace(true);
    DocumentBuilder db = dbf.newDocumentBuilder();
    return db;

  }

  /**
   * Печать в форматируемом виде xml-файла
   *  
   * @param doc            объект, в котором хранится вся структура документа
   * @param destination    наименование файла, в который будет произведена запись
   * @throws TransformerConfigurationException
   * @throws TransformerException
   */
  public static final void prettyPrint(Document doc, String destination)
      throws TransformerConfigurationException, TransformerException {

    TransformerFactory factory = TransformerFactory.newInstance();
    Transformer xform = factory.newTransformer();
    xform.setOutputProperty(OutputKeys.INDENT, "yes");
    xform.setOutputProperty(OutputKeys.ENCODING, WIN_CHARSET);
    xform.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    xform.transform(new DOMSource(doc), new StreamResult(destination));

  }
  
  /**
   * Печать в форматируемом виде xml-файла (включая удаление пустых узлов)
   * 
   * @param doc объект, в котором хранится вся структура документа
   * @param destination наименование файла, в который будет произведена запись
   * @param isTab флаг то, что разделителями выступает табуляция (4 проблема), в противном случае - 2 пробела
   * @throws TransformerConfigurationException
   * @throws TransformerException
   * @throws XPathException
   */
  public static final void prettyPrintToStripWhitespace(Document doc, String destination, boolean isTab)
      throws TransformerConfigurationException, TransformerException, XPathException {
    doc.normalize();
    XPathFactory xpathFactory = XPathFactory.newInstance();
    // XPath to find empty text nodes.
    XPathExpression xpathExp = xpathFactory.newXPath().compile(
          "//text()[normalize-space(.) = '']");  
    NodeList emptyTextNodes = (NodeList) 
            xpathExp.evaluate(doc, XPathConstants.NODESET);
    // Remove each empty text node from document.
    for (int i = 0; i < emptyTextNodes.getLength(); i++) {
        Node emptyTextNode = emptyTextNodes.item(i);
        emptyTextNode.getParentNode().removeChild(emptyTextNode);
    }
    TransformerFactory factory = TransformerFactory.newInstance();
    Transformer xform = factory.newTransformer();
    xform.setOutputProperty(OutputKeys.INDENT, "yes");
    xform.setOutputProperty(OutputKeys.ENCODING, WIN_CHARSET);
    xform.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", isTab ? "4" : "2");
    xform.transform(new DOMSource(doc), new StreamResult(destination));

  }

  /**
   * Преобразование первой буквы слова в нижний регистр
   * 
   * @param word преобразуемое слово
   * @return преобразованное слово
   */
  public static String initSmall(String word) {
    if (isEmpty(word))
      return "";
    return word.length() > 1 ? multipleConcat(String
        .valueOf(word.charAt(0)).toLowerCase(), word.substring(1))
        : word.toLowerCase();
  }

  /**
   * Преобразование первой буквы слова в верхний регистр
   * 
   * @param word преобразуемое слово
   * @return преобразованное слово
   */
  public static String initCap(String word) {
    if (isEmpty(word))
      return "";
    return word.length() > 1 ? multipleConcat(String
        .valueOf(word.charAt(0)).toUpperCase(), word.substring(1))
        : word.toUpperCase();
  }

  /**
   * Функция копирования файла в файл
   * 
   * @param sourceFile файл-источник, из которого будет производиться копирование
   * @param destinationFile файл, в который будет производиться копирование
   * 
   * @throws IOException в случае отсутствия файла-источника или файла-приемника
   */
  public static void copyFile(String sourceFile, String destinationFile) throws IOException {
    copyFile(new File(sourceFile), new File(destinationFile));
  }
  
  /**
   * Функция копирования файла в файл
   * 
   * @param sourceFile файл-источник, из которого будет производиться копирование
   * @param destinationFile файл, в который будет производиться копирование
   */
  public static void copyFile(File sourceFile, File destinationFile) throws IOException {
    FileChannel sourceChannel = null;
    FileChannel destChannel = null;
    try {
      sourceChannel = new FileInputStream(sourceFile).getChannel();
      destChannel = new FileOutputStream(destinationFile).getChannel();
      destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
    } finally {
      sourceChannel.close();
      destChannel.close();
    }
  }

  /**
   * Создание директории
   * 
   * @param dirName наименование директории
   */
  public static void makeDir(String dirName) {
    Mkdir dir = new Mkdir();
    dir.setDir(new File(dirName));
    dir.execute();
  }
  
  /**
   * Удаление директории
   * 
   * @param dirName наименование директории
   */
  public static void deleteDir(String dirName) {
    Delete deleteDir = new Delete();
    deleteDir.setDir(new File(dirName));
    deleteDir.execute();
  }

  /**
   * Вывод сообщения
   * 
   * @param message сообщение
   */
  public static void echoMessage(String message){
    Echo echo = new Echo();
    echo.setMessage(message);
    echo.execute();
  }

  /**
   * Преобразование рабочего состояния к строке
   * 
   * @param workstate рабочее состоние
   * @return состояние в виде строки
   */
  public static String getWorkStateAsString(WorkstateEnum workstate) {
    return workstate.name();
  }

  /**
   * Получение списка рабочих состояний из строки
   * 
   * @param workStatesAsString строка
   * @return список рабочих состояний
   */
  public static List<WorkstateEnum> getWorkStates(String workStatesAsString) {
    List<WorkstateEnum> result = null;
    if (!isEmpty(workStatesAsString)) {
      result = new ArrayList<WorkstateEnum>();
      String[] workStates = workStatesAsString.split(SEPARATOR);
      for (int j = 0; j < workStates.length; j++) {
        String workstate = workStates[j].trim();
        if (workstate.startsWith("!")){
          try {
            WorkstateEnum negatingWorkstate = WorkstateEnum.valueOf(workstate.substring(1));
            WorkstateEnum[] workstates = WorkstateEnum.class.getEnumConstants();
            for (int index = 0; index < workstates.length; index++){
              WorkstateEnum currWorkstate = workstates[index];
              if (!negatingWorkstate.equals(currWorkstate) && isEditFormState(currWorkstate) && !result.contains(currWorkstate)){
                result.add(currWorkstate);
              }
            }
          }
          catch (IllegalArgumentException e){}
          catch (NullPointerException e){}
        }
        else {
          try {
            WorkstateEnum currWorkstate = WorkstateEnum.valueOf(workstate);
            if (!result.contains(currWorkstate)){
              result.add(currWorkstate);
            }
          }
          catch (IllegalArgumentException e){}
        }
      }
    }
    return result;
  }
  
  
  /**
   * Преобразование типа поля к соответствующему представителю перечисления
   * 
   * @param fieldType тип поля
   * @return тип поля как представитель перечисления JepTypeEnum
   */
  public static JepTypeEnum getFieldTypeAsEnum(String fieldType) {
    try {
      return !isEmpty(fieldType) ? JepTypeEnum.valueOf(fieldType.toUpperCase()) : JepTypeEnum.STRING;
    }
    catch (IllegalArgumentException e){}
    catch (NullPointerException e){}
    return JepTypeEnum.STRING;
  }

  /**
   * Получение наименования поля в виде параметра Если поле составное, т.е.
   * его слова разделены _, то слово преобразуется к виду параметра Например,
   * поле FIELD_ID преобразуется к fieldId
   * 
   * @param field  наименование поля
   * @param prefix префикс
   * @return наименование поля как параметр
   */
  public static String getFieldIdAsParameter(String field, String prefix) {
    String formNameBuf = field.toLowerCase();
    String formName = formNameBuf;
    for (int i = 0; i < formNameBuf.length(); i++) {
      if (String.valueOf(formNameBuf.charAt(i)).equals(UNDERSCORE)
          && (i + 1 < formNameBuf.length())) {
        String twoSymbols = formNameBuf.substring(i, i + 2);
        formName = formName
            .replace(twoSymbols,
                String.valueOf(formNameBuf.charAt(i + 1))
                    .toUpperCase());
      }
    }
    if (!isEmpty(prefix))
      formName = multipleConcat(prefix, initCap(formName));
    return formName;
  }

  /**
   * Множественное объединение строк
   * 
   * @param strings объединяемые строки
   * @return единая строка
   */
  public static String multipleConcat(String... strings) {
    StringBuilder strBuilder = new StringBuilder(strings.length);
    for (String str : strings) {
      strBuilder.append(str);
    }
    return strBuilder.toString();
  }

  /**
   * Подбор подходящего типа поля для указанного виджета
   * 
   * @param fieldWidget виджет
   * @return потенциально близкий к виджету тип поля
   */
  public static String getAppropriateFieldType(String fieldWidget) {
    if (JEP_DATE_FIELD.equals(fieldWidget))
      return DATE.name();
    else if (JEP_MONEY_FIELD.equals(fieldWidget))
      return BIGDECIMAL.name();
    else if (JEP_NUMBER_FIELD.equals(fieldWidget)
        || JEP_COMBOBOX_FIELD.equals(fieldWidget))
      return INTEGER.name();
    else if (JEP_TEXT_FIELD.equals(fieldWidget)
        || JEP_TEXT_AREA_FIELD.equals(fieldWidget))
      return STRING.name();
    else if (JEP_TIME_FIELD.equals(fieldWidget))
      return TIME.name();
    else if (JEP_FILE_FIELD.equals(fieldWidget)
        || JEP_IMAGE_FIELD.equals(fieldWidget))
      return BINARY_FILE.name();
    else if (JEP_CHECKBOX_FIELD.equals(fieldWidget))
      return BOOLEAN.name();
    return STRING.name();
  }

  /**
   * Обрезает суфикс у идентификатора поля
   * 
   * @param fieldId  идентификатор поля
   * @return идентификатор без суфикса
   */
  public static String subtractFieldSuffix(String fieldId) {
    Integer lastIndexOfSuffix = null;
    if (isEmpty(fieldId))
      fieldId = new String();
    else if ((lastIndexOfSuffix = fieldId.toUpperCase().lastIndexOf(
        IDENTIFICATOR_SUFFIX)) != -1
        || (lastIndexOfSuffix = fieldId.toUpperCase().lastIndexOf(
            IDENTIFICATOR_CODE_SUFFIX)) != -1) {
      fieldId = fieldId.substring(0, lastIndexOfSuffix);
    }
    return fieldId;
  }

  /**
   * Обрезает суфикс у идентификатора пользовательской кнопки
   * 
   * @param buttonId  идентификатор кнопки
   * @return идентификатор без суфикса
   */
  public static String subtractCustomButtonSuffix(String buttonId) {
    Integer lastIndexOfSuffix = null;
    if (isEmpty(buttonId))
      buttonId = new String();
    else if ((lastIndexOfSuffix = buttonId.toUpperCase().lastIndexOf(
        BUTTON_IDENTIFICATOR_SUFFIX)) != -1
        || (lastIndexOfSuffix = buttonId.toUpperCase().lastIndexOf(
            BUTTON_IDENTIFICATOR_SUFFIX)) != -1) {
      buttonId = buttonId.substring(0, lastIndexOfSuffix);
    }
    return buttonId;
  }

  /**
   * Получение идентификатора отображаемого значения для выпадающего списка
   * 
   * @param comboBoxId  идентификатор комбобокса
   * @return поле отображаемого значения
   */
  public static String getDisplayValueForComboBox(String comboBoxId) {
    return multipleConcat(subtractFieldSuffix(comboBoxId).toUpperCase(),
        DISPLAY_VALUE_SUFFIX);
  }

  /**
   * Получение списка полей с опциями из списка всех полей
   * 
   * @param fields  список всех полей
   * @return список полей, являющихся либо выпадающим списком, либо листфилдом
   */
  public static List<ModuleField> getOptionField(List<ModuleField> fields) {
    List<ModuleField> optionFields = new ArrayList<ModuleField>();
    for (ModuleField moduleField : fields) {
      if (JEP_COMBOBOX_FIELD.equalsIgnoreCase(moduleField
          .getFieldWidget())
          || JEP_LIST_FIELD.equalsIgnoreCase(moduleField
              .getFieldWidget()))
        optionFields.add(moduleField);
    }
    return optionFields;
  }

  /**
   * Формирование URL вида <протокол>://<сервер>:<порт>
   * 
   * @param protocol   протокол
   * @param serverName сервер
   * @param serverPort порт
   * @return строка вида <протокол>://<сервер>:<порт>
   * @throws Exception
   */
  public static String prepareServerUrl(String protocol, String serverName,
      String serverPort) throws Exception {

    if (isEmptyOrNotInitializedParameter(serverName))
      throw new Exception("Server name must be declared!");
    if (isEmpty(protocol))
      protocol = HTTP_PROTOCOL;
    if (isEmptyOrNotInitializedParameter(serverPort))
      serverPort = DEFAULT_HTTP_PORT;

    return multipleConcat(protocol, "://", serverName, ":", serverPort);
  }

  /**
   * Получение имени сервера из пути деплоя
   * 
   * @param deployPath   Путь развертывания приложения
   * @return имя сервера
   */
  public static String getServerName(String deployPath) {
    Integer beginIndex = (deployPath.contains(OPMN_PROTOCOL) ? (multipleConcat(
        OPMN_PROTOCOL, "://")).length() : 0);
    for (int i = beginIndex; i < deployPath.length(); i++) {
      if (String.valueOf(deployPath.charAt(i)).equals(PATH_SEPARATOR)
          || String.valueOf(deployPath.charAt(i)).equals(":")) {
        return deployPath.substring(beginIndex, i);
      }
    }
    return deployPath;
  }

  /**
   * Валидация версии
   * 
   * @param version  проверяемая версия
   * @return флаг валидности
   */
  public static boolean checkVersion(String version) throws Exception {
    Pattern pattern = Pattern.compile("^[\\d]+([\\.][\\d]+)*$");
    boolean flag = !isEmpty(version) && pattern.matcher(version).matches();
    if (!flag) {
      throw new Exception("Not correct version");
    }
    return flag;
  }

  /**
   * Получение версии из строки
   * 
   * @param version  строка
   * @return версия в формате цифр, разделенных точками
   */
  public static String getInstallVersion(String version) throws Exception {
    if (isEmpty(version))
      return null;
    else {
      if (version.contains("\\"))
        version = version.split("\\")[1];
      if (checkVersion(version))
        return version;
    }
    return null;

  }

  /**
   * Запуск цели Ant по ее наименованию
   * 
   * @param targetName   наименование цели
   * @param properties   указание необходимых пользовательских свойств для выполнения данной цели
   */
  public static void runAntTarget(String targetName, Pair<String, String>... properties) {
    File buildFile = new File(BUILD_FILE);
    Project p = new Project();
    p.setUserProperty("ant.file", buildFile.getAbsolutePath());
    
    loadPropertiesFromFile(p, "build.properties");
    loadPropertiesFromFile(p, "deploy.properties");
    loadPropertiesFromFile(p, "local.properties");
    // инициализируем кастомные системные свойства, необходимые для выполнения таска
    for (Pair<String, String> prop : properties) {
      p.setUserProperty(prop.getKey(), prop.getValue());
    }
    boolean defaultTarget = isEmpty(targetName) || 
        BUILD_AND_DEPLOY_TASK_TARGET.equalsIgnoreCase(targetName);
    // для умолчательной цели делаем детальный вывод в консоль (для возможности отслеживания операций)
    if (defaultTarget) {
      DefaultLogger consoleLogger = new DefaultLogger();
      consoleLogger.setErrorPrintStream(System.err);
      consoleLogger.setOutputPrintStream(System.out);
      consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
      p.addBuildListener(consoleLogger);
    }
    try {
      p.fireBuildStarted();
      p.init();
      ProjectHelper helper = ProjectHelper.getProjectHelper();
      p.addReference("ant.projectHelper", helper);
      helper.parse(p, buildFile);
      p.executeTarget(defaultTarget ? p.getDefaultTarget() : targetName);
      p.fireBuildFinished(null);
    } catch (BuildException e) {
      p.fireBuildFinished(e);
    }
  }

  public static void loadPropertiesFromFile(Project p, String fileName) {
    File buildPropertiesFile = new File(fileName);
    if (buildPropertiesFile.exists())
      try {
        Properties props = new Properties();
        props.load(new FileInputStream(buildPropertiesFile));
        for (Object k : props.keySet()){
          String key = (String) k; 
          String value = props.getProperty(key);
          if (!JepRiaToolkitUtil.isEmptyOrNotInitializedParameter(value.split("/")[0])) {
            p.setUserProperty(key, value);
          }
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
  }

  /**
   * Функция определяет: является ли переданная ей строка пустой.
   * 
   * @param sourceString исходная строка, которую проверяем
   * 
   * @return возвращает true, если передано значение null или переданная
   *         строка состоит только из пробелов.
   */
  public static boolean isEmpty(String sourceString) {
    return sourceString == null || sourceString.trim().length() == 0;
  }
  
  /**
   * Функция определяет: является ли переданное ей число пустым или неопределенным.
   * 
   * @param sourceInteger
   *            исходное число, которое проверяем
   * 
   * @return возвращает true, если передано значение null или переданное
   *         значение не определено
   */
  public static boolean isEmpty(Integer sourceInteger) {
    return sourceInteger == null || sourceInteger.intValue() == UNDEFINED_INT;
  }
  
  /**
   * Функция определяет: является ли переданное ей число пустым или неопределенным.
   * 
   * @param sourceList
   *            исходное число, которое проверяем
   * 
   * @return возвращает true, если передано значение null или переданное
   *         значение не определено
   */
  public static boolean isEmpty(List<?> sourceList) {
    return sourceList == null || sourceList.isEmpty();
  }

  /**
   * Функция определяет: является ли переданный объект пустым.
   * 
   * @param obj   проверяемый объект
   */
  public static boolean isEmpty(Object obj) {
    if (obj == null) {
      return true;
    } else if (obj instanceof String) {
      return isEmpty((String) obj);
    } else if (obj instanceof Integer) {
      return isEmpty((Integer) obj);
    }
    else if (obj instanceof Integer) {
      return isEmpty((Integer) obj);
    }
    else if (obj instanceof List) {
      return isEmpty((List<?>) obj);
    }
    return false;
  }

  /**
   * Функция определяет: является ли переданная ей пустой строкой или
   * непроинициализированным параметром комманды.
   * 
   * @param sourceString   исходная строка, которую проверяем
   * 
   * @return возвращает true, если передано значение null или переданная
   *         строка состоит только из пробелов, а также строка является
   *         параметром Ant. Пример, ${PORT}, ${MODULE_NAME} и т.д.
   */
  public static boolean isEmptyOrNotInitializedParameter(String sourceString) {
    return isEmpty(sourceString) ? true : isNotInitializedParameter(sourceString);
  }
  
  public static boolean isNotInitializedParameter(String sourceString) {
    return !isEmpty(sourceString) && sourceString.startsWith("${") && sourceString.endsWith("}");
  }

  /**
   * Проверяет является ли workstate состоянием формы редактирования
   * 
   * @param workstate проверямое состояние
   * @return true, если проверяемое состояние принадлежит форме редактирования
   */
  public static boolean isEditFormState(WorkstateEnum workstate) {
    return SEARCH.equals(workstate) || CREATE.equals(workstate)
        || EDIT.equals(workstate) || VIEW_DETAILS.equals(workstate);
  }

  /**
   * Кодирование строки
   * 
   * @param inputString      кодируемая строка
   * @return кодированная строка
   * @throws UnsupportedEncodingException
   */
  public static String encode(String inputString)
    throws UnsupportedEncodingException {
    inputString = URLEncoder.encode(inputString, UTF_8);
    char[] chars = inputString.toCharArray();
    StringBuffer hex = new StringBuffer();
    for (int i = 0; i < chars.length; i++) {
      hex.append(Integer.toHexString((int) chars[i]));
    }
    return hex.toString();
  }

  /**
   * Декодирование строки
   * 
   * @param decodeString      декодируемая строка
   * @return раскодированная строка
   * @throws UnsupportedEncodingException
   */
  public static String decode(String decodeString) 
    throws UnsupportedEncodingException {
    StringBuilder sb = new StringBuilder();
    StringBuilder temp = new StringBuilder();
    // 49204c6f7665204a617661 split into two characters 49, 20, 4c...
    for (int i = 0; i < decodeString.length() - 1; i += 2) {
      // grab the hex in pairs
      String output = decodeString.substring(i, (i + 2));
      // convert hex to decimal
      int decimal = Integer.parseInt(output, 16);
      // convert the decimal to character
      sb.append((char) decimal);
      temp.append(decimal);
    }
    return URLDecoder.decode(sb.toString(), UTF_8);
  }
  
  /**
   * Сортировка полей на основании их следования в описании списочной и детальной форм
   * 
   * @param fields        список полей модуля
   * @param isDetailForm      признак поле детальной формы или списочной формы
   * @return отсортированный список полей
   */
  public static List<ModuleField> sortFields(List<ModuleField> fields, final boolean isDetailForm){
    List<ModuleField> result = new ArrayList<ModuleField>();
    for (ModuleField moduleField : fields){
      if (!isEmpty(moduleField.getFormIndex(isDetailForm))){
        result.add(moduleField);
      } 
    }
    Collections.sort(result, new Comparator<ModuleField>() {
      public int compare(ModuleField   moduleField1, ModuleField moduleField2){
        return moduleField1.getFormIndex(isDetailForm).compareTo(moduleField2.getFormIndex(isDetailForm));  
      }
    });
    return result; 
  }
  
  /**
   * Возврат строки без пробелов
   * 
   * @param str  преобразуемая строка
   * @return строка без пробельных символов
   */
  public static String getStringWithoutWhitespaces(String str){
    return isEmpty(str) ? str : str.replaceAll(REGEXP_FOR_BLANK, "");
  }
  
  /**
   * Метод преобразования идентификатора к адекватному наименованию
   * 
   * @param fieldId      идентфикатор
   * @return адекватное название идентификатора
   */
  public static String getNameFromID(String fieldId){
    if (isEmpty(fieldId)) return "";
    
    String[] results = fieldId.toLowerCase().replaceAll(UNDERSCORE, WHITE_SPACE).split(REGEXP_FOR_BLANK);
    StringBuilder result = new StringBuilder();
    for (String res : results){
      result
        .append(isEmpty(result.toString()) ? "" : WHITE_SPACE)
        .append(initCap(res));
    }
    return result.toString();
  }
  
  public static String appendIfHasNot(String base, String fragment) {
    return base.contains(fragment) ? "" : fragment;
  }
  
  public static String replacePackageModuleNames(String source, String packageName, String moduleName) {
    source = source.replaceAll("<%PackageName%>", packageName);
    source = source.replaceAll("<%packagename%>", packageName.toLowerCase());
    
    source = source.replaceAll("<%ModuleName%>", moduleName);
    source = source.replaceAll("<%modulename%>", moduleName.toLowerCase());
    
    return source;
  }
  
  public static void checkParameter(String paramName, String errorMessage) {
    if (isEmptyOrNotInitializedParameter(paramName)) {
      throw new BuildException(errorMessage);
    }
  }
  
  public static String getDefinitionProperty(String property, String defaultValue, ResourceBundle... appResourceBundles){
    String value = null;
    try {
      value = (appResourceBundles.length == 0 ? ApplicationDefinition.LAST.getResource() : appResourceBundles[0]).getString(property);
    }
    catch(Exception e){}
    return isEmpty(value) ? defaultValue : value;
  }
  
  public static String getApplicationDefinitionFile(){
    File file = new File(currentSourceDirectory());
    // filter files in current directory which match the requirements
    File[] appropriateFiles = file.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname.getName().endsWith(APPLICATION_SETTING_FILE_ENDING);
      }
    });
    if (appropriateFiles.length > 1){
      throw new BuildException("You should directly determine file for application definition with help -DAPPLICATION_STRUCTURE_FILE_PATH argument in command line!");
    }
    else if (appropriateFiles.length == 0){
      throw new BuildException("File of application definition not found in current directory!");
    }
    else {
      return appropriateFiles[0].getName();
    }
  }
  
  /**
   * Извлечение на основе регулярного выражения недостающих директорий файловой структуры и имени файлов
   * 
   * @param regexpPattern  шаблон регулярного выражения
   * @return список директорий и имен файлов
   */
  public static List<String> extractFileNamesByPattern(String regexpPattern){
    String[] parts = regexpPattern.split(PATH_SEPARATOR);
    List<String> result = new ArrayList<String>();
    String currentPath = multipleConcat(currentSourceDirectory(), PATH_SEPARATOR);
    Integer currentIndex = 0;
    for (int i = 0; i < parts.length; i++){
      String part = parts[i];
      if (i == parts.length - 1 && part.contains(LEFT_CURLY_BRACKET)){
        final boolean isPart = !part.matches(FILE_STRUCTURE_PATTERN);
        if (isPart){
          Pattern directoryPattern = Pattern.compile(FILE_STRUCTURE_PATTERN);
          Matcher m = directoryPattern.matcher(part);
          while (m.find()){
            Integer index = Integer.decode(m.group(1));
            if (index == currentIndex) {
              currentIndex++;
            }
            else {
              echoMessage(multipleConcat(ERROR_PREFIX, "Regexp '", regexpPattern, "' is incorrect. Check conditions!"));
              break;
            }
          }
        }
        final String patt = part;
        File[] files = new File(currentPath).listFiles(new FileFilter() {
          @Override
          public boolean accept(File file) {
            String convertValue = patt.replaceAll("\\{(.*?)\\}", "(.*)");
            return isPart ? file.isFile() && file.getName().matches(convertValue) : file.isDirectory();
          }
        });
        if (files == null)
          throw new BuildException(multipleConcat("The path '", regexpPattern, "' doesn't exist on the file system!"));
        
        if (files.length > 0){
          part = files[0].getName();
          if (isPart){
            Pattern directoryPattern = Pattern.compile(patt.replaceAll("\\{(.*?)\\}", "(.*)"));
            Matcher m = directoryPattern.matcher(part);
            while (m.find()){
              for (int ind = 1; ind <= m.groupCount(); ind++){
                String value = m.group(ind);
                result.add(value);
              }
            }
          }
          else {
            result.add(part);
          }
        }
      }
      else if (part.matches(FILE_STRUCTURE_PATTERN)){
        Pattern directoryPattern = Pattern.compile(FILE_STRUCTURE_PATTERN);
        Matcher m = directoryPattern.matcher(part);
        if (m.find()){
          Integer index = Integer.decode(m.group(1));
          if (index == currentIndex) {
            currentIndex++;
          }
          else {
            echoMessage(multipleConcat(ERROR_PREFIX, "Regexp '", regexpPattern, "' is incorrect. Check conditions!"));
            break;
          }
        }
        File[] files = new File(currentPath).listFiles(new FileFilter() {
          @Override
          public boolean accept(File file) {
            return file.isDirectory();
          }
        });
        part = files[0].getName();
        result.add(part);
      }
      currentPath += part + PATH_SEPARATOR;
    }
    return result;
  }
  
  /**
   * Подстановка значений директорий и имен файлов в передаваемом шаблоне
   * 
   * @param pattern    шаблон регулярного выражения
   * @return путь файловой системы
   */
  public static String convertPatternInRealPath(String pattern){
    return format(pattern, extractFileNamesByPattern(pattern).toArray());
  }
  
  /**
   * Подстановка значений директорий и имен файлов в передаваемом шаблоне и подавление возможного исключения, 
   * поскольку файла с указанным путем возможно не существует, что является штатной ситуацией.
   * 
   * @param pattern    шаблон регулярного выражения
   * @return путь файловой системы
   */
  public static String convertPatternInRealPathSupressException(String pattern){
    try {
      return format(pattern, extractFileNamesByPattern(pattern).toArray());
    }
    catch(BuildException e){
      return pattern;
    }
  }
  /**
   * Получение имени приложения из конфигурационного файла application.xml
   * 
   * @param applicationXmlPath путь до конфигурационного файла application.xml
   * 
   * @return наименование приложения
   */
  public static String getApplicationName(String applicationXmlPath) {
    Document doc;
    try {
      doc = getDOM(multipleConcat(currentSourceDirectory(), PATH_SEPARATOR, applicationXmlPath));
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, MODULE_TAG_NAME, PATH_SEPARATOR, "web", PATH_SEPARATOR, "context-root//text()"));
      return (String) expr.evaluate(doc, XPathConstants.STRING);
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, "File '", normalizePath(applicationXmlPath), "' is incorrect! ", e.getLocalizedMessage()));
    }
    finally {
      doc = null;
    }
    return null;
  }
  
  /**
   * Получение списка модулей приложения из main.gwt.xml
   * 
   * @param mainGwtXmlPath путь до main.gwt.xml
   * 
   * @return список модулей
   */
  public static List<String> getModuleNames(String mainGwtXmlPath) {
    Document doc;
    List<String> result = new ArrayList<String>();
    try {
      doc = getDOM(multipleConcat(currentSourceDirectory(), PATH_SEPARATOR, mainGwtXmlPath));
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, INHERITS_MAIN_GWT_XML_TAG_NAME, "[starts-with(@name,'com.technology')]"));
      NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
      if (nodes != null){
        for (int i = 0; i < nodes.getLength(); i++){
          Node node = nodes.item(i); 
          if (node.getNodeType() == Node.ELEMENT_NODE){
            Element el = (Element) node;
            String nameAttr = el.getAttribute("name");
            if (nameAttr.startsWith("com.technology") && 
                !nameAttr.equalsIgnoreCase("com.technology.jep.jepria.JepRia")){
              result.add(nameAttr.substring(nameAttr.lastIndexOf(DOT) + 1));
            }
          }
        }
      }
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, "File '", normalizePath(mainGwtXmlPath), "' is incorrect! ", e.getLocalizedMessage()));
    }
    finally {
      doc = null;
    }
    return result;
  }
  
  /**
   * Преобразование объектной модели в виде XML-файла 
   * 
   * @param object      преобразуемый объект
   * @param fileNameOrPath  путь до файла
   */
  public static void convertToXml(Object object, String fileNameOrPath){
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      // output pretty printed
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, WIN_CHARSET);
      jaxbMarshaller.marshal(object, getXMLSerializer(fileNameOrPath).asContentHandler());
    } catch (JAXBException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static Element convertModuleToXml(String moduleName, String applicationName){
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Module.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      // output pretty printed
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, WIN_CHARSET);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      jaxbMarshaller.marshal(Module.createModule(moduleName, applicationName), baos);
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      DocumentBuilder db = createDocumentBuilder();
      Document doc = db.parse(bais);
      Element root = doc.getDocumentElement();
      bais.close();
      baos.close();
      return root;
    } catch (JAXBException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Получение потока для мэппинга состояния объекта к xml-файлу.
   * 
   * @param fileNameOrPath    путь до xml-файла
   * @return поток для сериализации
   * @throws FileNotFoundException ошибка при сохранении объекта во внешний файл
   */
  private static XMLSerializer getXMLSerializer(String fileNameOrPath) throws FileNotFoundException {
        // configure an OutputFormat to handle CDATA
        OutputFormat of = new OutputFormat();
        // specify which of your elements you want to be handled as CDATA.
        // The use of the '^' between the namespaceURI and the localname
        // seems to be an implementation detail of the xerces code.
        // When processing xml that doesn't use namespaces, simply omit the
        // namespace prefix as shown in the third CDataElement below.
        of.setCDataElements(new String[] {"^".concat(PRESENTER_BOBY_TAG_NAME) });   // <presenterBody>
        of.setEncoding(WIN_CHARSET);
        // set any other options you'd like
        of.setIndenting(true);
        // create the serializer
        XMLSerializer serializer = new XMLSerializer(of);
        try {
      serializer.setOutputCharStream(new OutputStreamWriter(new FileOutputStream(fileNameOrPath), WIN_CHARSET));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

        return serializer;
    }
  
  /**
   * Преобразование объектной модели к DOM-представлению
   * 
   * @param object      преобразуемый объект
   * @param doc        существующее DOM-дерево
   */
  public static Document appendModuleToApplication(Application application, Module newModule){
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(application.getClass());
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      // output pretty printed
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, WIN_CHARSET);
      Document doc = createDOM();
      application.getModules().getModules().add(newModule);
      jaxbMarshaller.marshal(application, doc);
      return doc;
    } catch (JAXBException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Нормализация пути к файлу.
   * 
   * @param path  нормализуемый путь
   * @return нормализованный путь до файла
   */
  public static String normalizePath(String path){
    return new File(path).toURI().normalize().toString();
  }
  
  /**
   * Получение абсолютного пути на директории исходных кодов проекта.
   * 
   * @return абсолютный путь до директории src
   */
  public static String currentSourceDirectory(){
    return System.getProperty(CURRENT_DIRECTORY_ENVIRONMENT_VARIABLE);
  }
  
  public static ResourceBundle getResourceByPath(String filePath){
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), UTF_8));
      return new PropertyResourceBundle(reader);
    } catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      if (reader != null)
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    return null;
  }
  
  public static String extractStringFromQuotes(String strWithQuotes){
    return strWithQuotes.replaceAll("\"", "");
  }
  
  /**
   * Сравнение объектов на равенство.
   * 
   * @param obj1      первый сравниваемый объект
   * @param obj2      второй сравниваемый объект
   * @return признак равенства сравниваемых объектов
   */
  public static boolean equalWithNull(Object obj1, Object obj2) {
    if (obj1 == obj2) {
      return true;
    } else if (obj1 == null) {
      return false;
    } else {
      return obj1.equals(obj2);
    }
  }  

  /**
   * Конвертация текстовых ресурсов
   */
  public static void encodeTextResources() {
    runAntTarget(ALL_TEXT_ENCODE_TASK_TARGET);
  }
  
  /**
   * Создание структуры приложения с указанием файла для сборки
   * 
   * @param fileName    наименование файла сборки
   */
  public static void createApplicationStructure(String fileName) {
    runAntTarget(CREATE_STRUCTURE_TASK_TARGET, new Pair<String, String>(APPLICATION_STRUCTURE_FILE_PATH_TASK_ATTRIBUTE, fileName));
  }
  
  /**
   * Сборка и развертывание приложения
   */
  public static void buildAndDeploy() {
    runAntTarget(null);
  }
  
  public static void buildAndDeployWithCustomConfigName(String configName) {
    runAntTarget(BUILD_AND_DEPLOY_TASK_TARGET, new Pair<String, String>(BUILD_CONFIG_PROPERTY, configName));
  }
  
  /**
   * Сборка и развертывание приложения
   */
  public static void antClean() {
    runAntTarget(CLEAN_TASK_TARGET);
  }
}
