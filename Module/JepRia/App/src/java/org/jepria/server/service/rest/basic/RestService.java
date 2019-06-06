package org.jepria.server.service.rest.basic;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jepria.CastMap;
import org.jepria.CastMap.CastOnGetException;
import org.jepria.TypedValueParser;
import org.jepria.TypedValueParser.TypedValueParseException;
import org.jepria.TypedValueParserImpl;
import org.jepria.server.service.rest.swagger.Response;
import org.jepria.server.service.rest.swagger.SwaggerInfo;
import org.jepria.server.service.rest.swagger.TypeDeployer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

// В прикладной реализации сервиса регистрируются всевозможные эндпоинт-методы (или сущности целиком).
// Преимущества перед императивным подходом: решена проблема с выдачей 404 ошибки если запросили незарегистрированную сущность,
// для каждой сущности при регистрации можно указать не только data supplier, но и любую метаинформацию: Swagger, JsonSchema и т.д.
public class RestService extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public RestService() {}

  private final Map<MethodLocator, EndpointMethod> methods = new LinkedHashMap<>(); // maintain order for clarity

  public static interface EndpointMethod {
    Object getData(HttpServletRequest request, CastMap<String, ?> params) throws Exception;
    
    default SwaggerInfo swaggerInfo() {
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
    public final String path;

    public MethodLocator(String httpMethod, String path) {
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
          MethodLocator typed = (MethodLocator) obj;
          return typed.hashCode() == this.hashCode();
        }
      }
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

    final String path = req.getPathInfo();
    
    if ("/swagger.json".equals(path)) {
      
      Map<String, Object> swaggerJsonMap = buildSwaggerSchema(req);
      StringBuilder sb = new StringBuilder();
      serialize(swaggerJsonMap, sb);
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

      EndpointMethod endpointMethod = methods.get(newMethodLocator("get", path));
  
      if (endpointMethod != null) {
  
        final ParamCastMap paramMap = new ParamCastMap();
  
        try {
          paramMap.putAll(deserializeUrlParams(req));
        } catch (Throwable e) {
          e.printStackTrace();
  
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          response.setContentType("text/plain");
          response.setCharacterEncoding("utf-8");
          response.getWriter().println("Failed to parse URL parameters");
          response.flushBuffer();
          return;
        }
  
        // GET does not suppot body
  
        final Object result;
  
        try {
          // TODO better to pass the request here or put it into ThreadLocal like GWT does?
          result = endpointMethod.getData(req, paramMap);
  
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
          serialize(result, response.getWriter());
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
  protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

    final String path = req.getPathInfo();

    EndpointMethod endpointMethod = methods.get(newMethodLocator("post", path));

    if (endpointMethod != null) {

      final ParamCastMap paramMap = new ParamCastMap();

      try {
        paramMap.putAll(deserializeUrlParams(req));
      } catch (Throwable e) {
        e.printStackTrace();

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println("Failed to parse URL parameters");
        response.flushBuffer();
        return;
      }

      // TODO body params will discard the same URL params
      try {
        paramMap.putAll(deserializeBody(req));
      } catch (Throwable e) {
        e.printStackTrace();

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println("Failed to parse JSON body");
        response.flushBuffer();
        return;
      }


      final Object result;

      try {
        // TODO better to pass the request here or put it into ThreadLocal like GWT does?
        result = endpointMethod.getData(req, paramMap);

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
        serialize(result, response.getWriter());
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

  /**
   * @param req
   * @return
   * @throws RuntimeException if parsing fails
   */
  protected Map<String, Object> deserializeBody(HttpServletRequest req) {
    final Map<String, Object> map = new HashMap<>();

    if (req != null) {
      
      final String body;
      
      try (Scanner sc = new Scanner(new InputStreamReader(req.getInputStream(), "UTF-8"))) {// TODO extract charset
        sc.useDelimiter("\\Z");
        if (sc.hasNext()) {
          body = sc.next();
        } else {
          body = null;
        }
      } catch (RuntimeException e) {
        throw e;
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }

      
      
      final Map<String, Object> gsonMap;
      
      if (body != null && !"".equals(body)) {
        try {
          gsonMap = deserialize(body);
        } catch (Throwable e) {
          
          // limit body log size (and append a hint)
          final String bodyLog; 
          
          {
            final int bodyLogSizeLimit = 10000; // no need to extract constant
            int bodySize = body.length();
            if (bodySize > bodyLogSizeLimit) {
              bodyLog = body.substring(0, bodyLogSizeLimit) + "|... " + bodyLogSizeLimit + " out of " + (bodySize - bodyLogSizeLimit) + " chars displayed";
            } else {
              bodyLog = body;
            }
          }
          
          throw new RuntimeException("Failed to deserialize the request body to JSON: [" + bodyLog + "]", e);
        }
        
      } else {
        gsonMap = null;
      }

      if (gsonMap != null) {
        map.putAll(gsonMap);
      }
    }
    
    return map;
  }

  protected TypedValueParser getTypedValueParser() {
    return new TypedValueParserImpl();
  }


  // local class
  protected class ParamCastMap extends HashMap<String, Object> implements CastMap<String, Object> {

    private static final long serialVersionUID = -4621908477271689859L;

    @Override
    public Integer getInteger(Object key) {
      try {
        return getTypedValueParser().parse(get(key), Integer.class);
      } catch (TypedValueParseException e) {
        throw new CastOnGetException(key, e.getValue(), Integer.class);
      }
    }

    @Override
    public String getString(Object key) {
      try {
        return getTypedValueParser().parse(get(key), String.class);
      } catch (TypedValueParseException e) {
        throw new CastOnGetException(key, e.getValue(), String.class);
      }
    }

    @Override
    public BigDecimal getBigDecimal(Object key) {
      try {
        return getTypedValueParser().parse(get(key), BigDecimal.class);
      } catch (TypedValueParseException e) {
        throw new CastOnGetException(key, e.getValue(), BigDecimal.class);
      }
    }
  }

  /**
   * 
   * @param json may be {@code null}
   * @return
   * @throws RuntimeException if deserialization fails
   */
  protected Map<String, Object> deserialize(String json) {
    if (json == null) {
      return null;
    }
    return new Gson().fromJson(json, new TypeToken<Map<String, Object>>() { }.getType());
  }
  
  /**
   * 
   * @param object may be {@code null}
   * @param out to write result to
   * @throws RuntimeException if serialization fails
   */
  protected void serialize(Object object, Appendable out) {
    if (object == null) {
      return;
    }
    new Gson().toJson(object, out);
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
            final SwaggerInfo swaggerInfo = method.swaggerInfo();
            
            
            final String summary = swaggerInfo == null ? null : swaggerInfo.summary();
            final String description = swaggerInfo == null ? null : swaggerInfo.description();
            final List<?> parameters = swaggerInfo == null ? null : swaggerInfo.parameters();
            final Map<Integer, ?> responses = swaggerInfo == null ? null : swaggerInfo.responses();
            
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


