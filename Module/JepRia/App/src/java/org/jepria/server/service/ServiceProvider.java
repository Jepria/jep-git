package org.jepria.server.service;

import org.jepria.server.service.rest.EntityService;
import org.jepria.server.service.rest.SearchService;

import javax.servlet.http.HttpSession;
import java.util.function.Supplier;

public interface ServiceProvider {
  /**
   * @return сервис, воплощающий логику CRUD-операций (create, get-by-id, update, delete)
   */
  EntityService getEntityService();

  /**
   * @return сервис, воплощающий логику поиска объектов сущности
   */
  SearchService getSearchService(Supplier<HttpSession> session);
}
