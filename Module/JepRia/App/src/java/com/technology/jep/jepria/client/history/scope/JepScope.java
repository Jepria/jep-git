package com.technology.jep.jepria.client.history.scope;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.history.place.JepPlace.ACTIVE_MODULE_PARAMETER_NAME;
import static com.technology.jep.jepria.client.history.place.JepPlace.PRIMARY_KEY_PARAMETER_NAME;
import static com.technology.jep.jepria.client.history.place.JepPlace.SCOPE_MODULES_PARAMETER_NAME;
import static com.technology.jep.jepria.client.history.place.JepPlace.SEARCH_REQUEST_PARAMETER_NAME;
import static com.technology.jep.jepria.client.history.place.JepPlace.TEMPLATE_PROPERTIES_PARAMETER_NAME;
import static com.technology.jep.jepria.client.history.place.JepPlace.WORKSTATE_PARAMETER_NAME;
import static com.technology.jep.jepria.shared.history.JepHistoryConstant.SCOPE_PARAMETER_NAME_VALUE_SEPARATOR;
import static com.technology.jep.jepria.shared.history.JepHistoryConstant.SCOPE_PARAMETER_SEPARATOR;

import java.util.HashMap;
import java.util.Map;

import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.shared.history.JepHistoryToken;

/**
 * Уровень иерархии модулей.<br/>
 * Объект класса описывает состояние модулей приложения относящихся к одному уровню иерархии модулей.<br/>
 * Класс является одним из элементов поддержки History в приложениях JepRia (смотри описание в 
 * {@link com.technology.jep.jepria.client.history}).
 * @see com.technology.jep.jepria.client.history
 */
public class JepScope {

  /**
   * Идентификатор активного модуля.
   */
  private String activeModuleId;

  /**
   * Внешний ключ для записей модуля.
   */
  private Map<String, Object> foreignKey = new HashMap<String, Object>();
  
  /**
   * Первичный ключ текущей записи.
   */
  private Map<String, Object> primaryKey = new HashMap<String, Object>();

  /**
   * Параметры последнего поискового шаблона.
   */
  private Map<String, Object> templateProperties = new HashMap<String, Object>();
  
  /**
   * Идентификаторы модулей. Первый - родительский.
   */
  private String[] moduleIds = null;
  
  /**
   * Состояния модулей.
   */
  private WorkstateEnum[] moduleStates = null;

  public JepScope(String[] scopeModuleIds) {
    this(scopeModuleIds, null);
  }

  public JepScope(String[] scopeModuleIds, WorkstateEnum[] scopeModuleStates) {
    this.moduleIds = scopeModuleIds;
    this.moduleStates = scopeModuleStates;
    this.activeModuleId = scopeModuleIds[0];

    if(this.moduleStates == null) {
      int moduleNumber = this.moduleIds.length;
      this.moduleStates = new WorkstateEnum[moduleNumber];
    }
  }
  
  /**
   * Конструктор восстанавливающий (создающий) объект из строкового представления (из History Token'а).
   *
   * @param historyToken строковое представление объекта (History Token)
   */
  public JepScope(String historyToken) {
    String[] parameterTab = historyToken.split(SCOPE_PARAMETER_SEPARATOR);
    int activeIndex = 0;
    int paramNumber = parameterTab.length;
    for(int i = 0; i < paramNumber; i++) {
      String[] valueTab = parameterTab[i].split(SCOPE_PARAMETER_NAME_VALUE_SEPARATOR, 2); // Считаем, что значение - это все, что после первого разделителя.
      String name = valueTab[0];
      String value = valueTab.length > 1 ? valueTab[1] : null;
      if(SCOPE_MODULES_PARAMETER_NAME.equals(name)) {
        moduleIds = getModuleIdsFromParameter(value);
      } else if(WORKSTATE_PARAMETER_NAME.equals(name)) {
        moduleStates = getStatesFromParameter(value);
      } else if(ACTIVE_MODULE_PARAMETER_NAME.equals(name)) {
        activeIndex = getActiveModuleIndexFromParameter(value);
      } else if(SEARCH_REQUEST_PARAMETER_NAME.equals(name)) {
        foreignKey = JepHistoryToken.buildMapFromToken(value);
      } else if(PRIMARY_KEY_PARAMETER_NAME.equals(name)) {
        primaryKey = JepHistoryToken.buildMapFromToken(value);
      } else if(TEMPLATE_PROPERTIES_PARAMETER_NAME.equals(name)) {
        templateProperties = JepHistoryToken.buildMapFromToken(value);
      }
    }
    
    if(moduleIds == null) {
      throw new IllegalArgumentException(JepTexts.errors_scope_illegalArgument_moduleIds());
    }
    
    if(moduleStates == null || moduleStates.length != moduleIds.length) {
      throw new IllegalArgumentException(JepTexts.errors_scope_illegalArgument_moduleStates());
    }
    
    this.activeModuleId = moduleIds[activeIndex];
  }

