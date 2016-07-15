package com.technology.jep.jepria.client.message;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.ERROR_MESSAGEBOX_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.ERROR_MESSAGE_BOX_OK_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;
import static com.technology.jep.jepria.client.JepRiaClientConstant.TEXT_AREA_STYLE;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.technology.jep.jepria.client.widget.container.FieldSet;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

public class ErrorDialog extends MessageBox {
  
  private final static String PERCENT_100 = "100%";
  private final static int DEFAULT_DETAILS_HEIGHT = 250;
  private final static String SHOW_DETAILS = JepTexts.errors_dialog_details_show(),
                HIDE_DETAILS = JepTexts.errors_dialog_details_hide();
  private Button okButton, detailsButton;
  private FieldSet detailsFieldSet;
  
  public ErrorDialog(String headerText, Throwable th, String message) {
    super(headerText, message);
    this.getElement().setId(ERROR_MESSAGEBOX_ID);
    
    // Установка иконки.
    setIcon(JepImages.error());
    
    // Инициализация необходимых кнопок.
    okButton = new Button(JepTexts.errors_dialog_close());
    okButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        hide();
      }
    });
    
    Element okButtonElement = okButton.getElement();
    okButtonElement.setId(ERROR_MESSAGE_BOX_OK_BUTTON_ID);
    okButtonElement.addClassName(MAIN_FONT_STYLE);
    okButtonElement.getStyle().setFontSize(11, Unit.PX);

    detailsButton = new Button(JepTexts.errors_dialog_details_show());
    detailsButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        // Раскрывыаем контейнер с детализацией ошибки.
        if (!isDetailsOpened()) {
          Integer width = detailsButton.getOffsetWidth();
          detailsButton.setText(HIDE_DETAILS);
          detailsButton.setWidth(width + Unit.PX.getType());
          detailsFieldSet.setHeight(DEFAULT_DETAILS_HEIGHT + Unit.PX.getType());
          mainPanel.setHeight((iconMessageContainerHeight + buttonsContainerHeight) + Unit.PX.getType());
          mainPanel.setWidget(2, 0, detailsFieldSet);
        } else {
          detailsButton.setText(SHOW_DETAILS);
          mainPanel.remove(detailsFieldSet);
          mainPanel.setWidget(2, 0, new Hidden());
          mainPanel.setHeight((iconMessageContainerHeight + buttonsContainerHeight) + Unit.PX.getType());
        }
      }
    });
    
    Element detailsButtonElement = detailsButton.getElement();
    detailsButtonElement.addClassName(MAIN_FONT_STYLE);
    detailsButtonElement.getStyle().setFontSize(11, Unit.PX);
    
    addButton(PredefinedButton.OK, okButton);
    addButton(PredefinedButton.DETAILS, detailsButton);
    
    // Установка области детализации.
    detailsFieldSet = new FieldSet();
    detailsFieldSet.setCaptionHTML(JepTexts.errors_dialog_details_fieldset());
    Style style = detailsFieldSet.getElement().getStyle();
    style.setPaddingBottom(15, Unit.PX);
    style.setProperty("minHeight", DEFAULT_DETAILS_HEIGHT + Unit.PX.getType());
    detailsFieldSet.setHeight(DEFAULT_DETAILS_HEIGHT + Unit.PX.getType());

    TextArea detailsTextArea = new TextArea();
    detailsTextArea.setValue(buildDetails(th));
    detailsTextArea.setHeight(PERCENT_100);
    detailsTextArea.setWidth(PERCENT_100);
    detailsTextArea.addStyleName(TEXT_AREA_STYLE);
    Element element = detailsFieldSet.getElement();
    element.addClassName(MAIN_FONT_STYLE);
    element.getStyle().setFontWeight(FontWeight.BOLD);
    
    detailsFieldSet.add(detailsTextArea);
    
    mainPanel.setWidget(2, 0, new Hidden());
  }

  @Override
  public void show() {
    super.show();
    
    int panelWidth = mainPanel.getOffsetWidth();
    mainPanel.getElement().getStyle().setProperty("minWidth", panelWidth + Unit.PX.getType());
    
    okButton.setWidth(detailsButton.getOffsetWidth() + Unit.PX.getType());
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see
   * com.technology.jep.jepriashowcase.main.client.widget.container.GwtWindowBox
   * #dragResizeWidget(PopupPanel panel, int dx, int dy)
   */
  @Override
  protected void dragResizeWidget(PopupPanel panel, int dx, int dy) {
    super.dragResizeWidget(panel, dx, dy);

    // Необходимо выставить значение поля с детализацией, поскольку на
    // уровне стилей эту задачу не решить (проблемы возникают в IE с
    // заполнением оставшейся части фиксированной таблицы)
    if (isDetailsOpened()) {
      detailsFieldSet.setHeight(
          (mainPanel.getOffsetHeight() - iconMessageContainerHeight - buttonsContainerHeight - 45) 
          + Unit.PX.getType());
    }
  }

  /**
   * Метод, определяющий раскрыто ли поле с детализацией по ошибке
   * 
   * @return true - если данное поле раскрыто, в противном случае - false
   */
  public boolean isDetailsOpened() {
    return !detailsButton.getText().equals(SHOW_DETAILS);
  }

  /**
   * Создание сообщения об итоговой ошибке, выводимой пользователю.
   * 
   * @param th
   *            исключительная ситуация
   * @return итоговое сообщение об ошибке
   */
  private String buildDetails(Throwable th) {
    StringBuilder sb = new StringBuilder();
    sb.append("---------------------------------- Database ----------------------------------\n");
    sb.append(buildDbDetails(th));
    sb.append("----------------------------- Application Server -----------------------------\n");
    sb.append(JepMessageBoxImpl.instance.getStackTrace(th));
    return sb.toString();
  }

  /**
   * Создание сообщения об ошибке из БД.
   * 
   * @param th
   *            исключительная ситуация
   * @return сообщение об ошибке
   */
  private String buildDbDetails(Throwable th) {
    StringBuilder sb = new StringBuilder();
    Throwable dbCause = null;
    while (th.getCause() != null) {
      th = th.getCause();
      if (th instanceof ApplicationException) { // DaoSupport выбрасывает ApplicationException
        dbCause = th;
      }
    }
    if (dbCause != null) {
      String message = dbCause.getLocalizedMessage();
      sb.append(message);
    }

    return sb.toString();
  }
}
