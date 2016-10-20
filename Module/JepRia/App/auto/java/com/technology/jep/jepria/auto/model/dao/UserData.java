package com.technology.jep.jepria.auto.model.dao;
 
import com.technology.jep.jepria.auto.model.User;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
 
public interface UserData {
  
  /**
   * Создает пользователя по списку ролей.
   * @param roleShortNameList Список ролей через ","
   * @return Созданный пользователь.
   * @throws ApplicationException
   */
  User createUser(String roleShortNameList) throws Exception;
}
