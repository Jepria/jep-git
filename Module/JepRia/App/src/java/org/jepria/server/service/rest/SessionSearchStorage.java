package org.jepria.server.service.rest;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.jepria.server.load.rest.SearchEntity;

public class SessionSearchStorage implements SearchStorage {

  protected final String resourceName;
  
  protected final Supplier<HttpSession> session;
  
  public SessionSearchStorage(String resourceName, Supplier<HttpSession> session) {
    this.resourceName = resourceName;
    this.session = session;
  }
  
  private String getSessionKeySearchCommonPrefix() {
    return "Endpoint:Resource=" + resourceName + ";Key=SEARCH;";
  }
  
  private String getSessionKeySearchPrefix(String searchId) {
    return getSessionKeySearchCommonPrefix() + "SearchId=" + searchId + ";";
  }
  
  @Override
  public SearchEntity get(String searchId) {
    return (SearchEntity)session.get().getAttribute(getSessionKeySearchPrefix(searchId) );
  }
  
  protected String generateSearchID(SearchEntity searchEntity) {
    return UUID.randomUUID().toString();
  }
  
  @Override
  public String put(SearchEntity searchEntity) {
    String searchId = generateSearchID(searchEntity);
    update(searchId, searchEntity);
    return searchId;
  }
  
  @Override
  public SearchEntity update(String searchId, SearchEntity newSearchEntity) {
    final SearchEntity previous = get(searchId);
    session.get().setAttribute(getSessionKeySearchPrefix(searchId), newSearchEntity);
    return previous;
  }
  
  @Override
  public SearchEntity remove(String searchId) {
    final SearchEntity previous = get(searchId);
    session.get().removeAttribute(getSessionKeySearchPrefix(searchId));
    return previous;
  }
  
  @Override
  public Set<String> keySet() {
    Enumeration<String> sessionAttrNames = session.get().getAttributeNames();
    if (sessionAttrNames == null) {
      return new HashSet<>();
    } else {
      return Collections.list(sessionAttrNames).stream().filter(
          sessionAttrName -> sessionAttrName != null && sessionAttrName.startsWith(getSessionKeySearchCommonPrefix()))
          .collect(Collectors.toSet());
    }
  }
}
