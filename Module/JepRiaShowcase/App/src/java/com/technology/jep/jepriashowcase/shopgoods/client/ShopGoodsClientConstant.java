package com.technology.jep.jepriashowcase.shopgoods.client;
 
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.*;
import com.google.gwt.core.client.GWT;
import com.technology.jep.jepriashowcase.shopgoods.shared.ShopGoodsConstant;
import com.technology.jep.jepriashowcase.shopgoods.shared.text.ShopGoodsText;
 
public class ShopGoodsClientConstant extends ShopGoodsConstant {
 
	public static String[] scopeModuleIds = {SHOPGOODS_MODULE_ID, ALLSHOPGOODS_MODULE_ID}; 
 
	public static ShopGoodsText shopGoodsText = (ShopGoodsText) GWT.create(ShopGoodsText.class);
}
