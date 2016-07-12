package com.technology.jep.jepria.client.widget.field.multistate.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.technology.jep.jepria.client.widget.field.multistate.event.InputForbiddenEvent.InputForbiddenHandler;

/**
 * Событие, генерируемое виджетом {@link com.technology.jep.jepria.client.widget.field.masked.MaskedTextBox} в ситуации,
 * когда пользователь пытается ввести недопустимый символ.
 */
public class InputForbiddenEvent extends GwtEvent<InputForbiddenHandler> {

  /**
   * Обработчик для событий {@link InputForbiddenEvent}.
   */
  public interface InputForbiddenHandler extends EventHandler {

    void onInputForbidden(InputForbiddenEvent event);
  }

  /**
   * Реализующий данный интерфейс виджет - источник событий {@link InputForbiddenEvent}.
   */
  public interface HasInputForbiddenHandlers {

    /**
     * Устанавливает обработчик {@link InputForbiddenHandler}для событий
     * {@link InputForbiddenEvent}.
     * @param handler обработчик
     * @return регистрация события
     */
    HandlerRegistration addInputForbiddenHandler(InputForbiddenHandler handler);
  }

  /**
   * Тип обработчика.
   */
  private static Type<InputForbiddenHandler> TYPE = new Type<InputForbiddenHandler>();

  /**
   * Возвращает связанный с данным событием тип.
   * 
   * @return тип обработчика
   */
  public static Type<InputForbiddenHandler> getType() {
    return TYPE;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Type<InputForbiddenHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void dispatch(InputForbiddenHandler handler) {
    handler.onInputForbidden(this);
  }

}
