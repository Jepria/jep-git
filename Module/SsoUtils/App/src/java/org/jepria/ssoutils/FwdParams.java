package org.jepria.ssoutils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Класс-аггрегатор форвард-параметров, используемых при авторизации через SsoUi
 */
public class FwdParams {
  private String enterModule;
  private String queryString;
  private String fragment;
  
  public String getEnterModule() {
    return enterModule;
  }
  public void setEnterModule(String enterModule) {
    this.enterModule = enterModule;
  }
  public String getQueryString() {
    return queryString;
  }
  public void setQueryString(String queryString) {
    this.queryString = queryString;
  }
  public String getFragment() {
    return fragment;
  }
  public void setFragment(String fragment) {
    this.fragment = fragment;
  }
  
  /**
   * Тест набора форвард-параметров на пустоту.
   * Пустота набора определяется главным параметром {@code enterModule}
   */
  public boolean isEmpty() {
    return getEnterModule() == null;
  }
  
  /**
   * 
   * @param session
   * @return not null
   */
  public static FwdParams fromSession(HttpSession session) {
    FwdParams fwdParams = new FwdParams();
    fwdParams.setEnterModule((String)session.getAttribute(SsoUiConstants.REQUEST_PARAMETER_ENTER_MODULE));
    fwdParams.setQueryString((String)session.getAttribute(SsoUiConstants.REQUEST_PARAMETER_QUERY_STRING));
    fwdParams.setFragment((String)session.getAttribute(SsoUiConstants.REQUEST_PARAMETER_FRAGMENT));
    return fwdParams;
  }
  
  /**
   * 
   * @param request
   * @return not null
   */
  public static FwdParams fromRequest(HttpServletRequest request) {
    FwdParams fwdParams = new FwdParams();
    fwdParams.setEnterModule(request.getParameter(SsoUiConstants.REQUEST_PARAMETER_ENTER_MODULE));
    fwdParams.setQueryString(request.getParameter(SsoUiConstants.REQUEST_PARAMETER_QUERY_STRING));
    fwdParams.setFragment(request.getParameter(SsoUiConstants.REQUEST_PARAMETER_FRAGMENT));
    return fwdParams;
  }
}