  public void reset() {
    this.setModuleIds(new String[]{moduleIds[0]});
    this.resetForeignKey();
  }

  public void resetForeignKey() {
    foreignKey = new HashMap<String, Object>();
  }

  public String getMainModuleId() {
    return this.moduleIds[0];
  }

  public boolean isMain(String moduleId) {
    return getMainModuleId().equals(moduleId);
  }

  public void setForeignKey(Map<String, Object> foreignKey) {
    this.foreignKey = foreignKey;
  }

  public Map<String, Object> getForeignKey() {
    return foreignKey;
  }

  public WorkstateEnum[] getModuleStates() {
    return moduleStates;
  }

  public void setModuleStates(WorkstateEnum[] moduleStates) {
    this.moduleStates = moduleStates;
  }

  public WorkstateEnum getModuleState(String  moduleId) {
    return moduleStates[indexOfModule(moduleId)];
  }

  public String getActiveModuleId() {
    return activeModuleId;
  }

  public JepScope setActiveModuleId(String activeModuleId) {
    this.activeModuleId = activeModuleId;
    return this;
  }

  public JepScope setCurrentWorkstate(WorkstateEnum newWorkstate) {
    moduleStates[indexOfModule(this.activeModuleId)] = newWorkstate;
    return this;
  }

  public WorkstateEnum getCurrentWorkstate() {
    int moduleIndex = indexOfModule(this.activeModuleId);
    WorkstateEnum moduleState = moduleStates[moduleIndex];
    if(moduleState == null) {
      if(isMain(activeModuleId)) {
        moduleState = WorkstateEnum.SEARCH;
      } else {
        moduleState = WorkstateEnum.VIEW_LIST;
      }
      moduleStates[moduleIndex] = moduleState;
    }
    return moduleState;
  }
  
  public String toString() {
    StringBuilder sbResult = new StringBuilder();
    sbResult.append("{");
    
    sbResult.append("moduleIds=");
    sbResult.append("[");
    if(moduleIds != null) {
      for(int i = 0; i < moduleIds.length; i++) {
        if(i > 0) {
          sbResult.append(", ");
        }
        sbResult.append(moduleIds[i]);
      }
    }
    sbResult.append("]");
    sbResult.append("\n");
    
    sbResult.append("moduleStates=");
    sbResult.append("[");
    if(moduleStates != null) {
      for(int i = 0; i < moduleStates.length; i++) {
        if(i > 0) {
          sbResult.append(", ");
        }
        sbResult.append(moduleStates[i]);
      }
    }
    sbResult.append("]");
    sbResult.append("\n");

    sbResult.append("activeModule='" + activeModuleId + "'");
    sbResult.append("\n");

    sbResult.append("foreignKey='" + foreignKey + "'");
    sbResult.append("\n");
    
    sbResult.append("primaryKey='" + primaryKey + "'");
    sbResult.append("\n");

    sbResult.append("templateProperties='" + templateProperties + "'");
    sbResult.append("\n");
    
    sbResult.append("}");
    return sbResult.toString();
  }

