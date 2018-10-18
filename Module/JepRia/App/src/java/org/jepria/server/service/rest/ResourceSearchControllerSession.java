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

import org.jepria.server.load.rest.ColumnSortConfigurationDto;
import org.jepria.server.load.rest.Compat;
import org.jepria.server.load.rest.ListSorter;
import org.jepria.server.load.rest.SearchParamsDto;
import org.jepria.server.service.security.Credential;

import com.technology.jep.jepria.shared.JepRiaConstant;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.util.Mutable;

public class ResourceSearchControllerSession implements ResourceSearchController {

  protected final ResourceDescription resourceDescription;

  protected final Supplier<HttpSession> session;

  protected final boolean lazySearch;

  public static final boolean LAZY_SEARCH_DEFAULT = true;

  private final String searchUID;
 
  public ResourceSearchControllerSession(ResourceDescription resourceDescription,
      Supplier<HttpSession> session, boolean lazySearch) {
    this.resourceDescription = resourceDescription;
    this.session = session;
    this.lazySearch = lazySearch;
    
    // create single searchUID for a tuple {session,resource} 
    searchUID = Integer.toHexString(Objects.hash(session.get(), resourceDescription.getResourceName()));
  }

  public ResourceSearchControllerSession(
      ResourceDescription resourceDescription,
      Supplier<HttpSession> session) {
    this(resourceDescription, session, LAZY_SEARCH_DEFAULT);
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
  
  protected String generateSearchUID(SearchParamsDto searchParams) {
    return searchUID;
  }


  @Override
  public String postSearchRequest(SearchParamsDto searchParams, Credential credential) {

    final String searchId = generateSearchUID(searchParams);

    session.get().setAttribute(getSessionAttrNameSearchParams(searchId), searchParams);

    if (!lazySearch) {
      try {
        doSearch(searchId, credential);
      } catch (NoSuchSearchIdException e) {
        e.printStackTrace();
        // TODO print stack trace? SWALLOW, NO RE-THROW! 
      }
    }
    
    return searchId;
  }

  /**
   * Специфичная для DAO-поиска поисковая модель, формируемая на основе пользовательских поисковых параметров 
   *
   */
  private class SearchModel {
    private Map<String, Object> preparedTemplate;
    private int maxRowCount;
  }

  protected SearchModel createSearchModel(SearchParamsDto searchParams) {

    SearchModel searchModel = new SearchModel();

    Map<String, Object> template = searchParams.getTemplate();
    if (template == null) {
      // search with no params
      template = Collections.emptyMap();
    }

    Integer maxRowCount = searchParams.getMaxRowCount();
    if (maxRowCount == null) {     
      maxRowCount = JepRiaConstant.DEFAULT_MAX_ROW_COUNT;
    }

    searchModel.maxRowCount = maxRowCount;

    Map<String, Object> preparedTemplate = new HashMap<>(template);

    Map<String, JepLikeEnum> matchMap = resourceDescription.getRecordDefinition().getLikeMap();
    Map<String, JepTypeEnum> typeMap = resourceDescription.getRecordDefinition().getTypeMap();

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
   * Осуществляет DAO поиск и сохраняет результат в сессию
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
      resultset = Compat.recListToMapList(
          resourceDescription.getDao().find(
              Compat.mapToRecord(searchModel.preparedTemplate),
              autoRefreshFlag,
              searchModel.maxRowCount,
              credential.getOperatorId()));
    } catch (ApplicationException e) {
      //TODO
      throw new RuntimeException(e);
    }

    // TODO restore :
    // Сохраним флаг автообновления.
    // autoRefreshFlag.get();
    
    if (resultset == null) {
      resultset = new ArrayList<>();
    }
    
    
    // sorting
    List<ColumnSortConfigurationDto> fieldSortConfigs = searchParams.getListSortConfiguration();

    if (fieldSortConfigs != null && fieldSortConfigs.size() > 0) {
      ListSorter listSorter = new ListSorter(fieldSortConfigs, getFieldComparators());

      synchronized (resultset) {
        Collections.sort(resultset, listSorter);
      }
    }
    
    
    session.get().setAttribute(getSessionAttrNameSearchResultset(searchId), resultset);
  }
  
  protected Map<String, Comparator<Object>> getFieldComparators() {
    // TODO build from recordDefinition
    return null;
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
    
    // нужно отличить когда сессия не содержит атрибута или когда она содержит атрибут со значением null
    if (!Collections.list(session.get().getAttributeNames()).contains(sessionAttrName)) {
      doSearch(searchId, credential);
    }
    
    List<?> resultset = (List<?>)session.get().getAttribute(sessionAttrName);
    
    if (resultset == null) {
      throw new IllegalStateException("the method must not return null");
    }
    
    return resultset;
  }
  
  
  protected boolean allowedGetResultset(String searchId, Credential credential) throws NoSuchSearchIdException {
    // TODO this is a sample!
    return getResultsetLocal(searchId, credential).size() <= 5;
  }
  
  @Override
  public List<?> getEntireResultset(String searchId, Credential credential) throws NoSuchSearchIdException, UnsupportedMethodException {
    if (!allowedGetResultset(searchId, credential)) {
      throw new UnsupportedMethodException();
    } else {
      return getResultsetLocal(searchId, credential);
    }
  }
  
  @Override
  public List<?> fetchResultsetPaged(String searchId, int pageSize, int page, Credential credential) throws NoSuchSearchIdException {
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
