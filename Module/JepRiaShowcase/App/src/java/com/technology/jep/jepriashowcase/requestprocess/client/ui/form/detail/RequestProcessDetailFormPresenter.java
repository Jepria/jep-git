package com.technology.jep.jepriashowcase.requestprocess.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.*;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.*;
 
import com.google.gwt.place.shared.Place;
 
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormPresenter;
import com.technology.jep.jepriashowcase.requestprocess.shared.service.RequestProcessServiceAsync;
 
public class RequestProcessDetailFormPresenter<E extends PlainEventBus, S extends RequestProcessServiceAsync> 
    extends DetailFormPresenter<RequestProcessDetailFormView, E, S, StandardClientFactory<E, S>> { 
 
  public RequestProcessDetailFormPresenter(Place place, StandardClientFactory<E, S> clientFactory) {
    super(place, clientFactory);
  }
 
  /* public void bind() {
    super.bind();
    // Здесь размещается код связывания presenter-а и view 
  }
  */ 
 
  protected void adjustToWorkstate(WorkstateEnum workstate) {
  }
 
}
