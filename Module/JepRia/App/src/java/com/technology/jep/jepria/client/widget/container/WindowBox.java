package com.technology.jep.jepria.client.widget.container;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class WindowBox extends DialogBox implements HasOpenHandlers<WindowBox> {

  private static final int MIN_WIDTH = 100;
    private static final int MIN_HEIGHT = 100;
    
  private FlowPanel container;
  private FlowPanel controls;
  private Anchor close;
  private Anchor minimize;
  
  private int dragX;
  private int dragY;
  
  private int minWidth = MIN_WIDTH;
  private int minHeight = MIN_HEIGHT;
  
  private int dragMode;
  
  private boolean resizable;
  
  private boolean minimized;
  
  /**
   * Наименование селектора (класса стилей) всплывающего окна.
   */
  private static final String MAIN_WINDOW_BOX_STYLE = "jepRia-extras-WindowBox";
  
  /**
   * Наименование селектора (класса стилей) области сообщения всплывающего окна.
   */
  private static final String DIALOG_CONTAINER_WINDOW_BOX_STYLE = "jepRia-extras-dialog-container";
  
  /**
   * Наименование селектора (класса стилей) кнопки закрытия.
   */
  private static final String CLOSE_BUTTON_WINDOW_BOX_STYLE = "jepRia-extras-dialog-close";
  
  /**
   * Наименование селектора (класса стилей) кнопки уменьшения размеров всплывающего окна.
   */
  private static final String MINIMIZE_BUTTON_WINDOW_BOX_STYLE = "jepRia-extras-dialog-minimize";
  
  /**
   * Наименование селектора (класса стилей) кнопки увеличения размеров всплывающего окна.
   */
  private static final String MAXIMIZE_BUTTON_WINDOW_BOX_STYLE = "jepRia-extras-dialog-maximize";
  
  /**
   * Наименование селектора (класса стилей) области кнопок всплывающего окна.
   */
  private static final String DIALOG_CONTROLS_WINDOW_BOX_STYLE = "jepRia-extras-dialog-controls";

  /**
   * Static helper method to change the cursor for a given element when resizing is enabled.
     * 
   * @param dm The code describing the position of the element in question
   * @param element The {@link com.google.gwt.dom.client.Element} to set the cursor on
   */
  protected static void updateCursor(int dm, Element element) {
    Cursor cursor;
    
    switch (dm) {
        case 0: 
            cursor = Cursor.NW_RESIZE; 
            break;
        
        case 1: 
            cursor = Cursor.N_RESIZE; 
            break;
        
        case 2: 
            cursor = Cursor.NE_RESIZE; 
            break;
        
        case 3: 
            cursor = Cursor.W_RESIZE; 
            break;
        
        case 5: 
            cursor = Cursor.E_RESIZE; 
            break;
        
        case 6: 
            cursor = Cursor.SW_RESIZE; 
            break;
        
        case 7: 
            cursor = Cursor.S_RESIZE; 
            break;
        
        case 8: 
            cursor = Cursor.SE_RESIZE; 
            break;
        
        default: 
            cursor = Cursor.AUTO;
            break;
    }
    
    element.getStyle().setCursor(cursor);
  }

  /**
   * Creates a WindowBox which is permanent (no auto-hide), non-modal, has a "minimize"- and "close"-button
   * in the top-right corner and is not resizeable. The dialog box should not be shown until a child widget has been
   * added using {@link #add(com.google.gwt.user.client.ui.IsWidget)}.
   * <br><br>
   * This is the equivalent for calling <code>WindowBox(false, false, true, false)</code>.
   * 
   * @see WindowBox#WindowBox(boolean, boolean, boolean, boolean)
   */
  public WindowBox() {
      this(false, false, true, true, false);
  }
  
  /**
   * Creates a WindowBox which is permanent, non-modal, has a "minimize"- and "close"-button and is optionally resizeable.
   * The dialog box should not be shown until a child widget has been added using
     * {@link #setWidget(Widget)}.
   * 
   * @see WindowBox#WindowBox(boolean, boolean, boolean, boolean)
   * 
   * @param resizeable <code>true</code> to allow resizing by dragging the borders 
   */
  public WindowBox(boolean resizeable) {
      this(false, false, true, true, resizeable);
  }
  
  /**
   * Creates a WindowBox which is permanent and nonmodal, optionally resizeable and/or has a "minimize"- and 
   * "close"-button. The dialog box should not be shown until a child widget has been added using
     * {@link #setWidget(Widget)}.
   *  
     * @see WindowBox#WindowBox(boolean, boolean, boolean, boolean)
     * 
   * @param resizeable <code>true</code> to allow resizing by dragging the borders 
   * @param showCloseIcon <code>true</code> to show "close"-icon in the top right corner of the header
   */
    public WindowBox(boolean showCloseIcon, boolean resizeable) {
        this(false, false, true, showCloseIcon, resizeable);
    }
    
    /**
     * Creates a WindowBox which is permanent and nonmodal, optionally resizeable and/or has a "minimize"- and
     * "close"-button. The dialog box should not be shown until a child widget has been added using
     * {@link #setWidget(Widget)}.
     *  
     * @see WindowBox#WindowBox(boolean, boolean, boolean, boolean)
     * 
     * @param showMinimizeIcon <code>true</code> to show "minimize"-icon int the top right corner of the header
     * @param resizeable <code>true</code> to allow resizing by dragging the borders 
     * @param showCloseIcon <code>true</code> to show "close"-icon in the top right corner of the header
     */
    public WindowBox(boolean showMinimizeIcon, boolean showCloseIcon, boolean resizeable) {
        this(false, false, showMinimizeIcon, showCloseIcon, resizeable);
    }
    
    /**
     * Creates a WindowBox which is permanent, optionally modal, resizeable and/or has a "minimize"- and 
     * "close"-button. The dialog box should not be shown until a child widget has been added using
     * {@link #setWidget(Widget)}.
     * 
     * @param modal <code>true</code> if keyboard and mouse events for widgets not contained by the dialog 
     *          should be ignored
     * @param showMinimizeIcon <code>true</code> to show "minimize"-icon int the top right corner of the header
     * @param resizeable <code>true</code> to allow resizing by dragging the borders 
     * @param showCloseIcon <code>true</code> to show "close"-icon in the top right corner of the header
     */
    public WindowBox(boolean modal, boolean showMinimizeIcon, boolean showCloseIcon, boolean resizeable) {
        this(false, modal, showMinimizeIcon, showCloseIcon, resizeable);
    }

    /**
     * Creates an empty WindowBox with all configuration options.
     * The dialog box should not be shown until a child widget has been added using
     * {@link #setWidget(Widget)}.
     * 
     * @see DialogBox#DialogBox()
     * @see DialogBox#DialogBox(boolean)
     * @see DialogBox#DialogBox(boolean, boolean)
     * 
     * @param autoHide <code>true</code> if the dialog should be automatically hidden when the user clicks outside of it
     * @param modal <code>true</code> if keyboard and mouse events for widgets not contained by the dialog 
     *          should be ignored
     * @param showMinimizeIcon <code>true</code> to show "minimize"-icon int the top right corner of the header
     * @param showCloseIcon <code>true</code> to show "close"-icon in the top right corner of the header
     * @param resizeable <code>true</code> to allow resizing by dragging the borders 
     */
    public WindowBox(boolean autoHide, boolean modal, boolean showMinimizeIcon, boolean showCloseIcon, boolean resizeable) {
        super(autoHide, modal);
        
        this.setStyleName(MAIN_WINDOW_BOX_STYLE, true);

        this.container = new FlowPanel();
        this.container.addStyleName(DIALOG_CONTAINER_WINDOW_BOX_STYLE);
        
        this.close = new Anchor();
        this.close.setStyleName(CLOSE_BUTTON_WINDOW_BOX_STYLE);
        this.close.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                onCloseClick(event);
            }
        });
        setCloseIconVisible(showCloseIcon);

        this.minimize = new Anchor();
        this.minimize.setStyleName(MINIMIZE_BUTTON_WINDOW_BOX_STYLE);
        this.minimize.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                onMinimizeClick(event);
            }
        });
        setMinimizeIconVisible(showMinimizeIcon);
        
        Grid ctrlGrid = new Grid(1, 2);
        ctrlGrid.setWidget(0, 0, this.minimize);
        ctrlGrid.setWidget(0, 1, this.close);

        this.controls = new FlowPanel();
        this.controls.setStyleName(DIALOG_CONTROLS_WINDOW_BOX_STYLE);
        this.controls.add(ctrlGrid);
        this.dragMode = -1;
        
        this.resizable = resizeable;
    }
    
    /**
   * Sets the cursor to indicate resizability for a specified "drag-mode" (i.e. how the box is being resized)
   * on the dialog box. The position is described by an integer, as follows:
     * 
     * <pre>
     *  0-- --1-- --2
     *  |           |
     *  
     *  |           |
     *  3    -1     5
     *  |           |
     *  
     *  |           |
     *  6-- --7-- --8
     * </pre>
     * 
     * passing <code>-1</code> resets the cursor to the default.
   * @param dragMode
   */
  protected void updateCursor(int dragMode) {
    if (this.resizable) {
        updateCursor(dragMode,this.getElement());
        
        Element top = this.getCellElement(0,1);
        updateCursor(dragMode,top);
        
        top = Element.as(top.getFirstChild());
        if (top != null)
          updateCursor(dragMode,top);
    }
  }
  
  /**
   * Returns whether the dialog box is mouse-resizeable
   * 
   * @return  <code>true</code> if the user can resize the dialog with the mouse
   */
  public boolean isResizable() {
    return this.resizable;
  }

  /**
   * Set the dialog box to be resizeable by the user
   * 
   * @param resizable <code>true</code> if the user can resize the dialog with the mouse
   */
  public void setResizable(boolean resizable) {
    this.resizable = resizable;
  }

  /*
   * (non-Javadoc)
   * @see com.google.gwt.user.client.ui.DialogBox#onBrowserEvent(com.google.gwt.user.client.Event)
   */
  @Override
  public void onBrowserEvent(Event event) {
    
    // If we're not yet dragging, only trigger mouse events if the event occurs
    // in the caption wrapper
    if (this.resizable) {
      switch (event.getTypeInt()) {
      case Event.ONMOUSEDOWN:
      case Event.ONMOUSEUP:
      case Event.ONMOUSEMOVE:
      case Event.ONMOUSEOVER:
      case Event.ONMOUSEOUT:
        
        if (this.dragMode >= 0 || calcDragMode(event.getClientX(),event.getClientY()) >= 0) {
          // paste'n'copy from Widget.onBrowserEvent
          switch (DOM.eventGetType(event)) {
          case Event.ONMOUSEOVER:
            // Only fire the mouse over event if it's coming from outside this
            // widget.
          case Event.ONMOUSEOUT:
            // Only fire the mouse out event if it's leaving this
            // widget.
            Element related = event.getRelatedEventTarget().cast();
            if (related != null && getElement().isOrHasChild(related)) {
              return;
            }
            break;
          }
          DomEvent.fireNativeEvent(event, this, this.getElement());
          return;
        }
        if (this.dragMode<0)
          this.updateCursor(this.dragMode);
      }
    }
    
    super.onBrowserEvent(event);
  }

  /**
   * Recalculation of X-coordinate 
   * 
   * @param resize    resizeable element
   * @param clientX    input value of x-coordinate
   * @return  new value of x-coordinate
   */
  private int getRelX(Element resize, int clientX) {
    return clientX - resize.getAbsoluteLeft() +
      resize.getScrollLeft() +
        resize.getOwnerDocument().getScrollLeft();
  }
  
  /**
   * Recalculation of Y-coordinate
   * 
   * @param resize    resizeable element
   * @param clientY    input value of y-coordinate
   * @return  new value of y-coordinate
   */
  private int getRelY(Element resize, int clientY) {
    return clientY - resize.getAbsoluteTop() +
      resize.getScrollTop() +
        resize.getOwnerDocument().getScrollTop();
  }
  
  /**
   * Calculates the position of the mouse relative to the dialog box, and returns the corresponding "drag-mode"
   * integer, which describes which area of the box is being resized.
   * 
   * @param clientX The x-coordinate of the mouse in screen pixels
   * @param clientY The y-coordinate of the mouse in screen pixels
   * @return A value in range [-1..8] describing the position of the mouse (see {@link #updateCursor(int)} for more
   *         information)
   */
  protected int calcDragMode(int clientX, int clientY) {
    Element resize = this.getCellElement(2,2).getParentElement();
    int xr = this.getRelX(resize, clientX);
    int yr = this.getRelY(resize, clientY);
    
    int w = resize.getClientWidth();
    int h = resize.getClientHeight();
    
    if ((xr >= 0 && xr < w && yr >= -5 && yr < h) 
            || (yr >= 0 && yr < h && xr >= -5 && xr < w))
      return 8;
    
    resize = this.getCellElement(2,0).getParentElement();
    xr = this.getRelX(resize, clientX);
    yr = this.getRelY(resize, clientY);
    
    if ((xr >= 0 && xr < w && yr >= -5 && yr < h)
            || (yr >= 0 && yr < h && xr >= 0 && xr < w+5))
      return 6;
    
    resize = this.getCellElement(0,2).getParentElement();
    xr = this.getRelX(resize, clientX);
    yr = this.getRelY(resize, clientY);

    if ((xr >= 0 && xr < w && yr >= 0 && yr < h+5)
            || (yr >= 0 && yr < h && xr >= -5 && xr < w))
      return 2;
      
    resize = this.getCellElement(0,0).getParentElement();
    xr = this.getRelX(resize, clientX);
    yr = this.getRelY(resize, clientY);
      
    if ((xr >= 0 && xr < w && yr >= 0 && yr < h+5) 
            || (yr >= 0 && yr < h && xr >= 0 && xr < w+5))
      return 0;
      
    resize = this.getCellElement(0,1).getParentElement();
    xr = this.getRelX(resize, clientX);
    yr = this.getRelY(resize, clientY);

    if (yr >= 0 && yr < h)
      return 1;

    resize = this.getCellElement(1,0).getParentElement();
    xr = this.getRelX(resize, clientX);
    yr = this.getRelY(resize, clientY);

    if (xr >= 0 && xr < w)
      return 3;

    resize = this.getCellElement(2,1).getParentElement();
    xr = this.getRelX(resize, clientX);
    yr = this.getRelY(resize, clientY);

    if (yr >= 0 && yr < h)
      return 7;

    resize = this.getCellElement(1,2).getParentElement();
    xr = this.getRelX(resize, clientX);
    yr = this.getRelY(resize, clientY);

    if (xr >= 0 && xr < w)
      return 5;

    return -1;
  }
    
    /**
     * Convenience method to set the height, width and position of the given widget
     * 
     * @param panel
     * @param dx
     * @param dy
     */
    protected void dragResizeWidget(PopupPanel panel, int dx, int dy) {
        int x = this.getPopupLeft();
        int y = this.getPopupTop();
        
        Widget widget = panel.getWidget();
        
        // left + right
        if ((this.dragMode % 3) != 1) {
            int w = widget.getOffsetWidth();
            
            // left edge -> move left
            if ((this.dragMode % 3) == 0) {
                x += dx;
                w -= dx;
            } else {
                w += dx;
            }
            
            w = w < this.minWidth ? this.minWidth : w;
            
            widget.setWidth(w + "px");
        }
           
        // up + down
        if ((this.dragMode / 3) != 1) {
            int h = widget.getOffsetHeight();
            
            // up = dy is negative
            if ((this.dragMode / 3) == 0) {
                y += dy;
                h -= dy;
            } else {
                h += dy;
            }

            h = h < this.minHeight ? this.minHeight : h;
            
            widget.setHeight(h + "px");
        }
        
        if (this.dragMode / 3 == 0 || this.dragMode % 3 == 0)
            panel.setPopupPosition(x, y);
    }
  
  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.DialogBox#beginDragging(com.google.gwt.event.dom.client.MouseDownEvent)
   */
  @Override
  protected void beginDragging(MouseDownEvent event) {
    int dm = -1;
    
    if (this.resizable && !this.minimized)
      dm = this.calcDragMode(event.getClientX(),event.getClientY());
    
    if (this.resizable && dm >= 0) {
      this.dragMode = dm;
      
      DOM.setCapture(getElement());
      
      this.dragX = event.getClientX();
      this.dragY = event.getClientY();
      
      updateCursor(dm,RootPanel.get().getElement());
      
    } else {
      super.beginDragging(event);
    }
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.DialogBox#continueDragging(com.google.gwt.event.dom.client.MouseMoveEvent)
   */
  @Override
  protected void continueDragging(MouseMoveEvent event) {
    if (this.dragMode >= 0 && this.resizable) {
      this.updateCursor(this.dragMode);
      
            int dx = event.getClientX() - this.dragX;
            int dy = event.getClientY() - this.dragY;
            
            this.dragX = event.getClientX();
            this.dragY = event.getClientY();
            
            dragResizeWidget(this, dx, dy);
    } else {
        // this updates the cursor when dragging is NOT activated
        if (!this.minimized) {
            int dm = calcDragMode(event.getClientX(),event.getClientY());
          this.updateCursor(dm);
        }
      super.continueDragging(event);
    }
  }

  /*
   * (non-Javadoc)
   * @see com.google.gwt.user.client.ui.DialogBox#onPreviewNativeEvent(com.google.gwt.user.client.Event.NativePreviewEvent)
   */
  @Override
  protected void onPreviewNativeEvent(NativePreviewEvent event) {
    if (this.resizable) {
      // We need to preventDefault() on mouseDown events (outside of the
      // DialogBox content) to keep text from being selected when it
      // is dragged.
      NativeEvent nativeEvent = event.getNativeEvent();

      if (!event.isCanceled()
          && (event.getTypeInt() == Event.ONMOUSEDOWN)
          && calcDragMode(nativeEvent.getClientX(),nativeEvent.getClientY()) >= 0) {
        nativeEvent.preventDefault();
      }
    }
    
    super.onPreviewNativeEvent(event);
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.DialogBox#endDragging(com.google.gwt.event.dom.client.MouseUpEvent)
   */
  @Override
  protected void endDragging(MouseUpEvent event) {
    if (this.dragMode >= 0 && this.resizable) {
      DOM.releaseCapture(getElement());

        this.dragX = event.getClientX() - this.dragX;
        this.dragY = event.getClientY() - this.dragY;
        
        this.dragMode = -1;
        this.updateCursor(this.dragMode);
        RootPanel.get().getElement().getStyle().setCursor(Cursor.AUTO);
    }
    else {
      super.endDragging(event);
    }
  }

  /*
   * (non-Javadoc)
   * @see com.google.gwt.user.client.ui.DecoratedPopupPanel#setWidget(com.google.gwt.user.client.ui.Widget)
   */
  @Override
  public void setWidget(Widget widget) {
    if (this.container.getWidgetCount() == 0) {
      // setup
      this.container.add(this.controls);
      super.setWidget(this.container);
    } else {
      // remove the old one
      this.container.remove(1);
    }
    this.container.add(widget);
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.DecoratedPopupPanel#getWidget()
   */
  @Override
  public Widget getWidget() {
    if (this.container.getWidgetCount() > 1)
      return this.container.getWidget(1);
    else
      return null;
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.DecoratedPopupPanel#remove(com.google.gwt.user.client.ui.Widget)
   */
  @Override
  public boolean remove(Widget w) {
    return this.container.remove(w);
  }

  /**
   * Set whether the "close"-button should appear in the top right corner of the header
   * 
   * @param visible <code>true</code> if a "close"-button should be shown
   */
  public void setCloseIconVisible(boolean visible) {
    this.close.setVisible(visible);
  }
  
  /**
   * Set whether the "minimize"-button should appear in the top right corner of the header
   * 
   * @param visible <code>true</code> if a "minimize"-button should be shown
   */
  public void setMinimizeIconVisible(boolean visible) {
        this.minimize.setVisible(visible);
    }

  /**
   * Returns the FlowPanel that contains the controls. More controls can be
   * added directly to this.
   */
  public FlowPanel getControlPanel() {
    return this.controls;
  }

  /**
   * Called when the close icon is clicked. The default implementation hides the dialog box.
   * 
   * @param event The {@link ClickEvent} to handle
   */
  protected void onCloseClick(ClickEvent event) {
    hide();
  }
  
  /**
   * Called when the minimize icon is clicked. The default implementation hides the container of the dialog box.
   * 
   * @param event The {@link ClickEvent} to handle
   */
  protected void onMinimizeClick(ClickEvent event) {
      Widget widget = getWidget();
      
      if (widget == null)
          return;
      
      boolean visible = widget.isVisible();
      
      int offsetWidth = widget.getOffsetWidth();
      
      widget.setVisible(!visible);
      this.minimized = visible;
      
      if (visible) {
          this.container.setWidth(offsetWidth + "px");
          this.minimize.setStyleName(MAXIMIZE_BUTTON_WINDOW_BOX_STYLE);
      } else {
          this.container.setWidth("auto");
          this.minimize.setStyleName(MINIMIZE_BUTTON_WINDOW_BOX_STYLE);
      }
    }

  /*
   * (non-Javadoc)
   * @see com.google.gwt.event.logical.shared.HasOpenHandlers#addOpenHandler(com.google.gwt.event.logical.shared.OpenHandler)
   */
  @Override
  public HandlerRegistration addOpenHandler(OpenHandler<WindowBox> handler) {
    return addHandler(handler, OpenEvent.getType());
  }

  /*
   * (non-Javadoc)
   * @see com.google.gwt.user.client.ui.DialogBox#show()
   */
  @Override
  public void show() {
    boolean fireOpen = !isShowing();
    super.show();
    if (fireOpen) {
      OpenEvent.fire(this, this);
    }
  }

    /**
     * Sets the minimum width to which this widget can be resized by the user, if resizing is enabled. If the value
     * is invalid, it is reset to {@link #MIN_WIDTH}
     * 
     * @param minWidth A positive int value
     */
    public void setMinWidth(int minWidth) {
        if (minWidth < 1)
            minWidth = MIN_WIDTH;
        
        this.minWidth = minWidth;
    }

    /**
     * Sets the minimum height to which this widget can be resized by the user, if resizing is enabled. If the value
     * is invalid, it is reset to {@link #MIN_WIDTH}
     * 
     * @param minHeight A positive int value
     */
    public void setMinHeight(int minHeight) {
        if (minHeight < 1)
            minHeight = MIN_HEIGHT;
        
        this.minHeight = minHeight;
    }
}
