package com.technology.jep.jepria.server.util;

import static com.technology.jep.jepria.server.JepRiaServerConstant.HTTP_REQUEST_PARAMETER_LANG;
import static com.technology.jep.jepria.server.JepRiaServerConstant.LOCALE_KEY;
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;
import static com.technology.jep.jepria.shared.JepRiaConstant.HTTP_REQUEST_PARAMETER_LOCALE;
import static com.technology.jep.jepria.shared.JepRiaConstant.LOCAL_LANG;

import static com.technology.jep.jepria.shared.util.JepRiaUtil.isEmpty;

import java.io.BufferedReader;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.technology.jep.jepria.shared.exceptions.SystemException;
import com.technology.jep.jepria.shared.record.lob.JepClob;

/**
 * Класс содержащий вспомогательные/полезные функции.
 * 
 * @version 1.0
 */
public class JepServerUtil {
  /**
   * The User-Agent Http header.
   */
  private static final String USER_AGENT_HEADER = "User-Agent";
  
  /**
   * Стандартный форматировщик дат.<br/>
   * Преобразует строки в даты и даты в строки формата {@link com.technology.jep.jepria.shared.JepRiaConstant#DEFAULT_DATE_FORMAT}.
   */
  public static final SimpleDateFormat defaultDateFormatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
  static { defaultDateFormatter.setLenient(false); }

  /**
   * Карта соответствия mime-типов наиболее используемым расширениям файлов.
   */
  static private Map<String, String> fileExtensionMimeTypeMap = new HashMap<String, String>();
  static {
    fileExtensionMimeTypeMap.put("au", "audio/basic");
    fileExtensionMimeTypeMap.put("avi", "video/x-msvideo");
    fileExtensionMimeTypeMap.put("bin", "application/octet-stream");
    fileExtensionMimeTypeMap.put("bmp", "image/bmp");
    fileExtensionMimeTypeMap.put("crt", "application/x-x509-ca-cert");
    fileExtensionMimeTypeMap.put("css", "text/css");
    fileExtensionMimeTypeMap.put("dll", "application/x-msdownload");
    fileExtensionMimeTypeMap.put("doc", "application/msword");
    fileExtensionMimeTypeMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    fileExtensionMimeTypeMap.put("odt", "application/vnd.oasis.opendocument.text");
    fileExtensionMimeTypeMap.put("dot", "application/msword");
    fileExtensionMimeTypeMap.put("dvi", "application/x-dvi");
    fileExtensionMimeTypeMap.put("exe", "application/octet-stream");
    fileExtensionMimeTypeMap.put("gif", "image/gif");
    fileExtensionMimeTypeMap.put("gz", "application/x-gzip");
    fileExtensionMimeTypeMap.put("gzip", "application/x-gzip");
    fileExtensionMimeTypeMap.put("hlp", "application/winhlp");
    fileExtensionMimeTypeMap.put("htm", "text/html");
    fileExtensionMimeTypeMap.put("html", "text/html");
    fileExtensionMimeTypeMap.put("htmls", "text/html");
    fileExtensionMimeTypeMap.put("htt", "text/webviewhtml");
    fileExtensionMimeTypeMap.put("htx", "text/html");
    fileExtensionMimeTypeMap.put("ico", "image/x-icon");
    fileExtensionMimeTypeMap.put("jpe", "image/jpeg");
    fileExtensionMimeTypeMap.put("jpeg", "image/jpeg");
    fileExtensionMimeTypeMap.put("jpg", "image/jpeg");
    fileExtensionMimeTypeMap.put("java", "text/plain");
    fileExtensionMimeTypeMap.put("js", "application/x-javascript");
    fileExtensionMimeTypeMap.put("mov", "video/quicktime");
    fileExtensionMimeTypeMap.put("mp2", "video/mpeg");
    fileExtensionMimeTypeMap.put("mp3", "audio/mpeg");
    fileExtensionMimeTypeMap.put("mpeg", "video/mpeg");
    fileExtensionMimeTypeMap.put("mpg", "video/mpeg");
    fileExtensionMimeTypeMap.put("mpp", "application/vnd.ms-project");
    fileExtensionMimeTypeMap.put("pdf", "application/pdf");
    fileExtensionMimeTypeMap.put("png", "image/png");
    fileExtensionMimeTypeMap.put("ppt", "application/vnd.ms-powerpoint");
    fileExtensionMimeTypeMap.put("ps", "application/postscript");
    fileExtensionMimeTypeMap.put("qt", "video/quicktime");
    fileExtensionMimeTypeMap.put("rtf", "application/rtf");
    fileExtensionMimeTypeMap.put("sh", "application/x-sh");
    fileExtensionMimeTypeMap.put("shtml", "text/html");
    fileExtensionMimeTypeMap.put("tar", "application/x-tar");
    fileExtensionMimeTypeMap.put("tex", "application/x-tex");
    fileExtensionMimeTypeMap.put("tif", "image/tiff");
    fileExtensionMimeTypeMap.put("tiff", "image/tiff");
    fileExtensionMimeTypeMap.put("txt", "text/plain");
    fileExtensionMimeTypeMap.put("vrml", "x-world/x-vrml");
    fileExtensionMimeTypeMap.put("wav", "audio/x-wav");
    fileExtensionMimeTypeMap.put("xla", "application/vnd.ms-excel");
    fileExtensionMimeTypeMap.put("xlc", "application/vnd.ms-excel");
    fileExtensionMimeTypeMap.put("xlm", "application/vnd.ms-excel");
    fileExtensionMimeTypeMap.put("xls", "application/vnd.ms-excel");
    fileExtensionMimeTypeMap.put("xlt", "application/vnd.ms-excel");
    fileExtensionMimeTypeMap.put("xlw", "application/vnd.ms-excel");
    fileExtensionMimeTypeMap.put("csv", "application/vnd.ms-excel");
    fileExtensionMimeTypeMap.put("xml", "text/xml");
    fileExtensionMimeTypeMap.put("zip", "application/zip");
  };

