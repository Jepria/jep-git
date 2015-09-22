package com.technology.jep.jepriashowcase.search.client.shop;

import static com.technology.jep.jepriashowcase.search.shared.field.SearchFieldNames.*;
import static com.technology.jep.jepriashowcase.search.client.SearchClientConstant.searchText;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.shared.record.JepRecord;

public class ShoppingCart {

	private List<JepRecord> goodsList = new ArrayList<JepRecord>();
	private BigDecimal cartSum = BigDecimal.ZERO;
	private Widget shoppingCartwidget;
	private Label cartWidget;
	
	private static String CART_COOKIE_NAME = "jepriashowcase_cart_cookie";
	private static String CART_ID_LIST_SEPARATOR = ",";
	private ClickHandler cartClickHandler;
	
	private Storage localStorage;
	
	public ShoppingCart(Label cartWidget) {
		this.cartWidget = cartWidget;
		this.localStorage = Storage.getLocalStorageIfSupported();
		updateCartLabel();
		
		loadCart();
	}
	
	private void loadCart() {
		if (localStorage != null) {
			String idList = localStorage.getItem(CART_COOKIE_NAME);
			if (idList != null && idList.length() > 0) {
				String idListOK = "";
				try {
					for (String idStr : idList.split(CART_ID_LIST_SEPARATOR)) 
							idListOK += Integer.parseInt(idStr) + CART_ID_LIST_SEPARATOR;
	
					onLoadCart(idListOK);
				} catch (NumberFormatException e) {
					//Broken data from local storage, update
					saveCart();
				}
			}
		}
	}
	
	protected void onLoadCart(String idList) {
	}

	private void saveCart() {
		if (localStorage != null) {
			String goodsIdList = "";
			for (JepRecord good : goodsList) 
				goodsIdList += good.get(GOODS_ID) + CART_ID_LIST_SEPARATOR;
			
			localStorage.setItem(CART_COOKIE_NAME, goodsIdList);
		}
	}

	public void add(List<JepRecord> records) {
		for (JepRecord record : records)
			if (!isGoodInCart(record)) {
				goodsList.add(record);
				cartSum = cartSum.add((BigDecimal) record.get(PURCHASING_PRICE));
			}
		
		updateCartLabel();
		saveCart();
	}

	public void add(JepRecord record) {
		if (!isGoodInCart(record)) {
			goodsList.add(record);
			cartSum = cartSum.add((BigDecimal) record.get(PURCHASING_PRICE));
			
			updateCartLabel();
			saveCart();
		}
	}
	
	public void remove(JepRecord record) {
		if (isGoodInCart(record)) {
			goodsList.remove(record);
			cartSum = cartSum.subtract((BigDecimal) record.get(PURCHASING_PRICE));
			
			updateCartLabel();
			saveCart();
			forceClick();
		}
	}
	
	public List<JepRecord> getGoods() {
		return new ArrayList<JepRecord>(goodsList);
	}
	
	public BigDecimal getCartSum(){
		return cartSum;
	}
	
	private String getCartLabelHtml() {
		String cartText = goodsList.size() > 0 ? 
				searchText.cart_cart() + goodsList.size() + searchText.cart_goods() +
				searchText.cart_sum() + cartSum.toString() + searchText.cart_rur():
				searchText.cart_empty();
		
		return cartText;
	}
	
	public boolean isGoodInCart(JepRecord record) {
		return goodsList.contains(record);
	}

	public void addClickHandler(ClickHandler clickHandler) {
		cartWidget.addClickHandler(clickHandler);
		cartClickHandler = clickHandler;
	}

	private void updateCartLabel() {
		cartWidget.getElement().setInnerHTML(getCartLabelHtml());
	}
	
	private void forceClick() {
		if (cartClickHandler != null) 
			cartClickHandler.onClick(null);
	}
}

