package com.technology.jep.jepria.client.widget.list.header;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.AnimationType;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.list.JepColumn;
import com.technology.jep.jepria.client.widget.list.JepGrid;
import com.technology.jep.jepria.client.widget.list.header.menu.GridHeaderMenuBar;
import com.technology.jep.jepria.client.widget.list.header.menu.images.GridMenuImages;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

@SuppressWarnings("rawtypes")
public class ResizableHeader<T> extends Header<String> {

  private static final int MIN_COLUMN_WIDTH = 50;
  
  protected static String RESIZABLE_HEADER_RULER_STYLE = "jepRia-ResizableHeader-ruler";
  protected static String RESIZABLE_HEADER_SETUP_BUTTON_STYLE = "jepRia-ResizableHeader-setupButton";
  protected static String RESIZABLE_HEADER_MEASURING_ELEMENT_STYLE = "jepRia-ResizableHeader-measuringElement";
  protected static String RESIZABLE_HEADER_MENU_STYLE = "jepRia-ResizableHeader-menu";
  
  private Column<T, ?> column = null;
  private JepGrid<T> cellTable;
  
  private String title = "";
  private static final int minWidth = 20;
  private static final int grabWidth = 7;
  
  /**
   * Значение высоты опций выпадающего списка.
   */
  private static final int MENU_ITEM_HEIGHT = 27;
  
  private Element setupButton;
  private PopupPanel panel;
  private int popupHeight;
  
  private final Image menuImage = new Image(GridMenuImages.instance.setting());
  
  private int limit = 10;
  
  private boolean isConfigurable; 
  
  public ResizableHeader(String title, JepGrid<T> cellTable, Column<T, ?> column, boolean isConfigurable) {
    super(new HeaderCell());
    this.title = title;
    this.cellTable = cellTable;
    this.column = column;
    this.isConfigurable = isConfigurable;
  }

  @Override
  public String getValue() {
    return title;
  }
  
  public JepGrid<T> getGrid() {
    return cellTable;
  }
  
  public int getPopupHeight() {
    return popupHeight;
  }

  @Override
  public void render(Context context, SafeHtmlBuilder sb) {

    ColumnSortList sortList = cellTable.getColumnSortList();
    ColumnSortInfo sortedInfo = (sortList.size() == 0) ? null : sortList.get(0);
    Column<?, ?> sortedColumn = (sortedInfo == null) ? null : sortedInfo.getColumn();
    boolean isSortAscending = (sortedInfo == null) ? false : sortedInfo.isAscending();

    if (column == sortedColumn) {
      if (isSortAscending)
        sb.append(SafeHtmlUtils.fromTrustedString("&#x25B2;&nbsp;"));
      else
        sb.append(SafeHtmlUtils.fromTrustedString("&#x25BC;&nbsp;"));
    }
    
    super.render(context, sb);
  }

  @Override
  public void onBrowserEvent(Context context, Element target, NativeEvent event) {
    String eventType = event.getType();
    if (BrowserEvents.MOUSEMOVE.equals(eventType)) {
      new ColumnResizeHelper<T>(cellTable, column, target);
    }
    else if (BrowserEvents.MOUSEOVER.equals(eventType)){
      target.setTitle(JepClientUtil.jsTrim(((JepColumn) column).getHeaderText()));
    }
  }

  private void setCursor(Element element, Cursor cursor) {
    element.getStyle().setCursor(cursor);
  }
  
  private void showButton(double left, final Element el) {
    hideButton();
    
    setupButton = menuImage.getElement();
    setupButton.addClassName(RESIZABLE_HEADER_SETUP_BUTTON_STYLE);
    cellTable.getElement().appendChild(setupButton);
    
    Style style = setupButton.getStyle();
    style.setLeft(left - setupButton.getOffsetWidth() - cellTable.getScrollLeft(), Unit.PX);
    
    Event.sinkEvents(setupButton, Event.ONCLICK | Event.ONMOUSEOUT);
    Event.setEventListener(setupButton, new EventListener() {
          @Override
          public void onBrowserEvent(Event event) {
               switch(event.getTypeInt()) {
                 case Event.ONCLICK: {
                   hideHeaderMenulfOpen();
                   getHeaderMenu().showRelativeTo(menuImage);
                   break;
                 }
                 case Event.ONMOUSEOUT: {
                   hideButton();
                   break;
                 }
               }
          }
      });
  }

