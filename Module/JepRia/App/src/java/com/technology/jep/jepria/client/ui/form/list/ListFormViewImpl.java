package com.technology.jep.jepria.client.ui.form.list;

import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.widget.list.GridManager;

/**
 * Списочная форма.<br/>
 * Содержит общий функционал списочных форм.
 */
public class ListFormViewImpl<L extends GridManager> implements ListFormView<L> {
  
  /**
   * Презентер списочной формы.
   */
  protected JepPresenter<?, ?> presenter;

  /**
   * Компонент, который размещается в центральной (рабочей) области списочной формы.<br/>
   * Чаще всего, компонент является контейнером для других компонентов списочной формы (списка etc.).
   */
  protected Widget widget;
  
  /**
   * Управляющий списком класс по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
   * При необходимости, дополнительные управляющие классы используются уже на прикладном уровне.
   */
  protected L list;

  public ListFormViewImpl(L list) {
    this.list = list;
  }
  
  /**
   * Установка презентера списочной формы.
   *
   * @param presenter презентер списочной формы.
   */
  @Override
  @Deprecated
  public void setPresenter(JepPresenter presenter) {
    this.presenter = presenter;
  }

  /**
   * Получение представления списочной формы как визуального компонента.
   * 
   * @return визуальный компонент списочной формы.
   */
  @Override
  public Widget asWidget() {
    return widget;
  }
  
  /**
   * Установка центрального компонента (рабочей панели) списочной формы.
   *
   * @param widget центральный компонент (рабочая панель) списочной формы.
   */
  @Override
  public void setWidget(Widget widget) {
    this.widget = widget;
  }
  
  /**
   * Установка управляющего списком формы класса.
   *
   * @param list устанавливается управляющий класс по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
   * При необходимости, дополнительные управляющие классы выставляются уже на прикладном уровне.
   */
  @Override
  public void setListManager(L list) {
    this.list = list;
  }

  /**
   * Получение управляющего списком формы класса.
   *
   * @return получение управляющего класса по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
   * При необходимости, дополнительные управляющие классы получаются уже на прикладном уровне.
   */
  @Override
  public L getListManager() {
    return list;
  }

}
