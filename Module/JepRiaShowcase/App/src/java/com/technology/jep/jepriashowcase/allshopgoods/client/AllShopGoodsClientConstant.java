package com.technology.jep.jepriashowcase.allshopgoods.client;
 
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.*;
import com.google.gwt.core.client.GWT;
import com.technology.jep.jepriashowcase.allshopgoods.shared.AllShopGoodsConstant;
import com.technology.jep.jepriashowcase.allshopgoods.shared.text.AllShopGoodsText;
 
public class AllShopGoodsClientConstant extends AllShopGoodsConstant {
 
  public static String[] scopeModuleIds = {ALLSHOPGOODS_MODULE_ID, REQUEST_MODULE_ID}; 
 
  public static AllShopGoodsText allShopGoodsText = (AllShopGoodsText) GWT.create(AllShopGoodsText.class);
}
