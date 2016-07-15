package com.technology.jep.jepria.client.widget.field;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_OPTION_VALUE_HTML_ATTR;
import static com.technology.jep.jepria.client.JepRiaClientConstant.FIELD_DEFAULT_HEIGHT;
import static com.technology.jep.jepria.client.JepRiaClientConstant.FIELD_DEFAULT_WIDTH;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;
import com.technology.jep.jepria.client.widget.button.JepButton;
import com.technology.jep.jepria.shared.field.option.JepOption;

/**
 * Компонент редактирования для поля {@link com.technology.jep.jepria.client.widget.field.multistate.JepDualListField}.
 * Представляет собой два списка. Левый список содержит список опций, правый - список выбранных
 * опций. Элементы перемещаются между списками либо с помощью расположенного между списками 
 * блока кнопок, либо двойным кликом.
 */
public class DualListBox extends Composite implements HasWidgets, HasValueChangeHandlers<List<JepOption>> {
  /**
   * Ширина центральной панели с кнопками.
   */
  public static final int BUTTON_PANEL_WIDTH = 18;
  
  /**
   * Наименование CSS-стиля для данного компонента.
   */
  private static final String DUAL_LIST_BOX_STYLE = "jepRia-DualListBox";
  
  /**
   * Идентификатор поля - случайное значение.<br>
   * Используется для формирования ключа опций.
   */
  private final int fieldId = Random.nextInt();
  
  /**
   * Счётчик опций поля.<br>
   * Используется для формирования ключа опций.
   */
  private int inc = 0;
  
  /**
   * Панель, на которой располагаются компоненты виджета.
   */
  private final HorizontalPanel panel = new HorizontalPanel();
  
  /**
   * Левый список опций.
   */
  private final ListBox left = new ListBox();
  
  /**
   * Правый список опций.
   */
  private final ListBox right = new ListBox();  
  
  /**
   * Кнопка переноса элемента из левого списка в правый.
   */
  private final JepButton moveRight = new JepButton("", null, JepImages.right());//ID кнопке присваивается в {@link #setCompositeWebIds(String)}
  
  /**
   * Кнопка переноса элемента из правого списка в левый.
   */
  private final JepButton moveLeft = new JepButton("", null, JepImages.left());//ID кнопке присваивается в {@link #setCompositeWebIds(String)}
  
  /**
   * Кнопка переноса всех элементов в правый список.
   */
  private final JepButton moveAllRight = new JepButton("", null, JepImages.doubleRight());//ID кнопке присваивается в {@link #setCompositeWebIds(String)}
  
  /**
   * Кнопка переноса всех элементов в левый список.
   */
  private final JepButton moveAllLeft = new JepButton("", null, JepImages.doubleLeft());//ID кнопке присваивается в {@link #setCompositeWebIds(String)}

  /**
   * Компаратор, сравнивающий опции при добавлении элементов в левый и правый список.
   * По умолчанию сравнение осуществляется по имени опции без учёта регистра.
   */
  private Comparator<JepOption> optionComparator = new Comparator<JepOption>() {
    @Override
    public int compare(JepOption o1, JepOption o2) {
      return o1.getName().compareToIgnoreCase(o2.getName());
    }
  };
  
  /**
   * Текущий список опций.
   */
  private List<JepOption> options = new ArrayList<JepOption>();
  
  /**
   * Список выбранных опций (значение поля).
   */
  private List<JepOption> value = new ArrayList<JepOption>();
  
  /**
   * Хэш таблица ключ-опция.
   */
  private Map<String, JepOption> idToOption = new HashMap<String, JepOption>();
  
  /**
   * Хэш-таблица опция-ключ.
   */
  private Map<JepOption, String> optionToId = new HashMap<JepOption, String>();
  
  /**
   * ID объемлющего Jep-поля как Web-элемента.
   */
  private final String fieldIdAsWebEl;
  
  @Deprecated
  public DualListBox() {
    this("");
  }
  