  /**
   * Метод формирует строковое представление (History Token) одного уровня иерархии модулей (одного scope'а) 
   * (объекта {@link com.technology.jep.jepria.client.history.scope.JepScope}).<br/>
   * <br/>
   * Подробности смотри в описании к методу {@link com.technology.jep.jepria.client.history.scope.JepScope#toHistoryToken(WorkstateEnum workstate)} .
   *
   * @return строковое представление (History Token) одного уровня иерархии модулей (одного scope'а) 
   * (объекта {@link com.technology.jep.jepria.client.history.scope.JepScope})
   */
  public String toHistoryToken() {
    int activeModuleIndex = indexOfModule(activeModuleId);
    WorkstateEnum activeModuleWorkstate = moduleStates[activeModuleIndex];
    
    return toHistoryToken(activeModuleWorkstate);
  }

  /**
   * Метод формирует строковое представление (History Token) одного уровня иерархии модулей (одного scope'а) 
   * (объекта {@link com.technology.jep.jepria.client.history.scope.JepScope}).<br/>
   * <br/>
   * Формат формируемой строки:
   * <ul>
   *   <li>
   *     {@link com.technology.jep.jepria.client.history.place.JepPlace#SCOPE_MODULES_PARAMETER_NAME} - 
   *     наименование параметра содержащего список модулей данного уровня
   *   </li>
   *   <li>{@link com.technology.jep.jepria.shared.history.JepHistoryConstant#SCOPE_PARAMETER_NAME_VALUE_SEPARATOR} - разделитель между наименованием и значением параметра</li>
   *   <li>
   *     список идентификаторов модулей данного уровня перечисленных через запятую &laquo;,&raquo;
   *   </li>
   *   <li>{@link com.technology.jep.jepria.shared.history.JepHistoryConstant#SCOPE_PARAMETER_SEPARATOR} - разделитель между параметрами</li>
   *   <li>
   *     {@link com.technology.jep.jepria.client.history.place.JepPlace#WORKSTATE_PARAMETER_NAME} - 
   *     наименование параметра содержащего список состояний модулей данного уровня
   *   </li>
   *   <li>{@link com.technology.jep.jepria.shared.history.JepHistoryConstant#SCOPE_PARAMETER_NAME_VALUE_SEPARATOR} - разделитель между наименованием и значением параметра</li>
   *   <li>
   *     список состояний модулей данного уровня перечисленных через запятую &laquo;,&raquo;
   *   </li>
   *   <li>{@link com.technology.jep.jepria.shared.history.JepHistoryConstant#SCOPE_PARAMETER_SEPARATOR} - разделитель между параметрами</li>
   *   <li>
   *     {@link com.technology.jep.jepria.client.history.place.JepPlace#ACTIVE_MODULE_PARAMETER_NAME} - 
   *     наименование параметра содержащего индекс активного модуля
   *   </li>
   *   <li>{@link com.technology.jep.jepria.shared.history.JepHistoryConstant#SCOPE_PARAMETER_NAME_VALUE_SEPARATOR} - разделитель между наименованием и значением параметра</li>
   *   <li>
   *     индекс активного модуля
   *   </li>
   *   <li>{@link com.technology.jep.jepria.shared.history.JepHistoryConstant#SCOPE_PARAMETER_SEPARATOR} - разделитель между параметрами</li>
   *   <li>
   *     {@link com.technology.jep.jepria.client.history.place.JepPlace#SEARCH_REQUEST_PARAMETER_NAME} - 
   *     наименование параметра содержащего внешний ключ для данного уровня
   *   </li>
   *   <li>{@link com.technology.jep.jepria.shared.history.JepHistoryConstant#SCOPE_PARAMETER_NAME_VALUE_SEPARATOR} - разделитель между наименованием и значением параметра</li>
   *   <li>
   *     Строка представляющая внешний ключ.<br/>
   *     Строка формируется методом {@link com.technology.jep.jepria.shared.history.JepHistoryToken#getMapAsToken(Map map)}
   *   </li>
   *   <li>{@link com.technology.jep.jepria.shared.history.JepHistoryConstant#SCOPE_PARAMETER_SEPARATOR} - разделитель между параметрами</li>
   *   <li>
   *     {@link com.technology.jep.jepria.client.history.place.JepPlace#TEMPLATE_PROPERTIES_PARAMETER_NAME} - 
   *     наименование параметра содержащего поисковый шаблон для данного уровня
   *   </li>
   *   <li>{@link com.technology.jep.jepria.shared.history.JepHistoryConstant#SCOPE_PARAMETER_NAME_VALUE_SEPARATOR} - разделитель между наименованием и значением параметра</li>
   *   <li>
   *     Строка представляющая поисковый шаблон.<br/>
   *     Строка формируется методом {@link com.technology.jep.jepria.shared.history.JepHistoryToken#getMapAsToken(Map map)}
   *   </li>
   *   <li>{@link com.technology.jep.jepria.shared.history.JepHistoryConstant#SCOPE_PARAMETER_SEPARATOR} - разделитель между параметрами</li>
   *   <li>
   *     {@link com.technology.jep.jepria.client.history.place.JepPlace#PRIMARY_KEY_PARAMETER_NAME} - 
   *     наименование параметра содержащего последний полученный первичный ключ
   *   </li>
   *   <li>{@link com.technology.jep.jepria.shared.history.JepHistoryConstant#SCOPE_PARAMETER_NAME_VALUE_SEPARATOR} - разделитель между наименованием и значением параметра</li>
   *   <li>
   *     Строка представляющая последний полученный первичный ключ.<br/>
   *     Строка формируется методом {@link com.technology.jep.jepria.shared.history.JepHistoryToken#getMapAsToken(Map map)}
   *   </li>
   * </ul>
   * Пример: sm=Site,Page,Host&amp;ws=vd,null,vd&amp;am=2&amp;sr=siteId:33674&amp;tp=siteId:33674&amp;pk=hostId:522
   *
   * @param workstate новое рабочее состояние (режим) текущего активного модуля
   * @return строковое представление (History Token) одного уровня иерархии модулей (одного scope'а) 
   * (объекта {@link com.technology.jep.jepria.client.history.scope.JepScope})
   */
  public String toHistoryToken(WorkstateEnum workstate) {
    StringBuilder sbResult = new StringBuilder();
    // Список модулей.
    sbResult.append(SCOPE_MODULES_PARAMETER_NAME);
    sbResult.append(SCOPE_PARAMETER_NAME_VALUE_SEPARATOR);
    int moduleNumber = moduleIds.length;
    for(int i = 0; i < moduleNumber; i++) {
      if(i > 0) {
        sbResult.append(",");
      }
      sbResult.append(moduleIds[i]);
    }

    int activeModuleIndex = indexOfModule(activeModuleId);
    if(moduleStates != null) {
      moduleStates[activeModuleIndex] = workstate;
      
      // Состояния модулей.
      sbResult.append(SCOPE_PARAMETER_SEPARATOR);
      sbResult.append(WORKSTATE_PARAMETER_NAME);
      sbResult.append(SCOPE_PARAMETER_NAME_VALUE_SEPARATOR);
      for(int i = 0; i < moduleNumber; i++) {
        if(i > 0) {
          sbResult.append(",");
        }
        sbResult.append(moduleStates[i]);
      }
    }
    
    if(activeModuleIndex > 0) { // Так экономнее (когда активный главный, индекс не включаем).
      // Активный модуль.
      sbResult.append(SCOPE_PARAMETER_SEPARATOR);
      sbResult.append(ACTIVE_MODULE_PARAMETER_NAME);
      sbResult.append(SCOPE_PARAMETER_NAME_VALUE_SEPARATOR);
      sbResult.append(activeModuleIndex);
    }
    
    // Внешний ключ.
    if(foreignKey != null && foreignKey.size() > 0) {
      sbResult.append(SCOPE_PARAMETER_SEPARATOR);
      sbResult.append(SEARCH_REQUEST_PARAMETER_NAME);
      sbResult.append(SCOPE_PARAMETER_NAME_VALUE_SEPARATOR);
      sbResult.append(JepHistoryToken.getMapAsToken(foreignKey));
    }
    
    // Свойства поискового шаблона.
    if(templateProperties != null && templateProperties.size() > 0) {
      sbResult.append(SCOPE_PARAMETER_SEPARATOR);
      sbResult.append(TEMPLATE_PROPERTIES_PARAMETER_NAME);
      sbResult.append(SCOPE_PARAMETER_NAME_VALUE_SEPARATOR);
      sbResult.append(JepHistoryToken.getMapAsToken(templateProperties));
    }
    
    // Первичный ключ.
    if(primaryKey != null && primaryKey.size() > 0) {
      sbResult.append(SCOPE_PARAMETER_SEPARATOR);
      sbResult.append(PRIMARY_KEY_PARAMETER_NAME);
      sbResult.append(SCOPE_PARAMETER_NAME_VALUE_SEPARATOR);
      sbResult.append(JepHistoryToken.getMapAsToken(primaryKey));
    }
    
    return sbResult.toString();
  }
  
