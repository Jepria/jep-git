package org.jepria.server.service.rest;

import org.jepria.server.data.Dao;
import org.jepria.server.data.RecordComparator;
import org.jepria.server.data.RecordDefinition;
import org.jepria.server.service.security.Credential;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Supplier;

/**
 * Реализация поискового сервиса, состоящего на HTTP сессиях.
 */
// TODO отразить в названии класса тот факт, что это именно сессионная реализация (добавлением слова Session)

// TODO поддержать пул (несколько) поисков, с которыми один и тот же клиент может работать одновременно (см.далее)
// Текущая реализация позволяет одному клиенту обрабатывать только один поисковый запрос на одну сущность одновременно.
// Тот же самый клиент, запостивший второй поисковый запрос на ту же самую сущность, лишается возможности работать с первым поиском (так как поле searchUID уникально для сессии).
// Нужно поддержать некий пул поисков (допустим, 5 разных штук). В пуле могут содержаться только уникальные поиски
// (уникальность поискового запроса должна определяться составом запроса [то есть полями-фильтрами и их значениями],
//   энтитью [два одинаковых по составу поисковых запроса, направленные на разные сущности, являются разными запросами],
//   и /возможно/ временем создания поискового запроса для контроля актуальности результатов)
// Пул нужно ограничить несколькими штуками для того, чтобы не допускать переполнения памяти сессии (так как каждая сущность запроса хранится в сессии),
// в то же время пул должен мочь содержать как мимнимум несколько запросов, ибо одиночность есть бесчеловечность.
// При этом пул сождержит несколько уникальных запросов на поиск *именно по составу*, при этом searchId может вообще каждый раз генериться новое.
public class SearchServiceImpl implements SearchService {

  protected final Dao dao;

  protected final RecordDefinition recordDefinition;

  protected final Supplier<HttpSession> session;

  /**
   *
   * @param dao
   * @param recordDefinition
   * @param session используется Supplier потому что сессия – вещь зыбкая, неперсистентная (имеет право изменяться от вызова к вызову или с течением времени)
   */
  public SearchServiceImpl(Dao dao, RecordDefinition recordDefinition, Supplier<HttpSession> session) {

    this.dao = dao;

    this.recordDefinition = recordDefinition;

    this.session = session;

    // TODO improper value (will be proxy). How to obtain properly?
    final String entityName = dao.getClass().getSimpleName();

    // create single searchUID for a tuple {session,entity}
    searchUID = Integer.toHexString(Objects.hash(session.get().getId(), entityName)); // TODO is this UID unique enough?
    
    sessionAttrKeyPrefix = "SearchService;entity=" + entityName + ";searchId=" + searchUID;
  }

  private final String searchUID;
 
  /**
   * В сессионной реализации поискового сервиса, обращение клиента возможно только с searchId равным значению поля searchUID
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

    resultset = dao.find(
            searchRequest.getTemplate(),
            credential == null ? null : credential.getOperatorId());

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

      if (resultset.size() > 1) { // sort lengthy lists only

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
        fieldName -> recordDefinition.getFieldComparator(fieldName),
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

    List<?> resultset = getResultsetLocal(credential, false); // no need to sort for getting size only

    return resultset == null ? 0 : resultset.size();
  }

  /**
   *
   * @param credential
   * @param sort whether to return a resultset in "raw" order (as-is, without applying sort configuration from the search request), or with requested sort configuration applied
   * @return non-null, at least empty
   */
  protected List<?> getResultsetLocal(Credential credential, boolean sort) {
    
    // поиск (если необходимо)
    List<?> resultset = sessionResultset.get();
    
    if (resultset == null) {// поиск не осуществлялся или был инвалидирован
      doSearch(credential);
    }
    

    if (sort) {// сортировка (если необходимо)

      boolean resultsetSortValid = sessionResultsetSortValid.get();

      if (!resultsetSortValid) {// сортировка не осуществлялась или была инвалидирована
        doSort();
      }
    }


    // check session/resultset state
    resultset = sessionResultset.get();
    if (resultset == null) {
      throw new IllegalStateException("The session attribute must have already been set at this point");
    }

    return resultset;
  }
  
  @Override
  public List<?> getResultset(String searchId, Credential credential) throws NoSuchElementException {
    checkSearchIdOrElseThrow(searchId);
    
    return getResultsetLocal(credential, true);
  }
  
  @Override
  public List<?> getResultsetPaged(String searchId, int pageSize, int page, Credential credential) throws NoSuchElementException {
    checkSearchIdOrElseThrow(searchId);
    
    List<?> resultset = getResultsetLocal(credential, true);
    
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
