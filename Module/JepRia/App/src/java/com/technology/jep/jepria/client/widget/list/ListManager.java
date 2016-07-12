package com.technology.jep.jepria.client.widget.list;

import java.util.List;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SetSelectionModel;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.widget.MaskPanel;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.event.JepObservable;
import com.technology.jep.jepria.client.widget.event.JepObservableImpl;
import com.technology.jep.jepria.shared.load.PagingResult;
import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Абстрактный класс управления списком.<br/>
 * 
 * NB: Интерфейс (public методы) данного класса должен стремится (иметь максимальную схожесть) с интерфейсом java.util.List.<br/>
 * Желательно, методы, выполняющие схожие действия с методами класса {@link com.technology.jep.jepria.client.widget.field.FieldManager}
 * именовать схожим образом.<br/>
 * <br/>
 * Концепция поддержки обработки событий отражена в описании пакета {@link com.technology.jep.jepria.client.widget}.
 */
public abstract class ListManager<W extends Widget, P, S extends SetSelectionModel<JepRecord>> 
  implements JepObservable {

  /**
   * Уникальный идентификатор класса.
   */
  private Integer uid;

  /**
   * Компонент-список, действиями с которым, управляет класс.
   */
  protected W widget;

  /**
   * Инструментальная панель управления листанием набора данных.
   */ 
  protected P pagingToolBar;
  
  /**
   * Объект для работы со слушателями событий.
   */
  protected JepObservable observable;

  /**
   * Текущее состояние.
   */
  protected WorkstateEnum _workstate = null;
  
  protected MaskPanel gridMask;
  protected MaskPanel pagingToolBarMask;
  
  public ListManager() {
    uid = new Integer(Random.nextInt());
    observable = new JepObservableImpl();
    
    gridMask = new MaskPanel();
    pagingToolBarMask = new MaskPanel();
  }

  /**
   * Получение уникального идентификатора класса.
   *
   * @return уникальный идентификатор класса
   */
  public Integer getUID() {
    return uid;
  }
  
  /**
   * Установка компонента-списка, действиями с которым, управляет класс.
   *
   * @param widget компонент-список, действиями с которым, управляет класс
   */
  public void setWidget(W widget) {
    this.widget = widget;
  }

  /**
   * Получение компонента-списка, действиями с которым, управляет класс.
   *
   * @return компонент-список, действиями с которым, управляет класс
   */
  public W getWidget() {
    return widget;
  }

  /**
   * Установка инструментальной панели управления листанием набора данных.
   *
   * @param pagingToolBar панель управления листанием набора данных
   */
  public void setPagingToolBar(P pagingToolBar) {
    this.pagingToolBar = pagingToolBar;
  }
  
  /**
   * Получение инструментальной панели управления листанием набора данных.
   *
   * @return панель управления листанием набора данных
   */
  public P getPagingToolBar() {
    return pagingToolBar;
  }
  
  /**
   * Установка нового состояния списка.
   * 
   * @param workstate новое состояние
   */
  public void changeWorkstate(WorkstateEnum workstate) {
    onChangeWorkstate(workstate);
  }
  
  /**
   * Обработчик нового состояния.
   * 
   * @param newWorkstate новое состояние
   */
  protected void onChangeWorkstate(WorkstateEnum newWorkstate) {
    // Только в случае, если действительно изменяется состояние.
    if(newWorkstate != null && !newWorkstate.equals(_workstate)) {
      _workstate = newWorkstate;
    }
  }
  
  /**
   * Установка списка значений в компонент-списка.
   *
   * @param list список значений
   */
  public abstract void set(List<JepRecord> list);

  /**
   * Установка объекта-результата поиска в компонент-списка.
   *
   * @param pagingResult объект-результат поиска
   */
  public abstract void set(PagingResult<JepRecord> pagingResult);

  /**
   * Очистка списка значений в компоненте-списка.
   */
  public abstract void clear();
  
  /**
   * Получение выделенных записей списка.
   * 
   * @return модель выделения в списке
   */
  public abstract S getSelectionModel();
  
  /**
   * Добавление записи в список.
   * 
    * @param record запись
   */
  public abstract void add(JepRecord record);

  /**
   * Добавление записей в список.
   * 
    * @param list список записей
   */
  public abstract void add(List<JepRecord> list);
  
  /**
   * Удаление записи из списка.
   * 
    * @param index номер записи
   */
  public abstract void remove(int index);
  
  /**
   * Удаление записи из списка.
   * 
    * @param record запись
   */
  public abstract void remove(JepRecord record);
  
  /**
   * Изменение записи в списке (экземпляр, предварительно полученный с помощью get(int index).
   * 
    * @param record запись
   */
  public abstract void update(JepRecord record);
  
  /**
   * Изменение записи в списке (по порядковому номеру).
   * 
    * @param index номер записи
    * @param record запись
   */
  public abstract void update(int index, JepRecord record);
  
  /**
   * Получение записи из списка (по порядковому номеру).
   * 
    * @param index номер записи
    * 
   * @return запись
   */
  public abstract JepRecord get(int index);
  
  /**
   * Получение размера страницы набора данных.
   * 
   * @return размер страницы набора данных
   */
  public abstract int getPageSize();

  /**
   * Получение количества записей
   * 
   * @return количество записей
   */
  public abstract int size();
  
  
  /**
   * Накладывает маску на компоненты ({@link #widget компонент-список} и {@link #pagingToolBar инструментальную панель}),
   * отображает сообщение и приостанавливает взаимодействие с пользователем.
   * 
    * @param message сообщения для отображения
   */
  public void mask(String message) {
    gridMask.mask(widget.getElement(), message);
    
    if (pagingToolBar != null && pagingToolBar instanceof UIObject)
      pagingToolBarMask.mask(((UIObject) pagingToolBar).getElement(), null);
  }

  /**
   * Снимает маску с {@link #widget компонента-списка}, {@link #pagingToolBar инструментальной панели} и возобновляет взаимодействие 
   * с пользователем.
    */
  public void unmask() {
    gridMask.unmask();
    pagingToolBarMask.unmask();
  }

  /**
   * Добавление слушателя определенного типа собитий.<br/>
   * Реализуется вызовом метода {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#addListener(JepEventType eventType, JepListener listener)}
   * объекта {@link com.technology.jep.jepria.client.widget.list.ListManager#observable} .
   *
   * @param eventType тип события
   * @param listener слушатель
   */
  public void addListener(JepEventType eventType, JepListener listener) {
    observable.addListener(eventType, listener);
  }

  /**
   * Удаление слушателя определенного типа собитий.<br/>
   * Реализуется вызовом метода {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#removeListener(JepEventType eventType, JepListener listener)}
   * объекта {@link com.technology.jep.jepria.client.widget.list.ListManager#observable} .
   *
   * @param eventType тип события
   * @param listener слушатель
   */
  public void removeListener(JepEventType eventType, JepListener listener) {
    observable.removeListener(eventType, listener);
  }
  
  /**
   * Уведомление слушателей определенного типа о событии.<br/>
   * Реализуется вызовом метода {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#notifyListeners(JepEventType eventType, JepEvent event)}
   * объекта {@link com.technology.jep.jepria.client.widget.list.ListManager#observable} .
   *
   * @param eventType тип события
   * @param event событие
   */
  public void notifyListeners(JepEventType eventType, JepEvent event) {
    observable.notifyListeners(eventType, event);
  }

  /**
   * Получение списка слушателей определенного типа собитий.<br/>
   * Реализуется вызовом метода {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#getListeners(JepEventType eventType)}
   * объекта {@link com.technology.jep.jepria.client.widget.list.ListManager#observable} .
   *
   * @param eventType тип события
   *
   * @return список слушателей
   */
  public List<JepListener> getListeners(JepEventType eventType) {
    return observable.getListeners(eventType);
  }
  
}
