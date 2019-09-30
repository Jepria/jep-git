package org.jepria.server.service.rest;

import org.jepria.server.data.RecordComparator;
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
    searchUID = Integer.toHexString(Objects.hash(session.get().getId(), description.getEntityName())); // TODO is this UID unique enough?
    
    sessionAttrKeyPrefix = "ResourceSearchController;entity=" + description.getEntityName() + ";searchId=" + searchUID;
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
      String key = sessionAttrKeyPrefix + ";key=sreq;";
      return (SearchRequest)session.get().getAttribute(key);
    }
    @Override
    public void set(SearchRequest searchRequest) {
      String key = sessionAttrKeyPrefix + ";key=sreq;";
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
      String key = sessionAttrKeyPrefix + ";key=rset;";
      return (List<?>)session.get().getAttribute(key);
    }
    @Override
    public void set(List<?> resultset) {
      String key = sessionAttrKeyPrefix + ";key=rset;";
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
      String key = sessionAttrKeyPrefix + ";key=sort;";
      return Boolean.TRUE.equals(session.get().getAttribute(key));
    }
    @Override
    public void set(Boolean resultsetSortValid) {
      String key = sessionAttrKeyPrefix + ";key=sort;";
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
    

    if (existingRequest != null && searchRequest != null && Objects.equals(existingRequest.getTemplateToken(), searchRequest.getTemplateToken())) {
      // resultset remains valid

      // TODO do not test equality but test sublistness instead
      if (!Objects.equals(existingRequest.getListSortConfig(), searchRequest.getListSortConfig())) {
        invalidateSort();
      }
    } else {
      invalidateResultsetAndSort();
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
    
    
    Map<String, Integer> listSortConfig = searchRequest.getListSortConfig();
    if (listSortConfig != null) {

      final List<?> resultset = sessionResultset.get();

      if (resultset == null) {
        throw new IllegalStateException("The session attribute must have already been set at this point");
      }

      if (resultset.size() > 1) {

        final Comparator<Object> sortComparator = createRecordComparator(listSortConfig);

        Collections.sort(resultset, sortComparator);
        // sorting affects the session attribute as well
      }
    }
    
    // сессионный атрибут проставляется именно в doSort
    sessionResultsetSortValid.set(true);
  }
  
  /**
   * Создаёт Comparator записей для сортировки списка
   * @param listSortConfig <b>ordered</b> map, see {@link }
   * @return
   */
  protected Comparator<Object> createRecordComparator(Map<String, Integer> listSortConfig) {
    
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

  @Override
  public void invalidateResultset(String searchId) throws NoSuchElementException {
    checkSearchIdOrElseThrow(searchId);

    invalidateResultsetAndSort();
  }

  /**
   * Invalidates both resultset and sort
   */
  protected void invalidateResultsetAndSort() {
    sessionResultset.set(null);
    sessionResultsetSortValid.set(false);
  }

  /**
   * Invalidates sort validity only
   */
  protected void invalidateSort() {
    sessionResultsetSortValid.set(false);
  }
}
