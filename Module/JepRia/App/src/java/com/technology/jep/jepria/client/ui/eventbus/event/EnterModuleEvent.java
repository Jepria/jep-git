package com.technology.jep.jepria.client.ui.eventbus.event;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;

/**
 * Событие входа в модуль.
 */
public class EnterModuleEvent extends BusEvent<EnterModuleEvent.Handler> {

  /**
   * Implemented by handlers of EnterModuleEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link EnterModuleEvent} is fired.
     * 
     * @param event
     *            the {@link EnterModuleEvent}
     */
    void onEnterModule(EnterModuleEvent event);
  }

  /**
   * A singleton instance of Type&lt;EnterModuleHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();

  private final String moduleId;
  private final Place place;
  private final String displayName;

  public String getModuleId() {
    return moduleId;
  }
  
  public Place getPlace() {
    return place;
  }

  public EnterModuleEvent(String moduleId) {
    this(moduleId, null);
  }

  public EnterModuleEvent(String moduleId, Place place) {
    this.moduleId = moduleId;
    this.place = place;
    this.displayName = JepTexts.event_displayName_enterModule() + " " + moduleId;
  }
  
  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onEnterModule(this);
  }
  
  /**
   * Получение отображаемого в сообщениях, логах наименования события.
   * 
   * @return отображаемое в сообщениях, логах наименование события
   */
  public String getDisplayName() {
    return displayName;
  }
  
  /**
   * Переопределим формирование хеш-кода таким образом, чтобы при его формировании учитывался идентификатор модуля - основной атрибут объекта.
   *
   * @return хеш-код
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((moduleId == null) ? 0 : moduleId.hashCode());
    return result;
  }

  /**
   * Переопределим сравнение объектов таким образом, чтобы при сравнении учитывался идентификатор модуля - основной атрибут объекта.
   * 
   * @param obj объект, с которым сравнивается текущий объект
   * @return true - объекты равны, false - в противном случае
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    EnterModuleEvent other = (EnterModuleEvent) obj;
    if (moduleId == null) {
      if (other.moduleId != null)
        return false;
    } else if (!moduleId.equals(other.moduleId))
      return false;
    return true;
  }

}