  public DualListBox(String fieldIdAsWebEl) {
    this.fieldIdAsWebEl = fieldIdAsWebEl;
    
    left.setMultipleSelect(true);
    right.setMultipleSelect(true);
    
    panel.add(left);
    VerticalPanel buttonPanel = new VerticalPanel();
    buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
    buttonPanel.add(moveAllRight);
    buttonPanel.add(moveRight);
    buttonPanel.add(moveLeft);
    buttonPanel.add(moveAllLeft);
    
    panel.add(buttonPanel);
    panel.add(right);
    
    moveRight.addClickHandler(new ClickHandler(){
      @Override
      public void onClick(ClickEvent event) {
        onMoveRight();
      }});
    
    moveAllRight.addClickHandler(new ClickHandler(){
      @Override
      public void onClick(ClickEvent event) {
        onMoveAllRight();
      }});
    
    moveLeft.addClickHandler(new ClickHandler(){
      @Override
      public void onClick(ClickEvent event) {
        onMoveLeft();
      }});
    
    moveAllLeft.addClickHandler(new ClickHandler(){
      @Override
      public void onClick(ClickEvent event) {
        onMoveAllLeft();
      }});
    left.addDoubleClickHandler(new DoubleClickHandler(){
      @Override
      public void onDoubleClick(DoubleClickEvent event) {
        onMoveRight();
      }});
    right.addDoubleClickHandler(new DoubleClickHandler(){
      @Override
      public void onDoubleClick(DoubleClickEvent event) {
        onMoveLeft();
      }});
    panel.addStyleName(DUAL_LIST_BOX_STYLE);
    left.addStyleName(MAIN_FONT_STYLE);
    right.addStyleName(MAIN_FONT_STYLE);
    initWidget(panel);
    setListBoxWidth(FIELD_DEFAULT_WIDTH + Unit.PX.getType());
    setHeight(10 * (FIELD_DEFAULT_HEIGHT + 2) + Unit.PX.getType());
  }
  
