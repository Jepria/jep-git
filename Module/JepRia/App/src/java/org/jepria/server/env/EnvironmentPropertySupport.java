package org.jepria.server.env;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Гибкий интерфейс получения значений переменных окружения по имени.
 * <br/><br/>
 * Пример динмического получения значений переменных окружения в java-коде приложения:
 * <pre>
 * String name = "var";  // имя переменной
 * String value;         // считываемое значение
 * value = EnvironmentPropertySupport.getInstance(...).getProperty(name);                   // простое считывание значения
 * value = EnvironmentPropertySupport.getInstance(...).getProperty(name, "value-default");  // считывание значения, либо возврат значения по умолчанию, если переменная не определена
 * </pre>
 *
 * Переменные в окружении определяют одним (либо несколькими) из следующих способов:
 * <ol>
 *   <li>
 *     На уровне <a href="https://tomcat.apache.org/tomcat-7.0-doc/config/context.html#Environment_Entries">контекстных переменных Томката</a> в серверном конфигурационном файле Tomcat/conf/context.xml элементами {@code <Environment/>}
 *     <pre>
 *     &lt;Context&gt;
 *       ...
 *       &lt;Environment name="var" value="value" type="java.lang.String" override="true"/&gt;
 *       ...
 *     &lt;/Context&gt;</pre>
 *   </li>
 *   <li>
 *     На уровне свойств {@code JVM}
 *     <pre>
 *     java ... -Dname=value</pre>
 *   </li>
 *   <li>
 *     На уровне переменных окружения ОС
 *   </li>
 *   <li>
 *     Внутри приложения — в файле {@code war/WEB-INF/app-conf.default.properties} — значения по умолчанию
 *   </li>
 * </ol>
 * На каждом из уровней переменные могут определяться следующим образом
 * <br/>
 *
 * При таком определении все приложения, установленные на данном инстансе, будут считывать значение {@code value} переменной с именем {@code var}.
 * <br/><br/>
 * Можно переопределить значение переменной для одного приложения, добавив к имени переменной <i>контекст</i> этого приложения:
 * <pre>
 * &lt;Context&gt;
 *   &lt;!-- объявление переменной var без контекста (для всех приложений) --&gt;
 *   &lt;Environment name="var" value="4" type="java.lang.String" override="true"/&gt;
 *   &lt;!-- объявление переменной var в контексте (для приложения foo) --&gt;
 *   &lt;Environment name="foo#var" value="3-foo" type="java.lang.String" override="true"/&gt;
 *   &lt;!-- объявление переменной var в контексте (для приложения foo/bar) --&gt;
 *   &lt;Environment name="foo#bar#var" value="3-foo#bar" type="java.lang.String" override="true"/&gt;
 * &lt;/Context&gt;</pre>
 * При таком определении
 * <br/>
 * приложение, установленное под контекстным путём {@code /foo}, будет считывать значение {@code 3-foo} переменной с именем {@code var},
 * <br/>
 * приложение, установленное под контекстным путём {@code /foo/bar (foo#bar)}, будет считывать значение {@code 3-foo#bar} переменной с именем {@code var},
 * <br/>
 * остальные приложения, установленные на данном инстансе, будут считывать значение {@code 4} переменной с именем {@code var}.
 *
 * <br/>
 * <i>Использование символа {@code #} в смысле {@code /} обусловлено тем, что {@code /} является специальным символом в терминах {@code javax.naming.Context}.</i>
 *
 * <br/>
 * <br/>
 * Множество переменных можно вынести в общий конфигурационный файл ({@code .properties}), указав ссылку на этот файл в отдельной переменной с ключевым именем {@code app-conf.file}.
 *
 * <pre>
 * &lt;Context&gt;
 *   &lt;!-- определение переменных в конфигурационном файле --&gt;
 *   &lt;Environment name="app-conf.file" value="C:\app-conf.properties" type="java.lang.String" override="true"/&gt;
 * &lt;/Context&gt;
 * </pre>
 * Содержимое файла {@code C:\app-conf.properties}:
 * <pre>
 * var=4
 * foo#var=3-foo
 * foo#bar#var=3-foo#bar
 * </pre>
 * При таком определении<br/>
 * приложение, установленное под контекстным путём {@code /foo}, будет считывать значение {@code 3-foo} переменной с именем {@code var},<br/>
 * приложение, установленное под контекстным путём {@code /foo/bar (foo#bar)}, будет считывать значение {@code 3-foo#bar} переменной с именем {@code var},<br/>
 * остальные приложения, установленные на данном инстансе, будут считывать значение {@code 4} переменной с именем {@code var}.
 *
 * <br/><br/>
 * Можно комбинировать непосредственное определение переменных и определение в конфигурационном файле:
 * <br/>
 * <i>Приоритет в этом случае имеет значение переменной, определённой непосредственно.</i>
 * <pre>
 * &lt;Context&gt;
 *   &lt;!-- непосредственное объявление переменных var --&gt;
 *   &lt;Environment name="var" value="2" type="java.lang.String" override="true"/&gt;
 *   &lt;Environment name="foo#var" value="1-foo" type="java.lang.String" override="true"/&gt;
 *   &lt;Environment name="foo#bar#var" value="1-foo#bar" type="java.lang.String" override="true"/&gt;
 *   &lt;!-- определение переменных в конфигурационном файле --&gt;
 *   &lt;Environment name="app-conf.file" value="C:\app-conf.properties" type="java.lang.String" override="true"/&gt;
 * &lt;/Context&gt;</pre>
 * Содержимое файла {@code C:\app-conf.properties}:
 * <pre>
 * var=4
 * foo#var=3-foo
 * foo#bar#var=3-foo#bar
 * </pre>
 * При таком определении<br/>
 * приложение, установленное под контекстным путём {@code /foo}, будет считывать значение {@code 1-foo} переменной с именем {@code var},<br/>
 * приложение, установленное под контекстным путём {@code /foo/bar (foo#bar)}, будет считывать значение {@code 1-foo#bar} переменной с именем {@code var},<br/>
 * остальные приложения, установленные на данном инстансе, будут считывать значение {@code 2} переменной с именем {@code var}.
 *
 * <br/><br/>
 * В случае, если для разных приложений требуются отдельные конфигурационные файлы, каждый из них объявляется в контексте, аналогично.
 * <pre>
 * &lt;Context&gt;
 *   &lt;Environment name="app-conf.file" value="C:\app-conf.properties" type="java.lang.String" override="true"/&gt;
 *   &lt;Environment name="foo#app-conf.file" value="C:\foo\app-conf.properties" type="java.lang.String" override="true"/&gt;
 * &lt;/Context&gt;
 * </pre>
 * Содержимое файла {@code C:\app-conf.properties}:
 * <pre>
 * var=5
 * </pre>
 * Содержимое файла {@code C:\foo\app-conf.properties}:
 * <pre>
 * var=3-foo
 * </pre>
 * При таком определении<br/>
 * приложение, установленное под контекстным путём {@code /foo}, будет считывать значение {@code 3-foo} переменной с именем {@code var},<br/>
 * остальные приложения, установленные на данном инстансе, будут считывать значение {@code 5} переменной с именем {@code var}.
 *
 */
// Тестирование: см. https://sourceforge.net/p/javaenterpriseplatform/git/ci/master/tree/Module/JepRia/Doc/App/environment-property-support-testing.md
public interface EnvironmentPropertySupport {

  String getProperty(String name);

  default String getProperty(String name, String defaultValue) {
    String value = getProperty(name);
    return value == null ? defaultValue : value;
  }

  /**
   * Describes where the property has been actually retrieved from 
   * (due to the multiple property access object strategy).
   * Consists of property location and actual property name 
   * (the actual property name in the storage denoted by the location 
   * may differ from the name it was requested, regarding contexts)  
   * Used for debug purposes (represented by a human-readable string)
   */
  String getPropertySource(String name);

  /**
   * @param request
   * @return property support for web applications
   */
  static EnvironmentPropertySupport getInstance(HttpServletRequest request) {
    return new EnvironmentPropertySupportWebImpl(request.getServletContext());
  }

  static EnvironmentPropertySupport getInstance(ServletContext context) {
    return new EnvironmentPropertySupportWebImpl(context);
  }
}
