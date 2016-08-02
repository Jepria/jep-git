package com.technology.jep.jepria.client.widget.list.header.menu;

import static com.technology.jep.jepria.client.JepRiaClientConstant.DND_DATA_PROPERTY;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;

import com.google.gwt.dom.client.DataTransfer;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragEvent;
import com.google.gwt.event.dom.client.DragHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.dom.client.HasAllDragAndDropHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.MenuItem;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.list.JepGrid;
import com.technology.jep.jepria.client.widget.list.header.ResizableHeader.ActionEnum;

@SuppressWarnings("rawtypes")
public class CheckMenuItem extends MenuItem implements HasAllDragAndDropHandlers, EventListener, HasClickHandlers {
  
  private String id;
  private String menuTitle;
  
  private InputElement checkBoxElement = InputElement.as(DOM.createInputCheck());
  
  private ActionEnum action = ActionEnum.NORMAL;
  private boolean clicked = false;
  
  private JepGrid cellTable;
  
  /**
   * Creates a new check menu item.
   * 
   * @param text the text
   */
  public CheckMenuItem(String text, String id, JepGrid table) {
    super(SafeHtmlUtils.fromString(text));
    this.id = id;
    this.menuTitle = text;
    this.cellTable = table;
    
    getElement().setId(text + JepRiaAutomationConstant.GRID_HEADER_POPUP_MENU_ITEM_POSTFIX);
    getElement().setDraggable(Element.DRAGGABLE_TRUE);
    getElement().addClassName(MAIN_FONT_STYLE);
    getElement().getStyle().setFontSize(11, Unit.PX);

    addDragStartHandler(new DragStartHandler() {
      @Override
      public void onDragStart(DragStartEvent event) {
        ((GridHeaderMenuBar) getParentMenu()).setSelected(CheckMenuItem.this);
        //Required: set data for the event
        //Without that DND doesn't work in FF
        DataTransfer dataTransfer = event.getDataTransfer();
        dataTransfer.setData(DND_DATA_PROPERTY, "" + event.getSource());
        // optional: show copy of the image
        dataTransfer.setDragImage(getElement(), 10, 10);
      }
    });
    // required: you must add dragoverhandler to create a target
    addDragOverHandler(new DragOverHandler() {
      @Override
      public void onDragOver(DragOverEvent event) {
        Style style = getElement().getStyle();
        style.setBackgroundColor("#ffa");
      }
    });
    
    addDragLeaveHandler(new DragLeaveHandler() {
      @Override
      public void onDragLeave(DragLeaveEvent event) {
        Style style = getElement().getStyle();
        style.clearBackgroundColor();
      }
    });
    
    // add drop handler
    addDropHandler(new DropHandler() {
      @Override
      public void onDrop(DropEvent event) {
        // prevent the native text drop
        // the browser might navigate away from the current page
        event.preventDefault();
        
        GridHeaderMenuBar menuBar = (GridHeaderMenuBar) getParentMenu();
        CheckMenuItem lastSelected = menuBar.getSelectedItem();
        menuBar.setSelected(CheckMenuItem.this);
        menuBar.changeMenuItems(CheckMenuItem.this, lastSelected);
        menuBar.setSelected(lastSelected);
        
        Style style = getElement().getStyle();
        style.clearBackgroundColor();
      }        
    });
    
    checkBoxElement.setChecked(cellTable.isColumnVisible(id));
    Event.sinkEvents(checkBoxElement, Event.ONCLICK);
    Event.setEventListener(checkBoxElement, new EventListener() {
          @Override
          public void onBrowserEvent(Event event) {
               switch(event.getTypeInt()) {
                 case Event.ONCLICK: { 
                   action = ActionEnum.CHECK;
                   break;
                 }
               }
          }
      });
    getElement().insertFirst(checkBoxElement);
    
    setEnabled(!(cellTable.getColumnCount() == 2 && checkBoxElement.isChecked()));
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    checkBoxElement.setDisabled(!enabled);
    Style style = getElement().getStyle();
    style.setOpacity(enabled ? 1 : 0.6);
    checkBoxElement.getStyle().setCursor(enabled ? Cursor.POINTER : Cursor.DEFAULT);
  }

