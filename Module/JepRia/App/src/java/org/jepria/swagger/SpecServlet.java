package org.jepria.swagger;

import org.jepria.server.service.rest.gson.JsonBindingProvider;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Сервлет, обеспечивающий рендеринг интерфейса из файлов openapi-спецификаций приложения посредством библиотеки io.swagger-ui, с поддержкой динамических параметров.<br/><br/>
 *
 * Библиотека io.swagger-ui из коробки поддерживает рендеринг интерфейса из <i>статических</i> файлов openapi-спецификаций.
 * При содержании файлов спецификаций внутри веб-приложения, они могут содержать параметры, зависящие от окружения, на которое приложение установлено
 * (например, url эндпоинтов, которые они описывают).
 * Сервлет совместно с кастомизированной библиотекой io.swagger-ui поддерживают задание динамических параметров в исходных openapi-файлах
 * и динамическую подстановку значений этих параметров в рантайме (то есть при обращении к спецификации через web).<br/><br/>
 *
 * Пример использования.<br/>
 * <ol>
 *   <li>
 *     Объявление сервлета в web.xml<br/>
 *     Сервлет принимает и обрабатывает web-запросы отрендеренных openapi-спецификаций
 *     <pre>
 *      &lt;web-app&gt;
 *        ...
 *        &lt;servlet&gt;
 *         &lt;servlet-name&gt;ApiDocs&lt;/servlet-name&gt;
 *         &lt;servlet-class&gt;org.jepria.swagger.SpecServlet&lt;/servlet-class&gt;
 *         &lt;init-param&gt;
 *           &lt;!-- Путь к корневой папке со swagger-ui-ресурсами в веб-приложении --&gt;
 *           &lt;param-name&gt;swagger-ui-root-path&lt;/param-name&gt;
 *           &lt;param-value&gt;/swagger-ui&lt;/param-value&gt;
 *         &lt;/init-param&gt;
 *         &lt;init-param&gt;
 *           &lt;!-- Путь к корневой папке со spec-ресурсами в веб-приложении --&gt;
 *           &lt;param-name&gt;spec-root-path&lt;/param-name&gt;
 *           &lt;param-value&gt;/WEB-INF/api-spec&lt;/param-value&gt;
 *         &lt;/init-param&gt;
 *         &lt;init-param&gt;
 *           &lt;!-- Корневой (общий) URL-маппинг REST-сервисов в виде /*, соответствующий значению servlet-mapping/url-pattern Rest-сервлета --&gt;
 *           &lt;param-name&gt;api-servlet-path&lt;/param-name&gt;
 *           &lt;param-value&gt;/api&lt;/param-value&gt;
 *         &lt;/init-param&gt;
 *       &lt;/servlet&gt;
 *       &lt;servlet-mapping&gt;
 *         &lt;servlet-name&gt;ApiDocs&lt;/servlet-name&gt;
 *         &lt;url-pattern&gt;/api-docs/*&lt;/url-pattern&gt;
 *       &lt;/servlet-mapping&gt;
 *       ...
 *      &lt;/web-app&gt;
 *     </pre>
 *  </li>
 *  <li>
 *    При сборке приложения в папку {@code WEB-INF/api-spec} помещаются spec-ресурсы, описывающие API сущностей приложения.
 *    Предполагается, что на API отдельных сущностей имеется отдельный spec-ресурс.
 *    Spec-ресурс каждой сущности должен быть файлом с именем {@code swagger.json}, лежащим в папке с именем описываемой сущности.<br/>
 *    Пример иерархии файлов для описания API двух сущностей user и contract:
 *    <pre>
 *      /WEB-INF
 *        /api-spec
 *          /user
 *            swagger.json
 *          /contract
 *            swagger.json
 *    </pre>
 *  </li>
 *  <li>
 *    Для динамического определения сервера (части url), по которому swagger делает рантайм-обращения к собственно API из интерфейса,
 *    необходимо указать шаблонное значение {@code /{appContextPath}/{apiEndpoint}} поля {@code /servers/url} в spec-ресурсе swagger.json.
 *    При указании иного значения, в качестве сервера будет использовано собственно значение (например, {@code /application/api}).
 *  </li>
 *  <li>
 *    Web-обращения к интерфейсу openapi происходят по url {@code /api-docs} (url определяется маппингом сервлета в web.xml).
 *    В интерфейсе отображаются несколько независимых спецификаций (переключаемых комбо-боксом в правом верхнем углу),
 *    при этом имя каждой спецификации совпадает с именем папки, в которую при сборке был помещён соответствующий spec-ресурс.
 *  </li>
 * </ol>
 *
 */
