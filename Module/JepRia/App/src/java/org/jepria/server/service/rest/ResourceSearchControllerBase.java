package org.jepria.server.service.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

import javax.servlet.http.HttpSession;

import org.jepria.compat.CoreCompat;
import org.jepria.server.data.Dao;
import org.jepria.server.data.FieldType;
import org.jepria.server.data.MatchType;
import org.jepria.server.data.RecordComparator;
import org.jepria.server.data.RecordDefinition;
import org.jepria.server.security.Credential;

/**
 * Реализация поискового контроллера, состоящего на HTTP сессиях.
 */
// TODO отразить в названии класса тот факт, что это именно сессионная реализация (добавлением слова Session)
public class ResourceSearchControllerBase implements ResourceSearchController {

  // нет необходимости параметризовать, так как механизм поиска не специфицируется на прикладном уровне
  protected final Dao dao;
  protected final RecordDefinition recordDefinition;

  protected final Supplier<HttpSession> session;
  
  public ResourceSearchControllerBase(Dao dao, RecordDefinition recordDefinition, Supplier<HttpSession> session) {
    this.dao = dao;
    this.recordDefinition = recordDefinition;
    
    this.session = session;
    
    // create single searchUID for a tuple {session,resource} 
    searchUID = Integer.toHexString(Objects.hash(session.get(), dao.getClass().getName()));
  }

  private final String searchUID;
 
  /**
   * В сессионной реализации контроллера поиска, обращение клиента возможно только с searchId равным значению поля searchUID
   * @param searchId
   * @throws NoSuchElementException в случае несовпадающего searchId
   */
  private void checkSearchIdOrElseThrow(String searchId) throws NoSuchElementException {
    if (!searchUID.equals(searchId)) {
      throw new NoSuchElementException(searchId);
    }
  }

  
  
  
  
  /**
   * Property-интерфейс для управления атрибутами сессии 
   * @param <T>
   */
  protected static interface Property<T> {
    T get();
    void set(T object);
  }
  
  /**
   * Контейнер сохранённого в сессию клиентского поискового запроса.
   */
  //  паттерн "Свойство" использован для инкапсуляции чтения и записи атрибута сессии
  protected final Property<SearchRequest> sessionSearchRequest = new Property<SearchRequest>() {
    @Override
    public SearchRequest get() {
      final String key = "SearchController:DaoName=" + dao.getClass().getSimpleName() + ";SearchId=" + searchUID + ";Key=SearchRequest;";
      return (SearchRequest)session.get().getAttribute(key);
    }
    @Override
    public void set(SearchRequest searchRequest) {
      final String key = "SearchController:DaoName=" + dao.getClass().getSimpleName() + ";SearchId=" + searchUID + ";Key=SearchRequest;";
      if (searchRequest == null) {
        session.get().removeAttribute(key);
      } else {
        session.get().setAttribute(key, searchRequest);
      }
    }
  };
  
  /**
   * Контейнер сохранённого в сессию результирующего списка в соответствии с последним клиентским запросом 
   */
  //  паттерн "Свойство" использован для инкапсуляции чтения и записи атрибута сессии
  protected final Property<List<Map<String, ?>>> sessionResultset = new Property<List<Map<String, ?>>>() {
    @Override
    public List<Map<String, ?>> get() {
      final String key = "SearchController:DaoName=" + dao.getClass().getSimpleName() + ";SearchId=" + searchUID + ";Key=SearchResultset;";
      return (List<Map<String, ?>>)session.get().getAttribute(key);
    }

    @Override
    public void set(List<Map<String, ?>> resultset) {
      final String key = "SearchController:DaoName=" + dao.getClass().getSimpleName() + ";SearchId=" + searchUID + ";Key=SearchResultset;";
      if (resultset == null) {
        session.get().removeAttribute(key);
      } else {
        session.get().setAttribute(key, resultset);
      }
    }
  };

  /**
   * Контейнер сохранённого в сессию признака, является ли сохранённый в сессии результирующий список 
   * отсортированным в соответствии с последним клиентским запросом
   */
  //  паттерн "Свойство" использован для инкапсуляции чтения и записи атрибута сессии
  protected final Property<Boolean> sessionResultsetSortValid = new Property<Boolean>() {
    @Override
    public Boolean get() {
      final String key = "SearchController:DaoName=" + dao.getClass().getSimpleName() + ";SearchId=" + searchUID + ";Key=SearchResultsetSortValid;";
      return Boolean.TRUE.equals(session.get().getAttribute(key));
    }
    @Override
    public void set(Boolean resultsetSortValid) {
      final String key = "SearchController:DaoName=" + dao.getClass().getSimpleName() + ";SearchId=" + searchUID + ";Key=SearchResultsetSortValid;";
      if (Boolean.FALSE.equals(resultsetSortValid)) {
        session.get().removeAttribute(key);
      } else {
        session.get().setAttribute(key, true);
      }
    }
    
  };
  
  
  
  
  
