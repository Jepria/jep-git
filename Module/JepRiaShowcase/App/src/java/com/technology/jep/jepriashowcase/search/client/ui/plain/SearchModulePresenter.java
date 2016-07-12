package com.technology.jep.jepriashowcase.search.client.ui.plain;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.widget.event.JepEventType.*;
import static com.technology.jep.jepria.shared.field.JepFieldNames.MAX_ROW_COUNT;
import static com.technology.jep.jepriashowcase.search.shared.field.SearchFieldNames.*;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.history.place.JepViewListPlace;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.PagingEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.RefreshEvent;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainModulePresenter;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.list.ListManager;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.load.PagingResult;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;
import com.technology.jep.jepriashowcase.search.client.shop.ShoppingCart;
import com.technology.jep.jepriashowcase.search.shared.service.SearchServiceAsync;

public class SearchModulePresenter<V extends SearchModuleView, E extends PlainEventBus, S extends SearchServiceAsync, 
    F extends PlainClientFactory<E, S>> 
  extends PlainModulePresenter<V, E, S, F> 
  implements 
  PagingEvent.Handler,
  RefreshEvent.Handler {
  
  private JepMainServiceAsync mainService = clientFactory.getMainClientFactory().getMainService();
  
  protected JepRecord searchFormFields;
  protected PagingConfig searchTemplate = null;
  protected PagingResult<JepRecord> searchResult;
  protected ListManager list;
  protected ShoppingCart cart;
  protected S service;
  private boolean cartVisible = false;
  
  public SearchModulePresenter(String moduleId, Place place, F clientFactory) {
    super(moduleId, place, clientFactory);
    
    service = clientFactory.getService();
    
    list = view.getListManager();
    cart = new ShoppingCart(view.getShoppingCartLabel()) {
      @Override
      protected void onLoadCart(String goodsIdList) {
        JepRecord cartTemplate = new JepRecord();
        cartTemplate.set(GOODS_ID_LIST, goodsIdList);
        cartTemplate.set(MAX_ROW_COUNT, 999); 
        
        searchTemplate = new PagingConfig(cartTemplate);
        service.find(searchTemplate, new JepAsyncCallback<PagingResult<JepRecord>>() {
          public void onSuccess(PagingResult<JepRecord> pagingResult) {
            cart.add(pagingResult.getData());
          }
        });
      }
    };
    
    view.setShoppingCart(cart);
  };
  
  public void start(AcceptsOneWidget container, EventBus eventBus) {
    //Подписка activity-презентера на события EventBus.
    eventBus.addHandler(PagingEvent.TYPE, this);
    eventBus.addHandler(RefreshEvent.TYPE, this);
    
    super.start(container, eventBus);
  }
  
  protected void bind() {
    super.bind();
    
    view.addSearchButtonClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        String searchText = view.getSearchText();
        if (searchText != null && searchText.length() > 0) {
          
          searchFormFields = new JepRecord();
          searchFormFields.set(GOODS_NAME, searchText);
          searchFormFields.set(MAX_ROW_COUNT, 999); //!
          
          searchTemplate = new PagingConfig(searchFormFields);
          searchTemplate.setListUID(list.getUID());
          searchTemplate.setPageSize(list.getPageSize());
          
          eventBus.refresh();
          
          cartVisible = false;
        }
      }
    });
    
    cart.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (!cartVisible || event == null)
          showCart();
      }
    });
  
    list.addListener(PAGING_REFRESH_EVENT, new JepListener() {
      public void handleEvent(JepEvent event) {
        pagingRefresh(event);
      }
    });
  
    list.addListener(PAGING_SIZE_EVENT, new JepListener() {
      public void handleEvent(JepEvent event) {
        pagingSize(event);
      }
    });

    list.addListener(PAGING_GOTO_EVENT, new JepListener() {
      public void handleEvent(JepEvent event) {
        pagingGoto(event);
      }
    });
  }

  private void showCart() {
    list.set(cart.getGoods());
    
    cartVisible = true;
  }

  /**
   * Установка главного виджета(-контейнера) приложения.<br/>
   * В методе используется вызов вида : <code>mainEventBus.setMainView(clientFactory.getMainClientFactory().getMainView());</code> <br/>
   * При этом, при передаче <code>null</code> в качестве главного виджета приложения, текущий главный виджет удаляется с RootPanel'и.<br/>
   * Т.о., перегрузкой данного метода можно установить, при заходе на модуль приложения, любой главный виджет приложения или скрыть текущий.
   */
  protected void setMainView() {
    Log.trace(this.getClass() + ".setMainView()");
  
    mainEventBus.setMainView(null);
  }
  
  /**
   * Обработчик события обновления списка.
   *
   * @param event событие обновления списка
   */
  public void onRefresh(RefreshEvent event) {
    // Если существует сохраненный шаблон, по которому нужно обновлять список, то ...
    if(searchTemplate != null) {
      list.clear(); // Очистим список от предыдущего содержимого (чтобы не вводить в заблуждение пользователя).
      list.mask(JepTexts.loadingPanel_dataLoading()); // Выставим индикатор "Загрузка данных...".
      searchTemplate.setListUID(list.getUID()); // Выставим идентификатор получаемого списка данных.
      searchTemplate.setPageSize(list.getPageSize()); // Выставим размер получаемой страницы набора данных.
      service.find(searchTemplate, new JepAsyncCallback<PagingResult<JepRecord>>() {
        public void onSuccess(PagingResult<JepRecord> pagingResult) {
          list.set(pagingResult); // Установим в список полученные от сервиса данные.
          list.unmask(); // Скроем индикатор "Загрузка данных...".
        }

        public void onFailure(Throwable caught) {
          list.unmask(); // Скроем индикатор "Загрузка данных...".
          super.onFailure(caught);
        }

      });
    }
  }
  
  /**
   * Обработчик события листания набора данных.
   *
   * @param event событие листания набора данных
   */
  public void onPaging(PagingEvent event) {
    // Если поиск уже осуществлялся, то ...
    if(searchTemplate != null) {
      list.mask(JepTexts.loadingPanel_dataLoading()); // Выставим индикатор "Загрузка данных...".
      PagingConfig pagingConfig = event.getPagingConfig();
      pagingConfig.setListUID(list.getUID()); // Выставим идентификатор листаемого списка данных.
      pagingConfig.setTemplateRecord(searchFormFields); // Выставим параметры поиска на случай отсутствия списка в серверной сессии.
      clientFactory.getService().paging(pagingConfig, new JepAsyncCallback<PagingResult<JepRecord>>() {
        public void onSuccess(PagingResult<JepRecord> pagingResult) {
          list.set(pagingResult); // Установим в список полученные от сервиса данные.
          list.unmask(); // Скроем индикатор "Загрузка данных...".
        }

        public void onFailure(Throwable caught) {
          list.unmask(); // Скроем индикатор "Загрузка данных...".
          super.onFailure(caught);
        }

      });
    }
  }
  
  public void pagingRefresh(JepEvent event) {
    eventBus.refresh();
    // Важно при обновлении списка менять рабочее состояние на VIEW_LIST, чтобы скинуть состояние SELECTED (тем самым, скрыть кнопки работы с
    // конкретной, ранее выбранной, записью).
    // Вызов перехода на новый Place происходит ОБЯЗАТЕЛЬНО ПОСЛЕ подготовки данных для записи в History 
    // (изменения Scope в обработчиках шины событий).
    placeController.goTo(new JepViewListPlace());
  }
  
  public void pagingSize(JepEvent event) {
    PagingConfig pagingConfig = (PagingConfig)event.getParameter();
    eventBus.paging(pagingConfig);
  }
  
  public void pagingGoto(JepEvent event) {
    PagingConfig pagingConfig = (PagingConfig)event.getParameter();
    eventBus.paging(pagingConfig);
  }


}