public class SpecServlet extends HttpServlet {

  protected String swaggerUiRootPath;
  protected String specRootPath;
  protected String apiServletPath;

  @Override
  public void init() {
    swaggerUiRootPath = getServletConfig().getInitParameter("swagger-ui-root-path");
    if (swaggerUiRootPath == null || "".equals(swaggerUiRootPath)) {
      throw new NullPointerException("swagger-ui-root-path parameter is mandatory");
    } else {
      if (!swaggerUiRootPath.startsWith("/") || swaggerUiRootPath.length() > 1 && swaggerUiRootPath.endsWith("/")) {
        throw new IllegalArgumentException("swagger-ui-root-path parameter value must either be '/' or be starting with '/' but not ending with '/': " + swaggerUiRootPath);
      }
    }

    specRootPath = getServletConfig().getInitParameter("spec-root-path");
    if (specRootPath == null || "".equals(specRootPath)) {
      specRootPath = "/"; // default value
    } else {
      if (!specRootPath.startsWith("/") || specRootPath.length() > 1 && specRootPath.endsWith("/")) {
        throw new IllegalArgumentException("spec-root-path parameter value must either be '/' or be starting with '/' but not ending with '/': " + specRootPath);
      }
    }

    apiServletPath = getServletConfig().getInitParameter("api-servlet-path");
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String path = req.getPathInfo();
    if (path != null && path.startsWith(PATH_PREFIX__SWAGGER_UI + "/")) {
      // requested swagger-ui resource
      String resourcePath = path.substring(PATH_PREFIX__SWAGGER_UI.length());
      swaggerUi(req, resp, resourcePath);
      return;
    } else if (path != null && path.startsWith(PATH_PREFIX__SPEC + "/")) {
      // requested spec resource
      String resourcePath = path.substring(PATH_PREFIX__SPEC.length());
      spec(req, resp, resourcePath);
      return;
    } else {
      // requested ui probably with particular spec selected
      ui(req, resp, path);
    }
  }

  private static final String PATH_PREFIX__SPEC = "/_spec";

  // Note: This constant has an external referrer: io/swagger-ui/{version}/swagger-ui-{version}-multispec.jar/index.html.jsp
  private static final String PATH_PREFIX__SWAGGER_UI = "/_swagger-ui";

  protected void ui(HttpServletRequest req, HttpServletResponse resp, String specName) throws ServletException, IOException {
    String targetPath = swaggerUiRootPath + "/index.html.jsp";

    List<Spec> specs = collectSpecs(req.getServletContext(), specRootPath);

    if (specName != null && !"".equals(specName) && !"/".equals(specName)) {
      // select particular spec
      boolean specFound = false;
      for (Spec spec : specs) {
        if (specName.equals(spec.name())) {
          // set the selected spec as first element
          specs.remove(spec);
          specs.add(0, spec);
          specFound = true;
          break;
        }
      }
      if (!specFound) {
        resp.sendError(404, "No such spec to select: " + specName);
        resp.flushBuffer();
        return;
      }
    }

    String uiJsonUrls = buildUrls(specs, req.getContextPath() + req.getServletPath() + "/_spec");

    // deploy index.html.jsp with the following values
    req.setAttribute("org.jepria.swagger.apiDocsContext", req.getContextPath() + req.getServletPath());
    req.setAttribute("org.jepria.swagger.uiJsonUrls", uiJsonUrls);
    req.getRequestDispatcher(targetPath).forward(req, resp);
  }

  protected void swaggerUi(HttpServletRequest req, HttpServletResponse resp, String resourcePath) throws ServletException, IOException {
    // return the resource as-is
    req.getRequestDispatcher(swaggerUiRootPath + resourcePath).forward(req, resp);
  }

