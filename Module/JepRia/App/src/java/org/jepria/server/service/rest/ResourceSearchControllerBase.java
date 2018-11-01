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
import org.jepria.server.security.Credential;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.util.Mutable;

/**
 * Реализация поискового контроллера, состоящего на HTTP сессиях.
 */
// TODO отразить в названии класса тот факт, что это именно сессионная реализация (добавлением слова Session)
public class ResourceSearchControllerBase implements ResourceSearchController {

  // нет необходимости параметризовать, так как механизм поиска не специфицируется на прикладном уровне 
  protected final ResourceDescription<?> resourceDescription;

  protected final Supplier<HttpSession> session;

  private final String searchUID;
 
  public ResourceSearchControllerBase(ResourceDescription<?> resourceDescription,
      Supplier<HttpSession> session) {
    this.resourceDescription = resourceDescription;
    this.session = session;
    
    // create single searchUID for a tuple {session,resource} 
    searchUID = Integer.toHexString(Objects.hash(session.get(), resourceDescription.getResourceName()));
  }

  private String getSessionAttrNameCommonPrefix() {
    return "SearchController:ResourceName=" + resourceDescription.getResourceName() + ";";
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
    
    // инвалидируем предыдущие результаты поиска
    invalidateExistingResultset();
    
    return searchUID;
  }
  
  /**
   * Инвалидация существующих результатов поиска
   */
  private void invalidateExistingResultset() {
    session.get().removeAttribute(getSessionAttrNameSearchResultset(searchUID));
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

    preparedTemplate.entrySet().forEach(entry -> {

      final String key = entry.getKey();
      final Object value = entry.getValue();

      JepTypeEnum valueType = resourceDescription.getRecordDefinition().getFieldType(key);
      JepLikeEnum matchType = resourceDescription.getRecordDefinition().getFieldMatch(key);

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

    // collect field comparators      
    final Map<String, Comparator<Object>> fieldComparators = new HashMap<>();
    if (resourceDescription.getRecordDefinition().getFieldNames() != null) {
      resourceDescription.getRecordDefinition().getFieldNames().forEach(fieldName -> fieldComparators.put(fieldName,
          resourceDescription.getRecordDefinition().getFieldComparator(fieldName)));
    }
    
    new ListSorter(fieldSortConfigs, fieldComparators).sort(resultset);
    
    
    
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
  
  private boolean sessionContainsAttribute(String attributeName) {
    // проверка если сессия именно не содержит атрибута (а не если она содержит оный атрибут со значением null)
    return Collections.list(session.get().getAttributeNames()).contains(attributeName);
  }
  
  /**
   * 
   * @param searchId
   * @return non-null
   * @throws NoSuchSearchIdException
   */
  protected List<?> getResultsetLocal(String searchId, Credential credential) throws NoSuchSearchIdException {
    String sessionAttrName = getSessionAttrNameSearchResultset(searchId);
    
    if (!sessionContainsAttribute(sessionAttrName)) {
      // поиск не осуществлялся или был инвалидирован
      doSearch(searchId, credential);
    }
    
    List<?> resultset = (List<?>)session.get().getAttribute(sessionAttrName);
    
    if (resultset == null) {
      throw new IllegalStateException("the method must not return null");
    }
    
    return resultset;
  }
  
  @Override
  public List<?> getResultset(String searchId, Credential credential) throws NoSuchSearchIdException {
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
