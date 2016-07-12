package com.technology.jep.jepriashowcase.shopgoods.shared.service;
 
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
 
public interface ShopGoodsServiceAsync extends JepDataServiceAsync {
  void getShop(String shopName, AsyncCallback<List<JepOption>> callback);
  void getGoods(String goodsName, AsyncCallback<List<JepOption>> callback);
}
