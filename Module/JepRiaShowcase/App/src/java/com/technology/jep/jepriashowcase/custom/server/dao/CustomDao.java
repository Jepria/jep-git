package com.technology.jep.jepriashowcase.custom.server.dao;

import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.server.dao.JepDao;
import com.technology.jep.jepria.server.dao.transaction.annotation.After;
import com.technology.jep.jepria.server.dao.transaction.annotation.Before;
import com.technology.jep.jepria.server.dao.transaction.handler.*;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.Mutable;

public class CustomDao extends JepDao implements Custom {
  
  private static final Logger log = Logger.getLogger(CustomDao.class);
  
  /**
   * Пример кастомного обработчика начала транзакции.<br/>
   * Дополнительно к инициализации соединения с помощью {@link CallContext#begin(String)}
   * сообщает в логе о старте транзакции.<br/>
   * ВАЖНО: если, как в данном примере, обработчик реализуется в виде вложенного
   * в DAO класса, необходимо объявлять его как <code>public static</code>,
   * в противном случае будет выброшено <code>InstantiationException</code>.
   */
  public static class CustomStartTransactionHandler implements StartTransactionHandler{
    
    @Override
    public void handle(String dataSourceJndiName, String moduleName)
        throws ApplicationException {
      CallContext.begin(dataSourceJndiName, moduleName);
      log.trace("Начало транзакции");
    }
  };
  
  /**
   * Пример кастомного обработчика завершения транзакции.<br/>
   * Дополнительно к инициализации соединения с помощью {@link CallContext#begin(String)}
   * сообщает в логе о завершении транзакции.<br/>
   * ВАЖНО: если, как в данном примере, обработчик реализуется в виде вложенного
   * в DAO класса, необходимо объявлять его как <code>public static</code>,
   * в противном случае будет выброшено <code>InstantiationException</code>.
   */
  public static class CustomEndTransactionHandler extends EndTransactionHandlerImpl{
    
    @Override
    public void handle(Throwable caught) throws ApplicationException {
      log.trace("Конец транзакции");
      super.handle(caught);
    }
  };
  
  @Override
  public String getOperatorName(Integer operatorId) throws ApplicationException {
    String sqlQuery = 
      " begin ? := pkg_Operator.getOperatorName("
        + " operatorId => ?"
        + " );"
      + " end;";

    return super.execute(sqlQuery, String.class, 
      operatorId
    );
  }

  /**
   * Пример метода, содержащего транзакцию из нескольких последовательных обращений к базе.<br/>
   * Дополнительно определяются кастомные обработчики старта и завершения транзакции.
   */
  @Override
  @Before(startTransactionHandler = CustomStartTransactionHandler.class)
  @After(endTransactionHandler = CustomEndTransactionHandler.class)
  public void transaction() throws ApplicationException {    
    for (int i = 0; i < 3; i++) {
      testQuery(i);
    }
  }
  
  public void testQuery(Integer value) throws ApplicationException {
    String sqlQuery = 
      "insert into ejb_test(test_id, value) values (null, ?)";
    super.update(sqlQuery, value);
    simulateRandomError();    
  }

  private void simulateRandomError() {
    // имитация ошибки с вероятностью 20%
    int r = (new Random()).nextInt(5);
    if (r == 1) {
      throw new SystemException("Искусственная ошибка в процессе выполнения транзакции");
    }
  }

  @Override
  public List<JepRecord> find(JepRecord templateRecord,
      Mutable<Boolean> autoRefreshFlag, Integer maxRowCount,
      Integer operatorId) throws ApplicationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object create(JepRecord record, Integer operatorId)
      throws ApplicationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void update(JepRecord record, Integer operatorId)
      throws ApplicationException {
    throw new UnsupportedOperationException();
  }  
  
  @Override
  public void delete(JepRecord record, Integer operatorId)
      throws ApplicationException {
    throw new UnsupportedOperationException();
  }

}