  public String[] getModuleIds() {
    return moduleIds;
  }

  public JepScope setModuleIds(String[] newModuleIds) {
    this.moduleIds = newModuleIds;
    
    WorkstateEnum mainWorkstate = moduleStates == null ? null : moduleStates[0]; 
    this.moduleStates = getInitialModuleStates(moduleIds, null);
    this.moduleStates[0] = mainWorkstate; 
    return this;
  }
  
  private int indexOfModule(String moduleId) {
    int moduleNumber = moduleIds.length;
    for(int i = 0; i < moduleNumber; i++) {
      if(moduleIds[i].equals(moduleId)) {
        return i;
      }
    }
    return -1;
  }
  
  private static WorkstateEnum[] getInitialModuleStates(String[] moduleIds, WorkstateEnum workstate) {
    int moduleNumber = moduleIds.length;
    WorkstateEnum[] result = new WorkstateEnum[moduleNumber];
    for(int i = 0; i < moduleNumber; i++) {
      result[i] = i == 0 ? workstate : null; 
    }
    return result;
  }

  public void collapseIfMain() {
    if(isMainActive()) {
      setModuleIds(new String[]{moduleIds[0]});
      if(JepScopeStack.instance.size() < 2) {
        resetForeignKey();
      }
    }
  }
  
