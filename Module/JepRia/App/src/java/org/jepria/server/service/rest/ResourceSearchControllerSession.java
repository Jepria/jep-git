package org.jepria.server.service.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import javax.servlet.http.HttpSession;

import org.jepria.compat.Compat;
import org.jepria.server.load.rest.ColumnSortConfigurationDto;
import org.jepria.server.load.rest.ListSorter;
import org.jepria.server.load.rest.SearchParamsDto;
import org.jepria.server.service.security.Credential;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.util.Mutable;

public class ResourceSearchControllerSession implements ResourceSearchController {

  protected final ResourceDescription resourceDescription;

  protected final Supplier<HttpSession> session;

  private final String searchUID;
 
  public ResourceSearchControllerSession(ResourceDescription resourceDescription,
      Supplier<HttpSession> session) {
    this.resourceDescription = resourceDescription;
    this.session = session;
    
    // create single searchUID for a tuple {session,resource} 
    searchUID = Integer.toHexString(Objects.hash(session.get(), resourceDescription.getResourceName()));
  }

  private String getSessionAttrNameCommonPrefix() {
    return "SearchEngine:ResourceName=" + resourceDescription.getResourceName() + ";";
  }

  private String getSessionAttrNameSearchParams(String searchId) {
    return getSessionAttrNameCommonPrefix() + "SearchId=" + searchId + ";Key=SearchParams;";
  }

  private String getSessionAttrNameSearchResultset(String searchId) {
    return getSessionAttrNameCommonPrefix() + "SearchId=" + searchId + ";Key=SearchResultset;";
  }
  
  @Override
  public String postSearchRequest(SearchParamsDto searchParams, Credential credential) {
    session.get().setAttribute(getSessionAttrNameSearchParams(searchUID), searchParams);
    return searchUID;
  }

  /**
   * Специфичная для DAO-поиска поисковая модель, формируемая на основе пользовательских поисковых параметров 
   *
   */
  private class SearchModel {
    public Map<String, Object> preparedTemplate;
    public int maxResultsetSize;
  }

  protected SearchModel createSearchModel(SearchParamsDto searchParams) {

    SearchModel searchModel = new SearchModel();

    Map<String, Object> template = searchParams.getTemplate();
    if (template == null) {
      // search with no params
      template = Collections.emptyMap();
    }

    
    // maxResultsetSize: из клиентских поисковых параметров; если не задано - то из RecordDefinition 
    Integer maxResultsetSize = searchParams.getMaxResultsetSize();
    if (maxResultsetSize == null) {
      maxResultsetSize = resourceDescription.getRecordDefinition().getMaxResultsetSize();
    }
    searchModel.maxResultsetSize = maxResultsetSize;

    
    Map<String, Object> preparedTemplate = new HashMap<>(template);

    Map<String, JepLikeEnum> matchMap = resourceDescription.getRecordDefinition().getFieldMatch();
    Map<String, JepTypeEnum> typeMap = resourceDescription.getRecordDefinition().getFieldTypes();

    if (matchMap == null || matchMap.size() == 0 || typeMap == null || typeMap.size() == 0) {
      // leave unchanged
    } else {

      preparedTemplate.entrySet().forEach(entry -> {

        final String key = entry.getKey();
        final Object value = entry.getValue();

        JepTypeEnum valueType = typeMap.get(key);
        JepLikeEnum matchType = matchMap.get(key);

        if (valueType == JepTypeEnum.STRING && value instanceof String) {
          String valueStr = (String)value;
          if (matchType != null) {
            switch(matchType) {
            case FIRST:
              entry.setValue(valueStr + "%");
              break;
            case CONTAINS:
              entry.setValue("%" + valueStr + "%");
              break;
            case LAST:
              entry.setValue("%" + valueStr);
              break;
            case EXACT:
              // the value is already EXACT
              break;
            }
          }
        }
      });
    }

    searchModel.preparedTemplate = preparedTemplate;

    return searchModel;
  }

