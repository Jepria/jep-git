package org.jepria.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class SsoUiUtils {
  
  /**
   * Название контекстного параметра {@code <context-param>} в web.xml сервера,
   * хранящего контекстный путь приложения SsoUi на конкретном сервере
   */
  public static final String CONTEXT_PARAM_NAME_SSOUI_CONTEXT = "ssoUiContext";
  
  /**
   * Контекстный путь приложения SsoUi по умолчанию,
   * в случае если соответствующий {@code <context-param>} не задан в web.xml сервера
   * @see {@link #CONTEXT_PARAM_SSOUI_CONTEXT}
   */
  public static final String DEFAULT_SSOUI_CONTEXT = "/SsoUi";
  
  /**
   * Считывает и возвращает значение контекстного параметра с именем
   * {@link #CONTEXT_PARAM_NAME_SSOUI_CONTEXT} из заданного контекста,
   * либо {@link #DEFAULT_SSOUI_CONTEXT}, если указанный параметр не задан
   */
  public static String getSsoUiContext(ServletContext servletContext) {
    String ssoUiContext = servletContext.getInitParameter(CONTEXT_PARAM_NAME_SSOUI_CONTEXT);
    if (ssoUiContext == null || ssoUiContext.length() == 0) {
      return DEFAULT_SSOUI_CONTEXT;
    } else {
      return ssoUiContext;
    }
  }
  
  /**
   * Возвращает URL с необходимыми параметрами для перенаправления с логин-страницы любого приложения в соответствующее SsoUi
   * @param ssoUiContext контекстное имя приложения SsoUi на конкретном сервере (в виде {@code /SsoUi}), может быть получено из {@link #getSsoUiContext(ServletContext)}
   */
  public static String buildSsoUiUrl(String ssoUiContext, HttpServletRequest request) {
    
    // Закодируем амперсанды в исходном запросе для передачи его как параметр
    String forwardQueryString = (String)request.getAttribute(RequestDispatcher.FORWARD_QUERY_STRING);
    String forwardQueryStringEncoded = forwardQueryString == null ? null : encodeAmps(forwardQueryString);
    
    return ssoUiContext + "/Protected.jsp?" +
        SsoUiConstants.REQUEST_PARAMETER_ENTER_MODULE + "=" + (String)request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI) +
        (forwardQueryStringEncoded == null ? "" : "&" + SsoUiConstants.REQUEST_PARAMETER_QUERY_STRING + "=" + forwardQueryStringEncoded);
  }
  
  /**
   * Символ для замены апмерсанда
   */
  public static final char AMP_SUB = '_';
  
  /**
   * Кодирует амперсанды в строке, заменяя их символом '_' ({@link #AMP_SUB}).
   * Добавляет к результирующей строке префикс-блок {@code AMPS_/_AMPS} с перечислением индексов замененных
   * символов (для последующего восстановления методом {@link #decodeAmps}).
   * <br>
   * Например,<br>
   * {@code 'a=1&b=2&c=_' -> 'AMPS_3_7_AMPSa=1_b=2_c=_'}<br>
   * {@code 'x=y' -> 'AMPS__AMPSx=y'}
   *  
   * @param s
   * @return
   * @throws NullPointerException если входная строка {@code null}
   */
  public static String encodeAmps(String s) {
    if (s == null) {
      throw new NullPointerException();
    }
    
    StringBuilder prefix = new StringBuilder();
    prefix.append("AMPS_");
    char[] chars = s.toCharArray();
    int modCount = 0;
    for (int i = 0; i < chars.length; i++) {
      if (chars[i] == '&') {
        chars[i] = AMP_SUB;
        modCount++;
        
        if (modCount > 1) {
          prefix.append('_');
        }
        prefix.append(String.valueOf(i));
      }
    }
    prefix.append("_AMPS");
    
    prefix.append(chars);
    return prefix.toString();
  }
  
  /**
   * Декодирует строку, закодированную с помощью {@link #encodeAmps}
   * <br>
   * Например,<br>
   * {@code 'AMPS_3_7_AMPSa=1_b=2_c=_' -> 'a=1&b=2&c=_'}<br>
   * {@code 'AMPS__AMPSx=y' -> 'x=y'}
   *  
   * @param s
   * @return
   * @throws NullPointerException если входная строка {@code null}
   * @throws IllegalArgumentException если входная строка не начинается с блока {@code AMPS_/_AMPS}
   */
  public static String decodeAmps(String s) {
    if (s == null) {
      throw new NullPointerException();
    }
    
    Matcher m = Pattern.compile("AMPS_(.*?)_AMPS(.*)").matcher(s);
    if (m.matches()) {
      String serviceString = m.group(1);
      String encodedString = m.group(2);
      
      char[] chars = encodedString.toCharArray();
      
      if (serviceString != null && serviceString.length() > 0) {
        for (String index: serviceString.split("_")) {
          int i = Integer.parseInt(index);
          chars[i] = '&';
        }
      }
      
      return new String(chars);
      
    } else {
      throw new IllegalArgumentException("Encoded string must start with 'AMPS_/_AMPS' block");
    }
  }
}
