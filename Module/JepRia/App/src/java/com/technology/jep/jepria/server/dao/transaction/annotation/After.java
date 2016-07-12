package com.technology.jep.jepria.server.dao.transaction.annotation;

import java.lang.annotation.*;

import com.technology.jep.jepria.server.dao.transaction.handler.EndTransactionHandler;

/**
 * Позволяет указать кастомный обработчик начала транзакции.<br/>
 * К примеру, кастомный обработчик может использоваться, чтобы
 * сообщить в логе о завершении транзакции:
 * <pre>public class CustomDao extends JepDao implements Custom {
 *
 *  private static final Logger log = Logger.getLogger(CustomDao.class);
 *
 *  public static class CustomEndTransactionHandler extends EndTransactionHandlerImpl{
 *    {@literal @}Override
 *    public void handle(Throwable caught) throws ApplicationException {
 *      log.trace("Конец транзакции");
 *      super.handle(caught);
 *    }
 *  };
 *
 *  {@literal @}Override
 *  {@literal @}After(endTransactionHandler = CustomEndTransactionHandler.class)
 *  public void transactionMethod() throws ApplicationException {    
 *    . . .  
 *  }
 *}</pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface After {
  /**
   * Возвращает обработчик начала транзакции.
   * @return экземпляр обработчика
   */
  Class<? extends EndTransactionHandler> endTransactionHandler();
}
