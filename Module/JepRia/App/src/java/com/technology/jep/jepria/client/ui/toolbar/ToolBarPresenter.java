package com.technology.jep.jepria.client.ui.toolbar;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SELECTED;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_DETAILS;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_LIST;
import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.ADD_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.DELETE_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.EDIT_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.FIND_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.LIST_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.SAVE_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.SEARCH_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.UP_BUTTON_ID;
import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.UP_RIGHT_SEPARATOR_ID;
import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.VIEW_DETAILS_BUTTON_ID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.technology.jep.jepria.client.history.place.JepCreatePlace;
import com.technology.jep.jepria.client.history.place.JepEditPlace;
import com.technology.jep.jepria.client.history.place.JepSearchPlace;
import com.technology.jep.jepria.client.history.place.JepViewDetailPlace;
import com.technology.jep.jepria.client.history.place.JepViewListPlace;
import com.technology.jep.jepria.client.history.place.PlainPlaceController;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.AdjustExitScopeEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SetCurrentRecordEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SetSaveButtonEnabledEvent;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.client.widget.button.JepButton;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.report.JepReportParameters;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

public class ToolBarPresenter<V extends ToolBarView, E extends PlainEventBus, S extends JepDataServiceAsync, F extends StandardClientFactory<E, S>>
    extends JepPresenter<E, F> implements 
      AdjustExitScopeEvent.Handler,
      SetSaveButtonEnabledEvent.Handler,
      SetCurrentRecordEvent.Handler {
  
  /**
   * Соответствие между состоянием работы и множеством активных кнопок.
   */
  private Map<WorkstateEnum, Set<String>> enableByWorkstate = new HashMap<WorkstateEnum, Set<String>>();

  protected V view;
  protected PlainPlaceController<E, S, F> placeController;
  
  public ToolBarPresenter(Place place, F clientFactory) {
    super(place, clientFactory);
    
    view = (V)clientFactory.getToolBarView();
    placeController = (PlainPlaceController<E, S, F>)clientFactory.getPlaceController();
  }
  
  public void start(AcceptsOneWidget container, EventBus eventBus) {
    // Подписка activity-презентера на события EventBus.
    eventBus.addHandler(AdjustExitScopeEvent.TYPE, this);
    eventBus.addHandler(SetSaveButtonEnabledEvent.TYPE, this);
    eventBus.addHandler(SetCurrentRecordEvent.TYPE, this);
    
    // "Привязка" элементов представления к функционалу презентера.
    bind();
    // Переведем презентер модуля в заданный режим.
    changeWorkstate(place);
  }
  
  /**
   * Метод используется для перекрытия потомками с целью "привязки" элементов представления к функционалу презентера.
   */
  protected void bind() {
    view.setPresenter(this);
    
    // Установка поведения и реакции кнопок.
    bindButton(
      UP_BUTTON_ID,
      new WorkstateEnum[]{},
      new ClickHandler() {
        public void onClick(ClickEvent event) {
          up();
        }
      }
    );
    bindButton(
      ADD_BUTTON_ID,
      new WorkstateEnum[]{SELECTED, EDIT, VIEW_LIST, VIEW_DETAILS, SEARCH},
      new ClickHandler() {
        public void onClick(ClickEvent event) {
          add();
        }
      }
    );
    bindButton(
      SAVE_BUTTON_ID,
      new WorkstateEnum[]{CREATE, EDIT},
      new ClickHandler() {
        public void onClick(ClickEvent event) {
          save();
        };
      }
    );
    bindButton(
      EDIT_BUTTON_ID,
      new WorkstateEnum[]{SELECTED, VIEW_DETAILS},
      new ClickHandler() {
        public void onClick(ClickEvent event) {
          edit();
        };
      }
    );
    bindButton(
      DELETE_BUTTON_ID,
      new WorkstateEnum[]{SELECTED, EDIT, VIEW_DETAILS},
      new ClickHandler() {
        public void onClick(ClickEvent event) {
          delete();
        };
      }
    );
    bindButton(
      VIEW_DETAILS_BUTTON_ID,
      new WorkstateEnum[]{SELECTED, EDIT},
      new ClickHandler() {
        public void onClick(ClickEvent event) {
          viewDetails();          
        };
      }
    );
    bindButton(
      LIST_BUTTON_ID, 
      new WorkstateEnum[]{CREATE, EDIT, VIEW_DETAILS}, 
      new ClickHandler() {
        public void onClick(ClickEvent event) {
          list();
        };
      }
    );
    bindButton(
      SEARCH_BUTTON_ID,
      new WorkstateEnum[]{CREATE, EDIT, VIEW_LIST, VIEW_DETAILS, SELECTED},
      new ClickHandler() {
        public void onClick(ClickEvent event) {
          search();
        };
      }
    );
    bindButton(
      FIND_BUTTON_ID,
      new WorkstateEnum[]{SEARCH},
      new ClickHandler() {
        public void onClick(ClickEvent event) {
          doSearch();
        };
      }
    );
    // Закомментированные блоки указаны в качестве шаблона для использования в прикладных модулях.
//    bindButton(
//      REPORT_BUTTON_ID,
//      new WorkstateEnum[]{EDIT, VIEW_DETAILS, SELECTED},
//      new ClickHandler() {
//        public void onClick(ClickEvent event) {
//          report();
//        };
//      }
//    );
//    bindButton(
//      REFRESH_BUTTON_ID,
//      new WorkstateEnum[]{SELECTED, VIEW_LIST},
//      new ClickHandler() {
//        public void onClick(ClickEvent event) {
//          refresh();
//        };
//      }
//    );
//    bindButton(
//      EXCEL_BUTTON_ID,
//      new WorkstateEnum[]{VIEW_LIST, SELECTED},
//      new ClickHandler() {
//        public void onClick(ClickEvent event) {
//          excel();
//        };
//      }
//    );
//    bindButton(
//      TOOLBAR_HELP_BUTTON_ID,
//      new WorkstateEnum[]{CREATE, EDIT, VIEW_LIST, VIEW_DETAILS, SEARCH, SELECTED},
//      new ClickHandler() {
//        public void onClick(ClickEvent event) {
//          help();
//        };
//      }
//    );
    
    // Добавляем обработчик на нажатие кнопки Enter.
    view.addEnterClickListener(new JepListener() {
      public void handleEvent(JepEvent be) {
        // Для формы поиска очевидной на данное событие является реакция - поиск информации.
        if (SEARCH.equals(_workstate)) {
          doSearch();
        }
      }
    });    
  }
  
  /**
   * Обработчик настройки перехода на вышележащий уровень иерархии модулей (на родительский уровень).
   *
   * @param event событие настройки перехода на вышележащий уровень иерархии модулей (на родительский уровень)
   */
  public void onAdjustExitScope(AdjustExitScopeEvent event) {
    // Если уровень иерархии модулей больше одного, то отобразим кнопку перехода на родительский уровень.
    if(JepScopeStack.instance.size() > 1) {
      view.setItemVisible(UP_BUTTON_ID, true);
      view.setItemVisible(UP_RIGHT_SEPARATOR_ID, true);
    } else {
      view.setItemVisible(UP_BUTTON_ID, false);
      view.setItemVisible(UP_RIGHT_SEPARATOR_ID, false);
    }
  }

  /**
   * Обработчик события изменения доступности кнопки сохранения.
   * @param event событие изменения доступности кнопки сохранения
   */
  @Override
  public void onSetSaveButtonEnabled(SetSaveButtonEnabledEvent event) {
    view.setButtonEnabled(SAVE_BUTTON_ID, event.isEnabled());
  }
  
  /**
   * Получение активных кнопок для заданного состояния.
   * 
   * @param workstate новое состояние
   * @return множество идентификаторов активных кнопок
   */
  protected Set<String> getEnabledButtonIds(WorkstateEnum workstate) {
    return enableByWorkstate.get(workstate);
  }
  
  protected void bindButton(String buttonId, WorkstateEnum[] enablingWorkstates, ClickHandler clickHandler) {
    JepButton button = view.getButton(buttonId);
    if(button != null) { // Кнопка могла быть удалена.
      button.addClickHandler(clickHandler);
      
      // Установка соответствия активности состоянию.
      for(WorkstateEnum enablingWorkstate: enablingWorkstates) {
        Set<String> enabledButtonIds = getEnabledButtonIds(enablingWorkstate);
        if(enabledButtonIds == null) {
          enabledButtonIds = new HashSet<String>();
          enableByWorkstate.put(enablingWorkstate, enabledButtonIds);
        }
        enabledButtonIds.add(buttonId);
      }
    }
  }
  
  protected void onChangeWorkstate(WorkstateEnum workstate) {
    view.setButtonsEnabling(enableByWorkstate.get(workstate));
  }
  
  public void add() {
    placeController.goTo(new JepCreatePlace());
  }

  public void save() {
    eventBus.save();
  }
  
  public void edit() {
    placeController.goTo(new JepEditPlace());
  }

  public void delete() {
    eventBus.doDelete();
  }

  public void viewDetails() {
    placeController.goTo(new JepViewDetailPlace());
  }
  
  public void list() {
    eventBus.list();
    eventBus.refresh();
  }
  
  public void search() {
    placeController.goTo(new JepSearchPlace());
  }
  
  public void doSearch() {
    eventBus.doSearch();
  }
  
  public void report() {
    eventBus.prepareReport(new JepReportParameters(), null);
  }

  public void refresh() {
    eventBus.refresh();
    // Важно при обновлении списка менять рабочее состояние на VIEW_LIST, чтобы скинуть состояние SELECTED (тем самым, скрыть кнопки работы с
    // конкретной, ранее выбранной, записью).
    // Вызов перехода на новый Place происходит ОБЯЗАТЕЛЬНО ПОСЛЕ подготовки данных для записи в History 
    // (изменения Scope в обработчиках шины событий).
    placeController.goTo(new JepViewListPlace());
  }

  public void excel() {
    eventBus.showExcel();
  }

  public void help() {
    eventBus.showHelp();
  }

  public void up() {
    eventBus.exitScope();
  }
  
  /**
   * Текущая запись.
   */
  protected JepRecord currentRecord = null;
  
  @Override
  public void onSetCurrentRecord(SetCurrentRecordEvent event) {
    currentRecord = event.getCurrentRecord();
  }
}
