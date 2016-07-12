package com.technology.jep.jepria.shared.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.dto.JepDto;

/**
 * Общий предок интерфейсов асинхронных сервисов главного модуля.
 */
public interface JepMainServiceAsync {
  /**
   * Получение данных о текущем (вошедшем через SSO) пользователе.
   * 
   * @param callback данныe о текущем (вошедшем через SSO) пользователе
   */
  void getUserData(AsyncCallback<JepDto> callback);
  
  /**
   * Выход из приложения
   * 
   * @param currentUrl - текущий Url (куда возвращаться после Login)
   * @param callback Url, по которому нужно перейти в результате Logout
   */
  void logout(String currentUrl, AsyncCallback<String> callback);
}
