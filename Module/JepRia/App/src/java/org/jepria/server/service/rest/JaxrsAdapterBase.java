package org.jepria.server.service.rest;

import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.jepria.server.data.ColumnSortConfigurationDto;
import org.jepria.server.data.SearchRequestDto;
import org.jepria.server.service.security.JepSecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Path("") // important
/**
 * jaxrs-адаптер (транспортный слой)
 * <br/>
 * <i>В устаревшей терминологии: endpoint, EndpointBase</i>
 * <br/>
 */
public class JaxrsAdapterBase {

  protected JaxrsAdapterBase() {}

  @Context
  protected HttpServletRequest request;

  @Context
  protected JepSecurityContext securityContext;

  /**
   * Adapter for endpoint methods related to entity operations
   */
  public class EntityEndpointAdapter {

    protected final Supplier<EntityService> entityService;

    public EntityEndpointAdapter(Supplier<EntityService> entityService) {
      this.entityService = entityService;
    }

    public Object getRecordById(String recordId) {
      final Object record;

      try {
        record = entityService.get().getRecordById(recordId, securityContext.getCredential());
      } catch (NoSuchElementException e) {
        // 404
        throw new NotFoundException(e);
      }

      return record;
    }

    public Response create(Object record) {
      final String createdId = entityService.get().create(record, securityContext.getCredential());

      // ссылка на созданную запись
      final URI location = URI.create(request.getRequestURL() + "/" + createdId);
      Response response = Response.created(location).build();

      return response;
    }

    public void deleteRecordById(String recordId) {
      entityService.get().deleteRecord(recordId, securityContext.getCredential());
    }

    public void update(String recordId, Object record) {
      entityService.get().update(recordId, record, securityContext.getCredential());
    }

  }

  /**
   * Adapter for endpoint methods related to search operations
   */
  public class SearchEndpointAdapter {

    protected final Supplier<SearchService> searchService;

    public SearchEndpointAdapter(Supplier<SearchService> searchService) {
      this.searchService = searchService;
    }

    /**
     * Supports HTTP request headers:
     * <br/>
     * {@code "Extended-Response: resultset/paged-by-X/Y"}
     * <br/>
     * {@code "Extended-Response: resultset?pageSize=X&page=Y"}
     * <br/>
     * {@code "Extended-Response: resultset?page=Y&pageSize=X"}
     * <br/>
     * to get the search resultset in the same POST request response body, instead of POST+GET
     * @param searchRequestDto
     * @param extendedResponse Extended-Response header value
     * @param cacheControl Cache-Control header value
     * @param <T>
     * @return
     */
    public <T> Response postSearch(SearchRequestDto<T> searchRequestDto, String extendedResponse, String cacheControl) {

      final SearchService.SearchRequest searchRequest = convertSearchRequest(searchRequestDto);

      final String searchId = searchService.get().postSearchRequest(searchRequest, securityContext.getCredential());

      // ссылка на созданную запись
      final URI location = URI.create(request.getRequestURL() + "/" + searchId);
      Response response = Response.created(location).build();


      // клиент может запросить ответ, расширенный результатами поиска данного запроса
      // TODO переместить запрос расширенного ответа из заголовка в параметр запроса? Поддержать оба случая?
      if (extendedResponse != null) {
        response = ExtendedResponse.extend(response).valuesFrom(request).handler(new PostSearchExtendedResponseHandler(searchId)).create();
      }

      return response;
    }

    protected class SearchRequestImpl implements SearchService.SearchRequest {

      protected final Object templateDto;
      protected final String templateToken;
      protected final Map<String, Integer> listSortConfig;

      public SearchRequestImpl(Object templateDto, Map<String, Integer> listSortConfig) {
        this.templateDto = templateDto;

        // для преобразования в токен используется не общий контексно-зависимый сериализатор, а просто _некий_ сериализатор
        this.templateToken = templateDto == null ? null : new Gson().toJson(templateDto);

        this.listSortConfig = listSortConfig;
      }

      @Override
      public Object getTemplate() {
        return templateDto;
      }

      @Override
      public String getTemplateToken() {
        return templateToken;
      }

      @Override
      public Map<String, Integer> getListSortConfig() {
        return listSortConfig;
      }
    }

    /**
     * Converts SearchRequestDto (for transferring) to a SearchRequest (internal representation)
     * @param searchRequestDto
     * @return null for null
     */
    protected SearchService.SearchRequest convertSearchRequest(SearchRequestDto<?> searchRequestDto) {
      if (searchRequestDto == null) {
        return null;
      }

      final Object templateDto = searchRequestDto.getTemplate();
      final Map<String, Integer> listSortConfig = convertListSortConfig(searchRequestDto.getListSortConfiguration());

      return new SearchRequestImpl(templateDto, listSortConfig);
    }

