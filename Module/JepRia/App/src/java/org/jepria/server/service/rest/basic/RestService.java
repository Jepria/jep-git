package org.jepria.server.service.rest.basic;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

// В прикладной реализации сервиса регистрируются всевозможные эндпоинт-методы (или сущности целиком).
// Преимущества перед императивным подходом: решена проблема с выдачей 404 ошибки если запросили незарегистрированную сущность,
// для каждой сущности при регистрации можно указать не только data supplier, но и любую метаинформацию: Swagger, JsonSchema и т.д.
public class RestService extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public RestService() {}

  private final Map<MethodLocator, EndpointMethod> methods = new HashMap<>();

  public static abstract class EndpointMethod {
    public abstract Object getData(HttpServletRequest request, CastMap<String, ?> params) throws Exception;
    public EndpointMethod() {}
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
      httpMethod = httpMethod.toUpperCase();
    }
    if (path == null) {
      path = "";
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

    EndpointMethod endpointMethod = methods.get(newMethodLocator("GET", path));

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
        response.getWriter().println(new Gson().toJson(result));
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
  protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

    final String path = req.getPathInfo();

    EndpointMethod endpointMethod = methods.get(newMethodLocator("POST", path));

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
        response.getWriter().println(new Gson().toJson(result));
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
      try {
        final String body;
        try (Scanner sc = new Scanner(new InputStreamReader(req.getInputStream(), "UTF-8"))) {// TODO extract charset
          sc.useDelimiter("\\Z");
          if (sc.hasNext()) {
            body = sc.next();
          } else {
            body = null;
          }
        }

        final Map<String, Object> gsonMap;
        if (body != null) {
          gsonMap = deserialize(body);
        } else {
          gsonMap = new HashMap<>();
        }

        if (gsonMap != null) {
          map.putAll(gsonMap);
        }

      } catch (RuntimeException e) {
        throw e;
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }

    return map;
  }

  protected TypedValueParser getTypedValueParser() {
    return new TypedValueParserImpl();
  }
  
  /**
   * Creates a GsonBuilder instance that can be used to build Gson with various configuration settings.
   */
  protected GsonBuilder createGsonBuilder() {
    return new GsonBuilder();
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
   * @throws RuntimeException if parsing fails
   */
  protected Map<String, Object> deserialize(String json) {
    if (json == null) {
      return null;
    }
    return new Gson().fromJson(json, new TypeToken<Map<String, Object>>() { }.getType());
  }
}