  /**
   * Осуществляет DAO-поиск и сохраняет результат в сессию
   * @param searchId
   * @throws NoSuchSearchIdException
   */
  protected void doSearch(String searchId, Credential credential) throws NoSuchSearchIdException {

    SearchParamsDto searchParams = (SearchParamsDto)session.get().getAttribute(getSessionAttrNameSearchParams(searchId));
    if (searchParams == null) {
      // must not be thrown because the attribute has been set just before doSearch invocation
      throw new NoSuchSearchIdException(searchId);
    }
    
    SearchModel searchModel = createSearchModel(searchParams);
    
    Mutable<Boolean> autoRefreshFlag = new Mutable<Boolean>(false);

    List<Map<String, Object>> resultset;
    
    try {
      resultset = Compat.convertList(
          resourceDescription.getDao().find(
              Compat.convertRecord(searchModel.preparedTemplate),
              autoRefreshFlag,
              searchModel.maxResultsetSize,
              credential.getOperatorId()));
    } catch (ApplicationException e) {
      //TODO
      throw new RuntimeException(e);
    }

    // TODO move the autorefreshFlag logic from server-side to client-side
    // Сохраним флаг автообновления.
    // autoRefreshFlag.get();
    
    if (resultset == null) {
      resultset = new ArrayList<>();
    }
    
    
    // sorting
    List<ColumnSortConfigurationDto> fieldSortConfigs = searchParams.getListSortConfiguration();

    if (fieldSortConfigs != null && fieldSortConfigs.size() > 0) {
      Map<String, Comparator<Object>> fieldComparators = resourceDescription.getRecordDefinition().getFieldComparators();
      ListSorter listSorter = new ListSorter(fieldSortConfigs, fieldComparators);

      synchronized (resultset) {
        Collections.sort(resultset, listSorter);
      }
    }
    
    
    session.get().setAttribute(getSessionAttrNameSearchResultset(searchId), resultset);
  }
  
  @Override
  public SearchParamsDto getSearchParams(String searchId, Credential credential) throws NoSuchSearchIdException {
    String sessionAttrName = getSessionAttrNameSearchParams(searchId);
    
    // нужно отличить когда сессия не содержит атрибута или когда она содержит атрибут со значением null
    if (!Collections.list(session.get().getAttributeNames()).contains(sessionAttrName)) {
      throw new NoSuchSearchIdException(searchId);
    }
    
    SearchParamsDto searchParams = (SearchParamsDto)session.get().getAttribute(sessionAttrName);
    
    return searchParams;
  }
  
  @Override
  public int getResultsetSize(String searchId, Credential credential) throws NoSuchSearchIdException {
    return getResultsetLocal(searchId, credential).size();
  }
  
  /**
   * 
   * @param searchId
   * @return non-null
   * @throws NoSuchSearchIdException
   */
  protected List<?> getResultsetLocal(String searchId, Credential credential) throws NoSuchSearchIdException {
    String sessionAttrName = getSessionAttrNameSearchResultset(searchId);
    
    // проверка если сессия не содержит атрибута (а не если она содержит атрибут со значением null)
    if (!Collections.list(session.get().getAttributeNames()).contains(sessionAttrName)) {
      doSearch(searchId, credential);
    }
    
    List<?> resultset = (List<?>)session.get().getAttribute(sessionAttrName);
    
    if (resultset == null) {
      throw new IllegalStateException("the method must not return null");
    }
    
    return resultset;
  }
  
  
  protected void checkResultsetSizeOrThrow(String searchId, Credential credential) throws NoSuchSearchIdException, MaxResultsetSizeExceedException {
    final int actualResultsetSize = getResultsetSize(searchId, credential);
    
    SearchParamsDto searchParams = getSearchParams(searchId, credential);
    if (searchParams.getMaxResultsetSize() != null) {
      // check maxResultsetSize from the search params
      final int maxResultsetSize = searchParams.getMaxResultsetSize();
      if (actualResultsetSize > maxResultsetSize) {
        // TODO the exception handlers do not know who defined the maxResultsetSize limit (in this case: SearchParamsDto)
        throw new MaxResultsetSizeExceedException(actualResultsetSize, maxResultsetSize);
      }
    } else {
      // check maxResultsetSize from the RecordDefinition
      final int maxResultsetSize = resourceDescription.getRecordDefinition().getMaxResultsetSize();
      if (actualResultsetSize > maxResultsetSize) {
        // TODO the exception handlers do not know who defined the maxResultsetSize limit (in this case: RecordDefinition)
        throw new MaxResultsetSizeExceedException(actualResultsetSize, maxResultsetSize);
      }
    }
  }
  
  @Override
  public List<?> getResultset(String searchId, Credential credential) throws NoSuchSearchIdException, MaxResultsetSizeExceedException {
    checkResultsetSizeOrThrow(searchId, credential);
    return getResultsetLocal(searchId, credential);
  }
  
  @Override
  public List<?> getResultsetPaged(String searchId, int pageSize, int page, Credential credential) throws NoSuchSearchIdException {
    List<?> resultset = getResultsetLocal(searchId, credential);
    return paging(resultset, pageSize, page);
  }
  
  private static List<?> paging(List<?> resultset, int pageSize, int page) {
    if (resultset == null) {
      return null;
    }
    
    final int fromIndex = pageSize * (page - 1);
    
    if (fromIndex >= resultset.size()) {
      return null;
    }
    
    final int toIndex = Math.min(resultset.size(), fromIndex + pageSize);
    
    if (fromIndex < toIndex) {
      
      List<?> pageRecords = Collections.unmodifiableList(resultset.subList(fromIndex, toIndex));
      return pageRecords;
      
    } else {
      return null;
    }
  }
}
