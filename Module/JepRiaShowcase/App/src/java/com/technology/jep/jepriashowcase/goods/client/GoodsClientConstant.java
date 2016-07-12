package com.technology.jep.jepriashowcase.goods.client;
 
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.*;
import com.google.gwt.core.client.GWT;
import com.technology.jep.jepriashowcase.goods.shared.GoodsConstant;
import com.technology.jep.jepriashowcase.goods.shared.text.GoodsText;
 
public class GoodsClientConstant extends GoodsConstant {
 
  public static String[] scopeModuleIds = {GOODS_MODULE_ID, SHOPGOODS_MODULE_ID}; 
 
  public static GoodsText goodsText = (GoodsText) GWT.create(GoodsText.class);
}
