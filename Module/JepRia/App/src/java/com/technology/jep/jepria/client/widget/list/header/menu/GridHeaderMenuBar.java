package com.technology.jep.jepria.client.widget.list.header.menu;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;
import com.technology.jep.jepria.client.widget.list.JepColumn;
import com.technology.jep.jepria.client.widget.list.header.ResizableHeader;
import com.technology.jep.jepria.client.widget.list.header.ResizableHeader.ActionEnum;
import com.technology.jep.jepria.client.widget.list.header.menu.images.GridMenuImages;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

@SuppressWarnings({"unchecked", "rawtypes"})
public class GridHeaderMenuBar extends MenuBar {
  
  protected static String RESIZABLE_HEADER_MENU_BAR_STYLE = "jepRia-ResizableHeader-menuBar";
  protected static String RESIZABLE_HEADER_NAVIGATION_BUTTON_STYLE = "jepRia-ResizableHeader-navigationButton";
  protected static String RESIZABLE_HEADER_CLOSE_BUTTON_STYLE = "jepRia-ResizableHeader-closeButton";
  
  private ResizableHeader header;
  private Element upElement, downElement, closeElement;
  
  public GridHeaderMenuBar(ResizableHeader header) {
    super(true);
    this.header = header;
    // customize for our purposes
    setFocusOnHoverEnabled(false);
    getElement().addClassName(RESIZABLE_HEADER_MENU_BAR_STYLE);
  }
  
  public CheckMenuItem getSelectedItem(){
    return (CheckMenuItem) super.getSelectedItem();
  }
  
  @Override
  public void selectItem(MenuItem item) {
    if (!JepRiaUtil.isEmpty(item) && ((CheckMenuItem) item).isClicked()){
      super.selectItem(item);
      
      int indexItem = getItems().indexOf(item);
      boolean isFirstItem = indexItem == 0; 
      Style style = this.upElement.getStyle();
      style.setCursor(isFirstItem ? Cursor.DEFAULT : Cursor.POINTER);
      
      boolean isLastItem = indexItem == getMenuCount() - 1; 
      style = this.downElement.getStyle();
      style.setCursor(isLastItem ? Cursor.DEFAULT : Cursor.POINTER);
    }
  }
  
  public int getMenuCount(){
    return getItems().size();
  }
  
  public void setSelected(CheckMenuItem check){
    if (!JepRiaUtil.isEmpty(getSelectedItem()) && !getSelectedItem().equals(check)){
      getSelectedItem().setClicked(false);
    }
    check.setClicked(true);
    selectItem(check);
  }
  
  public void populateItems(final List<JepColumn> columns) {
    clearItems();
          
    for (final JepColumn currentColumn : columns) {
      final String headerText = currentColumn.getHeaderText();
      final CheckMenuItem check = new CheckMenuItem(headerText, currentColumn.getFieldName(), header.getGrid());
      check.addClickHandler(new ClickHandler(){
        @Override
        public void onClick(ClickEvent event) {
          ActionEnum action = check.getAction(); 
          check.setAction(ActionEnum.NORMAL);
          if (!check.isEnabled()) return;
          
          switch (action){
            case NAV_DOWN : {
              List<MenuItem> items = getItems();
              Integer currentIndex = items.indexOf(check);
              if (currentIndex < items.size() - 1){
                CheckMenuItem other = (CheckMenuItem) items.get(currentIndex + 1);
                setSelected(other);
                changeMenuItems(check, other);
                setSelected(check);
              }
              break;
            }
            case NAV_UP : {
              List<MenuItem> items = getItems();
              Integer currentIndex = items.indexOf(check);
              if (currentIndex < items.size() && currentIndex > 0){
                CheckMenuItem other = (CheckMenuItem) items.get(currentIndex - 1);
                setSelected(other);
                changeMenuItems(check, other);
                setSelected(check);
              }
              break;
            }
            case CHECK : {
              setSelected(check);  
              toggleColumn(currentColumn);
              
              for (MenuItem menuItem : getItems()){
                // Если осталась последняя колонка таблицы + 1 вспомогательная
                if (header.getGrid().getColumnCount() == 2){
                  if (((CheckMenuItem) menuItem).isChecked()){
                    menuItem.setEnabled(false);
                  }
                }
                else {
                  if (!menuItem.isEnabled()){
                    menuItem.setEnabled(true);
                  }
                }
              }
              break;
            }
            case NORMAL:
            default: {
              setSelected(check);  
              break;
            }
          }
        }
        });
        addItem(check);
    }
  }
  
