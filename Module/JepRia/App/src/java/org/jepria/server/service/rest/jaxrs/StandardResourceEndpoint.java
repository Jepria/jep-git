package org.jepria.server.service.rest.jaxrs;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jepria.compat.CoreCompat;
import org.jepria.server.data.ColumnSortConfigurationDto;
import org.jepria.server.data.Dao;
import org.jepria.server.data.DtoUtil;
import org.jepria.server.data.OptionDto;
import org.jepria.server.data.RecordDefinition;
import org.jepria.server.data.SearchRequestDto;
import org.jepria.server.service.rest.ResourceController;
import org.jepria.server.service.rest.ResourceControllerBase;
import org.jepria.server.service.rest.ResourceSearchController;
import org.jepria.server.service.rest.ResourceSearchControllerBase;
import org.jepria.server.service.rest.SearchRequest;

/**
 * Standard Jaxrs REST resource
 * <br/>
 * Не содержит Swagger-аннотаций.
 * <br/>
 * Из двух вариантов работы endpoint-методов
 * <ol>
 * <li>Возвращать объект типа Response с заданными entity и статусом</li>
 * <li>Возвращать объект-entity любого прикладного типа, порождать ошибочные статусы классами javax.ws.rs.*Exception</li>
 * </ol>
 * выбран второй вариант (первый вариант применяется только там, где второй невозможен)
 * 
 * @param TQ заглушка для обеспечения работоспособности Swagger:
 * Cейчас Swagger игнорирует (не документирует) endpoint-методы с generic-параметрами, в частности, метод {@code postSearch(SearchRequestDto<TS> searchRequestDto)}.
 * Чтобы Swagger документировал этот метод, необходимо создать прикланой класс-маркер {@code TQ extends SearchRequestDto<TS>} и объявить метод в виде
 * {@code postSearch(TQ searchRequestDto)}. При обеспечении работоспособности Swagger с generic-параметрами, убрать параметр {@code TQ}, 
 * и все его упоминания заменить {@code SearchRequestDto<TS>}  
 */
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class StandardResourceEndpoint<D extends Dao, TR, TW, TS, TQ extends SearchRequestDto<TS>> extends StandardEndpointBase {

  /**
   * Прикладное описание стандартного REST-ресурса  
   */
  public static interface Description<D extends Dao> {
    D getDao();
    /**
     * @return название сущности, которой управляет ресурс
     */
    String getResourceName();
    RecordDefinition getRecordDefinition();
  }

  protected final Description<D> description;

  // декомпозиция: вспомогательные (для рефлективного создания Dto) поля типа Class<?> не входят в Description<D> (чтобы не параметризовать Description типами) 
  protected final Class<TR> dtoReadClass;
  protected final Class<TS> dtoSearchClass;

  protected StandardResourceEndpoint(Description<D> description, Class<TR> dtoReadClass, Class<TS> dtoSearchClass) {
    this.description = description;
    this.dtoReadClass = dtoReadClass;
    this.dtoSearchClass = dtoSearchClass;
  }

  /**
   * Supplier protects the internal field from direct access from within the class members,
   * and initializes the field lazily (due to the DI: the injectable fields are being injected after the object construction)
   */
  protected final Supplier<ResourceController> resourceController = new Supplier<ResourceController>() {
    private ResourceController instance = null;
    @Override
    public ResourceController get() {
      if (instance == null) {
        instance = createResourceController();
      }
      return instance;
    }
  };

  /**
   * Локальное (внутреннее) расширение внешнего класса для упрощённого использования в наследниках.
   * Использование в наследниках упрощается наличием у локального класса конструктора без параметров
   * (локальный класс неявно зависит от содержащего класса)
   */
  protected class ResourceControllerImplLocal extends ResourceControllerBase {
    protected ResourceControllerImplLocal() {
      super(description.getDao(), description.getRecordDefinition());
    }
  }

  protected ResourceController createResourceController() {
    return new ResourceControllerImplLocal();
  }


  /**
   * Supplier protects the internal field from direct access from within the class members,
   * and initializes the field lazily (due to the DI: the injectable fields are being injected after the object construction)
   */
  protected final Supplier<ResourceSearchController> searchController = new Supplier<ResourceSearchController>() {
    private ResourceSearchController instance = null;
    @Override
    public ResourceSearchController get() {
      if (instance == null) {
        instance = createSearchController();
      }
      return instance;
    }
  };


  /**
   * Локальное (внутреннее) расширение внешнего класса для упрощённого использования в наследниках.
   * Использование в наследниках упрощается наличием у локального класса конструктора без параметров
   * (локальный класс неявно зависит от содержащего класса)
   */
  protected class ResourceSearchControllerImplLocal extends ResourceSearchControllerBase {
    protected ResourceSearchControllerImplLocal() {
      super(description.getDao(), description.getRecordDefinition(),
          new Supplier<HttpSession>() {
        @Override
        public HttpSession get() {
          return request.getSession();
        }
      });
    }
  }

  protected ResourceSearchController createSearchController() {
    return new ResourceSearchControllerImplLocal();
  }

  //////// CRUD ////////

  @GET
  @Path("{recordId}")
  public TR getResourceById(@PathParam("recordId") String recordId) {
    final TR resource;

    try {
      Map<String, ?> record = resourceController.get().getResourceById(recordId, getCredential());
      resource = DtoUtil.mapToDto(record, dtoReadClass);
    } catch (NoSuchElementException e) {
      // 404
      throw new NotFoundException(e);
    }

    return resource;
  }

  @POST
  public Response create(TW resource) {
    Map<String, ?> record = DtoUtil.dtoToMap(resource);
    final String createdId = resourceController.get().create(record, getCredential());
    
    // ссылка на созданный ресурс
    final URI location = URI.create(request.getRequestURL() + "/" + createdId);
    Response response = Response.created(location).build();
    
    return response;
  }

  @DELETE
  @Path("{recordId}")
  public void deleteResourceById(@PathParam("recordId") String recordId) {
    resourceController.get().deleteResource(recordId, getCredential());
  }

  @PUT
  @Path("{recordId}")
  public void update(@PathParam("recordId") String recordId, TW resource) {
    Map<String, ?> record = DtoUtil.dtoToMap(resource);
    resourceController.get().update(recordId, record, getCredential());
  }

  //////// OPTIONS ////////

  @GET
  public List<OptionDto> listAsOptions() {
    return listOptions(description.getResourceName());
  }
  
  @GET
  @Path("option/{optionEntityName}")
  public List<OptionDto> listOptions(@PathParam("optionEntityName") String optionEntityName) {
    final List<?> records;

    try {
      records = resourceController.get().listOptions(optionEntityName, getCredential());
    } catch (NoSuchElementException e) {
      // 404
      throw new NotFoundException(e);
    }
    if (records == null || records.isEmpty()) {
      // 204
      return null;
    } else {
      
      final List<Map<String, ?>> options = (List<Map<String, ?>>)records;
      
      final List<OptionDto> result = options.stream().map(record -> DtoUtil.mapToOptionDto(record)).collect(Collectors.toList());
      
      return result;
    }
  }

  //////// SEARCH ////////

  @POST
  @Path("search")
  /**
   * @param searchRequestDto
   * @return
   */
  public Response postSearch(TQ searchRequestDto) {
    
    final SearchRequest searchRequest = convertSearchRequest(searchRequestDto);
    
    final String searchId = searchController.get().postSearchRequest(searchRequest, getCredential());

    // ссылка на созданный ресурс
    final URI location = URI.create(request.getRequestURL() + "/" + searchId);
    Response response = Response.created(location).build();


    // клиент может запросить ответ, расширенный результатами поиска данного запроса
    response = ExtendedResponse.extend(response).valuesFrom(request).handler(new PostSearchExtendedResponseHandler(searchId)).create(); 

    return response;
  }
  
  private SearchRequest convertSearchRequest(SearchRequestDto<TS> searchRequestDto) {
    if (searchRequestDto == null) {
      return null;
    }
    
    final Map<String, Object> template = DtoUtil.dtoToMap(searchRequestDto.getTemplate());
    final LinkedHashMap<String, Integer> listSortConfig = convertListSortConfig(searchRequestDto.getListSortConfiguration());
    
    return new SearchRequest() {
      @Override
      public Map<String, Object> getTemplate() {
        return template;
      }
      
      @Override
      public LinkedHashMap<String, Integer> getListSortConfig() {
        return listSortConfig;
      }
    };
  }
  
  private SearchRequestDto<TS> convertSearchRequest(SearchRequest searchRequest) {
    if (searchRequest == null) {
      return null;
    }
    
    final SearchRequestDto<TS> searchRequestDto = new SearchRequestDto<>();
    searchRequestDto.setTemplate(DtoUtil.mapToDto(searchRequest.getTemplate(), dtoSearchClass));
    searchRequestDto.setListSortConfiguration(convertListSortConfig(searchRequest.getListSortConfig()));
    return searchRequestDto;
  }
  
  /**
   * Реализация хендлера для postSearch-заголовков
   */
  private class PostSearchExtendedResponseHandler implements ExtendedResponse.Handler {

    private final String searchId;

    public PostSearchExtendedResponseHandler(String searchId) {
      this.searchId = searchId;
    }

    @Override
    public Object handle(String value) {

      {// return resultset size
        if ("resultset-size".equals(value)) {
          try {
            return searchController.get().getResultsetSize(searchId, getCredential());
          } catch (Throwable e) {
            // TODO process jaxrs exceptions like NotFoundException or BadRequestException differently, or add "status":"exception" as an extended-response block 

            // do not re-throw
            e.printStackTrace();
            return null;
          }
        }
      }

      {// return paged resultset: 'resultset/paged-by-x/y' or 'resultset?pageSize=x&page=y'

        // TODO or better to use org.glassfish.jersey.uri.UriTemplate? 
        // https://stackoverflow.com/questions/17840512/java-better-way-to-parse-a-restful-resource-url

        Matcher m1 = Pattern.compile("resultset/paged-by-(\\d+)/(\\d+)").matcher(value);
        Matcher m2 = Pattern.compile("resultset\\?pageSize\\=(\\d+)&page\\=(\\d+)").matcher(value);
        Matcher m3 = Pattern.compile("resultset\\?page\\=(\\d+)&pageSize\\=(\\d+)").matcher(value);

        if (m1.matches() || m2.matches() || m3.matches()) {
          final int pageSize, page;
          if (m1.matches()) {
            pageSize = Integer.valueOf(m1.group(1));// TODO possible Integer overflow
            page = Integer.valueOf(m1.group(2));// TODO possible Integer overflow
          } else if (m2.matches()) {
            pageSize = Integer.valueOf(m2.group(1));// TODO possible Integer overflow
            page = Integer.valueOf(m2.group(2));// TODO possible Integer overflow
          } else if (m3.matches()) {
            pageSize = Integer.valueOf(m3.group(2));// TODO possible Integer overflow
            page = Integer.valueOf(m3.group(1));// TODO possible Integer overflow
          } else {
            // programmatically impossible
            throw new IllegalStateException();
          }

          // подзапрос на выдачу данных
          List<TR> subresponse;
          try {
            subresponse = getResultsetPaged(searchId, pageSize, page);
          } catch (Throwable e) {
            // TODO process jaxrs exceptions like NotFoundException or BadRequestException differently...

            // do not re-throw
            e.printStackTrace();
            return null;
          }

          if (subresponse == null) {
            subresponse = new ArrayList<>();
          }

          final String href = URI.create(request.getRequestURL() + "/" + searchId + "/" + value).toString();


          // компоновка ответа из ответа подзапроса, в виде
          // {
          //   "data": [<список с запрошенными результатами>],
          //   "href": "<для удобства: готовый url, по которому выдаются в точности те же данные, что и в поле data>"
          // }
          Map<String, Object> ret = new HashMap<>();
          ret.put("data", subresponse);
          ret.put("href", href);

          return ret;
        }
      }


      // намеренно не поддерживается возврат полного результата (/resultset) в extended-response, потому что в общем случае
      // клиент должен принять решение о том, запрашивать ли результат целиком только на основе ответа /resultset-size,
      // что невозможно в рамках одного запроса-ответа

      return null;
    }
  }

  @GET
  @Path("search/{searchId}")
  public SearchRequestDto<TS> getSearchRequest(
      @PathParam("searchId") String searchId) {
    final SearchRequest searchRequest;

    try {
      searchRequest = searchController.get().getSearchRequest(searchId, getCredential());
    } catch (NoSuchElementException e) {
      // 404
      throw new NotFoundException(e);
    }
    
    final SearchRequestDto<TS> result = convertSearchRequest(searchRequest);
    
    return result;
  }
  
  private LinkedHashMap<String, Integer> convertListSortConfig(List<ColumnSortConfigurationDto> listSortConfig) {
    final LinkedHashMap<String, Integer> ret = new LinkedHashMap<>();
    
    if (listSortConfig != null) {
      for (ColumnSortConfigurationDto colSorftConfig: listSortConfig) {
        ret.put(colSorftConfig.getColumnName(), "desc".equals(colSorftConfig.getSortOrder()) ? -1 : 1);
      }
    }
    
    return ret;
  }
  
  private List<ColumnSortConfigurationDto> convertListSortConfig(LinkedHashMap<String, Integer> listSortConfig) {
    final List<ColumnSortConfigurationDto> ret = new ArrayList<>();
    
    if (listSortConfig != null) {
      for (Map.Entry<String, Integer> colSortConfig: listSortConfig.entrySet()) {
        ColumnSortConfigurationDto colSortConfigDto = new ColumnSortConfigurationDto();
        colSortConfigDto.setColumnName(colSortConfig.getKey());
        colSortConfigDto.setSortOrder(colSortConfig.getValue() != null && colSortConfig.getValue() < 0 ? "desc" : "asc");
        ret.add(colSortConfigDto);
      }
    }
    
    return ret;
  }

  @GET
  @Path("search/{searchId}/resultset-size")
  public int getSearchResultsetSize(
      @PathParam("searchId") String searchId) {
    final int result;

    try {
      result = searchController.get().getResultsetSize(searchId, getCredential());
    } catch (NoSuchElementException e) {
      throw new NotFoundException(e);
    }

    return result;
  }

  @GET
  @Path("search/{searchId}/resultset")
  // either both pageSize and page are empty, or both are not empty
  public List<TR> getResultset(
      @PathParam("searchId") String searchId,
      @QueryParam("pageSize") Integer pageSize, 
      @QueryParam("page") Integer page) {

    // paging is supported not only with path params, but also with query params
    if (pageSize != null || page != null) {
      if (pageSize == null || page == null) {

        final String message = "Either 'pageSize' and 'page' query params are both empty (for getting whole resultset), "
            + "or both non-empty (for getting resultset paged)"; 
        throw new BadRequestException(message);

      } else {
        return getResultsetPaged(searchId, pageSize, page);
      }
    }

    return getResultset(searchId);  
  }

  protected List<TR> getResultset(String searchId) {

    final List<Map<String, ?>> records;

    try {
      records = searchController.get().getResultset(searchId, getCredential());
    } catch (NoSuchElementException e) {
      // 404
      throw new NotFoundException(e);
    }

    if (records == null || records.isEmpty()) {
      // 204
      return null;
    } else {

      final List<TR> result = records.stream().map(record -> DtoUtil.mapToDto(record, dtoReadClass)).collect(Collectors.toList());
      return result;
    }
  }

  @GET
  @Path("search/{searchId}/resultset/paged-by-{pageSize:\\d+}/{page}")
  public List<TR> getResultsetPaged(
      @PathParam("searchId") String searchId,
      @PathParam("pageSize") Integer pageSize, 
      @PathParam("page") Integer page) {

    // normalize paging parameters
    pageSize = pageSize == null ? CoreCompat.DEFAULT_PAGE_SIZE : pageSize;
    page = page == null ? 1 : page;

    final List<Map<String, ?>> records;

    try {
      records = searchController.get().getResultsetPaged(searchId, pageSize, page, getCredential());
    } catch (NoSuchElementException e) {
      // 404
      throw new NotFoundException(e);
    }

    if (records == null || records.isEmpty()) {
      // 204
      return null;
    } else {

      final List<TR> result = records.stream().map(record -> DtoUtil.mapToDto(record, dtoReadClass)).collect(Collectors.toList());
      return result;
    }
  }
}