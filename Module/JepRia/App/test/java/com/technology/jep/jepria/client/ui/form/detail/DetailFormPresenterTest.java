package com.technology.jep.jepria.client.ui.form.detail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.history.place.JepCreatePlace;
import com.technology.jep.jepria.client.history.place.JepEditPlace;
import com.technology.jep.jepria.client.history.place.JepSearchPlace;
import com.technology.jep.jepria.client.history.place.JepSelectedPlace;
import com.technology.jep.jepria.client.history.place.JepViewDetailPlace;
import com.technology.jep.jepria.client.history.place.JepViewListPlace;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.DoGetRecordEvent;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactoryImpl;
import com.technology.jep.jepria.client.widget.field.FieldManager;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

/**
 * Тест метода onChangeWorkstate. При входе в модуль (onEnterModule) два раза вызывается DetailFormPresenter.onChangeWorkstate: <br/>
 * <ol>
 *  <li>Установка состояния презентора. В MainModulePresenter.startModule (вызов plainPlaceController.goTo)</li>
 *  <li>После обработки события DoGetRecordEvent. В PlainModulePresenter.fillFields (вызов eventBus.doGetRecord)</li>
 * </ol>
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
     * {@inheritDoc} <br/>
     * Перекрыт, так как нет имплементации сервиса.
     */
    @Override
    public void onDoGetRecord(DoGetRecordEvent event) {
      adjustToRecord(new JepRecord());
    }
    
    /**
     * Перекрыт, так как логика не важна - важен сам факт вызова.
     */
    @Override
    protected void onChangeWorkstate(WorkstateEnum workstate) {
      onChangeWorkstateToVerify();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void adjustToWorkstate(WorkstateEnum workstate) {}
    
    /**
     * Видимый метод (public) для подсчета количество вызовов onChangeWorkstate.
     */
    public void onChangeWorkstateToVerify() {}
  }
  
  /**
   * Тест метода onChangeWorkstate детального презентора при входе в состояние {@link JepSelectedPlace}.
   * Считает успешным, если onChangeWorkstate не сработал. <br/>
   * @throws Exception
   */
  @Test
  public void testSelectedPlace() throws Exception {
    DetailFormPresenterChangeWorkstateVerify detailPresenter = createDetailPresenter(new JepSelectedPlace());
    
    verify(detailPresenter, Mockito.times(0)).onChangeWorkstateToVerify();
  }
  
  /**
   * Тест метода onChangeWorkstate детального презентора при входе в состояние {@link JepViewListPlace}.
   * Считает успешным, если onChangeWorkstate не сработал. <br/>
   * @throws Exception
   */
  @Test
  public void testViewListPlace() throws Exception {
    DetailFormPresenterChangeWorkstateVerify detailPresenter = createDetailPresenter(new JepViewListPlace());
    
    verify(detailPresenter, Mockito.times(0)).onChangeWorkstateToVerify();
  }
  
  /**
   * Тест метода onChangeWorkstate детального презентора при входе в состояние {@link JepSearchPlace}.
   * Считает успешным, если onChangeWorkstate сработал один раз. <br/>
   * @throws Exception
   */
  @Test
  public void testSearchPlace() throws Exception {
    DetailFormPresenterChangeWorkstateVerify detailPresenter = createDetailPresenter(new JepSearchPlace());
    
    verify(detailPresenter, Mockito.times(1)).onChangeWorkstateToVerify();
  }
  
  
  /**
   * Тест метода onChangeWorkstate детального презентора при входе в состояние {@link JepCreatePlace}.
   * Считает успешным, если onChangeWorkstate сработал один раз. <br/>
   * @throws Exception
   */
  @Test
  public void testCreatePlace() throws Exception {
    DetailFormPresenterChangeWorkstateVerify detailPresenter = createDetailPresenter(new JepCreatePlace());
    
    verify(detailPresenter, Mockito.times(1)).onChangeWorkstateToVerify();
  }
  
  /**
   * Тест метода onChangeWorkstate детального презентора при входе в состояние {@link JepEditPlace}.
   * Считает успешным, если onChangeWorkstate не сработал. <br/>
   * @throws Exception
   */
  @Test
  public void testEditPlace() throws Exception {
    DetailFormPresenterChangeWorkstateVerify detailPresenter = createDetailPresenter(new JepEditPlace());
    
    verify(detailPresenter, Mockito.times(0)).onChangeWorkstateToVerify();
  }
  
  /**
   * Тест метода onChangeWorkstate детального презентора при входе в состояние {@link JepEditPlace}
   * и инициализации формы данными через событие DoGetRecordEvent. <br/>
   * Считает успешным, если onChangeWorkstate сработал один раз.
   * @throws Exception
   */
  @Test
  public void testEditPlaceDoGetRecord() throws Exception {
    DetailFormPresenterChangeWorkstateVerify detailPresenter = createDetailPresenter(new JepEditPlace());
    
    // Эмулируем поведение PlainModulePresenter: вызываем событие DoGetRecord
    detailPresenter.onDoGetRecord(new DoGetRecordEvent(new PagingConfig(new JepRecord())));
    
    verify(detailPresenter).onChangeWorkstateToVerify();
  }
  
  /**
   * Тест метода onChangeWorkstate детального презентора при входе в состояние {@link JepViewDetailPlace}.
   * Считает успешным, если onChangeWorkstate не сработал. <br/>
   * @throws Exception
   */
  @Test
  public void testViewDetailPlace() throws Exception {
    DetailFormPresenterChangeWorkstateVerify detailPresenter = createDetailPresenter(new JepViewDetailPlace());
    
    verify(detailPresenter, Mockito.times(0)).onChangeWorkstateToVerify();
  }
  
  /**
   * Тест метода onChangeWorkstate детального презентора при входе в состояние {@link JepViewDetailPlace}
   * и инициализации формы данными через событие DoGetRecordEvent. <br/>
   * Считает успешным, если onChangeWorkstate сработал один раз.
   * @throws Exception
   */
  @Test
  public void testViewDetailDoGetRecord() throws Exception {
    DetailFormPresenterChangeWorkstateVerify detailPresenter = createDetailPresenter(new JepViewDetailPlace());
    
    // Эмулируем поведение PlainModulePresenter: вызываем событие DoGetRecord
    detailPresenter.onDoGetRecord(new DoGetRecordEvent(new PagingConfig(new JepRecord())));
    
    verify(detailPresenter).onChangeWorkstateToVerify();
  }

  /**
   * Тест метода onChangeWorkstate детального презентора при повторном входе (например, при возвращении из дочернего модуля) 
   * в состояние {@link JepViewDetailPlace} и инициализации формы данными через событие DoGetRecordEvent. <br/>
   * Считает успешным, если onChangeWorkstate сработал один раз.
   * @throws Exception
   */
  @Test
  public void testReturnToMain() throws Exception {
    Place detailPlace = new JepViewDetailPlace();
    DetailFormPresenterChangeWorkstateVerify detailPresenter = createDetailPresenter(detailPlace);
    
    // Эмулируем поведение PlainModulePresenter: вызываем событие DoGetRecord
    detailPresenter.onDoGetRecord(new DoGetRecordEvent(new PagingConfig(new JepRecord())));
    
    // При возвращении в основной модуль, повторно вызывается установка состояния 
    detailPresenter.setPlace(detailPlace);
    
    // Повторно эмулируем поведение PlainModulePresenter: вызываем событие DoGetRecord
    detailPresenter.onDoGetRecord(new DoGetRecordEvent(new PagingConfig(new JepRecord())));
    
    verify(detailPresenter, Mockito.times(2)).onChangeWorkstateToVerify();
  }

  /**
   * Создает детальный презентер для теста в рабочем состоянии detailPlace.
   * @param detailPlace Состояние схода.
   * @return Детальный презенте
   */
  private DetailFormPresenterChangeWorkstateVerify createDetailPresenter(Place detailPlace) {
    // Инициализируем фабрик, презенторы и вью.
    DetailFormView detailView = mock(DetailFormView.class);
    when(detailView.getFieldManager()).thenReturn(new FieldManager());
    
    @SuppressWarnings("unchecked")
    StandardClientFactoryImpl<PlainEventBus, JepDataServiceAsync> clientFactoryMock = mock(StandardClientFactoryImpl.class);
    when(clientFactoryMock.getDetailFormView()).thenReturn(detailView);
    
    PlainEventBus plainEventBus = new PlainEventBus(clientFactoryMock);
    when(clientFactoryMock.getEventBus()).thenReturn(plainEventBus);
    
    DetailFormPresenterChangeWorkstateVerify detailPresenter = PowerMockito.spy(
        new DetailFormPresenterChangeWorkstateVerify(detailPlace, clientFactoryMock));
    detailPresenter.start(null, plainEventBus);
    
    // Установка состояния презентора.
    detailPresenter.setPlace(detailPlace);
    return detailPresenter;
  }
}