  public void hideButton() {
    if (setupButton != null) {
      Event.setEventListener(setupButton, null);
      cellTable.getElement().removeChild(setupButton);
      setupButton = null;
    }
  }
  
  public void hideHeaderMenulfOpen() {
    if (getHeaderMenu().isShowing()) {
      getHeaderMenu().hide(true);
    }
  }
  
  protected PopupPanel getHeaderMenu() {
    if (panel == null) {
      final List<JepColumn> columns = cellTable.getColumns();
      panel = new DecoratedPopupPanel(true);
      panel.getElement().setId(JepRiaAutomationConstant.GRID_HEADER_POPUP_ID);
      panel.setGlassEnabled(true);
      panel.addStyleName(RESIZABLE_HEADER_MENU_STYLE);
      panel.setPreviewingAllNativeEvents(true);
      panel.setAnimationType(AnimationType.ROLL_DOWN);
      panel.addCloseHandler(new CloseHandler<PopupPanel>() {
        @Override
        public void onClose(CloseEvent<PopupPanel> event) {
          hideButton();
          panel = null;
        }
      });
      
      adjustPopupSize(columns.size());
      
      final GridHeaderMenuBar columnMenu = new GridHeaderMenuBar(this);
      columnMenu.populateItems(columns);
      panel.setWidget(columnMenu);
    }
    return panel;
  }

  public void adjustPopupSize(int columnCount) {
    NodeList<Element> nodes = panel.getElement().getElementsByTagName("div");
    this.popupHeight = columnCount * MENU_ITEM_HEIGHT;
    for (int i = 0; i < nodes.getLength(); i++){
      Element element = nodes.getItem(i);
      if (element.getClassName().contains("popupContent")){
        Style popupStyle = element.getStyle();
        boolean moreMenus = isScrollable(columnCount);
        if (moreMenus){
          popupStyle.setOverflowY(Overflow.SCROLL);
          popupStyle.setOverflowX(Overflow.HIDDEN);
          this.popupHeight = getLimit() * MENU_ITEM_HEIGHT;
        }
        else {
          popupStyle.clearOverflowY();
        }
        popupStyle.setHeight(this.popupHeight, Unit.PX);
      }
    }
  }
  
