package com.technology.jep.jepriashowcase.search.client.ui.plain;

import static com.technology.jep.jepriashowcase.search.client.SearchClientConstant.searchText;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ui.plain.PlainModuleViewImpl;
import com.technology.jep.jepria.client.widget.list.JepDataWidgetList;
import com.technology.jep.jepria.client.widget.list.ListManager;
import com.technology.jep.jepria.client.widget.list.PagingManager;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepriashowcase.search.client.shop.ShoppingCart;
import com.technology.jep.jepriashowcase.search.client.widget.list.ListItemViewImpl;
import com.technology.jep.jepriashowcase.search.client.widget.toolbar.PagingSimpleBar;

public class SearchModuleViewImpl extends PlainModuleViewImpl implements SearchModuleView {

  private Button searchButton;
  private TextBox searchField;
  private VerticalPanel searchPanel;
  private VerticalPanel searchResult;
  private HorizontalPanel pagingPanel;

  protected PagingManager list;
  protected PagingSimpleBar pagingBar;
  protected JepDataWidgetList dataList;
  protected ShoppingCart shoppingCart;
  protected RootPanel searchPanelElement, resultElement, pagingElement, cartElement;

  private Button authorizationButton;
  private Button exitButton;
  private Button currentUserButton;

  private Label errorLabel, currentUserLabel, shoppingCartWidget;

  public SearchModuleViewImpl() {

    list = new PagingManager();

    // панель управления поиском
    searchPanel = new VerticalPanel();
    searchPanel.getElement().getStyle().setMarginTop(5, Unit.PX);
    searchPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

    searchButton = new Button(searchText.search_searchButton());
    searchField = new TextBox();
    searchPanel.add(searchField);
    searchPanel.add(searchButton);

    // панель с результатами поиска
    searchResult = new VerticalPanel();
    searchResult.setWidth("100%");
    searchResult.addStyleName("jepRiaShowcase-searchResult");
    
    dataList = new JepDataWidgetList() {
      @Override 
      protected Widget getDataWidget(JepRecord data) {
        return new ListItemViewImpl(data, shoppingCart);
      }
    };

    pagingBar = new PagingSimpleBar(25);

    list.setWidget(dataList);
    list.setPagingToolBar(pagingBar);

    searchPanelElement = RootPanel.get("searchPanel");
    if (searchPanelElement != null) {
      searchPanelElement.add(searchPanel);
    }

    resultElement = RootPanel.get("searchResults");
    if (resultElement != null) {
      resultElement.add(dataList);
    }

    pagingElement = RootPanel.get("pagingBar");
    if (pagingElement != null) {
      pagingElement.add(pagingBar);
    }
    
    shoppingCartWidget = new Label("");
    shoppingCartWidget.getElement().getStyle().setCursor(Cursor.POINTER);
    cartElement = RootPanel.get("shoppingCart");
    if (cartElement != null) {
      cartElement.add(shoppingCartWidget);
    }

  }

  public void addSearchButtonClickHandler(ClickHandler clickHandler) {
    searchButton.addClickHandler(clickHandler);
  }
  
  public String getSearchText() {
    return searchField.getText();
  }

  public ListManager getListManager() {
    return list;
  }
  
  public Label getShoppingCartLabel() {
    return this.shoppingCartWidget;
  }
  
  public void setShoppingCart(ShoppingCart cart) {
    this.shoppingCart = cart;
  }
}