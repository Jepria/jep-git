package org.jepria.server.service.rest.jaxrs;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.ws.rs.core.Response.Status;

import org.jepria.server.dao.Dao;
import org.jepria.server.dao.DtoUtil;
import org.jepria.server.load.rest.SearchParamsDto;
import org.jepria.server.service.rest.ResourceController;
import org.jepria.server.service.rest.ResourceControllerBase;
import org.jepria.server.service.rest.ResourceSearchController;
import org.jepria.server.service.rest.ResourceSearchControllerBase;
import org.jepria.shared.RecordDefinition;

import io.swagger.annotations.ApiOperation;

/**
 * Standard Jaxrs REST resource
 */
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public abstract class StandardResourceEndpoint<D extends Dao, T> extends StandardEndpointBase {

  /**
   * Implementors provide standard applicational description for the REST resources  
   */
  public static interface Description<D extends Dao> {
    D getDao();
    String getResourceName();
    RecordDefinition getRecordDefinition();
  }
  
  protected final Description<D> description;
  
  // декомпозиция: Class<T> dtoClass не входит в Description<D>, чтобы не параметризовать Description типом T 
  protected final Class<T> dtoClass;

  protected StandardResourceEndpoint(Description<D> description, Class<T> dtoClass) {
    this.description = description;
    this.dtoClass = dtoClass;
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
      super(new Supplier<Dao>() {
        @Override
        public Dao get() {
          return description.getDao();
        }
      }, description.getRecordDefinition());
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
      super(new Supplier<Dao>() {
        @Override
        public Dao get() {
          return description.getDao();
        }
      }, description.getRecordDefinition(),
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
  @ApiOperation(value = "List this resource as options")
  public List<?> listAsOptions() {
    return listOptions(description.getResourceName());
  }

  @GET
  @Path("{recordId}")
  @ApiOperation(value = "Get resource by ID")
  public T getResourceById(@PathParam("recordId") String recordId) {
    final T resource;

    try {
      Map<String, ?> record = resourceController.get().getResourceById(recordId, getCredential());
      resource = DtoUtil.mapToDto(record, dtoClass);
    } catch (NoSuchElementException e) {
      // 404
      throw new NotFoundException(e);
    }

    return resource;
  }

  @POST
  @ApiOperation(value = "Create a new resource instance")
  public Response create(T resource) {
    Map<String, ?> record = DtoUtil.dtoToMap(resource);
    final String createdId = resourceController.get().create(record, getCredential());
    return Response.status(Status.CREATED).header("Location", createdId).build();
  }

  @DELETE
  @Path("{recordId}")
  @ApiOperation(value = "Delete resource by ID")
  public void deleteResourceById(@PathParam("recordId") String recordId) {
    resourceController.get().deleteResource(recordId, getCredential());
  }

  @PUT
  @Path("{recordId}")
  @ApiOperation(value = "Update resource by ID")
  public void update(@PathParam("recordId") String recordId, T resource) {
    Map<String, ?> record = DtoUtil.dtoToMap(resource);
    resourceController.get().update(recordId, record, getCredential());
  }

  //////// OPTIONS ////////

  @GET
  @Path("option/{optionEntityName}")
  @ApiOperation(value = "List options by option-entity name")
  public List<?> listOptions(@PathParam("optionEntityName") String optionEntityName) {
    final List<?> result;

    try {
      result = resourceController.get().listOptions(optionEntityName, getCredential());
    } catch (NoSuchElementException e) {
      // 404
      throw new NotFoundException(e);
    }
    if (result == null || result.isEmpty()) {
      // 204
      return null;
    } else {
      return result;
    }
  }

  //////// SEARCH ////////

  @POST
  @Path("search")
  @ApiOperation(value = "Post search params")
  /**
   * @param searchParams
   * @return
   */
  public Response postSearch(SearchParamsDto searchParams) {
    final String searchId = searchController.get().postSearchRequest(searchParams, getCredential());

    // ссылка на созданный ресурс
    final URI location = URI.create(request.getRequestURL() + "/" + searchId);
    Response response = Response.created(location).build();


    // клиент может запросить ответ, расширенный результатами поиска данного запроса
    response = ExtendedResponse.extend(response).valuesFrom(request).handler(new PostSearchExtendedResponseHandler(searchId)).create(); 

    return response;
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
          List<T> subresponse;
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
  @ApiOperation(value = "Get search request by ID")
  public SearchParamsDto getSearchRequest(
      @PathParam("searchId") String searchId) {
    final SearchParamsDto result;

    try {
      result = searchController.get().getSearchParams(searchId, getCredential());
    } catch (NoSuchElementException e) {
      // 404
      throw new NotFoundException(e);
    }

    return result;
  }

  @GET
  @Path("search/{searchId}/resultset-size")
  @ApiOperation(value = "Get search resultset size")
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
  @ApiOperation(value = "Get resultset: whole or paged with query parameters")
  // either both pageSize and page are empty, or both are not empty
  public List<T> getResultset(
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

  protected List<T> getResultset(String searchId) {

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

      final List<T> result = records.stream().map(record -> DtoUtil.mapToDto(record, dtoClass)).collect(Collectors.toList());
      return result;
    }
  }

  private static final int DEFAULT_PAGE_SIZE = 25;

  @GET
  @Path("search/{searchId}/resultset/paged-by-{pageSize:\\d+}/{page}")
  @ApiOperation(value = "Get resultset paged")
  public List<T> getResultsetPaged(
      @PathParam("searchId") String searchId,
      @PathParam("pageSize") Integer pageSize, 
      @PathParam("page") Integer page) {

    // normalize paging parameters
    pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
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

      final List<T> result = records.stream().map(record -> DtoUtil.mapToDto(record, dtoClass)).collect(Collectors.toList());
      return result;
    }
  }
}