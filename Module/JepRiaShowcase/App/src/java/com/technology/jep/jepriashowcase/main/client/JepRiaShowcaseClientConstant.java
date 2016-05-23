package com.technology.jep.jepriashowcase.main.client;

import com.google.gwt.core.client.GWT;
import com.technology.jep.jepriashowcase.main.shared.JepRiaShowcaseConstant;
import com.technology.jep.jepriashowcase.main.shared.text.JepRiaShowcaseText;

public class JepRiaShowcaseClientConstant extends JepRiaShowcaseConstant {
	// Идентификаторы модулей.
	public static final String CUSTOM_MODULE_ID = "Custom";
	public static final String SEARCH_MODULE_ID = "Search";
	public static final String SUPPLIER_MODULE_ID = "Supplier";
	public static final String GOODS_MODULE_ID = "Goods";
	public static final String SHOPGOODS_MODULE_ID = "ShopGoods";
	public static final String SIMPLE_MODULE_ID = "Simple";
	public static final String ALLSHOPGOODS_MODULE_ID = "AllShopGoods";
	public static final String REQUEST_MODULE_ID = "Request";
	public static final String REQUESTPROCESS_MODULE_ID = "RequestProcess";
	public static final String FEATURE_MODULE_ID = "Feature";
	
	/**
	 * Тексты клиентской части JepRiaShowcase.
	 */
	public static JepRiaShowcaseText jepRiaShowcaseText = (JepRiaShowcaseText) GWT.create(JepRiaShowcaseText.class);
	
	public static final String URL_FULL_SCREEN = "JepRiaShowcase.jsp?em=Goods&es=sh#sh:sm=Goods&ws=sh";
	public static final String URL_CUSTOM_MODULE = "JepRiaShowcaseCustom.jsp?em=Custom&es=sh#sh:sm=Custom&ws=sh";
	public static final String URL_SEARCH_MODULE = "JepRiaShowcaseSearch.jsp?em=Search&es=sh#sh:sm=Search&ws=sh";
	public static final String URL_SUPPLIER_MODULE = "JepRiaShowcase.jsp?em=Supplier&es=sh";
	public static final String URL_GOODS_MODULE = "JepRiaShowcase.jsp?em=Goods&es=sh";
	public static final String URL_SHOPGOODS_MODULE = "JepRiaShowcase.jsp?em=ShopGoods&es=sh";
	public static final String URL_ALLSHOPGOODS_MODULE = "JepRiaShowcase.jsp?em=AllShopGoods&es=sh";
	public static final String URL_REQUEST_MODULE = "JepRiaShowcase.jsp?em=Request&es=sh";
	public static final String URL_REQUESTPROCESS_MODULE = "JepRiaShowcase.jsp?em=RequestProcess&es=sh";
	public static final String URL_FEATURE_MODULE = "JepRiaShowcase.jsp?em=Feature&es=sh";
	

	/**
	 * Адрес SSO-модуля для запроса авторизации на сервере Oracle Application Server
	 */
	public static final String OAS_SSO_MODULE_URL = "/jsso/autoLogonServlet?username={0}&password={1}&url=/info/JepRiaShowcase/ProtectedPage.jsp";
	
	/**
	 * Адрес SSO-модуля для запроса авторизации на сервере WebLogic
	 */
	public static final String WL_SSO_MODULE_URL = "/info/SsoIntegration?j_username={0}&j_password={1}";
}
