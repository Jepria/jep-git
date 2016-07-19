package com.technology.jep.jepria.client.message;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.ALERT_MESSAGEBOX_ID;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

public class AlertMessageBox extends MessageBox {
  
  private Button okButton;
  
  public AlertMessageBox(String headerText, String message) {
    super(headerText, message);
    this.getElement().setId(ALERT_MESSAGEBOX_ID);
    
    // Установка иконки.
    setIcon(JepImages.warning());
    
    // Инициализация необходимых кнопок.
    okButton = new Button(JepTexts.errors_dialog_close());
    okButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        hide();
      }
    });
    
    Element okButtonElement = okButton.getElement();
    okButtonElement.addClassName(MAIN_FONT_STYLE);
    okButtonElement.getStyle().setFontSize(11, Unit.PX);
    okButton.setWidth(65 + Unit.PX.getType());
    
    addButton(PredefinedButton.OK, okButton);
  }
}