  public boolean isChecked() {
    return checkBoxElement.isChecked();
  }
  
  public String getId(){
    return id;
  }
  
  public String getMenuTitle(){
    return menuTitle;
  }
  
  public ActionEnum getAction(){
    return action;
  }
  
  public void setAction(ActionEnum act) {
    this.action = act;
  }
  
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  public void setChecked(boolean checked) {
    checkBoxElement.setChecked(checked);
  }
  
  public void toggle(){
    setChecked(!isChecked());
  }

  private HandlerManager handlerManager;

  protected HandlerManager createHandlerManager() {
    return new HandlerManager(this);
  }

  /**
   * Ensures the existence of the handler manager.
   *
   * @return the handler manager
   * */
  HandlerManager ensureHandlers() {
    return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
  }

  public final <H extends EventHandler> HandlerRegistration addBitlessDomHandler(
      final H handler, DomEvent.Type<H> type) {
    assert handler != null : "handler must not be null";
    assert type != null : "type must not be null";
    sinkBitlessEvent(type.getName());
    return ensureHandlers().addHandler(type, handler);
  }
  
  public final <H extends EventHandler> HandlerRegistration addDomHandler(
        final H handler, DomEvent.Type<H> type) {
      assert handler != null : "handler must not be null";
      assert type != null : "type must not be null";
      int typeInt = Event.getTypeInt(type.getName());
      if (typeInt == -1) {
        sinkBitlessEvent(type.getName());
      } else {
        sinkEvents(typeInt);
      }
      return ensureHandlers().addHandler(type, handler);
  }

  @Override
  public HandlerRegistration addDragEndHandler(DragEndHandler handler) {
    return addBitlessDomHandler(handler, DragEndEvent.getType());
  }

  @Override
  public HandlerRegistration addDragEnterHandler(DragEnterHandler handler) {
    return addBitlessDomHandler(handler, DragEnterEvent.getType());
  }

  @Override
  public HandlerRegistration addDragHandler(DragHandler handler) {
    return addBitlessDomHandler(handler, DragEvent.getType());
  }

  @Override
  public HandlerRegistration addDragLeaveHandler(DragLeaveHandler handler) {
    return addBitlessDomHandler(handler, DragLeaveEvent.getType());
  }

  @Override
  public HandlerRegistration addDragOverHandler(DragOverHandler handler) {
    return addBitlessDomHandler(handler, DragOverEvent.getType());
  }

  @Override
  public HandlerRegistration addDragStartHandler(DragStartHandler handler) {
    return addBitlessDomHandler(handler, DragStartEvent.getType());
  }

  @Override
  public HandlerRegistration addDropHandler(DropHandler handler) {
    return addBitlessDomHandler(handler, DropEvent.getType());
  }

  @Override
  public void fireEvent(GwtEvent<?> event) {
    if (handlerManager != null) {
      handlerManager.fireEvent(event);
    }
  }

  @Override
  public void onBrowserEvent(Event event) {
    switch (DOM.eventGetType(event)) {
      case Event.ONMOUSEOVER:
        // Only fire the mouse over event if it's coming from outside this widget.
      case Event.ONMOUSEOUT:
        // Only fire the mouse out event if it's leaving this widget.
        Element related = event.getRelatedEventTarget().cast();
        if (related != null && getElement().isOrHasChild(related)) {
          return;
        }
        break;
      case Event.ONCLICK:
        event.stopPropagation();
        break;
    }
    DomEvent.fireNativeEvent(event, this, this.getElement());
  }
  
  public void releaseHandlers(){
    Event.setEventListener(checkBoxElement, null);
  }

  public boolean isClicked() {
    return clicked;
  }

  public void setClicked(boolean clicked) {
    this.clicked = clicked;
  }
  
  @Override
    protected void setSelectionStyle(boolean selected) {
      super.setSelectionStyle(selected);
      
      if (selected){
        // show selected element
        JepClientUtil.adjustToTop(getElement());
      }
    }
}
