package org.jepria.server.service.rest.jaxrs;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jepria.server.load.rest.SearchParamsDto;
import org.jepria.server.service.rest.ResourceController;
import org.jepria.server.service.rest.ResourceControllerBase;
import org.jepria.server.service.rest.ResourceDescription;
import org.jepria.server.service.rest.ResourceSearchController;
import org.jepria.server.service.rest.ResourceSearchControllerBase;

import com.technology.jep.jepria.server.dao.JepDataStandard;

import io.swagger.annotations.ApiOperation;

/**
 * Standard Jaxrs REST resource
 */
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public abstract class StandardResourceEndpoint<D extends JepDataStandard> extends StandardEndpointBase {

  protected StandardResourceEndpoint() {}

  /**
   * Provides application resource description
   * @return
   */
  protected abstract ResourceDescription<D> getResourceDescription();

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
      super(getResourceDescription());
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
      super(getResourceDescription(),
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
  public Response listAsOptions() {
    return listOptions(getResourceDescription().getResourceName());
  }

  @GET
  @Path("{recordId}")
  @ApiOperation(value = "Get resource by ID")
  public Response getResourceById(@PathParam("recordId") String recordId) {
    Object record = resourceController.get().getResourceById(recordId, getCredential());
    if (record == null) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(record).build();
    }
  }

  @POST
  @ApiOperation(value = "Create a new instance")
  public Response create(Map<String, Object> instance) {
    Object createdId = resourceController.get().create(instance, getCredential());
    return Response.status(Status.CREATED).header("Location", createdId).build();
  }

  @DELETE
  @Path("{recordId}")
  @ApiOperation(value = "Delete resource by ID")
  public Response deleteResourceById(@PathParam("recordId") String recordId) {
    resourceController.get().deleteResource(recordId, getCredential());
    return Response.ok().build();
  }

  @PUT
  @Path("{recordId}")
  @ApiOperation(value = "Update resource by ID")
  public Response update(@PathParam("recordId") String recordId, Map<String, Object> fields) {
    resourceController.get().update(recordId, fields, getCredential());
    return Response.ok().build();
  }

  //////// OPTIONS ////////

  @GET
  @Path("option/{optionEntityName}")
  @ApiOperation(value = "List options by option-entity name")
  public Response listOptions(@PathParam("optionEntityName") String optionEntityName) {
    List<?> result = resourceController.get().listOptions(optionEntityName, getCredential());
    if (result == null || result.isEmpty()) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
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
    ExtendedResponseHeaderProcessor processor = new ExtendedResponseHeaderProcessor();
    processor.setRequest(request);
    processor.setResponse(response);
    processor.setHandler(new PostSearchExtendedResponseHandler(searchId));
    processor.process();
    if (processor.responseExtended()) {
      response = processor.getExtendedResponse();
    }

    return response;
  }

  /**
   * Реализация хендлера для postSearch-заголовков
   */
  private class PostSearchExtendedResponseHandler implements ExtendedResponseHeaderProcessor.Handler {

    private final String searchId;

    public PostSearchExtendedResponseHandler(String searchId) {
      this.searchId = searchId;
    }

    @Override
    public Object handle(String headerValue) {
      
      {// return resultset size
        if ("resultset-size".equals(headerValue)) {
          try {
            return searchController.get().getResultsetSize(searchId, getCredential());
          } catch (NoSuchElementException e) {
            // невозможно, поскольку searchId был создан в рамках этого же запроса
            throw new IllegalStateException();
          }
        }
      }

      {// return paged resultset: 'resultset/paged-by-x/y' or 'resultset?pageSize=x&page=y'

        // TODO or better to use org.glassfish.jersey.uri.UriTemplate? 
        // https://stackoverflow.com/questions/17840512/java-better-way-to-parse-a-restful-resource-url
        
        Matcher m1 = Pattern.compile("resultset/paged-by-(\\d+)/(\\d+)").matcher(headerValue);
        Matcher m2 = Pattern.compile("resultset\\?pageSize\\=(\\d+)&page\\=(\\d+)").matcher(headerValue);
        Matcher m3 = Pattern.compile("resultset\\?page\\=(\\d+)&pageSize\\=(\\d+)").matcher(headerValue);
  
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
          Response subresponse = getResultsetPaged(searchId, pageSize, page); 
          final Object subresponseData;
          if (subresponse != null && subresponse.getEntity() != null) {
            subresponseData = subresponse.getEntity(); 
          } else {
            subresponseData = new Object();// avoid null
          }
          

          final String href = URI.create(request.getRequestURL() + "/" + searchId + "/" + headerValue).toString();
          
          
          // компоновка ответа из ответа подзапроса, в виде
          // {
          //   "data": [<список с запрошенными результатами>],
          //   "href": "<для удобства: готовый url, по которому выдаются в точности те же данные, что и в поле data>"
          // }
          Map<String, Object> ret = new HashMap<>();
          ret.put("data", subresponseData);
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
  public Response getSearchRequest(
      @PathParam("searchId") String searchId) {
    final SearchParamsDto result;

    try {
      result = searchController.get().getSearchParams(searchId, getCredential());
    } catch (NoSuchElementException e) {
      // TODO log?
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(result).build();
  }

  @GET
  @Path("search/{searchId}/resultset-size")
  @ApiOperation(value = "Get search resultset size")
  public Response getSearchResultsetSize(
      @PathParam("searchId") String searchId) {
    final int result;

    try {
      result = searchController.get().getResultsetSize(searchId, getCredential());
    } catch (NoSuchElementException e) {
      // TODO log?
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(result).build();
  }

  @GET
  @Path("search/{searchId}/resultset")
  @ApiOperation(value = "Get resultset: whole or paged with query parameters")
  // either both pageSize and page are empty, or both are not empty
  public Response getResultset(
      @PathParam("searchId") String searchId,
      @QueryParam("pageSize") Integer pageSize, 
      @QueryParam("page") Integer page) {

    // paging is supported not only with path params, but also with query params
    if (pageSize != null || page != null) {
      if (pageSize == null || page == null) {
        return Response.status(Status.BAD_REQUEST.getStatusCode(), 
            "Either 'pageSize' and 'page' query params are both empty (for getting whole resultset), "
                + "or both non-empty (for getting resultset paged)").build();
      } else {
        return getResultsetPaged(searchId, pageSize, page);
      }
    }

    return getResultset(searchId);  
  }

  protected Response getResultset(String searchId) {

    final List<?> result;

    try {
      result = searchController.get().getResultset(searchId, getCredential());
    } catch (NoSuchElementException e) {
      // TODO log?
      return Response.status(Status.NOT_FOUND).build();
    }

    if (result == null || result.isEmpty()) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }

  private static final int DEFAULT_PAGE_SIZE = 25;

  @GET
  @Path("search/{searchId}/resultset/paged-by-{pageSize:\\d+}/{page}")
  @ApiOperation(value = "Get resultset paged")
  public Response getResultsetPaged(
      @PathParam("searchId") String searchId,
      @PathParam("pageSize") Integer pageSize, 
      @PathParam("page") Integer page) {

    // normalize paging parameters
    pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
    page = page == null ? 1 : page;

    final List<?> result;

    try {
      result = searchController.get().getResultsetPaged(searchId, pageSize, page, getCredential());
    } catch (NoSuchElementException e) {
      // TODO log?
      return Response.status(Status.NOT_FOUND).build();
    }

    if (result == null || result.isEmpty()) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }
}