package com.technology.jep.jepria.client.history.scope;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.shared.history.JepHistoryConstant.SCOPE_SEPARATOR;
import static com.technology.jep.jepria.shared.history.JepHistoryConstant.SCOPE_SEPARATOR_REGEXP;

import java.util.Stack;

import com.allen_sauer.gwt.log.client.Log;
import com.technology.jep.jepria.client.history.place.JepWorkstatePlace;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.main.MainClientFactory;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

/**
 * Состояние приложения.<br/>
 * Объект класса создается как singleton на уровне приложения и описывает текущее состояние приложения.<br/>
 * Класс является одним из основных элементов поддержки History в приложениях JepRia (смотри описание в 
 * {@link com.technology.jep.jepria.client.history}).
 * @see com.technology.jep.jepria.client.history
 */
public class JepScopeStack extends Stack<JepScope> {

  private static final long serialVersionUID = 3543460342703982514L;

  public static JepScopeStack instance = new JepScopeStack();
  private JepScopeStack() {}

  private MainClientFactory<MainEventBus, JepMainServiceAsync> clientFactory;
  
  /**
   * Признак завершённости входа
   * Используется здесь по причине глобальности стека
   */
  private boolean isUserEntered = false;

  private boolean isExitScope = false;

  @Override
  public String toString() {
    StringBuffer sbResult = new StringBuffer();
    sbResult.append("scopeStack={\n");
    boolean isFirst = true;
    for(JepScope scope: this) {
      if(isFirst) {
        isFirst = false;
      } else {
        sbResult.append(", ");
      }
      sbResult.append(scope);
    }
    sbResult.append("}\n");
    
    return sbResult.toString();
  }

  /**
   * Метод формирует строковое представление (History Token) текущего состояния приложения 
   * (объекта {@link com.technology.jep.jepria.client.history.scope.JepScopeStack}).<br/>
   * <br/>
   * Формат формируемой строки:
   * <ul>
   *   <li>
   *     Параметры, которые описывают {@link com.technology.jep.jepria.client.history.scope.JepScope}.<br/>
   *     Строка формируется методом {@link com.technology.jep.jepria.client.history.scope.JepScope#toHistoryToken()}
   *   </li>
   *   <li>
   *     {@link com.technology.jep.jepria.shared.history.JepHistoryConstant#SCOPE_SEPARATOR} - разделитель элементов 
   *     {@link com.technology.jep.jepria.client.history.scope.JepScope} стека 
   *     {@link com.technology.jep.jepria.client.history.scope.JepScopeStack}
   *   </li>
   *   <li>
   *     Параметры, которые описывают {@link com.technology.jep.jepria.client.history.scope.JepScope}.<br/>
   *     Строка формируется методом {@link com.technology.jep.jepria.client.history.scope.JepScope#toHistoryToken()}
   *   </li>
   *   <li>
   *     {@link com.technology.jep.jepria.shared.history.JepHistoryConstant#SCOPE_SEPARATOR} - разделитель элементов 
   *     {@link com.technology.jep.jepria.client.history.scope.JepScope} стека 
   *     {@link com.technology.jep.jepria.client.history.scope.JepScopeStack}
   *   </li>
   *   <li>...</li>
   *   <li>...</li>
   *   <li>
   *     Параметры, которые описывают самый ВЕРХНИЙ (последний) элемент {@link com.technology.jep.jepria.client.history.scope.JepScope} стека.<br/>
   *     Строка формируется методом {@link com.technology.jep.jepria.client.history.scope.JepScope#toHistoryToken(WorkstateEnum workstate)}
   *   </li>
   * </ul>
   *
   * @param workstatePlace новый Place (History Token/строка Url изменяется только при установке нового Place'а. Смотри 
   * {@link com.technology.jep.jepria.client.history})
   * @return строковое представление (History Token) текущего состояния приложения 
   * (объекта {@link com.technology.jep.jepria.client.history.scope.JepScopeStack})
   */
  public String toHistoryToken(JepWorkstatePlace workstatePlace) {
    StringBuilder sb = new StringBuilder();
    for(JepScope scope: this) {
      if(sb.length() > 0) {
        sb.append(SCOPE_SEPARATOR);
      }
      if(scope == lastElement()) {
        sb.append(scope.toHistoryToken(workstatePlace.getWorkstate()));
      } else {
        sb.append(scope.toHistoryToken());
      }
    }
    
    return sb.toString();
  }

  /**
   * Метод восстанавливает текущее состояние приложения 
   * (устанавливает своийства объекта {@link com.technology.jep.jepria.client.history.scope.JepScopeStack})
   * из строкового представления (History Token'а) состояния приложения .<br/>
   * <br/>
   * @param token строковое представление (History Token) состояния приложения
   */
  public void setFromHistoryToken(String token) {
    Log.debug(this.getClass() + ".setFromHistoryToken(): token = " + token);
  
    clear();    
    String[] scopeTokens = token.split(SCOPE_SEPARATOR_REGEXP);
    try {
      for(int i = 0; i < scopeTokens.length; i++) {
        String strScope = scopeTokens[i];
        JepScope scope = new JepScope(strScope);

        if(clientFactory.getModuleIds().contains(scope.getActiveModuleId())) {
          push(scope);
        } else {  // Кривой Url, устанавливаем умолчательное состояние.
          setDefaultState();
          break;
        }
      }
    } catch(Throwable th) { // При любой ошибке - считаем, что проблемы с разбором Url и ...
      setDefaultState(); // ... устанавливаем умолчательное состояние.
    }
  }

  public void setDefaultState() {
    clear();
    if (clientFactory.getModuleIds() != null) {
      String[] defaultModuleIds = new String[] {clientFactory.getModuleIds().get(0)}; 
      JepScope scope = new JepScope(defaultModuleIds);
      push(scope);
      clientFactory.getMessageBox().showError(JepTexts.errors_scopeStack_incorrectUrl_defaultPlace());
    } else {
      clientFactory.getMessageBox().showError(JepTexts.errors_scopeStack_incorrectUrl_cantStart());
    }
  }

  @Override
  public JepScope peek() {
    return empty() ? null : super.peek();
  }

  @Override
  public JepScope pop() {
    return empty() ? null : pop();
  }

  public void setMainClientFactory(MainClientFactory<MainEventBus, JepMainServiceAsync> clientFactory) {
    this.clientFactory = clientFactory;
  }

  /**
   * Получение главной клиентской фабрики приложения.<br/>
   * Используется для целей автоматизированного тестирования.
   *
   * @return главная клиентская фабрика приложения
   */
  public MainClientFactory<MainEventBus, JepMainServiceAsync> getMainClientFactory() {
    return clientFactory;
  }

  public void setUserEntered() {
    this.isUserEntered = true;
  }

  public boolean isUserEntered() {
    return isUserEntered;
  }
  
  public void setExitScope(boolean isExitScope) {
    this.isExitScope = isExitScope;
  }

  public boolean isExitScope() {
    return isExitScope;
  }
}
