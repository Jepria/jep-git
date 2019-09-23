package org.jepria.server.service.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.*;

/**
 * Функционал создания расширенных ответов сервиса по запросу клиентом в заголовке "extended-response".
 * <br>
 * Пример использования:
 * <br>
 * Предположим, некоторый сервис работает следующим образом:
 * <pre>
 * Запрос: /user/123
 * Ответ: {
 *   "name": "John",
 *   "age": 30
 * }
 * </pre>
 * Ответ сервиса может быть расширен другими данными, если клиент запрашивает их в заголовке запроса "extended-response".
 * Например, сервис поддерживает расширения ответов дополнительными данными по запросам "address" и "family".
 * Тогда сервис работает следующим образом:
 * <pre>
 * Запрос: /user/123; Header "extended-response: address"
 * Ответ: {
 *   "basic-response": {
 *     "name": "John",
 *     "age": 30
 *   },
 *   "extended-response": {
 *     "address": {
 *       "street": "20 Avenue",
 *       "building": 12
 *     }
 *   }
 * }
 * Запрос: /user/123; Header "extended-response: family"
 * Ответ: {
 *   "basic-response": {
 *     "name": "John",
 *     "age": 30
 *   },
 *   "extended-response": {
 *     "family": {
 *       "mother": {...},
 *       "father": {...}
 *     }
 *   }
 * }
 * Запрос: /user/123; Header "extended-response: address, family"
 * Ответ: {
 *   "basic-response": {
 *     "name": "John",
 *     "age": 30
 *   },
 *   "extended-response": {
 *     "address": {
 *       "street": "20 Avenue",
 *       "building": 12
 *     },
 *     "family": {
 *       "mother": {...},
 *       "father": {...}
 *     }
 *   }
 * }
 * </pre>
 * Реализация расширения для данного сервиса:
 * <pre>
 * class HandlerImpl implements ExtendedResponse.Handler {
 *   &#x40;Override
 *   public Object handle(String value) {
 *     if ("address".equals(value)) {
 *       // sample json tree building
 *       JsonTree tree = new JsonTree();
 *       tree.put("street", "20 Avenue");
 *       tree.put("building", 12);
 *       return tree;
 *     }
 *     if ("family".equals(value)) {
 *       // sample json tree building
 *       JsonTree tree = new JsonTree();
 *       tree.put("mother", getMother());
 *       tree.put("father", getFather());
 *       return tree;
 *     }
 *     return null;
 *   }
 * }
 * Response extendedResponse = ExtendedResponse.extend(basicResponse).valuesFrom(originalRequest).handler(new HandlerImpl()).create();
 * </pre>
 */
// TODO некрасивая архитектура класса (и сценарий его использования). Однако другие архитектуры (фабрика, кастомная аннотация, pojo-класс) ещё более некрасивы.
public class ExtendedResponse {
  
  /**
   * HTTP-заголовок запроса, требующий расширенного ответа
   */
  public static final String REQUEST_HEADER_NAME = "extended-response";
  
  /**
   * Конфигуратор расширения ответа 
   */
  public interface Configurator {
    /**
     * Конфигурирует источник значений: запрос.
     * @param request запрос, из заголовка которого берутся значения для обработки
     */
    Configurator valuesFrom(HttpServletRequest request);
    
    /**
     * Конфигурирует обработчик значений
     * @param handler обработчик значений
     */
    Configurator handler(Handler handler);
    
    /**
     * Создаёт расширенный ответ по конфигурации
     * @return расширенный ответ
     */
    Response create();
  }
  
  private ExtendedResponse() {}

  /**
   * Конфигурирует расширение заданного ответа
   * @param response ответ, подлежащий расширению
   * @return экземпляр конфигуратора
   */
  public static Configurator extend(Response response) {
    if (response == null) {
      throw new IllegalStateException("Cannot extend null response");
    }
    return new ConfiguratorImpl(response);
  }
  
  private static final class ConfiguratorImpl implements Configurator {
    private final Response response;
    
    private List<String> values;
    
    private Handler handler;
    
    public ConfiguratorImpl(Response response) {
      this.response = response;
    }

    @Override
    public Configurator valuesFrom(HttpServletRequest request) {
      final String header = request.getHeader(REQUEST_HEADER_NAME);
      if (header != null) {
        values = new ArrayList<>();
        String[] headerValues = header.split("\\s*,\\s*");
        for (String headerValue: headerValues) {
          if (headerValue != null) {
            values.add(headerValue);
          }
        }
      }
      
      return this;
    }

    @Override
    public Configurator handler(Handler handler) {
      this.handler = handler;
      return this;
    }

    @Override
    public Response create() {
      
      if (values == null) {
        // do not extend the response
        return response;
      }
      
      
      final Map<String, Object> extendedResponses = new HashMap<>();
      
      if (handler != null) {
        for (String value: values) {
          Object extendedEntityPart = handler.handle(value);
          
          if (extendedEntityPart != null) {
            extendedResponses.put(value, extendedEntityPart);
          } else {
            // null means that the particular value is not supported by the handler 
          }
        }
      }
      
      
      ResponseBuilder extendedResponseBuilder = Response.fromResponse(response);
      Map<String, Object> extendedEntity = new HashMap<>();
      
      Object basicEntity = response.getEntity();
      if (basicEntity == null) {
        // even if basic entity is null, make it present in the extended response
        basicEntity = Collections.emptyMap();
      }
      
      extendedEntity.put("basic-response", basicEntity);
      extendedEntity.put("extended-response", extendedResponses);
      extendedResponseBuilder.entity(extendedEntity);
      
      
      return extendedResponseBuilder.build();
    }
    
    
  }
  
  /**
   * Прикладной обработчик значений расширяемого ответа 
   */
  public static interface Handler {
    /**
     * @param value not null значение для обработки
     * @return объект, являющийся расширением ответа для заданного значения; null если значение не поддерживается обработчиком
     */
    Object handle(String value);
  }
}