  /**
   * Параметр / JNDI-имя, в котором передается URL контекст для формирования
   * полного URL для вызова EJB модуля. Например, для полного URL вызова EJB
   * вида "ormi://localhost:23791/Ejb3Template" в данном параметре будет
   * содержаться "ormi://localhost:23791". Данная константа используется для
   * вызова EJB расположенного на том же инстансе OC4J, что и вызывающий
   * модуль.<br/>
   * Если необходимо вызвать EJB расположенный на другом инстансе и (или)
   * Application сервере, то в константах модуля создается дополнительная
   * (-ые) константы вида: <br/>
   * <code>
   * static final String ENVIRONMENT_EJB_URL_CONTEXT_XXXX = "ejbUrlContextXxxx";
   * </code> <br/>
   * , где Xxxx - "логическое" (осмысленное) имя другого инстанса и (или)
   * Application сервера.
   */
  public static final String ENVIRONMENT_EJB_URL_CONTEXT = "ejbUrlContext";

  /**
   * Получение Remote-интерфейса EJB.
   * 
   * @param appLogin пользователь Application Сервера, под которым происходит
   *            подключение к EJB
   * @param appPassword пароль пользователя Application Сервера, под которым
   *            происходит подключение к EJB
   * @param ejbUrlContext URL контекст для формирования полного URL для вызова
   *            EJB модуля. Например, для полного URL вызова EJB вида
   *            "ormi://localhost:23791/ejb3Template" в данном параметре будет
   *            содержаться "ormi://localhost:23791".
   * @param ejbName имя интерфейса. Например, для
   *            "ormi://localhost:23791/ejb3Template" в данном параметре будет
   *            содержаться "ejb3Template".
   * @param beanName имя бина. Например, для
   *            "ormi://localhost:23791/ejb3Template" в данном параметре будет
   *            содержаться "Ejb3TemplateBean".
   * 
   * @return Remote-интерфейс EJB
   * @throws NamingException
   */
  public static Object ejbLookup(String appLogin, String appPassword, String ejbUrlContext, String ejbName, String beanName) throws NamingException {

    Context initial = null;
    Hashtable<String, String> env = new Hashtable<String, String>();

    env.put("java.naming.factory.initial", "oracle.j2ee.rmi.RMIInitialContextFactory");
    env.put("java.naming.provider.url", ejbUrlContext + "/" + ejbName);
    env.put("java.naming.security.principal", appLogin);
    env.put("java.naming.security.credentials", appPassword);

    initial = new InitialContext(env);

    return initial.lookup(beanName);
  }