  @Override
  public String postSearchRequest(SearchRequest searchRequest, Credential credential) {

    // В зависимости от существующих и новых поисковых параметров инвалидируем результирующий список и/или его сортировку
    final SearchRequest existingRequest = sessionSearchRequest.get();
    
    boolean invalidateResultset = true;
    boolean invalidateResultsetSort = true;
    
    if (existingRequest != null && searchRequest != null) {
      if (Objects.equals(existingRequest.getTemplate(), searchRequest.getTemplate())) {
        if (!Objects.equals(existingRequest.getListSortConfig(), searchRequest.getListSortConfig())) {
          // не инвалидируем результирующий список, если изменились только параметры сортировки
          invalidateResultset = false;
        }
      }
    }
    
    if (invalidateResultset) {
      sessionResultset.set(null);
    }
    if (invalidateResultsetSort) {
      sessionResultsetSortValid.set(false);
    }
    
    
    // сохраняем новые поисковые параметры
    sessionSearchRequest.set(searchRequest);
    
    return searchUID;
  }
  
  /**
   * Специфичная для DAO-поиска поисковая модель, формируемая на основе пользовательских поисковых параметров 
   *
   */
  private class SearchModel {
    public Map<String, Object> preparedTemplate;
    public int maxRowCount;
  }

  /**
   * Преобразует клиентский поисковый запрос в специфичную для DAO-поиска поисковую модель
   * @param searchRequest
   * @return
   */
  protected SearchModel createSearchModel(SearchRequest searchRequest) {

    SearchModel searchModel = new SearchModel();

    Map<String, Object> template = searchRequest.getTemplate();
    if (template == null) {
      // search with no params
      template = Collections.emptyMap();
    }

    
    // maxRowCount: если не задано в клиентском поисковом запросе, то дефолтное значение 
    Integer maxRowCount = (Integer)template.get(CoreCompat.MAX_ROW_COUNT__FIELD_NAME);
    searchModel.maxRowCount = maxRowCount != null ? maxRowCount : CoreCompat.DEFAULT_MAX_ROW_COUNT;

    
    Map<String, Object> preparedTemplate = new HashMap<>(template);

    // применяем MatchType к каждому полю
    for (Map.Entry<String, Object> entry: preparedTemplate.entrySet()) {
      final String key = entry.getKey();
      final Object value = entry.getValue();

      FieldType valueType = recordDefinition.getFieldType(key);
      MatchType matchType = recordDefinition.getMatchType(key);

      if (valueType == FieldType.STRING && value instanceof String) {
        String valueStr = (String)value;
        if (matchType != null) {
          switch(matchType) {
          case STARTS:
            entry.setValue(valueStr + "%");
            break;
          case CONTAINS:
            entry.setValue("%" + valueStr + "%");
            break;
          case ENDS:
            entry.setValue("%" + valueStr);
            break;
          case EXACT:
            // NO-OP
            break;
          }
        }
      }
    }

    searchModel.preparedTemplate = preparedTemplate;

    return searchModel;
  }