  public void changeMenuItems(CheckMenuItem firstItem, CheckMenuItem secondItem) {
    List<JepColumn> columns = header.getGrid().getColumns();
    
    int indexForFirst = header.getGrid().getIndexColumnById(firstItem.getId()),
        indexForSecond = header.getGrid().getIndexColumnById(secondItem.getId());
    
    JepColumn first = columns.get(indexForFirst),
        second = columns.get(indexForSecond);
    
    columns.set(indexForFirst, second);
    columns.set(indexForSecond, first);
    
    removeItem(firstItem);
    insertItem(firstItem, indexForSecond);
    
    header.getGrid().toggleColumn(first);
    header.getGrid().toggleColumn(first);
    
    removeItem(secondItem);
    insertItem(secondItem, indexForFirst);
    
    header.getGrid().toggleColumn(second);
    header.getGrid().toggleColumn(second);
    // Сохраним изменения порядка столбцов в Cookie
    header.getGrid().columnCharacteristicsChanged();
  }
  
  @Override
  public MenuItem addItem(MenuItem item) {
    if (item instanceof CheckMenuItem){
      DOM.setEventListener(item.getElement(), (CheckMenuItem) item);
    }
    return super.addItem(item);
  }
  
  @Override
  public void clearItems() {
    for (MenuItem menuItem : getItems()){
      ((CheckMenuItem) menuItem).releaseHandlers();
      DOM.setEventListener(menuItem.getElement(), null);
    }
    super.clearItems();
  }
  
  @Override
  public void onLoad(){
    super.onLoad();
    
    this.closeElement = addCloseButton();
    this.upElement = addNavigationButton(true);
    this.downElement = addNavigationButton(false);
    
    // из-за абсолютного позиционирования кнопок вверх и вниз вынуждены увеличивать ширину меню
    setWidth((getElement().getOffsetWidth() + upElement.getOffsetWidth() + downElement.getOffsetWidth()) + Unit.PX.getType());
  }

  public Element addCloseButton() {
    Element element = DOM.createDiv();
    element.setId(JepRiaAutomationConstant.GRID_HEADER_POPUP_CLOSE_ID);
    element.addClassName(RESIZABLE_HEADER_CLOSE_BUTTON_STYLE);
    element.setInnerHTML("x");
    getElement().getParentElement().appendChild(element);
    
    Event.sinkEvents(element, Event.ONCLICK);
    Event.setEventListener(element, new EventListener() {
          @Override
          public void onBrowserEvent(Event event) {
               switch(event.getTypeInt()) {
                 case Event.ONCLICK: { 
                   header.hideHeaderMenulfOpen();
                   break;
                 }
               }
          }
      });
    return element;
  }

  public Element addNavigationButton(final boolean isUp) {
    final Element container = DOM.createDiv();
    container.setId(isUp? JepRiaAutomationConstant.GRID_HEADER_POPUP_NAVIG_UP_ID : JepRiaAutomationConstant.GRID_HEADER_POPUP_NAVIG_DOWN_ID);
    container.addClassName(RESIZABLE_HEADER_NAVIGATION_BUTTON_STYLE);
    Image button = new Image(isUp ? GridMenuImages.instance.upButton() : GridMenuImages.instance.downButton());
    Style style = container.getStyle();
    style.setProperty("background", "url(" + button.getUrl() + ") #f0f0f0 center center no-repeat");
    style.setHeight(header.getPopupHeight(), Unit.PX);
    boolean scrollable = header.isScrollable(getMenuCount());
    // эмпирически высчитаны отступы справа со скроллом и без него
    style.setRight(isUp ? (scrollable ? 28 : 11) : (scrollable ? 45 : 28), Unit.PX);
    getElement().getParentElement().appendChild(container);
    
    Event.sinkEvents(container, Event.ONCLICK);
    Event.setEventListener(container, new EventListener() {
          @Override
          public void onBrowserEvent(Event event) {
               switch(event.getTypeInt()) {
                 case Event.ONCLICK: { 
                   CheckMenuItem currentSelected = getSelectedItem();
                   if (!JepRiaUtil.isEmpty(currentSelected)){
                     currentSelected.setAction(isUp ? ActionEnum.NAV_UP : ActionEnum.NAV_DOWN);
                     currentSelected.fireEvent(new ClickEvent() { });
                   }
                   break;
                 }
               }
          }
      });
    return container;
  }
  
  @Override
  public void onUnload(){
    clearItems();
    
    Event.setEventListener(upElement, null);
    Event.setEventListener(downElement, null);
    Event.setEventListener(closeElement, null);
    
    super.onUnload();
  }
  
  private void toggleColumn(JepColumn currentColumn){
    header.hideButton();
    header.getGrid().toggleColumn(currentColumn);
    header.getGrid().columnCharacteristicsChanged();
  }
}