  protected String[] getModuleIdsFromParameter(String moduleIds) {
    return moduleIds.split(",");
  }

  protected WorkstateEnum[] getStatesFromParameter(String statesValue) {
    String[] scopeStateTab = statesValue.split(",");
    int moduleNumber = scopeStateTab.length;
    WorkstateEnum[] result = new WorkstateEnum[moduleNumber];
    for(int i = 0; i < moduleNumber; i++) {
      result[i] = WorkstateEnum.fromString(scopeStateTab[i].trim());
    }
    return result;
  }

  /**
   * Преобразует строковое представление индекса активного модуля в целочисленное значение.
   *
   * @param activeModuleIndex строковое представление индекса активного модуля
   * @return целочисленное значение индекса активного модуля
   */
  protected int getActiveModuleIndexFromParameter(String activeModuleIndex) {
    return activeModuleIndex != null ? Integer.parseInt(activeModuleIndex) : 0;
  }
  
  /**
   * Определение совпадения (по набору модулей)
   * 
   * @return true в случае совпадения
   */
  public boolean isMatch(JepScope other) {
    String[] moduleIds1 = this.getModuleIds();
    String[] moduleIds2 = other.getModuleIds();
    if(moduleIds1.length != moduleIds2.length) {
      return false;
    } else {
      for(int i = 0; i < moduleIds1.length; i++) {
        if(!moduleIds1[i].equals(moduleIds2[i])) {
          return false;
        }
      }
    }
    return true;
  }

  private static String[] cloneStringArray(String[] other) {
    int length = other.length;
    String[] result = new String[length];
    for(int i = 0; i < length; i++) {
      result[i] = other[i];
    }
    return result;
  }

  private static WorkstateEnum[] cloneWorkstateEnumArray(WorkstateEnum[] other) {
    int length = other.length;
    WorkstateEnum[] result = new WorkstateEnum[length];
    for(int i = 0; i < length; i++) {
      result[i] = other[i];
    }
    return result;
  }

  public boolean isMainActive() {
    return isMain(activeModuleId);
  }

  public void setPrimaryKey(Map<String, Object> primaryKey) {
    this.primaryKey = primaryKey;
  }

  public Map<String, Object> getPrimaryKey() {
    return primaryKey;
  }

  public void setTemplateProperties(Map<String, Object> templateProperties) {
    this.templateProperties = templateProperties;
  }

  public Map<String, Object> getTemplateProperties() {
    return templateProperties;
  }

}
