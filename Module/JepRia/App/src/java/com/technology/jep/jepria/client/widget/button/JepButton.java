package com.technology.jep.jepria.client.widget.button;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Кнопка тулбара.
 */
public class JepButton extends Button {
  
  /**
   * Наименование стиля недоступной кнопки.
   */
  private static final String disabledStyle = "jepRia-JepButton-buttonDisabled";
  
  /**
   * Интервал между меткой и иконкой (когда иконка располагается слева или справа от подписи).
   */
  private static final String padding = "5px";
  
  /**
   * Возможные позиции иконки на кнопке.<br>
   * Только для кнопок, которые содержат и иконку, и подпись.
   */
  public enum IconPosition {
    /**
     * Иконка над текстом.
     */
    TOP,
    /**
     * Иконка под текстом.
     */
    BOTTOM,
    /**
     * Иконка слева от текста.
     */
    LEFT,
    /**
     * Иконка справа от текста.
     */
    RIGHT,
  }
  
  /**
   * Создаёт кнопку с наименованием и иконкой. <br>
   * Также устанавливает всплывающую подсказку.
   * @param id идентификатор
   * @param name наименование
   * @param icon иконка
   * @param iconPosition расположение иконки
   */
  public JepButton(String id, String name, ImageResource icon, IconPosition iconPosition) {
    this.getElement().setId(id); // Поддержка automation
    
    if (icon != null) {
      Image image = new Image(icon);
      if (JepRiaUtil.isEmpty(name)) {
        DOM.insertChild(getElement(), image.getElement(), 1);
      }
      else {
        CellPanel panel = null;
        switch(iconPosition) {
          case TOP: {
            VerticalPanel vPanel = new VerticalPanel();
            vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            vPanel.add(image);
            vPanel.add(new Label(name));
            panel = vPanel;
            break;
          }
          case BOTTOM: {
            VerticalPanel vPanel = new VerticalPanel();
            vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            vPanel.add(new Label(name));
            vPanel.add(image);
            panel = vPanel;
            break;
          }
          case LEFT: {
            HorizontalPanel hPanel = new HorizontalPanel();
            hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            hPanel.add(image);
            Label textLabel = new Label(name);
            textLabel.getElement().getStyle().setProperty("paddingLeft", padding);
            hPanel.add(textLabel);
            panel = hPanel;
            break;
          }
          case RIGHT: {
            HorizontalPanel hPanel = new HorizontalPanel();
            hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            Label textLabel = new Label(name);
            textLabel.getElement().getStyle().setProperty("paddingRight", padding);
            hPanel.add(textLabel);
            hPanel.add(image);
            panel = hPanel;
          }
        }
        DOM.insertChild(getElement(), panel.getElement(), 1);
        setTitle(name);
      }
    } else {
      setText(name);
      setTitle(name);
    }
  }
  
  /**
   * Создаёт кнопку с подписью и иконкой слева от подписи.
   * @param id идентификатор
   * @param name наименование
   * @param icon иконка
   */
  public JepButton(String id, String name, ImageResource icon) {
    this(id, name, icon, IconPosition.LEFT);
  }
  
  /**
   * Создаёт кнопку с указанным текстом
   * @param id идентификатор
   * @param name наименование
   */
  public JepButton(String id, String name) {
    this(id, name, null, null);
  }

  /**
   * Установка доступности или недоступности кнопки.
   * @param enabled флаг
   */
  @Override
  public void setEnabled(boolean enabled) {
    if (!enabled) {
      addStyleName(disabledStyle);
    }
    else {
      removeStyleName(disabledStyle);      
    }
    super.setEnabled(enabled);
  }

}
