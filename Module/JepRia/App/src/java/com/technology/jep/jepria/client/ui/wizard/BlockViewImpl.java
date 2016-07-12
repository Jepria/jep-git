package com.technology.jep.jepria.client.ui.wizard;

import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.widget.field.FieldManager;

public class BlockViewImpl implements BlockView {

  private Block widget; 
  
  /**
   * Управляющий полями класс по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
   * При необходимости, дополнительные управляющие классы используются уже на прикладном уровне.
   */
  protected FieldManager fields;

  public BlockViewImpl() {
    this(new FieldManager());
  }
  
  public BlockViewImpl(FieldManager fields) {
    this.fields = fields;
  }
  
  public Block asWidget() {
    return widget;
  }
  
  /**
   * Установка визуального компонента блока визарда.
   *
   * @param widget визуальный компонент блока визарда.
   */
  public void setWidget(Widget widget) {
    this.widget = new Block(widget);
  }
  
  /**
   * Установка визуального компонента блока визарда.
   *
   * @param widget визуальный компонент блока визарда.
   * @param position положение 
   */
  public void setWidget(Widget widget, BlockPositionEnum position) {
    this.widget = new Block(widget, position);
  }
  
  public void setFieldManager(FieldManager fieldManager) {
    this.fields = fieldManager;
  }

  public FieldManager getFieldManager() {
    return fields;
  }
}
