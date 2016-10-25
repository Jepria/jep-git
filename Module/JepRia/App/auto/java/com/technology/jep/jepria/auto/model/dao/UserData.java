package com.technology.jep.jepria.auto.model.dao;
 
import java.util.List;

import com.technology.jep.jepria.auto.model.User;
 
public interface UserData {
  
  /**
   * Получает пользователя для теста.
   * @param login - Логин. 
   * @param roleSNameList - Список ролей.
   * @return Пользователь. {@link com.technology.jep.jepria.auto.model.User}
   * @throws Exception
   */
  public User createUser(String login, List<String> roleSNameList) throws Exception;
}