  protected void spec(HttpServletRequest req, HttpServletResponse resp, String resourcePath) throws ServletException, IOException {
    String targetPath = specRootPath + resourcePath;

    if (apiServletPath != null) {
      // deploy the resource: set 'url' for template 'server' and 'servers' fields
      // Note: for some reason, 'server' field is ignored by the swagger-ui (although it could be used instead of 'servers' field according to docs), so work with 'servers' only

      final Map<String, Object> specObject;
      try (InputStream specIn = req.getServletContext().getResourceAsStream(targetPath)) {
        specObject = JsonBindingProvider.getJsonb().fromJson(specIn, Map.class); // new HashMap<String, Object>(){}.getClass() not working
      }

      List<Map<String, Object>> serversObject = (List<Map<String, Object>>) specObject.get("servers");
      if (serversObject != null) {
        for (Map<String, Object> serverObject : serversObject) {
          if (serverObject != null) {
            String url = (String) serverObject.get("url");
            if ("/{appContextPath}/{apiEndpoint}".equals(url)) {
              serverObject.put("url", req.getContextPath() + apiServletPath);
            }
          }
        }
      } else {
        serversObject = new ArrayList<>();
        Map<String, Object> serverObject = new HashMap<>();
        serverObject.put("url", req.getContextPath() + apiServletPath);
        serversObject.add(serverObject);
        specObject.put("servers", serversObject);
      }

      resp.setContentType("application/json");
      resp.setCharacterEncoding("UTF-8");

      JsonBindingProvider.getJsonb().toJson(specObject, resp.getWriter());
      resp.setStatus(HttpServletResponse.SC_OK);
      resp.flushBuffer();

    } else {
      // return the resource as-is

      // Note: this is an abnormal case!
      req.getRequestDispatcher(targetPath).forward(req, resp);
    }
  }

  interface Spec {
    String resourcePath();
    String name();
  }

  /**
   * Collects spec resources from within the web application resource tree (actually from its subtree starting at the {@code specRootPath} node)
   * @param servletContext
   * @param specRootPath must start with '/' but not end with '/'
   * @return
   */
  protected List<Spec> collectSpecs(ServletContext servletContext, String specRootPath) {

    if (specRootPath == null || !specRootPath.startsWith("/") || specRootPath.endsWith("/") ) {
      throw new IllegalArgumentException("specRootPath must start with '/' but not end with '/'");
    }

    Set<String> specPaths = new HashSet<>();
    collectSpecPaths(servletContext, specRootPath + "/", specPaths);

    List<Spec> ret = new ArrayList<>();
    for (String specPath: specPaths) {

      // starts with '/', does not end with '/'
      final String specName;
      {
        String n = specPath.substring(specRootPath.length(), specPath.lastIndexOf('/'));
        if ("".equals(n)) {
          n = "/";
        }
        specName = n;
      }

      Spec spec = new Spec() {
        @Override
        public String name() {
          return specName;
        }
        @Override
        public String resourcePath() {
          return specPath.substring(specRootPath.length());
        }
        @Override
        public String toString() {
          return "[" + name() + ":" + resourcePath() + "]";
        }
      };

      ret.add(spec);
    }

    return ret;
  }

  protected void collectSpecPaths(ServletContext servletContext, String specRootPath, Set<String> target) {
    Set<String> paths = servletContext.getResourcePaths(specRootPath);
    for (String path: paths) {
      if (path.endsWith("/")) {
        // this path represents a folder
        collectSpecPaths(servletContext, path, target);
      } else {
        // this path represents a file
        String filename = path.substring(path.lastIndexOf('/') + 1);
        if (filename.equals("swagger.json")) {
          target.add(path);
        }
      }
    }
  }

  protected String buildUrls(List<Spec> specs, String rootUrl) {
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    boolean first = true;
    for (Spec spec: specs) {

      if (first) { first = false; } else { sb.append(','); }

      sb.append('{');
      sb.append("\"url\": \"").append(rootUrl).append(spec.resourcePath()).append('\"');
      sb.append(',');
      sb.append(" \"name\": \"").append(spec.name()).append('\"');
      sb.append('}');
    }
    sb.append(']');

    return sb.toString();
  }
}