  /**
   * Получение Local-интерфейса EJB.
   * 
   * @param beanName имя бина. Например, для
   *            "java:comp/env/ejb/Ejb3TemplateBean" в данном параметре будет
   *            содержаться "Ejb3TemplateBean".
   * 
   * @return Local-интерфейс EJB
   * @throws NamingException
   */
  public static Object ejbLookup(String beanName) throws NamingException {
    return new InitialContext().lookup("java:comp/env/ejb/" + beanName);
  }

  /**
   * Считывание объекта из JNDI по его полному имени.
   * 
   * @param username имя пользователя, имеющего доступ на чтение JNDI
   * @param password пароль
   * @param path полное JNDI-имя объекта
   * @return значение объекта
   */
  public static Object jndiLookup(String username, String password, String path) {
    Object result = null;
    String ejbUrlContext = JepServerUtil.getEnvironmentValue(ENVIRONMENT_EJB_URL_CONTEXT);

    if (ejbUrlContext != null) {
      ejbUrlContext = ejbUrlContext.replace('\\', '/'); // Обход багофичи Oracle AS (там в Environment Variables '//' заменяет на '\\').

      Hashtable<String, String> env = new Hashtable<String, String>();
      env.put(Context.INITIAL_CONTEXT_FACTORY, "oracle.j2ee.rmi.RMIInitialContextFactory");
      env.put(Context.PROVIDER_URL, ejbUrlContext);
      env.put(Context.SECURITY_PRINCIPAL, username);
      env.put(Context.SECURITY_CREDENTIALS, password);
      try {
        InitialContext ic = new InitialContext(env);
        result = ic.lookup(path);
      } catch (NamingException ex) {
        throw new SystemException("JNDI object having path '" + path + "' not found", ex);
      }
    } else {
      throw new SystemException("'ejbUrlContext' environment variable should be specified", null);
    }

    return result;
  }

  /**
   * Получение значения переменной окружения.
   * 
   * @param name имя переменной
   * 
   * @return строковое значение параметра
   */
  public static String getEnvironmentValue(String name) {
    return System.getenv(name);
  }

  /**
   * Функция определяет: является ли текущий язык основным языком для пользователей.
   * 
   * @param request запрос, используя который из сессии получим текущий язык
   * 
   * @return возвращает true, если текущий язык является основным языком для
   *         пользователей. В противном случае возвращает false.
   */
  public static boolean isLocalLang(HttpServletRequest request) {
    return getLocale(request).getLanguage().equals((new Locale(LOCAL_LANG, "")).getLanguage());
  }

  /**
   * Получение локали следующими шагами:<br/>
   * 1) из параметра запроса по ключу locale (приоритет перед lang, поскольку GWT для клиента использует именно locale)<br/>
   * 2) из параметра запроса по ключу lang<br/>
   * 3) из параметра сессии<br/>
   * 4) из запроса<br/>
   * 5) локаль по умолчанию: ru<br/><br/>
   * 
   * Первая найденная локаль возвращается и сохраняется в сессию.
   * 
   * @param request HTTP-запрос
   * @return локаль
   */
  public static Locale getLocale(HttpServletRequest request) {
    Locale locale;
    
    String lang = (String) request.getParameter(HTTP_REQUEST_PARAMETER_LOCALE);
    if (lang == null) {
      lang = (String) request.getParameter(HTTP_REQUEST_PARAMETER_LANG);
      if (lang == null) {
        locale = (Locale) request.getSession().getAttribute(LOCALE_KEY);
        if (locale == null) {
          locale = request.getLocale();
          if (locale == null) {
            locale = new Locale(LOCAL_LANG);
          }
        }
      } else {
        locale = new Locale(lang);
      }
    } else {
      locale = new Locale(lang);
    }
    
    request.getSession().setAttribute(LOCALE_KEY, locale);
    
    return locale;
  }
  
  public static String detectMimeType(String fileExtension) {
    return fileExtension != null ? fileExtensionMimeTypeMap.get(fileExtension.toLowerCase()) : null;
  }
  
  public static String getServerUrl(HttpServletRequest request) {
    return request.getScheme().toLowerCase()
        + "://"
        + request.getServerName()
        + ":"
        + request.getServerPort();
  }
    
  /**
   * Возвращает имя приложения.
   * Достигается путем вызова {@link ServletContext#getContextPath()}.
   * 
   * @param context контекст сервлета
   * @return имя приложения
   */
  public static String getApplicationName(ServletContext context) {
    String[] splitted = context.getContextPath().split("/");
    return splitted[splitted.length - 1];
  }
  
