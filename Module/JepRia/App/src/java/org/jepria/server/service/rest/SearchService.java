package org.jepria.server.service.rest;

import org.jepria.server.service.security.Credential;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Сервис поиска объектов сущности
 * <br/>
 * <i>В устаревшей терминологии: контроллер поиска, ResourceSearchController</i>
 * <br/>
 * Интерфейс предусматривает только методы создания и чтения (create/POST, read/GET).
 * Методы модификации и удаления (update/PUT, delete/DELETE &mdash; стратегия обновления, переиспользования и очистки созданных объектов) 
 * определяются конкретной реализацией
 */
public interface SearchService {

  /**
   * Интерфейс клиентского поискового запроса для использования внутри сервиса (internal representation)
   */
  public interface SearchRequest {

    /**
     * @return оригинальный поисковый шаблон
     */
    Object getTemplate();

    /**
     * @return поисковый шаблон в виде строкового токена, используемый для сравнения двух шаблонов на equals (сравнение оригинальных объектов может быть ненадёжным)
     */
    String getTemplateToken();

    /**
     * @return <b>упорядоченный</b> map.
     * <br/>
     * ключ: имя столбца списка для сортировки записей по нему,
     * <br/>
     * значение: порядок сортировки записей списка по данному столбцу (неотрицательное: по возрастанию, отрицательное: по убыванию)
     * <br/>
     */
    Map<String, Integer> getListSortConfig();
  }
  
  /**
   * 
   * @param searchRequest
   * @return non-null
   */
  String postSearchRequest(SearchRequest searchRequest, Credential credential);
  /**
   * 
   * @param searchId
   * @return non-null
   * @throws NoSuchElementException if the requested searchId does not exist 
   */
  SearchRequest getSearchRequest(String searchId, Credential credential) throws NoSuchElementException;

  /**
   * 
   * @param searchId
   * @param pageSize
   * @param page
   * @return
   * @throws NoSuchElementException if the requested searchId does not exist
   */
  List<?> getResultsetPaged(String searchId, int pageSize, int page, Credential credential) throws NoSuchElementException;

  /**
   * 
   * @param searchId
   * @param credential
   * @return
   * @throws NoSuchElementException if the requested searchId does not exist
   */
  List<?> getResultset(String searchId, Credential credential) throws NoSuchElementException;

  /**
   * @param searchId
   * @return
   * @throws NoSuchElementException if the requested searchId does not exist
   */
  int getResultsetSize(String searchId, Credential credential) throws NoSuchElementException;

  /**
   * Invalidates resultset (if any) by the searchId
   * @param searchId
   * @throws NoSuchElementException if the requested searchId does not exist
   */
  void invalidateResultset(String searchId) throws NoSuchElementException;
}
