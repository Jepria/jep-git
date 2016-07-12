package com.technology.jep.jepria.client.ui.form.detail;

import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.widget.field.FieldManager;

/**
 * Абстрактная детальная форма.<br/>
 * Содержит общий функционал детальных форм.
 */
public abstract class DetailFormViewImpl implements DetailFormView {
  
  /**
   * Презентер детальной формы.
   */
  protected JepPresenter presenter;

  /**
   * Компонент, который является визуальным представлением детальной формы.<br/>
   * Чаще всего, компонент является контейнером для других компонентов детальной формы (полей etc.).
   */
  protected Widget widget;
  
  /**
   * Управляющий полями класс по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
   * При необходимости, дополнительные управляющие классы используются уже на прикладном уровне.
   */
  protected FieldManager fields;

  public DetailFormViewImpl(FieldManager fields) {
    this.fields = fields;
  }
  
  /**
   * Установка презентера детальной формы.
   *
   * @param presenter презентер детальной формы.
   */
  @Deprecated
  public void setPresenter(JepPresenter presenter) {
    this.presenter = presenter;
  }

  /**
   * Получение представления детальной формы как визуального компонента.
   * 
   * @return визуальный компонент детальной формы.
   */
  public Widget asWidget() {
    return widget;
  }
  
  /**
   * Установка визуального компонента детальной формы.
   *
   * @param widget визуальный компонент детальной формы.
   */
  public void setWidget(Widget widget) {
    this.widget = widget;
  }

  /**
   * Установка управляющего полями формы класса.
   *
   * @param fields устанавливается управляющий класс по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
   * При необходимости, дополнительные управляющие классы выставляются уже на прикладном уровне.
   */
  public void setFieldManager(FieldManager fields) {
    this.fields = fields;
  }

  /**
   * Получение управляющего полями формы класса.
   *
   * @return получение управляющего класса по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
   * При необходимости, дополнительные управляющие классы получаются уже на прикладном уровне.
   */
  public FieldManager getFieldManager() {
    return fields;
  }
  
}
