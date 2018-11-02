package org.jepria.server.service.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

import javax.servlet.http.HttpSession;

import org.jepria.compat.CoreCompat;
import org.jepria.server.dao.JepDataStandard;
import org.jepria.server.load.ColumnSortConfiguration;
import org.jepria.server.load.ListSorter;
import org.jepria.server.load.rest.ColumnSortConfigurationDto;
import org.jepria.server.load.rest.SearchParamsDto;
import org.jepria.server.security.Credential;

import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;

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
    public int maxRowCount;
  }

  protected SearchModel createSearchModel(SearchParamsDto searchParams) {

    SearchModel searchModel = new SearchModel();

    Map<String, Object> template = searchParams.getTemplate();
    if (template == null) {
      // search with no params
      template = Collections.emptyMap();
    }

    
    // maxRowCount: если не задано в клиентских параметрах, то дефолтное значение 
    Integer maxRowCount = (Integer)template.get(CoreCompat.MAX_ROW_COUNT__FIELD_NAME);
    searchModel.maxRowCount = maxRowCount != null ? maxRowCount : CoreCompat.DEFAULT_MAX_ROW_COUNT;

    
    Map<String, Object> preparedTemplate = new HashMap<>(template);

    // применяем MatchType к каждому полю
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
  protected void doSearch(String searchId, Credential credential) throws NoSuchElementException {

    SearchParamsDto searchParams = (SearchParamsDto)session.get().getAttribute(getSessionAttrNameSearchParams(searchId));
    if (searchParams == null) {
      // must not be thrown because the attribute has been set just before doSearch invocation
      throw new NoSuchElementException(searchId);
    }
    
    SearchModel searchModel = createSearchModel(searchParams);
    
    List<Map<String, ?>> resultset;
    
    try {
      // TODO remove backward compatibility: resourceDescription.getDao() must return org.jepria.server.dao.JepDataStandard
      JepDataStandard dao = CoreCompat.convertDao(resourceDescription.getDao());
      resultset = dao.find(
              searchModel.preparedTemplate,
              searchModel.maxRowCount,
              credential.getOperatorId());
    } catch (Throwable e) {
      //TODO
      throw new RuntimeException(e);
    }

    if (resultset == null) {
      resultset = new ArrayList<>();
    }
    
    
    
    // sorting
    
    
    // collect columnSortConfigurations
    final List<ColumnSortConfiguration> columnSortConfigurations = convertColumnSortConfigurations(searchParams.getListSortConfiguration());
    
    // collect field comparators      
    final Map<String, Comparator<Object>> fieldComparators = new HashMap<>();
    if (resourceDescription.getRecordDefinition().getFieldNames() != null) {
      resourceDescription.getRecordDefinition().getFieldNames().forEach(fieldName -> fieldComparators.put(fieldName,
          resourceDescription.getRecordDefinition().getFieldComparator(fieldName)));
    }
    
    Collections.sort(resultset, new ListSorter(columnSortConfigurations, fieldComparators));
    
    session.get().setAttribute(getSessionAttrNameSearchResultset(searchId), resultset);
  }
  
  /**
   * @param list
   * @return or null if the input is null
   */
  protected static List<ColumnSortConfiguration> convertColumnSortConfigurations(List<ColumnSortConfigurationDto> list) {
    if (list == null) {
      return null;
    }
    
    List<ColumnSortConfiguration> ret = new ArrayList<>();
    for (ColumnSortConfigurationDto item: list) {
      ColumnSortConfiguration columnSortConfiguration = new ColumnSortConfiguration(
          item.getColumnName(),
          "desc".equals(item.getSortOrder()) ? -1 : 1);
      ret.add(columnSortConfiguration);
    }
    return ret;
  }
  
  @Override
  public SearchParamsDto getSearchParams(String searchId, Credential credential) throws NoSuchElementException {
    String sessionAttrName = getSessionAttrNameSearchParams(searchId);
    
    // нужно отличить когда сессия не содержит атрибута или когда она содержит атрибут со значением null
    if (!Collections.list(session.get().getAttributeNames()).contains(sessionAttrName)) {
      throw new NoSuchElementException(searchId);
    }
    
    SearchParamsDto searchParams = (SearchParamsDto)session.get().getAttribute(sessionAttrName);
    
    return searchParams;
  }
  
  @Override
  public int getResultsetSize(String searchId, Credential credential) throws NoSuchElementException {
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
  protected List<?> getResultsetLocal(String searchId, Credential credential) throws NoSuchElementException {
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
  public List<?> getResultset(String searchId, Credential credential) throws NoSuchElementException {
    return getResultsetLocal(searchId, credential);
  }
  
  @Override
  public List<?> getResultsetPaged(String searchId, int pageSize, int page, Credential credential) throws NoSuchElementException {
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
