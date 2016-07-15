package com.technology.jep.jepria.client.message;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.CONFIRM_MESSAGEBOX_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.CONFIRM_MESSAGE_BOX_NO_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.CONFIRM_MESSAGE_BOX_YES_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

public class ConfirmMessageBox extends MessageBox {
  
  private Button yesButton, noButton;
  
  public ConfirmMessageBox(String headerText, String message, final ConfirmCallback confirmCallback) {
    super(headerText, message);
    this.getElement().setId(CONFIRM_MESSAGEBOX_ID);
    
    // Установка иконки.
    setIcon(JepImages.question());
    
    // Инициализация необходимых кнопок.
    yesButton = new Button(JepTexts.yes());
    yesButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        assert confirmCallback != null : "Confirm callback should be defined!";
        confirmCallback.onConfirm(true);
        hide();
      }
    });
    Element yesButtonElement = yesButton.getElement();
    yesButtonElement.setId(CONFIRM_MESSAGE_BOX_YES_BUTTON_ID);
    yesButtonElement.addClassName(MAIN_FONT_STYLE);
    yesButtonElement.getStyle().setFontSize(11, Unit.PX);
    yesButton.setWidth(65 + Unit.PX.getType());
    
    noButton = new Button(JepTexts.no());
    noButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        assert confirmCallback != null : "Confirm callback should be defined!";
        confirmCallback.onConfirm(false);
        hide();
      }
    });
    Element noButtonElement = noButton.getElement();
    noButtonElement.setId(CONFIRM_MESSAGE_BOX_NO_BUTTON_ID);
    noButtonElement.addClassName(MAIN_FONT_STYLE);
    noButtonElement.getStyle().setFontSize(11, Unit.PX);
    noButton.setWidth(65 + Unit.PX.getType());
    
    addButton(PredefinedButton.YES, yesButton);
    addButton(PredefinedButton.NO, noButton);
  }

  @Override
  protected void onCloseClick(ClickEvent event) {
    noButton.click();
    super.onCloseClick(event);
  }
}