  /**
   * Возвращает имя модуля, предназначенное для передачи в базу.
   * Имя модуля формируется из имени приложения и имени сервлета,
   * из которого вырезано слово &quot;Servlet&quot;.
   * @param config конфигурация сервлета
   * @return имя модуля
   */
  public static String getModuleName(ServletConfig config) {
    String applicationName = getApplicationName(config.getServletContext());
    String servletName = config.getServletName();
    String applicationModuleName = servletName.replace("Servlet", "");
    return applicationName + "." + applicationModuleName;
  }

  public static boolean isTomcat(HttpServletRequest request) {
    boolean result = false;
    try {
      result = request.getServletContext().getServerInfo().toLowerCase().contains("tomcat");
    } catch(java.lang.NoSuchMethodError ex) {
      // OAS doesn't support method getServletContext() of request object
    }
    
    return result;
  }
  
  /**
   * Формирует {@link com.technology.jep.jepria.shared.record.lob.JepClob} из {@link java.sql.Types#CLOB}.
   * @param clob
   * @return JepClob
   */
  public static JepClob toJepClob(Clob clob){
    if (clob == null) throw new NullPointerException();
    
    StringBuilder str = new StringBuilder();
    
    disableClobPrefetch(clob);
    
    try (BufferedReader bufferRead = new BufferedReader(clob.getCharacterStream())) {
      String bufferStr;
      while ((bufferStr = bufferRead.readLine()) != null) {
        str.append(bufferStr);
      }
    } catch (Exception  e) {
      e.printStackTrace();
    }
    
    return new JepClob(str.toString());
  }
  
  /**
   * Выставление флага {@code activePrefetch false} в объекте oracle.sql.CLOB
   * <br><br>
   * Метод &mdash; обход бага в ojdbc6, ojdbc8: при переходе на версию DB oracle 12.2 
   * становится некорректной кодировка потока чтения из объекта oracle.sql.CLOB
   * (в версии 12.1 баг не наблюдается).
   * <br><br>
   * Подробная трассировка бага:
   * <pre>
java.sql.Clob javaClob = ... ;
java.io.Reader reader = javaClob.getCharacterStream();
reader.read();
-> oracle.jdbc.driver.OracleClobReader.read() // because instanceof oracle.jdbc.driver.OracleClobReader
  -> oracle.jdbc.driver.OracleClobReader.needChars()
    -> oracle.jdbc.OracleClob.getChars()
      -> oracle.sql.CLOB.getChars() // because instanceof oracle.sql.CLOB
        -> oracle.sql.ClobDBAccess.getChars()
          -> oracle.jdbc.driver.T4CConnection.getChars() // because instanceof oracle.jdbc.driver.T4CConnection
            -> { ...
                 if (oracle_sql_CLOB.isActivePrefetch()) {
                   // read prefetched data. <b>This data is getting encoded incorrectly</b>
                 }
                 ...
                 clobMsg.read(...); // read the rest data of the clob. This data is getting encoded correctly
                 ...
               }
               // Выставление флага activePrefetch false заставляет данные клоба 
               // считываться полностью методом clobMsg.read, в правильной кодировке 
   * </pre>
   * @param clob
   * @deprecated удалить данный метод вместе со всеми его вызовами, если баг починится компанией oracle в {@code ojdbc.jar}
   */
  @Deprecated
  public static void disableClobPrefetch(Clob clob) {
    if (clob instanceof oracle.sql.CLOB) {
      ((oracle.sql.CLOB)clob).setActivePrefetch(false);
    }
  }
  
  /**
   * Определяет: является ли клиентский браузер мобильным ?
   *
   * @param request запрос
   * @return true - клиентский барузер является мобильным, false - клиентский браузер не является мобильным
   * @see <a href="http://detectmobilebrowsers.com/" target="_blank">http://detectmobilebrowsers.com/</a>
   */
  public static boolean isMobile(HttpServletRequest request) {
    boolean result = false;
    String ua = request.getHeader(USER_AGENT_HEADER);
    
    if(!isEmpty(ua)) {
      ua = ua.toLowerCase();
      if(ua.matches(".*(android|avantgo|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")
        || ua.substring(0,4).matches("1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|e\\-|e\\/|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\\-|2|g)|yas\\-|your|zeto|zte\\-")) {
        
        result = true;
      }
    }
     
    return result;
  }
  
}
