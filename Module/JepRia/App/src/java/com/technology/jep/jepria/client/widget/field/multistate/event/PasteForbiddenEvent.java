package com.technology.jep.jepria.client.widget.field.multistate.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.technology.jep.jepria.client.widget.field.multistate.event.PasteForbiddenEvent.PasteForbiddenHandler;

/**
 * Событие, генерируемое виджетом {@link com.technology.jep.jepria.client.widget.field.masked.MaskedTextBox} в ситуации,
 * когда пользователь пытается вставить недопустимое значение.
 */
public class PasteForbiddenEvent extends GwtEvent<PasteForbiddenHandler> {

  /**
   * Обработчик для событий {@link PasteForbiddenEvent}.
   */
  public interface PasteForbiddenHandler extends EventHandler {

    void onPasteForbidden(PasteForbiddenEvent event);
  }

  /**
   * Реализующий данный интерфейс виджет - источник событий {@link PasteForbiddenEvent}.
   */
  public interface HasPasteForbiddenHandlers {

    /**
     * Устанавливает обработчик {@link PasteForbiddenHandler} для событий
     * {@link PasteForbiddenEvent}.
     * @param handler обработчик
     * @return регистрация события
     */
    HandlerRegistration addPasteForbiddenHandler(PasteForbiddenHandler handler);
  }

  /**
   * Тип обработчика.
   */
  private static Type<PasteForbiddenHandler> TYPE = new Type<PasteForbiddenHandler>();

  /**
   * Возвращает связанный с данным событием тип.
   * 
   * @return тип обработчика
   */
  public static Type<PasteForbiddenHandler> getType() {
    return TYPE;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Type<PasteForbiddenHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void dispatch(PasteForbiddenHandler handler) {
    handler.onPasteForbidden(this);
  }

}