  /**
   * Установка ID внутренних компонентов DualListBox: правого списка как INPUT и кнопок перемещения опций между двумя списками.
   * @param fieldIdAsWebEl ID JepDualListField'а, который берется за основу ID внутренних компонентов
   */
  public void setCompositeWebIds(String fieldIdAsWebEl) {
    // Правой части присваивается INPUT_POSTFIX, а не RIGHTPART_POSTFIX потому что:
    // 1) удобнее из общего INPUT брать значение поля
    // 2) enability Jep-полей определяется в общем случае по INPUT
    right.getElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_FIELD_INPUT_POSTFIX);
    left.getElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_DUAL_LIST_FIELD_LEFTPART_POSTFIX);
    moveRight.getElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_DUAL_LIST_FIELD_MOVERIGHT_BTN_POSTFIX);
    moveLeft.getElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_DUAL_LIST_FIELD_MOVELEFT_BTN_POSTFIX);
    moveAllRight.getElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_DUAL_LIST_FIELD_MOVEALLRIGHT_BTN_POSTFIX);
    moveAllLeft.getElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_DUAL_LIST_FIELD_MOVEALLLEFT_BTN_POSTFIX);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add(Widget w) {
    panel.add(w);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandlerRegistration addValueChangeHandler(
      ValueChangeHandler<List<JepOption>> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  /**
   * Метод не поддерживается.
   */
  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

  /**
   * Формирование всплывающей подсказки для опции.<br>
   * По умолчанию в качестве всплывающей подсказки используется имя.
   * Если необходимо иначе сформировать подсказку, данный метод
   * требуется переопределить в классе-наследнике.
   * @param option опция
   * @return всплывающая подсказка
   */
  public String getItemTitle(JepOption option) {
    return option.getName();
  }
  
  /**
   * Получение списка выбранных опций (значения).
   * @return список выбранных опций
   */
  public List<JepOption> getValue() {
    return value;
  }
  
  /**
   * Установка доступности или недоступности компонента.
   * 
   * @param enabled true - компонент доступен, false - компонент заблокирован
   */  
  public void setEnabled(boolean enabled) {
    left.setEnabled(enabled);
    moveLeft.setEnabled(enabled);
    moveRight.setEnabled(enabled);
    moveAllLeft.setEnabled(enabled);
    moveAllRight.setEnabled(enabled);
    right.setEnabled(enabled);
  }
  
  /**
   * Установка высоты компонента.<br>
   */
  public void setHeight(String height) {
    panel.setHeight(height);  
  }
  
  /**
   * Метод не поддерживается.
   */
  @Override
  public Iterator<Widget> iterator() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean remove(Widget w) {
    return panel.remove(w);
  }
  
  /**
   * Установка ширины списков.<br>
   * Ширина для обоих списков устанавливается одинаковая.
   * @param width ширина
   */
  public void setListBoxWidth(String width) {
    left.setWidth(width);
    right.setWidth(width);
  }
  
  /**
   * Установка компаратора, сравнивающего добавляемые в списки элементы.
   * @param comparator
   */
  public void setOptionComparator(Comparator<JepOption> comparator) {
    this.optionComparator = comparator;
  }
  
  /**
   * Установка списка доступных опций.<br>
   * Если среди выбранных опций (в значении поля) присутствуют элементы,
   * отсутствующие в заданном списке, то они также добавляются.
   * @param optionList список опций
   */
  public void setOptions(List<JepOption> optionList) {
    List<JepOption> newOptions = new ArrayList<JepOption>();
    for (JepOption option : optionList) {
      newOptions.add(option);
      if (optionToId.get(option) == null) {
        String key = getKey();
        optionToId.put(option, key);
        idToOption.put(key, option);        
      }
    }
    for (JepOption option : value) {
      if (!newOptions.contains(option)) {
        newOptions.add(option);
      }
    }
    options = newOptions;
    left.clear();
    for (JepOption option : options) {
      if (!value.contains(option)) {
        addItem(left, option, optionToId.get(option));
      }
    }
  }
  
  /**
   * Установка списка выбранных опций (значения поля).<br>
   * Если переданный список содержит значения, не присутствующие
   * в списке опций компонента, то их копии добавляются в список
   * доступных опций.
   * @param value список выбранных опций
   */
  public void setValue(List<JepOption> value) {
    left.clear();
    right.clear();
    
    for (JepOption valueItem : value) {
      if (!options.contains(valueItem)) {
        options.add(valueItem);
        String key = getKey();
        optionToId.put(valueItem, key);
        idToOption.put(key, valueItem);  
      }
    }
    
    for (JepOption option : options) {
      if (value.contains(option)) {
        addItem(right, option, optionToId.get(option));
      }      
      else {
        addItem(left, option, optionToId.get(option));
      }
    }
    
    this.value = value;
  }
  
  /**
   * Служебный метод, добавляющий элемент в список и устанавливающий для него подсказку (title) и ID.<br>
   * При добавлении элементы сортируются с использованием поля {@link #optionComparator}.
   * @param listBox список
   * @param item добавляемый элемент
   * @param value значение элемента
   */
  private void addItem(final ListBox listBox, final JepOption item, final String value) {
    SelectElement selectElement = SelectElement.as(listBox.getElement());
    NodeList<OptionElement> options = selectElement.getOptions();
    int index = 0;
    String name = item.getName();    
    for (int i = 0; i < options.getLength(); i++) {
      JepOption currentListOption = idToOption.get(options.getItem(i).getValue());
      if (optionComparator.compare(item, currentListOption) < 0) {
        break;
      }
      else {
        index++;
      }
    }
    listBox.insertItem(name, value, index);
    
    // Установка подсказки (title) элемента и его ID
    for (int i = 0; i < options.getLength(); i++) {
      OptionElement optionElement = options.getItem(i);
      if (value.equals(optionElement.getValue())) {
        optionElement.setTitle(getItemTitle(item));
        if (fieldIdAsWebEl != null) {
          optionElement.setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_DUAL_LIST_FIELD_MENU_ITEM_INFIX + name);
        }
        optionElement.setAttribute(JEP_OPTION_VALUE_HTML_ATTR, name);
        return;
      }
    }
  }

  /**
   * Служебный метод, позволяющий получить список содержащихся в списке опций.
   * @param listBox список
   * @return список опций
   */
  private List<JepOption> getItems(ListBox listBox) {
    List<JepOption> items = new ArrayList<JepOption>();
    for (int i = 0; i < listBox.getItemCount(); i++) {
      items.add(idToOption.get(listBox.getValue(i)));
    }
    return items;
  }
  
  /**
   * Генерирует ключ для опции.
   * @return ключ
   */
  private String getKey() {
    return "dualListBox_" + fieldId + "_option_" + inc++;
  }

  /**
   * Служебный метод, позволяющий получить список выбранных опций в списке.
   * @param listBox список
   * @return список выбранных опций
   */
  private List<JepOption> getSelectedItems(ListBox listBox) {
    List<JepOption> selection = new ArrayList<JepOption>();
    for (int i = 0; i < listBox.getItemCount(); i++) {
      if (listBox.isItemSelected(i)) {
        selection.add(idToOption.get(listBox.getValue(i)));
      }
    }
    return selection;
  }
  
  /**
   * Перенос опции в левый список.<br>
   * Опция исключается из списка выбранных.
   * @param option опция, подлежащая переносу
   */
  private void moveLeft(JepOption option) {
    value.remove(option);
    String key = optionToId.get(option);
    removeByValue(right, key);
    addItem(left, option, key);
  }
  
  /**
   * Перенос опции в правый список.<br>
   * Опция добавляется в список выбранных.
   * @param option опция, подлежащая переносу
   */
  private void moveRight(JepOption option) {
    value.add(option);
    String key = optionToId.get(option);
    removeByValue(left, key);
    addItem(right, option, key);
  }

  /**
   * Действие по нажатию на кнопку переноса всех опций в правый список.<br>
   * Поочерёдно переносит все опции из левого списка в правый и генерирует событие
   * {@link com.google.gwt.event.logical.shared.ValueChangeEvent}.
   */
  private void onMoveAllRight() {
    for (JepOption option : getItems(left)) {
      moveRight(option);
    }
    ValueChangeEvent.fire(DualListBox.this, getValue());
  }

  /**
   * Действие по нажатию на кнопку переноса всех опций в левый список.<br>
   * Поочерёдно переносит все опции из правого списка в левый и генерирует событие
   * {@link com.google.gwt.event.logical.shared.ValueChangeEvent}.
   */
  private void onMoveAllLeft() {
    for (JepOption option : getItems(right)) {
      moveLeft(option);
    }
    ValueChangeEvent.fire(DualListBox.this, getValue());
  }


  /**
   * Действие по нажатию на кнопку переноса выбранных в левом списке опций в правый.<br>
   * Поочерёдно переносит выбранные опции из левого списка в правый и генерирует событие
   * {@link com.google.gwt.event.logical.shared.ValueChangeEvent}.
   */
  private void onMoveRight() {
    for (JepOption option : getSelectedItems(left)) {
      moveRight(option);
    }
    ValueChangeEvent.fire(DualListBox.this, getValue());
  }

  /**
   * Действие по нажатию на кнопку переноса выбранных в правом списке опций в левый.<br>
   * Поочерёдно переносит выбранные опции из правого списка в левый и генерирует событие
   * {@link com.google.gwt.event.logical.shared.ValueChangeEvent}.
   */
  private void onMoveLeft() {
    for (JepOption option : getSelectedItems(right)) {
      moveLeft(option);
    }
    ValueChangeEvent.fire(DualListBox.this, getValue());
  }

  /**
   * Служебный метод, удаляющий элемент из компонента {@link ListBox} по его значению (value).
   * @param listBox компонент
   * @param value значение элемента
   */
  private static void removeByValue(ListBox listBox, String value) {
    for (int i = 0; i < listBox.getItemCount(); i++) {
      if (listBox.getValue(i).equals(value)) {
        listBox.removeItem(i);
        break;
      }
    }
  }
}