  /**
   * Осуществляет DAO-поиск и сохраняет результирующий список в сессию
   * @param searchId
   */
  protected void doSearch(Credential credential) {

    final SearchRequest searchRequest = sessionSearchRequest.get();
    if (searchRequest == null) {
      throw new IllegalStateException("The session attribute must have already been set at this point");
    }
    
    SearchModel searchModel = createSearchModel(searchRequest);
    
    List<Map<String, ?>> resultset;
    
    try {
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
    
    // сессионный атрибут проставляется именно в doSearch
    sessionResultset.set(resultset);
  }
   
  /**
   * Осуществляет сортировку результирующего списка, сохранённого в сессию, и выставляет сессионный признак валидности его сортировки
   * @param searchId
   */
  protected void doSort() {
    
    final SearchRequest searchRequest = sessionSearchRequest.get();
    if (searchRequest == null) {
      throw new IllegalStateException("The session attribute must have already been set at this point");
    }
    
    
    LinkedHashMap<String, Integer> listSortConfig = searchRequest.getListSortConfig();
    if (listSortConfig != null) {
      
      final Comparator<Map<String, ?>> sortComparator = createSortComparator(listSortConfig);
      
      final List<Map<String, ?>> resultset = sessionResultset.get();
      
      if (resultset == null) {
        throw new IllegalStateException("The session attribute must have already been set at this point");
      }

      Collections.sort(resultset, sortComparator);
      // sorting affects the session attribute as well 
    }
    
    // сессионный атрибут проставляется именно в doSort
    sessionResultsetSortValid.set(true);
  }
  
  /**
   * 
   * @param listSortConfig non null
   * @return
   */
  protected Comparator<Map<String, ?>> createSortComparator(LinkedHashMap<String, Integer> listSortConfig) {
    
    if (listSortConfig == null) {
      throw new IllegalArgumentException("input must not be null");
    }
    
    
    final LinkedHashMap<String, Comparator<Object>> fieldComparators = new LinkedHashMap<>();
    
    for (Map.Entry<String, Integer> columnSortConfig: listSortConfig.entrySet()) {
    
      final String fieldName = columnSortConfig.getKey();
      
      final Integer sortOrder0 = columnSortConfig.getValue();
      final int sortOrder = sortOrder0 != null ? sortOrder0 : 1;
          
      final Comparator<Object> fieldComparator = getFieldComparatorOrDefault(fieldName);
      
      // apply sort order
      final Comparator<Object> fielComparatorWithSortOrder = new Comparator<Object>() {
        @Override
        public int compare(Object o1, Object o2) {
          return fieldComparator.compare(o1, o2) * sortOrder;
        }
      };
      
      fieldComparators.putIfAbsent(fieldName, fielComparatorWithSortOrder);
    }
    
    
    
    // При сортировке записи будут считаться "условно" равными, если будут равны значения только тех полей, которые участвуют в сравнении.
    // Однако в этом случае результат сортировки двух списков, состоящих из одних и тех же элементов (но различающиеся порядком элементов)
    // по одним и тем же полям может различаться в зависимости от изначального порядка элементов в этих списках.
    // Чтобы этого избежать, при "условном" равенстве записей (в последнюю очередь) нужно проводить уточняющую сортировку по заведомо уникальному полю (primary key)
    for (String primaryKey: recordDefinition.getPrimaryKey()) {
      fieldComparators.putIfAbsent(primaryKey, getFieldComparatorOrDefault(primaryKey));
    }
    
    
    
    return new RecordComparator(fieldComparators);
  }
  
  // TODO вспомогательный метод. Но стоит ли он своего наличия?
  protected Comparator<Object> getFieldComparatorOrDefault(String fieldName) {
    final Comparator<Object> fieldComparator0 = recordDefinition.getFieldComparator(fieldName);
    return fieldComparator0 != null ? fieldComparator0 : CoreCompat.getDefaultComparator();
  }
  
  @Override
  public SearchRequest getSearchRequest(String searchId, Credential credential) throws NoSuchElementException {
    checkSearchIdOrElseThrow(searchId);
    
    SearchRequest searchRequest = sessionSearchRequest.get();
    if (searchRequest == null) {
      throw new IllegalStateException("The session attribute must have already been set at this point");
    }
    
    return searchRequest;
  }
  
  @Override
  public int getResultsetSize(String searchId, Credential credential) throws NoSuchElementException {
    checkSearchIdOrElseThrow(searchId);
    
    return getResultsetLocal(credential).size();
  }
  
  /**
   * 
   * @param searchId
   * @return non-null
   * @throws NoSuchSearchIdException
   */
  protected List<Map<String, ?>> getResultsetLocal(Credential credential) throws NoSuchElementException {
    
    // поиск (если необходимо)
    List<Map<String, ?>> resultset = sessionResultset.get();
    
    if (resultset == null) {
      // поиск не осуществлялся или был инвалидирован
      doSearch(credential);
      
      resultset = sessionResultset.get();
      
      if (resultset == null) {
        throw new IllegalStateException("The session attribute must have already been set at this point");
      }
    }
    
    
    // сортировка (если необходимо)
    boolean resultsetSortValid = sessionResultsetSortValid.get();
    
    if (!resultsetSortValid) {
      // сортировка не осуществлялась или была инвалидирована
      doSort();
      
      resultset = sessionResultset.get();
      
      if (resultset == null) {
        throw new IllegalStateException("The session attribute must have already been set at this point");
      }
    }
    
    
    return resultset;
  }
  
  @Override
  public List<Map<String, ?>> getResultset(String searchId, Credential credential) throws NoSuchElementException {
    checkSearchIdOrElseThrow(searchId);
    
    return getResultsetLocal(credential);
  }
  
  @Override
  public List<Map<String, ?>> getResultsetPaged(String searchId, int pageSize, int page, Credential credential) throws NoSuchElementException {
    checkSearchIdOrElseThrow(searchId);
    
    List<Map<String, ?>> resultset = getResultsetLocal(credential);
    
    return paging(resultset, pageSize, page);
  }
  
  private static List<Map<String, ?>> paging(List<Map<String, ?>> resultset, int pageSize, int page) {
    if (resultset == null) {
      return null;
    }
    
    final int fromIndex = pageSize * (page - 1);
    
    if (fromIndex >= resultset.size()) {
      return null;
    }
    
    final int toIndex = Math.min(resultset.size(), fromIndex + pageSize);
    
    if (fromIndex < toIndex) {
      
      List<Map<String, ?>> pageRecords = Collections.unmodifiableList(resultset.subList(fromIndex, toIndex));
      return pageRecords;
      
    } else {
      return null;
    }
  }
}
