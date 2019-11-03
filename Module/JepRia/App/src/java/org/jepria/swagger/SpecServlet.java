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

public class SpecServlet extends HttpServlet {

  protected String swaggerUiPath;
  protected String specRootPath;
  protected String apiMappingUrl;

  @Override
  public void init() {
    swaggerUiPath = getServletConfig().getInitParameter("swagger-ui-root-path");
    if (swaggerUiPath == null) {
      throw new NullPointerException("swagger-ui-root-path parameter is mandatory");
    }

    specRootPath = getServletConfig().getInitParameter("spec-root-path");
    if (specRootPath == null || "".equals(specRootPath)) {
      specRootPath = "/"; // default value
    } else {
      if (!specRootPath.startsWith("/") || specRootPath.length() > 1 && specRootPath.endsWith("/")) {
        throw new IllegalArgumentException("spec-root-path parameter value must either be '/' or (must start with '/' and must not end with '/'): " + specRootPath);
      }
    }

    apiMappingUrl = getServletConfig().getInitParameter("api-mapping-url");
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String path = req.getPathInfo();
    if (path == null || "".equals(path) || "/".equals(path)) {
      entry(req, resp);
      return;
    } else if (path.startsWith("/swagger-ui/")) {
      String resourcePath = path.substring("/swagger-ui".length());
      swaggerUi(req, resp, resourcePath);
      return;
    } else if (path.startsWith("/spec/")) {
      String resourcePath = path.substring("/spec".length());
      spec(req, resp, resourcePath);
      return;
    } else {
      // TODO message?
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Either api-docs root / or /swagger-ui/* or /spec/* resource may be requested");
    }
  }

  protected void entry(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String targetPath = swaggerUiPath + "/index.html.jsp";

    // deploy index.html.jsp with the following values
    req.setAttribute("org.jepria.swagger.specServletMapping", getServletMapping(req));
    String uiJsonUrls =  buildUrls(collectSpecs(req.getServletContext(), specRootPath), getRootUrl(req) + getServletMapping(req) + "/spec");
    req.setAttribute("org.jepria.swagger.uiJsonUrls", uiJsonUrls);
    req.getRequestDispatcher(targetPath).forward(req, resp);
  }

  protected void swaggerUi(HttpServletRequest req, HttpServletResponse resp, String resourcePath) throws ServletException, IOException {
    // return the resource as-is
    req.getRequestDispatcher(swaggerUiPath + resourcePath).forward(req, resp);
  }

  protected void spec(HttpServletRequest req, HttpServletResponse resp, String resourcePath) throws ServletException, IOException {
    String targetPath = specRootPath + resourcePath;

    if (apiMappingUrl != null) {
      // deploy the resource: set 'url' for template 'server' and 'servers' fields
      // Note: for some reason, 'server' field is ignored by the swagger-ui (although it could be used instead of 'servers' field according to docs), so works with 'servers' only

      final Map<String, Object> specAsMap;
      try (InputStream specIn = req.getServletContext().getResourceAsStream(targetPath)) {
        specAsMap = JsonBindingProvider.getJsonb().fromJson(specIn, Map.class); // new HashMap<String, Object>(){}.getClass() not working
      }

      List<Map<String, Object>> servers = (List<Map<String, Object>>) specAsMap.get("servers");
      if (servers != null) {
        for (Map<String, Object> server : servers) {
          if (server != null) {
            String url = (String) server.get("url");
            if ("/{appContextPath}/{apiEndpoint}".equals(url)) {
              server.put("url", req.getContextPath() + apiMappingUrl);
            }
          }
        }
      } else {
        servers = new ArrayList<>();
        Map<String, Object> server = new HashMap<>();
        server.put("url", req.getContextPath() + apiMappingUrl);
        servers.add(server);
        specAsMap.put("servers", servers);
      }

      resp.setContentType("application/json");
      resp.setCharacterEncoding("UTF-8");

      JsonBindingProvider.getJsonb().toJson(specAsMap, resp.getWriter());
      resp.setStatus(HttpServletResponse.SC_OK);
      resp.flushBuffer();

    } else {
      // return the resource as-is

      // Note: this is abnormal case!
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

  protected String getRootUrl(HttpServletRequest request) {
    String contextPath = request.getContextPath();
    String url = request.getRequestURL().toString();
    int index = url.indexOf(contextPath);
    if (index == -1) {
      throw new IllegalStateException("HttpServletRequest.getRequestURL() must contain HttpServletRequest.getContextPath()");
    }
    return url.substring(0, index + contextPath.length());
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

  /**
   * For the servlet which this request has been dispatched to, returns the actual url mapping which this request matched.
   * May differ from the value of {@code servlet-mapping/url-pattern} servlet parameter value
   * @param request
   * @return
   */
  protected String getServletMapping(HttpServletRequest request) {
    final String uri = request.getRequestURI();
    final String ctx = request.getContextPath();
    final String path = request.getPathInfo();
    if (!uri.startsWith(ctx)) {
      throw new IllegalStateException("HttpServletRequest.getRequestURI() must start with HttpServletRequest.getContextPath()");
    }
    if (path != null && !uri.endsWith(path)) {
      throw new IllegalStateException("HttpServletRequest.getRequestURI() must end with HttpServletRequest.getPathInfo()");
    }

    String ret = uri.substring(ctx.length());
    if (path != null) {
      ret = ret.substring(0, ret.length() - path.length());
    }
    return ret;
  }
}
