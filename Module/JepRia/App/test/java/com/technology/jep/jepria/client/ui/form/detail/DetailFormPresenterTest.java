package com.technology.jep.jepria.client.ui.form.detail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.internal.WhiteboxImpl;

import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.history.place.JepViewDetailPlace;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormPresenter;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormView;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactoryImpl;
import com.technology.jep.jepria.client.widget.field.FieldManager;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

/**
 * Тест DetailFormPresenter
 */
public class DetailFormPresenterTest {

  /**
   * Заглушка для DetailFormPresenter, с помощью которой можно посчитать количество вызовов onChangeWorkstate, 
   * так как в оригинальном классе модификатор onChangeWorkstate - protected.
   */
  class DetailFormPresenterChangeWorkstateVerify
      extends DetailFormPresenter<DetailFormView, PlainEventBus, JepDataServiceAsync, StandardClientFactory<PlainEventBus, JepDataServiceAsync>> {
    
    public DetailFormPresenterChangeWorkstateVerify(Place place, StandardClientFactory<PlainEventBus, JepDataServiceAsync> clientFactory) {
      super(place, clientFactory);
    }

    /**
     * Перекрыт, так как логика не важна - важен сам факт вызова.
     */
    @Override
    protected void onChangeWorkstate(WorkstateEnum workstate) {
      onChangeWorkstateToVerify();
    }

    @Override
    protected void adjustToWorkstate(WorkstateEnum workstate) {}
    
    /**
     * Видимый метод (public) для подсчета количество вызовов onChangeWorkstate.
     */
    public void onChangeWorkstateToVerify() {}
  }
  
  /**
   * Тест onChangeWorkstate. При входе в модуль (onEnterModule) два раза вызывается DetailFormPresenter.onChangeWorkstate: <br/>
   * <ol>
   *  <li>Установка состояния презентора. В MainModulePresenter.startModule (вызов plainPlaceController.goTo)</li>
   *  <li>После обработки события DoGetRecordEvent. В PlainModulePresenter.fillFields (вызов eventBus.doGetRecord)</li>
   * </ol>
   * @throws Exception
   */
  @Test
  public void testOnChangeWorkstate() throws Exception {
        
    // Инициализируем фабрик, презенторы и вью.
    DetailFormView detailView = mock(DetailFormView.class);
    when(detailView.getFieldManager()).thenReturn(new FieldManager());
    
    @SuppressWarnings("unchecked")
    StandardClientFactoryImpl<PlainEventBus, JepDataServiceAsync> clientFactoryMock = mock(StandardClientFactoryImpl.class);
    when(clientFactoryMock.getDetailFormView()).thenReturn(detailView);
    when(clientFactoryMock.getEventBus()).thenReturn(new PlainEventBus(clientFactoryMock));
    
    Place detailPlace = new JepViewDetailPlace();
    DetailFormPresenterChangeWorkstateVerify detailPresenter = PowerMockito.spy(
        new DetailFormPresenterChangeWorkstateVerify(detailPlace, clientFactoryMock));
    
    // Установка состояния презентора.
    detailPresenter.setPlace(detailPlace);
    
    // Метод, который вызывается в обработчике события onDoGetRecord.
    WhiteboxImpl.<Void>invokeMethod(detailPresenter, "adjustToRecord", new JepRecord());
    
    verify(detailPresenter).onChangeWorkstateToVerify();
  }
}
