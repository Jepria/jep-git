package com.technology.jep.jepria.client.ui.wizard;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.technology.jep.jepria.client.history.place.BlockPlaceController;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.JepEventBus;
import com.technology.jep.jepria.client.widget.field.FieldManager;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

@SuppressWarnings("rawtypes")
public abstract class BlockPresenter
  <V extends BlockView, 
    S extends JepDataServiceAsync, 
      F extends BlockClientFactory<S>> 
        extends JepPresenter<JepEventBus, F>{
  
  protected V view;
  protected S service;
  protected BlockPlaceController blockPlaceController;
  
  /**
   * Управляющий полями класс по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
   * При необходимости, дополнительные управляющие классы используются уже на прикладном уровне.
   */
  protected FieldManager fields;
  
  /**
   * Создает презентер блока визарда в заданном режиме (Place).
   *
   * @param place режим, в котором необходимо создать презентер
   * @param clientFactory клиентская фабрика блока визарда
   */
  @SuppressWarnings("unchecked")
  public BlockPresenter(Place place, F clientFactory) {
    super(place, clientFactory);
    view = (V) clientFactory.getView();
    fields = view.getFieldManager();
    service = clientFactory.getService();
    blockPlaceController = clientFactory.getPlaceController();
  }

  @Override
  public void start(AcceptsOneWidget panel, EventBus eventBus) {
    bind();
    
    changeWorkstate(place);
  }
  
  protected abstract void adjustToWorkstate(WorkstateEnum workstate);
  
  protected void bind() {}
  
  protected void onChangeWorkstate(WorkstateEnum workstate) {
    fields.changeWorkstate(workstate);
  }
}