  public boolean isScrollable(int columnCount){
    return getLimit() < columnCount;
  }
  
  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  class ColumnResizeHelper<E> implements NativePreviewHandler {

    private HandlerRegistration handler;
    private JepGrid<E> table;
    private Column<E, ?> col;
    private Element el;
    private int grabOffset;
    private boolean mousedown;
    private boolean skipClick;
    private Element measuringElement;
    private Element ruler;

    private int newWidth;

    public ColumnResizeHelper(JepGrid<E> table, Column<E, ?> col, Element el) {
      this.el = el;
      this.table = table;
      this.col = col;

      handler = Event.addNativePreviewHandler(this);
    }

    private void blockEvent(NativePreviewEvent event) {
      event.cancel();
      event.getNativeEvent().preventDefault();
      event.getNativeEvent().stopPropagation();
    }    

    @SuppressWarnings("unchecked")
    @Override
    public void onPreviewNativeEvent(NativePreviewEvent event) {
      NativeEvent nativeEvent = event.getNativeEvent();
      String eventType = nativeEvent.getType();

      Element eventTargetEl = nativeEvent.getEventTarget().cast();
      int absoluteLeft = eventTargetEl.getAbsoluteLeft();
      int offsetLeft = eventTargetEl.getOffsetLeft();
      int absoluteTop = eventTargetEl.getAbsoluteTop();
      int tableLeft = table.getAbsoluteLeft();
      int offsetWidth = eventTargetEl.getOffsetWidth();

      int clientX = nativeEvent.getClientX();
      int clientY = nativeEvent.getClientY();
      boolean grabZone = clientX > absoluteLeft + offsetWidth - grabWidth;
      boolean buttonZone = !grabZone && clientX > absoluteLeft + offsetWidth - grabWidth - 10 && clientY > absoluteTop + 5
          && clientY < absoluteTop + 13;

      if (eventType.equals("dblclick") && grabZone) {
        blockEvent(event);

        double max = MIN_COLUMN_WIDTH;
        startMeasuring();
        for (E t : table.getVisibleItems()) {
          Object value = col.getValue(t);
          SafeHtmlBuilder sb = new SafeHtmlBuilder();
          Cell<Object> cell = (Cell<Object>) col.getCell();
          cell.render(null, value, sb);
          max = Math.max(measureText(sb.toSafeHtml().asString()), max);
        }

        finishMeasuring();
        table.setColumnWidth(col, (max + grabWidth) + Unit.PX.getType());
        table.columnCharacteristicsChanged();

        resetCursor(el); 
        removeHandler();
      }

      if (eventType.equals("mousemove")) {
        blockEvent(event);

        // в IE после !увеличения! ширины колонки срабатывает click и
        // происходит сортировка
        // блокируем обработку click сразу после mouseup (блокировка
        // отключается при mousemove)
        skipClick = false;

        if (mousedown) {
          newWidth = clientX - el.getAbsoluteLeft() + grabOffset;
          moveRuler(clientX + grabOffset - tableLeft);
        } else {
          // setting cursor
          if (grabZone) {
            setCursor(el, Cursor.COL_RESIZE);
          } else {
            resetCursor(el);
          }

          if (buttonZone && isConfigurable) {
            showButton(offsetLeft + offsetWidth, el);
          }
        }
      }

      if (eventType.equals("mouseout")) {
        blockEvent(event);

        if (mousedown) {
          //
        } else {
          removeHandler();
        }
      }

      if (eventType.equals("mousedown")) {
        if (grabZone) {
          blockEvent(event);

          mousedown = true;
          grabOffset = absoluteLeft + offsetWidth - clientX;

          newWidth = clientX - el.getAbsoluteLeft() + grabOffset;
          showRuler(clientX + grabOffset - tableLeft);
        }
      }

      if (eventType.equals("mouseup")) {

        if (mousedown) {
          blockEvent(event);

          mousedown = false;

          // в IE после !увеличения! ширины колонки срабатывает click
          // и происходит сортировка
          // блокируем обработку click сразу после mouseup (блокировка
          // отключается при mousemove)
          skipClick = true;

          newWidth = newWidth < minWidth ? minWidth : newWidth;

          table.setColumnWidth(col, newWidth + Unit.PX.getType());
          table.columnCharacteristicsChanged();

          hideRuler();
        }
      }
      if (eventType.equals("click")) {
        if (skipClick) {
          // в IE после !увеличения! ширины колонки срабатывает click
          // и происходит сортировка
          // блокируем обработку click сразу после mouseup (блокировка
          // отключается при mousemove)

          blockEvent(event);
          skipClick = false;
        } else {
          if (!grabZone) {
            blockEvent(event);

            ColumnSortList sortList = table.getColumnSortList();
            sortList.push(column);
            ColumnSortEvent.fire(table, sortList);
          }
        }
      }
    }

    private void showRuler(double left) {
      hideRuler();

      ruler = DOM.createDiv();
      ruler.addClassName(RESIZABLE_HEADER_RULER_STYLE);
      table.getElement().appendChild(ruler);
      Style style = ruler.getStyle();
      style.setLeft(left, Unit.PX);
      style.setHeight(table.getOffsetHeight(), Unit.PX);
    }

    private void moveRuler(double left) {
      if (ruler != null) {
        ruler.getStyle().setLeft(left, Unit.PX);
      }
    }

    private void hideRuler() {
      if (ruler != null) {
        table.getElement().removeChild(ruler);
        ruler = null;
      }
    }

    private void startMeasuring() {
      measuringElement = DOM.createDiv();
      measuringElement.addClassName(RESIZABLE_HEADER_MEASURING_ELEMENT_STYLE);
      table.getElement().appendChild(measuringElement);
    }

    private double measureText(String text) {
      measuringElement.setInnerHTML(text);
      return measuringElement.getOffsetWidth();
    }

    private void finishMeasuring() {
      table.getElement().removeChild(measuringElement);
    }

    private void resetCursor(Element elc) {
      if (!JepRiaUtil.isEmpty(column.getDataStoreName())){ // column.isSortable()
        setCursor(elc, Cursor.POINTER);
      }
      else {
        setCursor(elc, Cursor.DEFAULT);
      }
    }

    private void removeHandler() {
      handler.removeHandler();
      table.redrawHeaders();
    }

  }

  static class HeaderCell extends AbstractCell<String> {
    public HeaderCell() {
      super("click", "mousedown", "mousemove", "mouseover", "dblclick");
    }

    @Override
    public void render(Context context, String value, SafeHtmlBuilder sb) {
      sb.append(SafeHtmlUtils.fromString(value));
    }
  }
  
  public enum ActionEnum {NORMAL, CHECK, NAV_UP, NAV_DOWN};
};