    /**
     * Converts SearchRequest (internal representation) to a SearchRequestDto (for transferring)
     * @param searchRequest
     * @return null for null
     */
    protected SearchRequestDto<?> convertSearchRequest(SearchService.SearchRequest searchRequest) {
      if (searchRequest == null) {
        return null;
      }

      final SearchRequestDto<Object> searchRequestDto = new SearchRequestDto<>();
      searchRequestDto.setTemplate(searchRequest.getTemplate());
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
              return searchService.get().getResultsetSize(searchId, securityContext.getCredential());
            } catch (Throwable e) {
              // TODO process jaxrs exceptions like NotFoundException or BadRequestException differently, or add "status":"exception" as an Extended-Response block

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
            List<?> subresponse;
            try {
              subresponse = getResultsetPaged(searchId, pageSize, page, null);
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


        // намеренно не поддерживается возврат полного результата (/resultset) в Extended-Response, потому что в общем случае
        // клиент должен принять решение о том, запрашивать ли результат целиком только на основе ответа /resultset-size,
        // что невозможно в рамках одного запроса-ответа

        return null;
      }
    }


    public SearchRequestDto<?> getSearchRequest(
            String searchId) {
      final SearchService.SearchRequest searchRequest;

      try {
        searchRequest = searchService.get().getSearchRequest(searchId, securityContext.getCredential());
      } catch (NoSuchElementException e) {
        // 404
        throw new NotFoundException(e);
      }

      final SearchRequestDto<?> result = convertSearchRequest(searchRequest);

      return result;
    }

    /**
     * @param listSortConfig
     * @return <b>ordered</b> map, modifiable collection, null for null is important
     */
    protected Map<String, Integer> convertListSortConfig(List<ColumnSortConfigurationDto> listSortConfig) {
      if (listSortConfig == null) {
        return null;
      }

      final LinkedHashMap<String, Integer> ret = new LinkedHashMap<>();
      for (ColumnSortConfigurationDto colSortConfig: listSortConfig) {
        ret.put(colSortConfig.getColumnName(), "desc".equals(colSortConfig.getSortOrder()) ? -1 : 1);
      }
      return ret;
    }

    /**
     *
     * @param listSortConfig <b>ordered</b> map
     * @return null for null is important
     */
    private List<ColumnSortConfigurationDto> convertListSortConfig(Map<String, Integer> listSortConfig) {
      if (listSortConfig == null) {
        return null;
      }

      final List<ColumnSortConfigurationDto> ret = new ArrayList<>();
      for (Map.Entry<String, Integer> colSortConfig: listSortConfig.entrySet()) {
        ColumnSortConfigurationDto colSortConfigDto = new ColumnSortConfigurationDto();
        colSortConfigDto.setColumnName(colSortConfig.getKey());
        colSortConfigDto.setSortOrder(colSortConfig.getValue() != null && colSortConfig.getValue() < 0 ? "desc" : "asc");
        ret.add(colSortConfigDto);
      }
      return ret;
    }

    /**
     * Invalidates the resultset if the request contains header {@code Cache-Control: no-cache}
     * @param searchId
     * @param cacheControl Cache-Control header value
     */
    protected void invalidateResultsetOnNoCache(String searchId, String cacheControl) {
      if ("no-cache".equals(cacheControl)) {
        searchService.get().invalidateResultset(searchId);
      }
    }

    public int getSearchResultsetSize(String searchId, String cacheControl) {

      invalidateResultsetOnNoCache(searchId, cacheControl);

      final int result;

      try {
        result = searchService.get().getResultsetSize(searchId, securityContext.getCredential());
      } catch (NoSuchElementException e) {
        throw new NotFoundException(e);
      }

      return result;
    }

    /**
     *
     * @param searchId
     * @param pageSize
     * @param page
     * @param cacheControl Cache-Control header value
     * @return
     */
    // either both pageSize and page are empty, or both are not empty
    public List<?> getResultset(
            String searchId,
            Integer pageSize,
            Integer page,
            String cacheControl) {

      // paging is supported not only with path params, but also with query params
      if (pageSize != null || page != null) {
        if (pageSize == null || page == null) {

          final String message = "Either 'pageSize' and 'page' query params are both empty (for getting whole resultset), "
                  + "or both non-empty (for getting resultset paged)";
          throw new BadRequestException(message);

        } else {
          return getResultsetPaged(searchId, pageSize, page, cacheControl);
        }
      }

      return getResultset(searchId, cacheControl);
    }

    /**
     *
     * @param searchId
     * @param cacheControl Cache-Control header value
     * @return
     */
    protected List<?> getResultset(String searchId, String cacheControl) {

      invalidateResultsetOnNoCache(searchId, cacheControl);

      final List<?> records;

      try {
        records = searchService.get().getResultset(searchId, securityContext.getCredential());
      } catch (NoSuchElementException e) {
        // 404
        throw new NotFoundException(e);
      }

      if (records == null || records.isEmpty()) {
        // 204
        return null;
      } else {

        return records;
      }
    }

    // TODO move the constant out of here?
    protected static final int DEFAULT_PAGE_SIZE = 25;

    /**
     *
     * @param searchId
     * @param pageSize
     * @param page
     * @param cacheControl Cache-Control header value
     * @return
     */
    public List<?> getResultsetPaged(
            String searchId,
            Integer pageSize,
            Integer page,
            String cacheControl) {

      // normalize paging parameters
      pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
      page = page == null ? 1 : page;

      invalidateResultsetOnNoCache(searchId, cacheControl);

      final List<?> records;

      try {
        records = searchService.get().getResultsetPaged(searchId, pageSize, page, securityContext.getCredential());
      } catch (NoSuchElementException e) {
        // 404
        throw new NotFoundException(e);
      }

      if (records == null || records.isEmpty()) {
        // 204
        return null;
      } else {

        return records;
      }
    }
  }
}
