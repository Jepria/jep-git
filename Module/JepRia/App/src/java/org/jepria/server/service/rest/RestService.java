package org.jepria.server.service.rest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jepria.CastMap;
import org.jepria.CastMap.CastOnGetException;
import org.jepria.CastMapBase;
import org.jepria.TypedValueParser;
import org.jepria.TypedValueParserImpl;
import org.jepria.server.service.apispec.ApiSpec;
import org.jepria.server.service.apispec.Response;
import org.jepria.server.service.apispec.TypeDeployer;

// В прикладной реализации сервиса регистрируются всевозможные эндпоинт-методы (или сущности целиком).
// Преимущества перед императивным подходом: решена проблема с выдачей 404 ошибки если запросили незарегистрированную сущность,
// для каждой сущности при регистрации можно указать не только data supplier, но и любую метаинформацию: Swagger, JsonSchema и т.д.
public class RestService extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public RestService() {}

  private final Map<MethodLocator, EndpointMethod> methods = new LinkedHashMap<>(); // maintain order for clarity

  public static interface EndpointMethod {
    /**
     * 
     * @param request not null
     * @param pathParams not null
     * @param queryParams not null
     * @param bodyParams not null
     * @return
     * @throws Exception
     */
    Object getData(HttpServletRequest request, 
        List<String> pathParams, 
        CastMap<String, ?> queryParams, 
        CastMap<String, ?> bodyParams) throws Exception;
    
    default ApiSpec apiSpec() {
      return null;
    }
  }

  protected void registerEndpointMethodOption(String entity, EndpointMethod endpointMethod) {
    Objects.requireNonNull(entity);
    Objects.requireNonNull(endpointMethod);

    registerEndpointMethod("get", "/option/" + entity, endpointMethod);
  }

  protected void registerEndpointMethod(String httpMethod, String path, EndpointMethod endpointMethod) {
    Objects.requireNonNull(httpMethod);
    Objects.requireNonNull(endpointMethod);

    MethodLocator locator = newMethodLocator(httpMethod, path);
    if (methods.containsKey(locator)) {
      throw new IllegalArgumentException("The method has already been registered, cannot register twice");
    } else {
      methods.put(locator, endpointMethod);
    }
  }

  /**
   * Normalize arguments and create new {@link MethodLocator}
   * @param httpMethod
   * @param path
   * @return
   */
  protected MethodLocator newMethodLocator(String httpMethod, String path) {
    if (httpMethod != null) {
      httpMethod = httpMethod.toLowerCase();
    }
    if (path == null || "".equals(path)) {
      path = "/";
    }
    return new MethodLocator(httpMethod, path);
  }
  
  protected class MethodLocator {
    public final String httpMethod;
    /**
     * nullable
     */
    public final String path;

    /**
     * 
     * @param httpMethod not null
     * @param path nullable
     */
    public MethodLocator(String httpMethod, String path) {
      Objects.requireNonNull(httpMethod);
      this.httpMethod = httpMethod;
      this.path = path;
    }

    @Override
    public int hashCode() {
      return Objects.hash(httpMethod, path);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      } else {
        if (!(obj instanceof MethodLocator)) {
          return false;
        } else {
          MethodLocator locator = (MethodLocator) obj;
          
          if (httpMethod == null) {
            if (locator.httpMethod != null) {
              return false;
            }
          } else {
            if (!httpMethod.equalsIgnoreCase(locator.httpMethod)) {
              return false;
            }
          }
          
          if (path == null) {
            if (locator.path != null) {
              return false;
            }
          } else {
            // TODO consider null, empty and '/' path equal?
            if (!path.equalsIgnoreCase(locator.path)) {
              return false;
            } 
          }
          
          return true;
        }
      }
    }
  }

  protected class EndpointMethodRouted {
    public final EndpointMethod endpointMethod;
    public final List<String> pathParams;
    
    public EndpointMethodRouted(EndpointMethod endpointMethod, List<String> pathParams) {
      this.endpointMethod = endpointMethod;
      this.pathParams = pathParams;
    }
  }
  
  /**
   * 
   * @param httpMethod
   * @param path nullable
   * @return or else empty list, not null
   */
  protected List<EndpointMethodRouted> route(String httpMethod, String path) {
    
    final List<EndpointMethodRouted> ret = new ArrayList<>();
    
    for (MethodLocator locator: methods.keySet()) {
      final EndpointMethod endpointMethod = methods.get(locator);
      
      if (httpMethod.equalsIgnoreCase(locator.httpMethod)) {
        final List<String> pathParams = new ArrayList<>();
        
        if (path == null && locator.path == null) {
          // empty path param list
          ret.add(new EndpointMethodRouted(endpointMethod, pathParams));
          
        } else if (path != null && locator.path != null) {
          Matcher m = Pattern.compile(locator.path).matcher(path);
          if (m.matches()) {
            // parse path params
            for (int i = 0; i < m.groupCount(); i++) {
              pathParams.add(m.group(i));
            }
            ret.add(new EndpointMethodRouted(endpointMethod, pathParams));
          }
        }
      }
    } 
    
    return ret;
  }
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {

    final String path = req.getPathInfo();
    
    if ("/swagger.json".equals(path)) {
      
      Map<String, Object> swaggerJsonMap = buildSwaggerSchema(req);
      StringBuilder sb = new StringBuilder();
      new JsonSerializer().serialize(swaggerJsonMap, sb);
      String swaggerJson = sb.toString();
      
      if (swaggerJson != null) {
        response.setContentType("applcation/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(swaggerJson);
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
        
      } else {
        // the resource was found, but it is empty
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        response.flushBuffer();
      }
      
    } else {

      List<EndpointMethodRouted> endpointMethodsRouted = route("get", path);
  
      if (endpointMethodsRouted != null) {
  
        if (endpointMethodsRouted.size() != 1) {
          throw new IllegalStateException("Multiple endpoint methods match the requested path");
        }
        
        final EndpointMethodRouted endpointMethodRouted = endpointMethodsRouted.iterator().next();
        
        final CastMap<String, Object> queryParams = new CastMapBase(getTypedValueParser());
  
        try {
          queryParams.putAll(deserializeUrlParams(req));
        } catch (Throwable e) {
          e.printStackTrace();
  
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          response.setContentType("text/plain");
          response.setCharacterEncoding("utf-8");
          response.getWriter().println("Failed to parse URL parameters");
          response.flushBuffer();
          return;
        }
  
        // GET does not support body
  
        final Object result;
  
        try {
          // TODO better to pass the request here or put it into ThreadLocal like GWT does?
          result = endpointMethodRouted.endpointMethod.getData(req, endpointMethodRouted.pathParams, queryParams, null);
  
        } catch (CastOnGetException e) {
  
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          response.setContentType("text/plain");
          response.setCharacterEncoding("utf-8");
          response.getWriter().println("Cannot parse the parameter " + e.getKey() + "=" + e.getValue() + " as " + e.getCastTo().getCanonicalName());
          response.flushBuffer();
          return;
  
        } catch (Exception e) {
          // TODO handle properly
          throw new RuntimeException(e);
        }
  
        if (result != null) {
          response.setStatus(HttpServletResponse.SC_OK);
          response.setContentType("application/json");
          response.setCharacterEncoding("UTF-8");
          new JsonSerializer().serialize(result, response.getWriter());
          response.flushBuffer();
          return;
          
        } else {
          // empty response, write nothing, do not set content type as far as there is no content (however not a NO_CONTENT status)
          response.setStatus(HttpServletResponse.SC_OK);
          response.flushBuffer();
          return;
        }
  
      } else {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.flushBuffer();
        return;
      }
    }
  }
  
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {

    final String path = req.getPathInfo();

    List<EndpointMethodRouted> endpointMethodsRouted = route("post", path);
    
    if (endpointMethodsRouted != null) {

      if (endpointMethodsRouted.size() != 1) {
        throw new IllegalStateException("Multiple endpoint methods match the requested path");
      }
      
      final EndpointMethodRouted endpointMethodRouted = endpointMethodsRouted.iterator().next();

      final CastMap<String, Object> queryParams = new CastMapBase(getTypedValueParser());

      try {
        queryParams.putAll(deserializeUrlParams(req));
      } catch (Throwable e) {
        e.printStackTrace();

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println("Failed to parse URL parameters");
        response.flushBuffer();
        return;
      }

      final CastMap<String, Object> bodyParams = new CastMapBase(getTypedValueParser());
      
      final Map<String, Object> m;
      // TODO determine the charset from the request header
      try (Reader reader = new InputStreamReader(req.getInputStream(), Charset.forName("UTF-8"))) {
        m = new JsonSerializer().deserialize(reader);
        
      } catch (IOException e) {
        throw new RuntimeException(e);
        
      } catch (JsonParseException e) {
        e.printStackTrace();

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println("Failed to parse JSON body");
        response.flushBuffer();
        return;
      }
      
      bodyParams.putAll(m);


      final Object result;

      try {
        // TODO better to pass the request here or put it into ThreadLocal like GWT does?
        result = endpointMethodRouted.endpointMethod.getData(req, endpointMethodRouted.pathParams, queryParams, bodyParams);

      } catch (CastOnGetException e) {

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println("Cannot parse the parameter " + e.getKey() + "=" + e.getValue() + " as " + e.getCastTo().getCanonicalName());
        response.flushBuffer();
        return;

      } catch (Exception e) {
        // TODO handle properly
        throw new RuntimeException(e);
      }

      if (result != null) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new JsonSerializer().serialize(result, response.getWriter());
        response.flushBuffer();
        return;
        
      } else {
        // empty response, write nothing, do not set content type as far as there is no content (however not a NO_CONTENT status)
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
        return;
      }

    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.flushBuffer();
      return;
    }
  }
  
  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse response) throws IOException {

    final String path = req.getPathInfo();

    List<EndpointMethodRouted> endpointMethodsRouted = route("put", path);
    
    if (endpointMethodsRouted != null) {

      if (endpointMethodsRouted.size() != 1) {
        throw new IllegalStateException("Multiple endpoint methods match the requested path");
      }
      
      final EndpointMethodRouted endpointMethodRouted = endpointMethodsRouted.iterator().next();

      final CastMap<String, Object> queryParams = new CastMapBase(getTypedValueParser());

      try {
        queryParams.putAll(deserializeUrlParams(req));
      } catch (Throwable e) {
        e.printStackTrace();

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println("Failed to parse URL parameters");
        response.flushBuffer();
        return;
      }

      final CastMap<String, Object> bodyParams = new CastMapBase(getTypedValueParser());
      
      final Map<String, Object> m;
      // TODO determine the charset from the request header
      try (Reader reader = new InputStreamReader(req.getInputStream(), Charset.forName("UTF-8"))) {
        m = new JsonSerializer().deserialize(reader);
        
      } catch (IOException e) {
        throw new RuntimeException(e);
        
      } catch (JsonParseException e) {
        e.printStackTrace();

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println("Failed to parse JSON body");
        response.flushBuffer();
        return;
      }
      
      bodyParams.putAll(m);


      final Object result;

      try {
        // TODO better to pass the request here or put it into ThreadLocal like GWT does?
        result = endpointMethodRouted.endpointMethod.getData(req, endpointMethodRouted.pathParams, queryParams, bodyParams);

      } catch (CastOnGetException e) {

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println("Cannot parse the parameter " + e.getKey() + "=" + e.getValue() + " as " + e.getCastTo().getCanonicalName());
        response.flushBuffer();
        return;

      } catch (Exception e) {
        // TODO handle properly
        throw new RuntimeException(e);
      }

      if (result != null) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new JsonSerializer().serialize(result, response.getWriter());
        response.flushBuffer();
        return;
        
      } else {
        // empty response, write nothing, do not set content type as far as there is no content (however not a NO_CONTENT status)
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
        return;
      }

    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.flushBuffer();
      return;
    }
  }
  
  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse response) throws IOException {

    final String path = req.getPathInfo();

    List<EndpointMethodRouted> endpointMethodsRouted = route("put", path);
    
    if (endpointMethodsRouted != null) {

      if (endpointMethodsRouted.size() != 1) {
        throw new IllegalStateException("Multiple endpoint methods match the requested path");
      }
      
      final EndpointMethodRouted endpointMethodRouted = endpointMethodsRouted.iterator().next();

      final CastMap<String, Object> queryParams = new CastMapBase(getTypedValueParser());

      try {
        queryParams.putAll(deserializeUrlParams(req));
      } catch (Throwable e) {
        e.printStackTrace();

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println("Failed to parse URL parameters");
        response.flushBuffer();
        return;
      }

      // DELETE does not support body params

      final Object result;

      try {
        // TODO better to pass the request here or put it into ThreadLocal like GWT does?
        result = endpointMethodRouted.endpointMethod.getData(req, endpointMethodRouted.pathParams, queryParams, null);

      } catch (CastOnGetException e) {

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println("Cannot parse the parameter " + e.getKey() + "=" + e.getValue() + " as " + e.getCastTo().getCanonicalName());
        response.flushBuffer();
        return;

      } catch (Exception e) {
        // TODO handle properly
        throw new RuntimeException(e);
      }

      if (result != null) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new JsonSerializer().serialize(result, response.getWriter());
        response.flushBuffer();
        return;
        
      } else {
        // empty response, write nothing, do not set content type as far as there is no content (however not a NO_CONTENT status)
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
        return;
      }

    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.flushBuffer();
      return;
    }
  }

  /**
   * @param req
   * @return or else empty map, non null
   */
  // return namely Map<String, String>, not Map<String, Object>
  protected Map<String, String> deserializeUrlParams(HttpServletRequest req) {
    final Map<String, String> map = new HashMap<>();

    if (req != null) {
      Map<String, String[]> original = req.getParameterMap();
      if (original != null) {
        for (String key: original.keySet()) {
          String[] values = original.get(key);
          if (values != null) {
            map.put(key, values[values.length - 1]);
          }
        }
      }
    }

    return map;
  }

  protected TypedValueParser getTypedValueParser() {
    return new TypedValueParserImpl();
  }

  protected Map<String, Object> buildSwaggerSchema(HttpServletRequest request) {
    final String basePath;
    final String title;

    String requestURI = request.getRequestURI();
    String path = request.getPathInfo();
    if (path != null) {
      if (!requestURI.endsWith(path)) {
        throw new IllegalStateException("HttpServletRequest.getRequestURI() must end with HttpServletRequest.getPathInfo()");
      }
      basePath = requestURI.substring(0, requestURI.length() - path.length());
    } else {
      basePath = requestURI;
    }
    
    String contextPath = request.getContextPath();
    if (contextPath.startsWith("/")) {
      title = contextPath.substring(1);
    } else {
      title = contextPath;
    }
    
    return new SwaggerSchema(basePath, title);
  }
  
  // protected: specific for the servlet class
  protected class SwaggerSchema extends HashMap<String, Object> {
    private static final long serialVersionUID = -6030879117607380842L;
    
    protected final TypeDeployer typeDeployer = new TypeDeployer.Recursive();
    
    public SwaggerSchema(String basePath, String title) {
      // meta info
      {
        put("swagger", "2.0"); // TODO constant value?
        
        {
          Map<String, String> info = new HashMap<>();
          info.put("version", "1.0.0");// TODO constant value?
          info.put("title", title);
          put("info", info);
        }
      }
      
      // base path
      put("basePath", basePath);
      
      
      Map<String, Map<String, Object>> paths = new HashMap<>(); 
      {
        for (MethodLocator locator: methods.keySet()) {
          Map<String, Object> pathItem = paths.get(locator.path);
          
          if (pathItem == null) {
            pathItem = new HashMap<String, Object>();
            paths.put(locator.path, pathItem);
          }
          
          // method object
          {
            final String httpMethod = locator.httpMethod.toLowerCase(); // swagger is case-sensitive!
            
            Map<String, Object> methodObject = new HashMap<>();
            
            methodObject.put("operationId", "operation-" + (int)(Math.random() * Integer.MAX_VALUE));// TODO friendly value
            
            methodObject.put("consumes", Arrays.asList("application/json;charset=utf-8"));
            methodObject.put("produces", Arrays.asList("application/json;charset=utf-8"));
            
            final EndpointMethod method = methods.get(locator);
            final ApiSpec apiSpec = method.apiSpec();
            
            
            final String summary = apiSpec == null ? null : apiSpec.summary();
            final String description = apiSpec == null ? null : apiSpec.description();
            final List<?> parameters = apiSpec == null ? null : apiSpec.parameters();
            final Map<Integer, ?> responses = apiSpec == null ? null : apiSpec.responses();
            
            if (summary != null) {
              methodObject.put("summary", summary);
            }
            if (description != null) {
              methodObject.put("description", description);
            }
             
            if (parameters != null && parameters.size() > 0) {
              methodObject.put("parameters", parameters);
            }
            
            if (responses != null) {
              // convert responses to plain objects
              Map<Integer, Object> responsesMap = new HashMap<>();
              
              for (Integer responseCode: responses.keySet()) {
                final Object responseObject = responses.get(responseCode);
                final Object responsePlain;
                
                if (responseObject instanceof Response) {
                  
                  final Response response = (Response) responseObject;
                  Map<String, Object> responseMap = new HashMap<>();
                  
                  {
                    responseMap.put("description", response.description);

                    Map<String, Object> schema = new HashMap<>();
                    typeDeployer.deploy(response.type, schema);
                    responseMap.put("schema", schema);
                  }
                  
                  responsePlain = responseMap;
                  
                } else {
                  // retain unchanged
                  responsePlain = responseObject;
                }
                
                responsesMap.put(responseCode, responsePlain);
              }
              
              methodObject.put("responses", responsesMap);
              
            } else {
              // at least put an empty map, otherwise the swagger will not show responses at all (even through 'Try it out')
              methodObject.put("responses", new HashMap<>());
            }
              
            pathItem.put(httpMethod, methodObject);
          }
          
        }
      }
      put("paths", paths);
      
      
      Map<String, Object> definitions = typeDeployer.getDefinitions();
      if (definitions.size() > 0) {
        put("definitions", definitions);
      }
    }
  }
}


