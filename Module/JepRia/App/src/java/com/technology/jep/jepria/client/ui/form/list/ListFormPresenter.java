package com.technology.jep.jepria.client.ui.form.list;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SELECTED;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_LIST;
import static com.technology.jep.jepria.client.widget.event.JepEventType.CHANGE_SORT_EVENT;
import static com.technology.jep.jepria.client.widget.event.JepEventType.PAGING_GOTO_EVENT;
import static com.technology.jep.jepria.client.widget.event.JepEventType.PAGING_REFRESH_EVENT;
import static com.technology.jep.jepria.client.widget.event.JepEventType.PAGING_SIZE_EVENT;
import static com.technology.jep.jepria.client.widget.event.JepEventType.ROW_CLICK_EVENT;
import static com.technology.jep.jepria.client.widget.event.JepEventType.ROW_DOUBLE_CLICK_EVENT;
import static com.technology.jep.jepria.shared.JepRiaConstant.EXCEL_DEFAULT_FILE_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.EXCEL_DEFAULT_SERVLET;
import static com.technology.jep.jepria.shared.JepRiaConstant.EXCEL_FILE_NAME_PARAMETER;
import static com.technology.jep.jepria.shared.JepRiaConstant.LIST_UID_REQUEST_PARAMETER;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.history.place.JepSelectedPlace;
import com.technology.jep.jepria.client.history.place.JepViewDetailPlace;
import com.technology.jep.jepria.client.history.place.JepViewListPlace;
import com.technology.jep.jepria.client.history.place.PlainPlaceController;
import com.technology.jep.jepria.client.message.ConfirmCallback;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.DeleteEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.DoDeleteEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.PagingEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.RefreshListEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SearchEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SetCurrentRecordEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SetListUIDEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.ShowExcelEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SortEvent;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.list.GridManager;
import com.technology.jep.jepria.client.widget.list.JepColumnConfig;
import com.technology.jep.jepria.shared.load.FindConfig;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.load.PagingResult;
import com.technology.jep.jepria.shared.load.SortConfig;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ListFormPresenter<V extends ListFormView, E extends PlainEventBus, S extends JepDataServiceAsync, 
    F extends StandardClientFactory<E, S>>
  extends JepPresenter<E, F>
  implements 
    PagingEvent.Handler, 
    RefreshListEvent.Handler,
    SearchEvent.Handler,
    SetListUIDEvent.Handler,
    SortEvent.Handler,
    ShowExcelEvent.Handler,
    DeleteEvent.Handler,
    DoDeleteEvent.Handler,
    SetCurrentRecordEvent.Handler
  {
  
  protected V view;
  protected S service;
  
  protected PlainPlaceController placeController;
  
  /**
   * Управляющий списком класс по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
   * При необходимости, дополнительные управляющие классы используются уже на прикладном уровне.
   */
  protected GridManager list;
  
  /**
   * Поле для сохранения параметров поиска.
   */
  protected PagingConfig searchTemplate = null;
  
  /**
   * Текущая запись.
   */
  protected JepRecord currentRecord = null;

  /**
   * Идентификатор списка.
   */
  protected Integer listUID = null;
  
  public ListFormPresenter(Place place, F clientFactory) {
    super(place, clientFactory);
    
    view = (V)clientFactory.getListFormView();
    service = (S)clientFactory.getService();

    placeController = clientFactory.getPlaceController();
    
    list = view.getListManager();
  }
  
  public void start(AcceptsOneWidget container, EventBus eventBus) {
    // Подписка activity-презентера на события EventBus.
    eventBus.addHandler(PagingEvent.TYPE, this);
    eventBus.addHandler(RefreshListEvent.TYPE, this);
    eventBus.addHandler(SortEvent.TYPE, this);
    eventBus.addHandler(DeleteEvent.TYPE, this);
    eventBus.addHandler(DoDeleteEvent.TYPE, this);
    eventBus.addHandler(SearchEvent.TYPE, this);
    eventBus.addHandler(ShowExcelEvent.TYPE, this);
    
    eventBus.addHandler(SetListUIDEvent.TYPE, this);
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
    
    list.addListener(ROW_CLICK_EVENT, new JepListener() {
      public void handleEvent(JepEvent event) {
        onRowClick(event);
      }
    });
    
    list.addListener(ROW_DOUBLE_CLICK_EVENT, new JepListener() {
      public void handleEvent(JepEvent event) {
        onRowDoubleClick(event);
      }
    });
  
    list.addListener(CHANGE_SORT_EVENT, new JepListener() {
      public void handleEvent(JepEvent event) {
        onChangeSort(event);
      }
    });
    
    list.addListener(PAGING_REFRESH_EVENT, new JepListener() {
      public void handleEvent(JepEvent event) {
        onPagingRefresh(event);
      }
    });
  
    list.addListener(PAGING_SIZE_EVENT, new JepListener() {
      public void handleEvent(JepEvent event) {
        onPagingSize(event);
      }
    });
  
    list.addListener(PAGING_GOTO_EVENT, new JepListener() {
      public void handleEvent(JepEvent event) {
        onPagingGoto(event);
      }
    });
    
  }
  
  protected void onChangeWorkstate(WorkstateEnum workstate) {
    list.changeWorkstate(workstate);
  }

  /**
   * Обработчик события установки текущей записи.
   */
  @Override
  public void onSetCurrentRecord(SetCurrentRecordEvent event) {
    currentRecord = event.getCurrentRecord();    
  }
  
  /**
   * Обработчик события поиска.
   *
   * @param event событие поиска
   */
  @Override
  public void onSearch(SearchEvent event) {
    searchTemplate = event.getPagingConfig(); // Запомним поисковый шаблон.
    eventBus.setListUID(list.getUID()); // Запомним uid списка.
    eventBus.refreshList();
  }
  
  /**
   * Обработчик события обновления списка.
   *
   * @param event событие обновления списка
   */
  @Override
  public void onRefreshList(RefreshListEvent event) {
    // Если существует сохраненный шаблон, по которому нужно обновлять список, то ...
    if(searchTemplate != null) {
      list.clear(); // Очистим список от предыдущего содержимого (чтобы не вводить в заблуждение пользователя).
      list.mask(JepTexts.loadingPanel_dataLoading()); // Выставим индикатор "Загрузка данных...".
      searchTemplate.setListUID(listUID); // Выставим идентификатор получаемого списка данных.
      searchTemplate.setPageSize(list.getPageSize()); // Выставим размер получаемой страницы набора данных.
      clientFactory.getService().find(searchTemplate, new JepAsyncCallback<PagingResult<JepRecord>>() {
        public void onSuccess(PagingResult<JepRecord> pagingResult) {
          onRefreshSuccess(pagingResult);
        }

        public void onFailure(Throwable caught) {
          onRefreshFailure(caught);
          super.onFailure(caught);
        }

      });
    }
  }
  
  /**
   * Hook-метод, вызываемый при успешном обновлении (в частности, после поиска) списка. <br/>
   * Предназначен для переопределения в наследниках.
   */
  protected void onRefreshSuccess(PagingResult<JepRecord> pagingResult) {
    list.set(pagingResult); // Установим в список полученные от сервиса данные.
    list.unmask(); // Скроем индикатор "Загрузка данных...".
  }
  
  /**
   * Hook-метод, вызываемый при неуспешном обновлении (в частности, после поиска) списка. <br/>
   * Предназначен для переопределения в наследниках.
   */
  protected void onRefreshFailure(Throwable caught) {
    list.unmask(); // Скроем индикатор "Загрузка данных...".
  }
  
  /**
   * Обработчик события сортировки.
   *
   * @param event событие сортировки
   */
  @Override
  public void onSort(SortEvent event) {
    // Если поиск уже осуществлялся, то ...
    if(searchTemplate != null) {
      list.mask(JepTexts.loadingPanel_dataSorting()); // Выставим индикатор "Сортировка данных...".
      SortConfig sortConfig = event.getSortConfig();
      sortConfig.setListUID(listUID); // Выставим идентификатор сортируемого списка данных.
      sortConfig.setTemplateRecord(searchTemplate.getTemplateRecord()); // Выставим параметры поиска на случай отсутствия списка в серверной сессии.
      sortConfig.setPageSize(list.getPageSize()); // Выставим размер получаемой страницы набора данных.
      clientFactory.getService().sort(sortConfig, new JepAsyncCallback<PagingResult<JepRecord>>() {
        public void onSuccess(PagingResult<JepRecord> pagingResult) {
          onSortSuccess(pagingResult);
        }
        public void onFailure(Throwable caught) {
          onSortFailure(caught);
          super.onFailure(caught);
        }

      });
    }
  }
  
  /**
   * Hook-метод, вызываемый при успешной сортировке списка. <br/>
   * Предназначен для переопределения в наследниках.
   */
  protected void onSortSuccess(PagingResult<JepRecord> pagingResult) {
    list.set(pagingResult); // Установим в список полученные от сервиса данные.
    list.unmask(); // Скроем индикатор "Сортировка данных...".
  }
  
  /**
   * Hook-метод, вызываемый при неуспешной сортировке списка. <br/>
   * Предназначен для переопределения в наследниках.
   */
  protected void onSortFailure(Throwable caught) {
    list.unmask(); // Скроем индикатор "Сортировка данных...".
  }
  
  /**
   * Обработчик события листания набора данных.
   *
   * @param event событие листания набора данных
   */
  @Override
  public void onPaging(PagingEvent event) {
    // Если поиск уже осуществлялся, то ...
    if(searchTemplate != null) {
      list.mask(JepTexts.loadingPanel_dataLoading()); // Выставим индикатор "Загрузка данных...".
      PagingConfig pagingConfig = event.getPagingConfig();
      pagingConfig.setListUID(listUID); // Выставим идентификатор листаемого списка данных.
      pagingConfig.setTemplateRecord(searchTemplate.getTemplateRecord()); // Выставим параметры поиска на случай отсутствия списка в серверной сессии.
      clientFactory.getService().paging(pagingConfig, new JepAsyncCallback<PagingResult<JepRecord>>() {
        public void onSuccess(PagingResult<JepRecord> pagingResult) {
          onPagingSuccess(pagingResult);
        }

        public void onFailure(Throwable caught) {
          onPagingFailure(caught);
          super.onFailure(caught);
        }

      });
    }
  }
  /**
   * Hook-метод, вызываемый при успешном листании списка. <br/>
   * Предназначен для переопределения в наследниках.
   */
  protected void onPagingSuccess(PagingResult<JepRecord> pagingResult) {
    list.set(pagingResult); // Установим в список полученные от сервиса данные.
    list.unmask(); // Скроем индикатор "Загрузка данных...".
  }
  
  /**
   * Hook-метод, вызываемый при неуспешном листании списка. <br/>
   * Предназначен для переопределения в наследниках.
   */
  protected void onPagingFailure(Throwable caught) {
    list.unmask(); // Скроем индикатор "Загрузка данных...".
  }
  
  @Override
  public void onSetListUID(SetListUIDEvent event) {
    listUID = event.getListUID();
  }
  
  public void onRowClick(JepEvent event) {
    eventBus.setCurrentRecord((JepRecord)event.getParameter());
    // Вызов перехода на новый Place происходит ОБЯЗАТЕЛЬНО ПОСЛЕ подготовки данных для записи в History 
    // (изменения Scope в обработчиках шины событий).
    placeController.goTo(new JepSelectedPlace());
  }

  public void onRowDoubleClick(JepEvent event) {
    placeController.goTo(new JepViewDetailPlace());
  }
  
  public void onChangeSort(JepEvent event) {
    SortConfig sortConfig = (SortConfig)event.getParameter();
    eventBus.sort(sortConfig);
  }
  
  public void onPagingRefresh(JepEvent event) {
    eventBus.refreshList();
    // Важно при обновлении списка менять рабочее состояние на VIEW_LIST, чтобы скинуть состояние SELECTED (тем самым, скрыть кнопки работы с
    // конкретной, ранее выбранной, записью).
    // Вызов перехода на новый Place происходит ОБЯЗАТЕЛЬНО ПОСЛЕ подготовки данных для записи в History 
    // (изменения Scope в обработчиках шины событий).
    placeController.goTo(new JepViewListPlace());
  }
  
  public void onPagingSize(JepEvent event) {
    PagingConfig pagingConfig = (PagingConfig)event.getParameter();
    eventBus.paging(pagingConfig);
  }
  
  public void onPagingGoto(JepEvent event) {
    PagingConfig pagingConfig = (PagingConfig)event.getParameter();
    eventBus.paging(pagingConfig);
  }

  /**
   * Счетчик удаляемых записей.
   */
  private int deleteCounter = 0;
  
  /** 
   * Обработчик события Удалить.<br/>
   * Метод вызывает диалог подтверждения удаления и, в обработчике ConfirmCallback, взывает обработчик непосредственно удаления 
   * {@link #onDeleteConfirmation(Set) onDeleteConfirmation}.
   * 
   * @param event событие Удалить
   */
  @Override
  public void onDoDelete(DoDeleteEvent event) {
    // Проверим состояние, чтобы обеспечить срабатывание данного обработчика только при активной списочной форме.
    if(SELECTED.equals(_workstate)) {
      final Set<JepRecord> records = list.getSelectionModel().getSelectedSet();
      messageBox.confirmDeletion(records.size() > 1, new ConfirmCallback() {
        public void onConfirm(Boolean yes) {
          if(yes) {
            deleteCounter = 0;
            onDeleteConfirmation(records);
          }
        }
      });
    }
  }
  
  /**
   * Обработчик удаления, вызывающий непосредственно сервис удаления.
   *
   * @param records записи, которые необходимо удалить
   */
  protected void onDeleteConfirmation(Set<JepRecord> records) {
    deleteCounter = records.size();
    JepClientUtil.showLoadingPanel(null, JepTexts.loadingPanel_deletingRecords());
    for (final JepRecord record : records) {
      FindConfig deleteConfig = new FindConfig(record);
      deleteConfig.setListUID(listUID);
      clientFactory.getService().delete(deleteConfig, new JepAsyncCallback<Void>() {
        public void onFailure(Throwable th) {
          clientFactory.getExceptionManager().handleException(th, JepTexts.form_deleteError());
          deleteCounter--;
          if (deleteCounter == 0) JepClientUtil.hideLoadingPanel();
        }
        public void onSuccess(Void result) {
          eventBus.delete(record);
          deleteCounter--;
          if (deleteCounter == 0) JepClientUtil.hideLoadingPanel();
        }
      });
    }
  }
  
  /**
   * Обработчик события удаления.<br/>
   * В данном методе происходит удаление записи из списка.
   *
   * @param event событие удаления
   */
  @Override
  public void onDelete(DeleteEvent event) {
    list.remove(event.getRecord());
  }
  
  /**
   * Обработчик события выгрузки в Excel.
   *
   * @param event событие выгрузки в Excel
   */
  @Override
  public void onShowExcel(final ShowExcelEvent event){
    if (list.size() > 0 ) {
      List<JepColumnConfig> columns = list.getColumnModel();
      
      List<String> reportHeaders = new ArrayList<String>(); // Список содержащий названия колонок.
      List<String> reportFields = new ArrayList<String>(); // Список содержащий идентификаторы полей, из которых брать данные для колонок.
      List<JepRecord> selectedRecords = new ArrayList<JepRecord>(list.getSelectionModel().getSelectedSet()); // Список выбранных записей.
      
      for(JepColumnConfig column: columns) {
        reportHeaders.add(column.getHeader());
        reportFields.add(column.getId());
      }

      clientFactory.getService().prepareExcel(searchTemplate, selectedRecords, reportHeaders, reportFields,
        new JepAsyncCallback<Void>() {
          public void onSuccess(Void result) {
            String fileName = event.getFileName();
            if (fileName == null) {
              fileName = EXCEL_DEFAULT_FILE_NAME;
            }
            String excelServlet = event.getExcelServlet();
            if (excelServlet == null) {
              excelServlet = EXCEL_DEFAULT_SERVLET;
            }
            String servletURL = GWT.getModuleBaseURL() + excelServlet;
            String parameters = LIST_UID_REQUEST_PARAMETER + "=" + listUID + "&" + EXCEL_FILE_NAME_PARAMETER + "=" + fileName;
            // Обратимся к сервлету для формирования представления Excel.
            Window.open(
              servletURL + "?" + parameters 
              , "_blank"
              , ""
            );  
          }
        }
      );
    } else {
      messageBox.alert(JepTexts.action_noSelectedRecordForExcel());
    }
  }
  
  /**
   * {@inheritDoc}<br> 
   * Обрабатываем лишь специфичные для списочной формы состояния. 
   */
  @Override
  public boolean isAcceptableWorkstate(WorkstateEnum workstate) {
    return VIEW_LIST.equals(workstate) || SELECTED.equals(workstate);
  }
}
