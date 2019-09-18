package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.widget.event.JepEventType.CHANGE_CHECK_EVENT;

import java.util.*;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.technology.jep.jepria.client.async.DataLoader;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent.CheckChangeHandler;
import com.technology.jep.jepria.client.widget.field.tree.TreeField;
import com.technology.jep.jepria.shared.field.option.JepOption;

/**
 * Поле для работы со значениями, представленными в виде иерархического дерева.
 */
public class JepTreeField extends JepMultiStateField<TreeField<JepOption>, HTML> {

  /**
   * Список узлов, которые необходимо отметить.
   */
  protected List<JepOption> checkedValues = null;
  
  /**
   * Список узлов, которые необходимо раскрыть.
   */
  protected List<JepOption> expandedValues = null;

  /**
   * Возможность выбора узлов дерева (по умолчанию, допускается выделение узлов). 
   */
  private boolean checkable = true;
  
  /**
   * Флаг установленного загрузчика.
   */
  protected boolean hasLoader = false;
  
  private final static int DEFAULT_TREE_FIELD_HEIGHT = 300;
  
  public JepTreeField() {
    this(null);
  }
  
  public JepTreeField(String fieldLabel){
    this(Document.get().createUniqueId(), fieldLabel);
  }
  
  public JepTreeField(String fieldIdAsWebEl, String fieldLabel){
    super(fieldIdAsWebEl, fieldLabel);
    // установка высоты по умолчанию
    setFieldHeight(DEFAULT_TREE_FIELD_HEIGHT);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void addEditableCard() {
    editableCard = new TreeField<JepOption>(fieldIdAsWebEl);
    editablePanel.add(editableCard);
  }
  
  /**
   * Установка загрузчика узлов нижележащего уровня.
   * 
   * @param loader загрузчик узлов нижележащего уровня
   */
  public void setLoader(final DataLoader<JepOption> loader){
    hasLoader = true;
    editableCard.setLoader(new DataLoader<JepOption>() {
      public void load(Object loadConfig, final AsyncCallback<List<JepOption>> callback) {
        loader.load(loadConfig, new JepAsyncCallback<List<JepOption>>() {
          @Override
          public void onSuccess(List<JepOption> result) {
            callback.onSuccess(result);
            processExpanding();
            processChecking();
          }
          @Override
          public void onFailure(Throwable caught) {
            callback.onFailure(caught);
          }
        });
      }
    });
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public void setValue(Object value) {
    Object oldValue = getValue();
    if(!Objects.equals(oldValue, value)) {
      // Создание копии списка элементов важно, поскольку в методе processChecking
      // происходит удаление элементов списка, что приводит к изменению состояния 
      // currentRecord при смене рабочего состояния формы
      this.checkedValues = new ArrayList<JepOption>((List<JepOption>)value);
      processChecking();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<JepOption> getValue() {
    return editableCard.getCheckedSelection();
  }
  
  /**
   * Пометить узлы в дереве, как частично выделенные.
   * @param values список узлов
   */
  public void setPartialSelected(List<JepOption> values) {
    editableCard.setPartialSelected(values);
  }

  /**
   * Устанавливает стиль поведения (каскадного выделения) для отмечаемых узлов.<br/>
   * Возможные значения:
   * <ul>
   *  <li>NONE - каскадного выделения никаких узлов не происходит</li>
   *  <li>PARENTS - каскадно выделяются все родители</li>
   *  <li>CHILDREN - каскадно выделюятся все дети</li>
   * </ul>
   * Замечание: при установке значения CHILDREN отмечаются (очевидно) только отрисованные дочерние узлы.
   * 
   * @param checkCascade стиль поведения (каскадного выделения) для отмечаемых узлов
   */
  public void setCheckStyle(TreeField.CheckCascade checkCascade) {
    editableCard.setCheckStyle(checkCascade);
  }
  
  /**
   * Устанавливает какие узлы можно отмечать.<br/>
   * Возможные значения:
   * <ul>
   *  <li>BOTH - можно отмечать и узлы и конечные листья</li>
   *  <li>PARENT - только родительские узлы (узлы, которые содержат дочерние элементы)</li>
   *  <li>LEAF - только листья (узлы, которые НЕ содержат дочерних элементов)</li>
   * </ul>
   *
   * @param checkNodes какие узлы можно отмечать
   */  
  public void setCheckNodes(TreeField.CheckNodes checkNodes) {
    editableCard.setCheckNodes(checkNodes);
  }

  /**
   * Возвращает признак возможности выбора элементов дерева.
   * 
   * @return признак возможности выбора элементов дерева
   */
  public boolean isCheckable() {
    return checkable;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setFieldHeight(int fieldHeight) {
    editableCard.setHeight(fieldHeight + Unit.PX.getType());
  }
  
  /**
   * Метод не поддерживается.
   */
  @Override
  public void setEditable(boolean editable) {
    throw new UnsupportedOperationException();
  }
  
  /**
   * Метод предка перегружен пустой реализацией, т.к. в данном компоненте карта Просмотра не используется.
   * 
   * @param value значение для карты Просмотра
   */
  @Override
  protected void setViewValue(Object value) {

  }
  
  /**
   * Проверяет: содержит ли допустимое значение поле.
   *
   * @return true - если поле содержит допустимое значение, false - в противном случае
   */
  @Override
  public boolean isValid() {
    clearInvalid();
    if(!allowBlank) {
      if(getValue().size() == 0) {
        markInvalid(JepTexts.checkForm_mandatoryField());
        return false;
      }
    }
    return true;
  }
  
  /**
   * Указывает, какие узлы необходимо раскрыть.
   */
  public void setExpanded(List<JepOption> expandedValues) {
    // Создание копии списка элементов важно, поскольку в методе processExpanding
    // происходит удаление элементов списка, что может привести к потенциальным ошибкам 
    // в клиентских модулях
    this.expandedValues = new ArrayList<JepOption>(expandedValues);
    editableCard.setCheckable(false);
    processExpanding();
  }
  
  /**
   * Раскрывает отрисованные узлы и удаляет их из списка узлов, которые необходимо раскрыть 
   * {@link com.technology.jep.jepria.client.widget.field.multistate.JepTreeField#expandedValues}.
   */
  protected void processExpanding() {
    if(expandedValues != null && expandedValues.size() > 0) {
      setLoadingImage(true);
      JepOption option = expandedValues.get(0);
      editableCard.setExpanded(option, true);
      if (editableCard.isNodeOpened(option)) {
        expandedValues.remove(0);
        processExpanding();
      } else {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
          @Override
          public void execute() {
            processExpanding();
          }
        });
      }
    } else {
      setLoadingImage(false);
      editableCard.setCheckable(checkable);
    }
  }

  /**
   * Отмечает отрисованные узлы и удаляет их из списка узлов, которые необходимо отметить 
   * {@link com.technology.jep.jepria.client.widget.field.multistate.JepTreeField#checkedValues}.
   */
  protected void processChecking() {
    if(checkedValues != null && checkedValues.size() > 0) {
      Iterator<JepOption> iterator = checkedValues.iterator();
      while(iterator.hasNext()) {
        JepOption option = iterator.next();
        // Удаляем значение, т.к. открытие узлов - это разовая (в данном случае) операция
        // и НЕ нужно повторно открывать указанные узлы (которые пользователь, возможно, уже закрыл).
        editableCard.setChecked(option, true);
        iterator.remove();
      }
    }
  }
  
  /**
   * Обработчик нового состояния
   * 
   * @param newWorkstate новое состояние
   */
  @Override
  protected void onChangeWorkstate(WorkstateEnum newWorkstate) {
    
    // В данном компоненте работаем ТОЛЬКО с картой Редактирования.
    showWidget(getWidgetIndex(editablePanel));
    
    // Если карта Редактирования уже создана (первый раз метод вызывается в предке, когда карты Редактирования еще нет).
    if(editableCard != null) {
      // При смене состояния прокручиваем карту Редактирования наверх.
      if (hasLoader) editableCard.refresh();
      if(WorkstateEnum.isEditableState(newWorkstate)) { // Для случая Редактирования: ...
        editableCard.setCheckable(true);// позволим отмечать узлы дерева
        checkable = true;
        editableCard.setBorders(true); // отобразим границы рабочей области компонента
        editableCard.setBackgroundColor("white"); // установим белый фон рабочей области компонента
      } else { // Для случая Просмотра: ...
        editableCard.setCheckable(false); // запретим отмечать узлы дерева
        checkable = false;
        editableCard.setBorders(false); // скроем границы рабочей области компонента
        editableCard.setBackgroundColor("transparent"); // установим прозрачный фон рабочей области компонента
      }      
    }
  
  }
  
  /**
   * {@inheritDoc}
   */
  public void setEnabled(boolean enabled) {
    editableCard.setEnabled(enabled);
  }
  
  /**
   * Метод не поддерживается данным полем.
   */
  @Override
  public String getRawValue(){
    throw new UnsupportedOperationException("TreeField does not have a raw value.");
  }
  
  /**
   * Очищает значение поля.<br/>
   * После очистки значения поля, все узлы дерева сворачиваются.<br/>
   * Карта Просмотра не очищается, т.к. в данном компоненте она не используется.
   */
  @Override
  public void clear() {
    checkedValues = null;
    expandedValues = null;
    editableCard.refresh();
  }
  
  /**
   * Добавление слушателя определенного типа собитий.<br/>
   * Концепция поддержки обработки событий и пример реализации метода отражен в описании пакета {@link com.technology.jep.jepria.client.widget}.
   *
   * @param eventType тип события
   * @param listener слушатель
   */
  @Override
  public void addListener(JepEventType eventType, JepListener listener) {
    switch(eventType) {
      case CHANGE_CHECK_EVENT:
        addChangeCheckListener();
        break;
    }
    super.addListener(eventType, listener);
  }
  
  /**
   * Добавление прослушивателей для реализации прослушивания события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_CHECK_EVENT }.
   */
  protected void addChangeCheckListener() {
    editableCard.addCheckChangeHandler(new CheckChangeHandler<JepOption>() {
      @Override
      public void onCheckChange(CheckChangeEvent<JepOption> event) {
        notifyListeners(CHANGE_CHECK_EVENT, new JepEvent(JepTreeField.this, event));
      }
    });
  }
  
  /**
   * Установка видимости флага "Выделить все".<br>
   * По умолчанию флаг невидим.
   * @param visible если true, то показать, в противном случае - скрыть
   */
  public void setSelectAllCheckBoxVisible(boolean visible) {
    editableCard.setSelectAllCheckBoxVisible(visible);
  }
  
  @Override
  protected void setWebIds() {
    super.setWebIds();
    editableCard.setCompositeWebIds(fieldIdAsWebEl);
  }
}
