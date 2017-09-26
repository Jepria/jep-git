package com.technology.jep.jepria.client.message;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.container.WindowBox;
import com.technology.jep.jepria.shared.exceptions.IdNotFoundException;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

public class MessageBox extends WindowBox {

  protected FlexTable mainPanel;
  private DockPanel iconMessageContainer, buttonsContainer;
  protected int iconMessageContainerHeight, buttonsContainerHeight;
  private Image icon;
  private Button focusedButton = null;
  
  // list of available buttons (map : the button and the correspondent type)
  Map<PredefinedButton, Button> buttons = new LinkedHashMap<PredefinedButton, Button>();
  
  public MessageBox(String message) {
    this(JepTexts.alert_dialog_title(), message);
  }
  
  public MessageBox(String headerText, String message) {
    // Enable enlarging and shrinking.
    super(false, true, true);
    // Set the dialog box's caption.
    setText(headerText);
    // Enable glass background.
    setGlassEnabled(true);

    // DialogBox is a SimplePanel, so you have to set its widget
    // property to whatever you want its contents to be.
    mainPanel = new FlexTable();
    
    Element captionElement = ((HTML) getCaption()).getElement();
    captionElement.addClassName(MAIN_FONT_STYLE);
    captionElement.getStyle().setFontSize(11, Unit.PX);
    captionElement.getStyle().setFontWeight(FontWeight.BOLD);
    HTML label = new HTML(message);
    label.getElement().addClassName(MAIN_FONT_STYLE);

    iconMessageContainer = new DockPanel();
    iconMessageContainer.setSpacing(10);
    iconMessageContainer.add(label, DockPanel.EAST);

    buttonsContainer = new DockPanel();
    buttonsContainer.setSpacing(5);
    buttonsContainer.setWidth("100%");

    mainPanel.setWidget(0, 0, iconMessageContainer);
    mainPanel.setWidget(1, 0, buttonsContainer);
    CellFormatter cellFormatter = mainPanel.getCellFormatter();
    cellFormatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
    cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
    cellFormatter.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
    setWidget(mainPanel);
  }
  
  /**
   * Префикс id (HTML-атрибут) кнопок
   */
  public final static String MESSAGE_BOX_BUTTON_ID_PREFIX = "MESSAGE_BOX_BUTTON_";
  
  @Override
  public void show() {
    // Attach Icon if it needs.
    if (!JepRiaUtil.isEmpty(icon)){
      iconMessageContainer.add(icon, DockPanel.WEST);
    }
    
    // Attach Buttons if it needs.
    for(Map.Entry<PredefinedButton, Button> buttonEntry: buttons.entrySet()) {
      
      Button button = buttonEntry.getValue();
      
      // Если id для кнопки не задан, то будет сгенерирован автоматически. 
      if(JepRiaUtil.isEmpty(button.getElement().getId())) {
        button.getElement().setId(MESSAGE_BOX_BUTTON_ID_PREFIX + buttonEntry.getKey().name());
      }
          
      buttonsContainer.add(button, DockPanel.WEST);
      button.addFocusHandler(new FocusHandler(){
        @Override
        public void onFocus(FocusEvent event) {
          focusedButton = (Button) event.getSource();
        }
      });
      button.addBlurHandler(new BlurHandler(){
        @Override
        public void onBlur(BlurEvent event) {
          focusedButton = null;
        }
      });
    }
    
    super.show();

    iconMessageContainerHeight = iconMessageContainer.getOffsetHeight();
    buttonsContainerHeight = buttonsContainer.getOffsetHeight();

    CellFormatter cellFormatter = mainPanel.getCellFormatter();
    cellFormatter.getElement(0, 0).getStyle().setHeight(iconMessageContainerHeight, Unit.PX);
    cellFormatter.getElement(1, 0).getStyle().setHeight(buttonsContainerHeight, Unit.PX);
    mainPanel.getElement().getStyle().setProperty("minWidth", 300 + Unit.PX.getType());
    
    boolean isFirst = true;
    // Loop all buttons
    for (Iterator<Button> buttonIterator = buttons.values().iterator(); buttonIterator.hasNext(); isFirst = false){
      final Button b = buttonIterator.next(); 
      boolean isNotLast = buttonIterator.hasNext();
      if (isFirst){
        b.getElement().getParentElement().getStyle().setTextAlign(isNotLast ? TextAlign.RIGHT : TextAlign.CENTER);
        focusedButton = b;
        b.setFocus(true);
      }
      else if (isNotLast){
        buttonsContainer.setCellWidth(b, b.getOffsetWidth() + Unit.PX.getType());
      }
    }
    center();
  }
  
  protected void setIcon(ImageResource icon) {
    this.icon = new Image(icon);
  }
  
  protected void addButton(PredefinedButton type, Button button) {
    buttons.put(type, button);
  }
  
  /**
   * {@inheritDoc}
   * 
   * Особенности:<br/>
   * Переопределение события позволяет перехватывать нажатие клавиши Enter и вызывать обработчик для первой кнопки в списке.
   */
  @Override
  protected void onPreviewNativeEvent(NativePreviewEvent event) {
    if (event.getTypeInt() == Event.ONKEYDOWN) {
      if (isShowing() && isAttached()) {
        if (focusedButton != null) {
          int keyCode = event.getNativeEvent().getKeyCode();
          if (keyCode == KeyCodes.KEY_ENTER) {
            focusedButton.click();
          } else if (keyCode == KeyCodes.KEY_TAB) {
            List<Button> buttonList = new ArrayList<>(buttons.values());
            int currentFocusIndex = buttonList.indexOf(focusedButton);
            int nextButtonIndex = currentFocusIndex + 1 >= buttonList.size() ? 0 : currentFocusIndex + 1;
            focusedButton = buttonList.get(nextButtonIndex);
            buttonList.get(nextButtonIndex).setFocus(true);
          }
          event.cancel();
        }
      }
    }
    super.onPreviewNativeEvent(event);
  }
  
  /**
   * Добавление обработчика нажатия кнопки.
   * 
   * @param type      тип кнопки (если передан не соответствующий, то выбрасывается {@link com.technology.jep.jepria.shared.exceptions.IdNotFoundException})
   * @param handler    обработчик кнопки
   * @return регистрация обработчика
   */
  public HandlerRegistration addButtonClickHandler(PredefinedButton type, ClickHandler handler){
    Button button = buttons.get(type); 
    if (button != null && handler != null){
      return button.addClickHandler(handler);
    }
    throw new IdNotFoundException(JepClientUtil.substitute(JepTexts.messageBoxButton_idNotFoundError(), type));
  }
}
