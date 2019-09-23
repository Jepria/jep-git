package org.jepria.server.service.rest;

import org.jepria.server.data.Dao;
import org.jepria.server.data.RecordComparator;
import org.jepria.server.data.RecordDefinition;
import org.jepria.server.service.security.Credential;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Supplier;

/**
 * Реализация поискового контроллера, состоящего на HTTP сессиях.
 */
// TODO отразить в названии класса тот факт, что это именно сессионная реализация (добавлением слова Session)
public class ResourceSearchControllerImpl implements ResourceSearchController {

  protected final ResourceDescription description;

  protected final Supplier<HttpSession> session;

  public ResourceSearchControllerImpl(ResourceDescription description, Supplier<HttpSession> session) {

    this.description = description;

    this.session = session;
    
    // create single searchUID for a tuple {session,resource}
    searchUID = Integer.toHexString(Objects.hash(session.get().getId(), description.getEntityName()));
    
    sessionAttrKeyPrefix = "SearchController:EntityName=" + description.getEntityName() + ";SearchId=" + searchUID;
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
  
  private final String sessionAttrKeyPrefix;
  
  /**
   * Контейнер сохранённого в сессию клиентского поискового запроса.
   */
  //  паттерн "Свойство" использован для инкапсуляции чтения и записи атрибута сессии
  private final Property<SearchRequest> sessionSearchRequest = new Property<SearchRequest>() {
    @Override
    public SearchRequest get() {
      String key = sessionAttrKeyPrefix + ";Key=SearchRequest;";
      return (SearchRequest)session.get().getAttribute(key);
    }
    @Override
    public void set(SearchRequest searchRequest) {
      String key = sessionAttrKeyPrefix + ";Key=SearchRequest;";
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
  private final Property<List<?>> sessionResultset = new Property<List<?>>() {
    @Override
    public List<?> get() {
      String key = sessionAttrKeyPrefix + ";Key=SearchResultset;";
      return (List<?>)session.get().getAttribute(key);
    }
    @Override
    public void set(List<?> resultset) {
      String key = sessionAttrKeyPrefix + ";Key=SearchResultset;";
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
  private final Property<Boolean> sessionResultsetSortValid = new Property<Boolean>() {
    @Override
    public Boolean get() {
      String key = sessionAttrKeyPrefix + ";Key=SearchResultsetSortValid;";
      return Boolean.TRUE.equals(session.get().getAttribute(key));
    }
    @Override
    public void set(Boolean resultsetSortValid) {
      String key = sessionAttrKeyPrefix + ";Key=SearchResultsetSortValid;";
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
      if (Objects.equals(existingRequest.getTemplateToken(), searchRequest.getTemplateToken())) {
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
   * Осуществляет DAO-поиск и сохраняет результирующий список в сессию
   */
  protected void doSearch(Credential credential) {

    final SearchRequest searchRequest = sessionSearchRequest.get();
    if (searchRequest == null) {
      throw new IllegalStateException("The session attribute must have already been set at this point");
    }
    
    List<?> resultset;
    
    try {
      resultset = description.getDao().find(
              searchRequest.getTemplate(),
              credential.getOperatorId());
    } catch (Throwable e) {
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
   */
  protected void doSort() {
    
    final SearchRequest searchRequest = sessionSearchRequest.get();
    if (searchRequest == null) {
      throw new IllegalStateException("The session attribute must have already been set at this point");
    }
    
    
    LinkedHashMap<String, Integer> listSortConfig = searchRequest.getListSortConfig();
    if (listSortConfig != null) {
      
      final Comparator<Object> sortComparator = createRecordComparator(listSortConfig);
      
      final List<?> resultset = sessionResultset.get();
      
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
   * Создаёт Comparator записей для сортировки списка
   * @return
   */
  protected Comparator<Object> createRecordComparator(LinkedHashMap<String, Integer> listSortConfig) {
    
    return new RecordComparator(new ArrayList<>(listSortConfig.keySet()),
        fieldName -> description.getRecordDefinition().getFieldComparator(fieldName),
        fieldName -> {
          if (listSortConfig != null) {
            Integer sortOrder = listSortConfig.get(fieldName);
            if (sortOrder != null) {
              return sortOrder;
            }
          }
          return 1;
        });
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
   * @return non-null
   */
  protected List<?> getResultsetLocal(Credential credential) {
    
    // поиск (если необходимо)
    List<?> resultset = sessionResultset.get();
    
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
  public List<?> getResultset(String searchId, Credential credential) throws NoSuchElementException {
    checkSearchIdOrElseThrow(searchId);
    
    return getResultsetLocal(credential);
  }
  
  @Override
  public List<?> getResultsetPaged(String searchId, int pageSize, int page, Credential credential) throws NoSuchElementException {
    checkSearchIdOrElseThrow(searchId);
    
    List<?> resultset = getResultsetLocal(credential);
    
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
