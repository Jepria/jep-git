package com.technology.jep.jepria.client.ui.form.list;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.DEFAULT_REFRESH_DELAY;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SELECTED;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_LIST;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Timer;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.shared.load.PagingResult;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

/**
 * Презентер списочной формы, предоставляющий возможность автообновления.
 */
public class AutoRefreshListFormPresenter<
  V extends ListFormView,
  E extends PlainEventBus,
  S extends JepDataServiceAsync,
  F extends StandardClientFactory<E, S>>
  extends ListFormPresenter<V, E , S, F> {

  /**
   * Флаг, показывающий, активен или нет таймер автообновления.
   */
  protected boolean refreshTimerActive = false;
  
  /**
   * Таймер автообновления.
   */
  protected Timer refreshTimer;

  /**
   * Интервал между автообновлениями в миллисекундах.
   */
  private int refreshDelay = DEFAULT_REFRESH_DELAY;
  
  public AutoRefreshListFormPresenter(Place place,
      F clientFactory) {
    super(place, clientFactory);
  }

  /**
   * Устанавливает интервал между автообновлениями.
   * @param refreshDelay интервал в миллисекундах (должен быть положительным)
   */
  public void setRefreshDelay(int refreshDelay) {
    if (refreshDelay <= 0) {
      throw new IllegalArgumentException(JepTexts.errors_list_refreshDelayIllegalValue());
    }
    this.refreshDelay = refreshDelay;
  }

  /**
   * Возвращает интервал между автообновлениями.
   * @return интервал в миллисекундах
   */
  public int getRefreshDelay() {
    return refreshDelay;
  }

  /**
   * Метод, проверяющий необходимость автообновления и при необходимости устанавливающий таймер.
   */
  @Override
  protected void onRefreshSuccess(PagingResult<JepRecord> pagingResult) {
    super.onRefreshSuccess(pagingResult);
    /*
     * Проверяем необходимость обновления, если таймер ещё не создан.
     */
    if (!refreshTimerActive) {
      clientFactory.getService().isRefreshNeeded(listUID, new JepAsyncCallback<Boolean>() {
        public void onSuccess(Boolean result) {
          /*
           * При необходимости ставим таймер, обновляющий списочную форму.
           */
          if (result) {
            refreshTimer = new Timer() {
              public void run() {
                refreshTimerActive = false; // таймер сработал
                if ((VIEW_LIST.equals(_workstate)) || (SELECTED.equals(_workstate))) {
                  eventBus.refreshList();
                }
              }
            };
            /*
             * Запретим создавать новые таймеры, пока не сработал существующий.
             */
            refreshTimerActive = true;
            refreshTimer.schedule(refreshDelay);
          }
        }
      });
    }
  }

  /**
   * Переопределённый метод, отключающий таймер автообновления при уходе со списочной формы.
   */
  @Override
  public void onChangeWorkstate(WorkstateEnum newWorkstate) {
    /*
     * Если уходим со списочной формы, таймер необходимо отключить.
     */
    if (!(SELECTED.equals(newWorkstate) || VIEW_LIST.equals(newWorkstate)) && refreshTimerActive){
      refreshTimer.cancel();
      refreshTimerActive = false;
    }
    super.onChangeWorkstate(newWorkstate);
  }
  
}
