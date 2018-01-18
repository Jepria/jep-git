package org.jepria.ssoutils;

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
    
    StringBuilder urlSb = new StringBuilder();
    
    urlSb.append(ssoUiContext).append("/Protected.jsp?")
        .append(SsoUiConstants.REQUEST_PARAMETER_ENTER_MODULE).append('=').append((String)request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI));
    
    // Закодируем амперсанды в исходном запросе для передачи его как параметр
    String queryString = (String)request.getAttribute(RequestDispatcher.FORWARD_QUERY_STRING);
    if (queryString != null && !"".equals(queryString)) {
      
      // Закодируем амперсанды в исходном запросе для передачи его как параметр
      String queryStringEncoded = encodeQueryStr(queryString);
      
      urlSb.append('&').append(SsoUiConstants.REQUEST_PARAMETER_QUERY_STRING).append('=').append(queryStringEncoded);
    }
    
    
    return urlSb.toString();
  }
  
  /**
   * Функция для кодирования строки параметров URL (части после символа ?).
   * Кодирование позволяет передавать строку в GET-параметре другого URL
   * <br><br>
   * Заменяет амперсанды строке символом '_'.
   * Добавляет к результирующей строке префикс-блок {@code enc_/_enc} с перечислением индексов замененных
   * символов (для последующего восстановления методом {@link #decodeQueryStr}).
   * <br>
   * Например,<br>
   * {@code encodeQueryStr("a=1&b=2&c=_") === "enc_3_7_enca=1_b=2_c=_"}<br>
   * {@code encodeQueryStr("x=y") === "enc__encx=y"}
   *  
   * @param s
   * @return
   * @throws NullPointerException если входная строка {@code null}
   */
  public static String encodeQueryStr(String s) {
    if (s == null) {
      throw new NullPointerException();
    }
    
    StringBuilder prefix = new StringBuilder();
    prefix.append("enc_");
    char[] chars = s.toCharArray();
    int modCount = 0;
    for (int i = 0; i < chars.length; i++) {
      if (chars[i] == '&') {
        chars[i] = '_';
        modCount++;
        
        if (modCount > 1) {
          prefix.append('_');
        }
        prefix.append(String.valueOf(i));
      }
    }
    prefix.append("_enc");
    
    prefix.append(chars);
    return prefix.toString();
  }
  
  /**
   * Возвращает текст JS-функции для кодирования фрагмента URL (части после символа #).
   * Кодирование позволяет передавать строку в GET-параметре другого URL.
   * <br><br>
   * Алгоритм кодирования аналогичен методу {@link #encodeQueryStr}.
   * 
   * @return текст JS-функции
   */
  public static String encodeFragmentFunction() {
    // код представлен inline-строкой для простоты восприятия и сравнения с методом encodeQueryStr
    return
        "function(s) { " +
        "  var prefix = 'enc_'; " +
        "  var chars = s.split(''); " +
        "  var modCount = 0; " +
        "  for (var i = 0; i < chars.length; i++) {  " +
        "    if (chars[i] === '&') { " +
        "      chars[i] = '_'; " +
        "      modCount++; " +
        "      " +
        "      if (modCount > 1) { " +
        "        prefix += '_'; " +
        "      } " +
        "      prefix += i; " +
        "    } " +
        "  } " +
        "  prefix += '_enc'; " +
        "  " +
        "  prefix += chars.join(''); " +
        "  return prefix; " +
        "} ";
  }
  
  /**
   * Декодирует строку, закодированную методом {@link #encodeQueryStr}
   * <br>
   * Например,<br>
   * {@code decodeQueryStr("enc_3_7_enca=1_b=2_c=_") === "a=1&b=2&c=_"}<br>
   * {@code decodeQueryStr("enc__encx=y") === "x=y"}
   *  
   * @param s
   * @return
   * @throws NullPointerException если входная строка {@code null}
   * @throws IllegalArgumentException если входная строка не начинается с блока {@code enc_/_enc}
   */
  public static String decodeQueryStr(String s) {
    if (s == null) {
      throw new NullPointerException();
    }
    
    Matcher m = Pattern.compile("enc_(.*?)_enc(.*)").matcher(s);
    if (m.matches()) {
      String serviceString = m.group(1);
      String encodedString = m.group(2);
      
      char[] chars = encodedString.toCharArray();
      
      if (serviceString != null && serviceString.length() > 0) {
        for (String index: serviceString.split("_")) {
          int i = Integer.parseInt(index);
          if (i < chars.length) {
            chars[i] = '&';
          }
        }
      }
      
      return new String(chars);
      
    } else {
      throw new IllegalArgumentException("Encoded string must start with 'enc_/_enc' block");
    }
  }
  
  /**
   * Декодирует строку, закодированную JS-функцией {@link #encodeFragmentFunction}
   * <br><br>
   * Алгоритм кодирования аналогичен методу {@link #decodeQueryStr}.
   * 
   * @param s
   * @return
   * @throws NullPointerException если входная строка {@code null}
   * @throws IllegalArgumentException если входная строка не начинается с блока {@code enc_/_enc}
   */
  public static String decodeFragment(String s) {
    return decodeQueryStr(s);
  }
}
