package com.technology.jep.jepriashowcase.search.client.widget.list;

import static com.technology.jep.jepriashowcase.search.shared.field.SearchFieldNames.*;
import static com.technology.jep.jepriashowcase.search.client.SearchClientConstant.searchText;

import java.math.BigDecimal;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepriashowcase.search.client.shop.ShoppingCart;

@SuppressWarnings("deprecation")
public class ListItemViewImpl extends CellPanel {

	private Element tableRow;
	private Element imageColumn;
	private Element nameColumn;
	private Element priceColumn;
	private Element buttonColumn;
	private Button removeButton, buyButton;
	private Label inCartLabel; 
	private final JepRecord good;
	private final ShoppingCart shoppingCart;
	

	public ListItemViewImpl(JepRecord record, ShoppingCart cart) {
		super();
		
		this.good = record;
		this.shoppingCart = cart;

		tableRow = DOM.createTR();
		tableRow.addClassName("jepRiaShowcase-searchItem");
		DOM.appendChild(getBody(), tableRow);
		
		DOM.setElementProperty(getTable(), "cellSpacing", "0");
		DOM.setElementProperty(getTable(), "cellPadding", "0");

		setWidth("100%");

		imageColumn = DOM.createTD();
		imageColumn.addClassName("jepRiaShowcase-imageColumn");
		DOM.appendChild(tableRow, imageColumn);
		Image imageElement = new Image("", 0, 0, 100, 100);
		add(imageElement, imageColumn);

		nameColumn = DOM.createTD();
		nameColumn.addClassName("jepRiaShowcase-nameColumn");
		DOM.appendChild(tableRow, nameColumn);
		add(new Hyperlink((String) record.get(GOODS_NAME), "#"), nameColumn);
		add(new Label(searchText.listItem_article() + (Integer) record.get(GOODS_ID)), nameColumn);

		priceColumn = DOM.createTD();
		priceColumn.addClassName("jepRiaShowcase-priceColumn");
		DOM.appendChild(tableRow, priceColumn);
		add(new Label("" + (BigDecimal) record.get(PURCHASING_PRICE) + searchText.listItem_rur()), priceColumn);
		add(new Label("(" + (String) record.get(UNIT_NAME) + ")"), priceColumn);

		buttonColumn = DOM.createTD();
		buttonColumn.addClassName("jepRiaShowcase-buttonColumn");
		DOM.appendChild(tableRow, buttonColumn);

		inCartLabel = new Label(searchText.listItem_inCart());
		buyButton = new Button(searchText.listItem_buyButton());
		buyButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//Window.alert("Buy?");
				shoppingCart.add(good);
				
				buyButton.removeFromParent();
				updateButtonColumnContent();
			}
		});
		
		removeButton = new Button(searchText.listItem_deleteButton());
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				shoppingCart.remove(good);
				
				inCartLabel.removeFromParent();
				removeButton.removeFromParent();
				updateButtonColumnContent();
			}
		});

		updateButtonColumnContent();
	}

	private void updateButtonColumnContent() {
		if (!shoppingCart.isGoodInCart(good)) {
			add(buyButton, buttonColumn);
		} else {
			add(inCartLabel, buttonColumn);
			add(removeButton, buttonColumn);
		}
	}
}
